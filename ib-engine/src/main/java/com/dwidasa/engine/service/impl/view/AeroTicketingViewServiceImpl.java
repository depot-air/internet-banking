package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.model.airline.*;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.AeroTicketingViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



@Service("aeroTicketingViewService")
public class AeroTicketingViewServiceImpl implements AeroTicketingViewService {
	@Autowired
	private CacheManager cacheManager;

	private static Logger logger = Logger.getLogger(AeroTicketingViewServiceImpl.class);
    Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss")
            .serializeNulls().create();
    public AeroTicketingViewServiceImpl() {

    }

    private class AeroFlightViewMessageCustomizer implements MessageCustomizer {
		
		private AeroFlightViewMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
            String customData = "";
            AirSearchFlight airSearchFlight = new AirSearchFlight();
            if (atv.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
        		airSearchFlight.setProviderCode(atv.getProviderCode());
        		airSearchFlight.setFromAirport(atv.getDepartureAirportCode());
        		airSearchFlight.setToAirport(atv.getDestinationAirportCode());
        		airSearchFlight.setDepartDate(atv.getDepartDate());
        		airSearchFlight.setReturnDate(atv.getReturnDate());
        		airSearchFlight.setPaxAdult(atv.getTotalAdult());
        		airSearchFlight.setPaxChild(atv.getTotalChildren());
        		airSearchFlight.setPaxInfant(atv.getTotalInfant());
        		airSearchFlight.setOneWay(atv.getIsDepartOnly());
        		
        		List<String> airlineCodes = new ArrayList<String>();
            	if (atv.getTransactionType().equals(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH)) {
            		airlineCodes.add(atv.getDepartAirlineCode());
            	} else {	
            		String[] codes = atv.getDepartAirlineCode().split(","); 
            		for (int i = 0; i < codes.length; i++) {
    					airlineCodes.add(codes[i]);
    				}
            	}
            	airSearchFlight.setAirlineCodes(airlineCodes);
            } else {
        		airSearchFlight.setProviderCode(atv.getProviderCode());
        		airSearchFlight.setFromAirport(atv.getDepartureAirportCode());
        		airSearchFlight.setToAirport(atv.getDestinationAirportCode());
        		airSearchFlight.setDepartDate(atv.getDepartDate());
        		airSearchFlight.setReturnDate(atv.getReturnDate());
        		airSearchFlight.setOneWay(atv.getIsDepartOnly());
        		
        		String[] codes = atv.getDepartAirlineCode().split(","); 
        		List<String> airlineCodes = new ArrayList<String>();
        		for (int i = 0; i < codes.length; i++) {
					airlineCodes.add(codes[i]);
				}
        		airSearchFlight.setAirlineCodes(airlineCodes);
        		airSearchFlight.setPaxAdult(atv.getTotalAdult());
        		airSearchFlight.setPaxChild(atv.getTotalChildren());
        		airSearchFlight.setPaxInfant(atv.getTotalInfant());
            }
//    		customData = PojoJsonMapper.toJson(airSearchFlight);
            customData = gson.toJson(airSearchFlight);
            transaction.setFreeData1(customData);

        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {        	
            List<AeroFlightView> views = (List<AeroFlightView>) view;
            if (transaction.getResponseCode().equals("00")) {
            	String bit48 = transaction.getFreeData1();
            	//System.out.println("bit48=" + bit48);
            	int pax = transaction.getCreatedby().intValue();
            	if (transaction.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            		decomposeVoltras(views, bit48, false, pax);	//sama dg voltras balikannya dibuat sama
            	} else {
            		decomposeVoltras(views, bit48, true, pax);	
            	}
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }
        
		private void decomposeVoltras(List<AeroFlightView> views, String bit48, boolean isVoltras, int pax) {
			PacAirItem[] mcArray = gson.fromJson(bit48, PacAirItem[].class);
			List<PacAirItem> pacAirItems = Arrays.asList(mcArray);            	
			for(PacAirItem pacAirItem : pacAirItems) {
				AeroFlightView flightView = new AeroFlightView();
				List<PacFlight> pacFlights = pacAirItem.getPacFlights();
				PacFlight pacFlight = pacFlights.get(0);
				flightView.setSessionId(pacAirItem.getSessionId());
				flightView.setFlightNumber(pacFlight.getFlightNo());
				flightView.setAirlineId(pacFlight.getAirlineCode());
				if (flightView.getAirlineId() != null && !flightView.getAirlineId().equals("")) {
					Airline airline;
		        	if (isVoltras) {
		        		airline = cacheManager.getAirlineByCode(flightView.getAirlineId());	//voltras berisi kode GA, JT, dsb
		        	} else {
		        		airline = cacheManager.getAirlineById(flightView.getAirlineId());	//aeroticketing berisi 1,2,3, dsb
		        	}
		        	flightView.setAirlineName(airline.getAirlineName());		            
		        }
				flightView.setDepartureAirportCode(pacFlight.getFromAirport());
				Airport airport = cacheManager.getAirport(pacFlight.getFromAirport());
				flightView.setDepartureAirportFullName(airport.getAirportCity());
				
				flightView.setArrivalAirportCode(pacFlight.getToAirport());
				airport = cacheManager.getAirport(pacFlight.getToAirport());
				flightView.setArrivalAirportFullName(airport.getAirportCity());
				
				flightView.setFlightDate(pacFlight.getDepartTime());
				flightView.setEtd(pacFlight.getDepartTime());
				flightView.setEta(pacFlight.getArriveTime());
				flightView.setEtaConnecting(pacFlight.getArriveTime()); 	//disimpan di etaConnecting untuk mempermudah penampilan UI
				if (pacFlights.size() > 1) {	//connecting / transit
					AeroConnectingFlight connectingFlightView = new AeroConnectingFlight();
					PacFlight connectingPacFlight = pacFlights.get(1);
					connectingFlightView.setConnectingFlightFno(connectingPacFlight.getFlightNo());
					connectingFlightView.setConnectingFlightFrom(connectingPacFlight.getFromAirport());
					airport = cacheManager.getAirport(connectingPacFlight.getFromAirport());
					connectingFlightView.setConnectingFlightCityFrom(airport.getAirportCity());
					
					connectingFlightView.setConnectingFlightTo(connectingPacFlight.getToAirport());
					airport = cacheManager.getAirport(connectingPacFlight.getToAirport());
					connectingFlightView.setConnectingFlightCityTo(airport.getAirportCity());
					
					connectingFlightView.setConnectingFlightDate(connectingPacFlight.getDepartTime());	//date = etd
					connectingFlightView.setConnectingFlightEtd(connectingPacFlight.getDepartTime());
					connectingFlightView.setConnectingFlightEta(connectingPacFlight.getArriveTime());
					
					flightView.setAeroConnectingFlight(connectingFlightView);
					flightView.setFlightType(AirConstants.AEROTICKETING.FLIGHT_TYPE.CONNECTIING);
					flightView.setIsConnectingFlight(true);
					flightView.setEtaConnecting(connectingFlightView.getConnectingFlightEta());	//set ETA = ETA penerbangan terakhir
					if (pacFlights.size() == 3) {	//connecting flight ke 2
						AeroConnectingFlight connectingFlightView2 = new AeroConnectingFlight();
						PacFlight connectingPacFlight2 = pacFlights.get(2);
						connectingFlightView2.setConnectingFlightFno(connectingPacFlight2.getFlightNo());
						connectingFlightView2.setConnectingFlightFrom(connectingPacFlight2.getFromAirport());
						airport = cacheManager.getAirport(connectingPacFlight2.getFromAirport());
						connectingFlightView2.setConnectingFlightCityFrom(airport.getAirportCity());
						
						connectingFlightView2.setConnectingFlightTo(connectingPacFlight2.getToAirport());
						airport = cacheManager.getAirport(connectingPacFlight2.getToAirport());
						connectingFlightView2.setConnectingFlightCityTo(airport.getAirportCity());
						
						connectingFlightView2.setConnectingFlightDate(connectingPacFlight2.getDepartTime());
						connectingFlightView2.setConnectingFlightEtd(connectingPacFlight2.getDepartTime());
						connectingFlightView2.setConnectingFlightEta(connectingPacFlight2.getArriveTime());
						
						flightView.setAeroConnectingFlight2(connectingFlightView2);
						flightView.setEtaConnecting(connectingFlightView2.getConnectingFlightEta());	//set ETA = ETA penerbangan terakhir
					}
				} else {
					flightView.setFlightType(AirConstants.AEROTICKETING.FLIGHT_TYPE.DIRECT);
					flightView.setIsConnectingFlight(false);
				}
			
				List<PacFlightClassGroup> pacFlightClassGroups = pacAirItem.getPacFlightClassGroups();
				PacFlightClassGroup firstClassGroup = pacFlightClassGroups.get(0);
				List<PacFlightClass> pacFlightClasses = firstClassGroup.getPacFlightClasses();
				List<AeroFlightClass> aeroFlightClasses = new ArrayList<AeroFlightClass>();
				
				if (pacFlightClasses != null ) {
					for(PacFlightClass pacFlightClass : pacFlightClasses) {
						AeroFlightClass flightClass = new AeroFlightClass();
						if (pacFlightClass.getAvailableSeat() >= pax ) {	//lebih besar dan sama dengan pax,  && pacFlightClass.getFare().compareTo(BigDecimal.ZERO) > 0 
							if (isVoltras) {
								flightClass.setClassId(pacFlightClass.getCode());	
							} else {
								flightClass.setClassId(pacFlightClass.getName());
							}
							flightClass.setClassName(pacFlightClass.getCode());
							flightClass.setAvaliableSeat(pacFlightClass.getAvailableSeat());
							flightClass.setPrice(pacFlightClass.getFare());
							aeroFlightClasses.add(flightClass);
						}
					}
				}
				List<AeroFlightClass> secondAeroFlightClasses = new ArrayList<AeroFlightClass>();
				List<AeroFlightClass> thirdAeroFlightClasses = new ArrayList<AeroFlightClass>();
//				if (pacFlightClassGroups.size() > 1) {	//Connecting Sum Local, masih belum unutk yg connecting 3 flight
				if (flightView.getIsConnectingFlight()) {
					if (pacFlightClassGroups.size() == 1) {	//throughfare 
						AeroConnectingFlight aeroConnectingFlight = flightView.getAeroConnectingFlight();
						AeroFlightClass connectingFlightClass = aeroFlightClasses.get(0);
						if (connectingFlightClass != null) {
							aeroConnectingFlight.setConnectingFlightClassname(connectingFlightClass.getClassId());
							aeroConnectingFlight.setConnectingFlightPrice(connectingFlightClass.getPrice());
							aeroConnectingFlight.setConnectingFlightSeat(connectingFlightClass.getAvaliableSeat());
							aeroConnectingFlight.setThroughFare(true);
							flightView.setAeroConnectingFlight(aeroConnectingFlight);
						}
						
					} else  {
						PacFlightClassGroup secondClassGroup = pacFlightClassGroups.get(1);
						List<PacFlightClass> secondPacFlightClasses = secondClassGroup.getPacFlightClasses();
						if (secondPacFlightClasses != null) {
							for(PacFlightClass secondPacFlightClass : secondPacFlightClasses) {
								AeroFlightClass secondFlightClass = new AeroFlightClass();
								if (secondPacFlightClass.getAvailableSeat() >= pax ) {
									secondFlightClass.setClassId(secondPacFlightClass.getCode());
									secondFlightClass.setClassName(secondPacFlightClass.getCode());
									secondFlightClass.setAvaliableSeat(secondPacFlightClass.getAvailableSeat());
									secondFlightClass.setPrice(secondPacFlightClass.getFare());
									secondAeroFlightClasses.add(secondFlightClass);
								}
							}
						}
						
						AeroConnectingFlight aeroConnectingFlight = flightView.getAeroConnectingFlight();
						if (secondAeroFlightClasses.size() > 0) {
							AeroFlightClass connectingFlightClass = secondAeroFlightClasses.get(0);
							if (connectingFlightClass != null) {
								aeroConnectingFlight.setConnectingFlightClassname(connectingFlightClass.getClassId());
								aeroConnectingFlight.setConnectingFlightPrice(connectingFlightClass.getPrice());
								aeroConnectingFlight.setConnectingFlightSeat(connectingFlightClass.getAvaliableSeat());
								aeroConnectingFlight.setThroughFare(false);
								flightView.setAeroConnectingFlight(aeroConnectingFlight);
							}
						}					
						
						if (pacFlightClassGroups.size() == 3) {	//Connecting Sum Local, masih belum unutk yg connecting 3 flight
							PacFlightClassGroup thirdClassGroup = pacFlightClassGroups.get(2);
							List<PacFlightClass> thirdPacFlightClasses = thirdClassGroup.getPacFlightClasses();
							if (thirdPacFlightClasses != null) {
								for(PacFlightClass thirdPacFlightClass : thirdPacFlightClasses) {
									AeroFlightClass thirdFlightClass = new AeroFlightClass();
									if (thirdPacFlightClass.getAvailableSeat() >= pax ) {
										thirdFlightClass.setClassId(thirdPacFlightClass.getCode());
										thirdFlightClass.setClassName(thirdPacFlightClass.getCode());
										thirdFlightClass.setAvaliableSeat(thirdPacFlightClass.getAvailableSeat());
										thirdFlightClass.setPrice(thirdPacFlightClass.getFare());
	//									for(AeroFlightClass afc : aeroFlightClasses) {
	//										if (afc.getClassId().equals(thirdFlightClass.getClassId())) {
	//											afc.setPrice(afc.getPrice().add(thirdFlightClass.getPrice()));
	//											tempClasses.add(afc);
	//										}
	//									}
										thirdAeroFlightClasses.add(thirdFlightClass);
									}
								}
							}
							AeroConnectingFlight aeroConnectingFlight2 = flightView.getAeroConnectingFlight2();
							if (thirdAeroFlightClasses.size() > 0) {
								AeroFlightClass connectingFlightClass2 = thirdAeroFlightClasses.get(0);
								if (connectingFlightClass2 != null) {
									aeroConnectingFlight2.setConnectingFlightClassname(connectingFlightClass2.getClassId());
									aeroConnectingFlight2.setConnectingFlightPrice(connectingFlightClass2.getPrice());
									aeroConnectingFlight2.setConnectingFlightSeat(connectingFlightClass2.getAvaliableSeat());
									aeroConnectingFlight2.setThroughFare(false);
									flightView.setAeroConnectingFlight2(aeroConnectingFlight2);
								}	
							}
						}
					}
				}
				
				flightView.setAeroFlightClasses(aeroFlightClasses);
				if (aeroFlightClasses.size() > 0) {
					AeroFlightClass firstClass = aeroFlightClasses.get(0);
					if (pacFlightClassGroups.size() > 1) {
						flightView.setAeroFlightClasses(aeroFlightClasses);
					}
					flightView.setSelectedClass(firstClass);
					flightView.setTicketPrice(firstClass.getPrice());
					flightView.setSelectedClassId(firstClass.getClassId());	//firstClass.getClassName()
					flightView.setIsDepartureFlight(pacAirItem.isBoolDepart());
//					if (flightView.getIsConnectingFlight()) {
//						AeroConnectingFlight aeroConnectingFlight = flightView.getAeroConnectingFlight();
//						flightView.setArrivalAirportCode(aeroConnectingFlight.getConnectingFlightTo());
//						if (pacFlights.size() == 3) {	//connecting flight ke 2
//							AeroConnectingFlight aeroConnectingFlight2 = flightView.getAeroConnectingFlight2();
//							flightView.setArrivalAirportCode(aeroConnectingFlight2.getConnectingFlightTo());
//						}
//					}
					views.add(flightView);
				}
			}
		}    
    }



    @Override
	public List<AeroFlightView> getSearch(AeroTicketingView view) {
		List<AeroFlightView> asvs = Collections.synchronizedList(new ArrayList<AeroFlightView>());

        Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroFlightViewMessageCustomizer();
        customizer.compose(view, t);

        link.sendMessage();
        if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	Integer paxLimit = 0; 
        	if (view.getProviderCode().equals(AirConstants.VOLTRAS.PROVIDER.CODE)) {
        		paxLimit = view.getTotalAdult() + view.getTotalChildren();	// + view.getTotalInfant(), infant dipangku
        	}
        	t.setCreatedby(Long.parseLong("" + paxLimit));	//untuk menyimpan pax
            customizer.decompose(asvs, t);
        }
        else {
//        	if (view.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
        		PacResponseError pre = gson.fromJson(t.getFreeData1(), PacResponseError.class);
        		if (pre != null && pre.getCode() != null) throw new BusinessException("IB-1009", t.getResponseCode(), pre.getDescription());
        		else throw new BusinessException("IB-1009", t.getResponseCode());
//        	} else {
//        		throw new BusinessException("IB-1009", t.getResponseCode());
//        	}
        }

        return asvs;
	}

    private class AeroPriceDetailMessageCustomizer implements MessageCustomizer {

		private AeroPriceDetailMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
            logger.info("atv.getSessionId()=" + atv.getSessionId());
            //session_id an-45 Session ID -Rata Kiri -Padding with Space
            String customData = StringUtils.rightPad(atv.getSessionId(), 45, " ");
            //Flight type an-1 1 = depart 2 = return
            String type = atv.getIsCheckingDepart() ? AeroFlightView.DEPART_RETURN.DEPART : AeroFlightView.DEPART_RETURN.RETURN;
            customData += StringUtils.rightPad(type, 1, " ");
            if (type.equals(AeroFlightView.DEPART_RETURN.DEPART)) {
            	//flightno an-8 Flight no -Rata Kiri -Padding with Space 
                AeroFlightView departFlight = atv.getDepartureFlight();
                customData += StringUtils.rightPad(departFlight.getFlightNumber(), 8, " ");
                //class_id an-7 Flight class -Rata Kiri -Padding with Space
                AeroFlightClass departClass = departFlight.getSelectedClass();
                customData += StringUtils.rightPad(departClass.getClassId(), 7, " ");
            } else {
                AeroFlightView returnFlight = atv.getReturnFlight();
                customData += StringUtils.rightPad(returnFlight.getFlightNumber(), 8, " ");
                AeroFlightClass returnClass = returnFlight.getSelectedClass();
                customData += StringUtils.rightPad(returnClass.getClassId(), 7, " ");
            }

            transaction.setFreeData1(customData);
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	AeroFlightView flight = (AeroFlightView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (transaction.getResponseCode().equals("00")) {
            	String bit48 = transaction.getFreeData1();
            	//Airline Id an-2 Airlines ID where apply in AeroTicket's system -Rata Kiri -Padding with Space
            	//Airline name an-20 -Rata Kiri -Padding with Space 
            	flight.setAirlineName(bit48.substring(2, 22).trim());
            	//Flight type an-1 Flight type consists of direct/transit/connecting 1=direct 2=transit 3=connecting
            	String flightType = bit48.substring(22, 23);
            	flight.setFlightType((flightType.equals("1") ? AirConstants.AEROTICKETING.FLIGHT_TYPE.DIRECT : flightType.equals("2") ? AirConstants.AEROTICKETING.FLIGHT_TYPE.TRANSIT : AirConstants.AEROTICKETING.FLIGHT_TYPE.CONNECTIING));
            	//Flight from an-3 Departure airport code (3 digit) -Rata Kiri -Padding with Space
            	flight.setDepartureAirportCode(bit48.substring(23, 26));
            	//Flight via an-3 Via airport code (3 digit) -Rata Kiri -Padding with Space
            	flight.setVia(bit48.substring(26, 29).trim());
            	//Flight to an-3 Arrival airport code (3 digit) -Rata Kiri -Padding with Space
            	flight.setArrivalAirportCode(bit48.substring(29, 32));
            	//Flight date an-10 go/return date (yyyy-mm-dd) 
            	String flightDate = bit48.substring(32, 42);
				try {
					flight.setFlightDate(sdf.parse(flightDate));
				} catch (ParseException e) {
					flight.setFlightDate(new Date());
				}
            	//Flight fno an-8 Airline no -Rata Kiri -Padding with Space
				flight.setFlightNumber(bit48.substring(42, 50).trim());
            	//Flight etd an-5 Departure time (hh:mm)
				try {
					flight.setEtd(sdfTime.parse(flightDate + " " + bit48.substring(50, 55)) );
				} catch (ParseException e) {
					flight.setEtd(new Date());
				}	
            	//Flight eta an-5 arrival  time (hh:mm)
				try {
					flight.setEta(sdfTime.parse(flightDate + " " + bit48.substring(55, 60)) );
				} catch (ParseException e) {
					flight.setEta(new Date());
				}	
            	//Flight class id an-7 -Rata Kiri -Padding with Space
				flight.getSelectedClass().setClassId(bit48.substring(60, 67));
            	//Flight class an-2 -Rata Kiri -Padding with Space
				flight.getSelectedClass().setClassLabel(bit48.substring(67, 69));
            	//Flight class_name an-10 -Rata Kiri -Padding with Space
				flight.getSelectedClass().setClassName(bit48.substring(69, 79));
            	//Flight adult pax n1 -Rata kanan -Zero leading padding 
				int adultPax = Integer.parseInt(bit48.substring(79, 80));
				AeroPassengerSummary adultPassengerSummary = new AeroPassengerSummary();
				adultPassengerSummary.setPax(adultPax);
				if (adultPax != 0) {
					adultPassengerSummary.setPassengerType(AeroPassenger.PASSENGER_TYPE.ADULT);
	            	//Flight adult basic n12 -Rata kanan -Zero leading padding 
    				adultPassengerSummary.setBasic(EngineUtils.getBigDecimalValue(bit48.substring(80, 92), 0));
	            	//Flight adult tax n12 -Rata kanan -Zero leading padding 
    				adultPassengerSummary.setTax(EngineUtils.getBigDecimalValue(bit48.substring(92, 104), 0));
	            	//Flight adult iwjr n12 -Rata kanan -Zero leading padding 
    				adultPassengerSummary.setIwjr(EngineUtils.getBigDecimalValue(bit48.substring(104, 116), 0));
	            	//Flight adult service n12 -Rata kanan -Zero leading padding 
    				adultPassengerSummary.setService(EngineUtils.getBigDecimalValue(bit48.substring(116, 128), 0));
	            	//Flight adult total n12 -Rata kanan -Zero leading padding 
    				adultPassengerSummary.setTotal(EngineUtils.getBigDecimalValue(bit48.substring(128, 140), 0));
				}    				
				flight.setAdultPassengerSummary(adultPassengerSummary);

				//Flight child pax n1 -Rata kanan -Zero leading padding
				int childPax = Integer.parseInt(bit48.substring(140, 141));
				AeroPassengerSummary childPassengerSummary = new AeroPassengerSummary();
				childPassengerSummary.setPax(childPax);
				if (childPax != 0) {
					childPassengerSummary.setPassengerType(AeroPassenger.PASSENGER_TYPE.CHILD);
					//Flight child basic n12 -Rata kanan -Zero leading padding
					childPassengerSummary.setBasic(EngineUtils.getBigDecimalValue(bit48.substring(141, 153), 0));
	            	//Flight child tax n12 -Rata kanan -Zero leading padding 
					childPassengerSummary.setTax(EngineUtils.getBigDecimalValue(bit48.substring(153, 165), 0));
					//Flight child iwjr n12 -Rata kanan -Zero leading padding 
					childPassengerSummary.setIwjr(EngineUtils.getBigDecimalValue(bit48.substring(165, 177), 0));
	            	//Flight child service n12 -Rata kanan -Zero leading padding 
					childPassengerSummary.setService(EngineUtils.getBigDecimalValue(bit48.substring(177, 189), 0));
	            	//Flight child total n12 -Rata kanan -Zero leading padding 
					childPassengerSummary.setTotal(EngineUtils.getBigDecimalValue(bit48.substring(189, 201), 0));
				}
				flight.setChildPassengerSummary(childPassengerSummary);
				
            	//Flight infant pax n1 -Rata kanan -Zero leading padding 
				int infantPax = Integer.parseInt(bit48.substring(201, 202));
				AeroPassengerSummary infantPassengerSummary = new AeroPassengerSummary();
				infantPassengerSummary.setPax(infantPax);
				if (infantPax != 0) {
					infantPassengerSummary.setPassengerType(AeroPassenger.PASSENGER_TYPE.INFANT);
					//Flight infant basic n12 -Rata kanan -Zero leading padding 
					infantPassengerSummary.setBasic(EngineUtils.getBigDecimalValue(bit48.substring(202, 214), 0));
	            	//Flight infant tax n12 -Rata kanan -Zero leading padding
					infantPassengerSummary.setTax(EngineUtils.getBigDecimalValue(bit48.substring(214, 226), 0));
	            	//Flight infant iwjr n12 -Rata kanan -Zero leading padding
					infantPassengerSummary.setIwjr(EngineUtils.getBigDecimalValue(bit48.substring(226, 238), 0));
	            	//Flight infant service n12 -Rata kanan -Zero leading padding 
					infantPassengerSummary.setService(EngineUtils.getBigDecimalValue(bit48.substring(238, 250), 0));
	            	//Flight infant total n12 -Rata kanan -Zero leading padding 
					infantPassengerSummary.setTotal(EngineUtils.getBigDecimalValue(bit48.substring(250, 262), 0));
				}
				flight.setInfantPassengerSummary(infantPassengerSummary);
            	//Flight commission n12 -Rata kanan -Zero leading padding
				flight.setComission(EngineUtils.getBigDecimalValue(bit48.substring(262, 274), 0));
            	//Flight total n12 -Rata kanan -Zero leading padding 
				flight.setTicketPrice(EngineUtils.getBigDecimalValue(bit48.substring(274, 286), 0));
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }

    }

	@Override
	public AeroFlightView getPriceDetail(AeroTicketingView view) {
		AeroFlightView flightView;
		if (view.getIsCheckingDepart()) {
			flightView = view.getDepartureFlight();
			flightView.setAirlineName(view.getDepartAirlineName());
		} else {
			flightView = view.getReturnFlight();
			flightView.setAirlineName(view.getReturnAirlineName());
		}
        Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroPriceDetailMessageCustomizer();
        customizer.compose(view, t);

        link.sendMessage();
        if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            customizer.decompose(flightView, t);
        }
        else {
        	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
//        	if (view.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
        		PacResponseError pre = gson.fromJson(t.getFreeData1(), PacResponseError.class);
        		if (pre != null && pre.getCode() != null) throw new BusinessException("IB-1009", t.getResponseCode(), pre.getDescription());
        		else throw new BusinessException("IB-1009", t.getResponseCode());
//        	} else {
//        		throw new BusinessException("IB-1009", t.getResponseCode());
//        	}
        }

        return flightView;
	}
	
    private class AeroIssueMessageCustomizer implements MessageCustomizer {

		private AeroIssueMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
            transaction.setFreeData1(atv.getBookId());
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	AeroTicketingView aero = (AeroTicketingView) view;
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (transaction.getResponseCode().equals("00")) {
            	String bit48 = transaction.getFreeData1();
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }
    }
	@Override
	public AeroTicketingView getIssue(AeroTicketingView view) {
		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroIssueMessageCustomizer();
        customizer.compose(view, t);

        link.sendMessage();
        if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            customizer.decompose(view, t);
        }
        else {
        	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
//        	if (view.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
        		PacResponseError pre = gson.fromJson(t.getFreeData1(), PacResponseError.class);
        		if (pre != null && pre.getCode() != null) throw new BusinessException("IB-1009", t.getResponseCode(), pre.getDescription());
        		else throw new BusinessException("IB-1009", t.getResponseCode());
//        	} else {
//        		throw new BusinessException("IB-1009", t.getResponseCode());
//        	}
        }

        return view;
	}

	public static void main(String[] args) {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
		//String bit48 = "[{\"pacFlights\":[{\"flightNo\":\"QG 815\",\"airlineCode\":\"QG\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 04:10:00\",\"arriveTime\":\"20 06 2014 05:35:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"H\",\"availableSeat\":6,\"fare\":833700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":6,\"fare\":888700.0,\"currency\":\"IDR\"},{\"code\":\"F\",\"availableSeat\":6,\"fare\":954700.0,\"currency\":\"IDR\"},{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 302\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 05:30:00\",\"arriveTime\":\"20 06 2014 07:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":1,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":3,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":3,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"QG 811\",\"airlineCode\":\"QG\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 06:10:00\",\"arriveTime\":\"20 06 2014 07:30:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 304\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 06:30:00\",\"arriveTime\":\"20 06 2014 08:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":4,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":0,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 306\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 07:50:00\",\"arriveTime\":\"20 06 2014 09:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":1,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":2,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":2,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"QG 801\",\"airlineCode\":\"QG\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 07:50:00\",\"arriveTime\":\"20 06 2014 09:10:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 308\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 08:50:00\",\"arriveTime\":\"20 06 2014 10:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":1,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":3,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":3,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 310\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 10:10:00\",\"arriveTime\":\"20 06 2014 11:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":5,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":5,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 312\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 11:10:00\",\"arriveTime\":\"20 06 2014 12:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":1,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"QG 803\",\"airlineCode\":\"QG\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 11:35:00\",\"arriveTime\":\"20 06 2014 12:55:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 314\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 12:30:00\",\"arriveTime\":\"20 06 2014 14:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":7,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":8,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 316\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 13:30:00\",\"arriveTime\":\"20 06 2014 15:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":1,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":8,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"QG 805\",\"airlineCode\":\"QG\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 13:40:00\",\"arriveTime\":\"20 06 2014 15:00:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 318\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 15:10:00\",\"arriveTime\":\"20 06 2014 16:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":6,\"fare\":1292400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":8,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":8,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":8,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":8,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":8,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 320\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 16:10:00\",\"arriveTime\":\"20 06 2014 17:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":4,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":4,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":4,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":4,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":8,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"QG 813\",\"airlineCode\":\"QG\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 16:15:00\",\"arriveTime\":\"20 06 2014 17:35:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 322\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 17:10:00\",\"arriveTime\":\"20 06 2014 18:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":1,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":4,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 324\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 18:10:00\",\"arriveTime\":\"20 06 2014 19:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":4,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":7,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"QG 817\",\"airlineCode\":\"QG\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 18:10:00\",\"arriveTime\":\"20 06 2014 19:40:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 326\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 19:30:00\",\"arriveTime\":\"20 06 2014 21:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 328\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 20:30:00\",\"arriveTime\":\"20 06 2014 22:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":1,\"fare\":1194500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":1,\"fare\":1292400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":1,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":4,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":4,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":4,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 330\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 21:50:00\",\"arriveTime\":\"20 06 2014 23:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":3,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":6,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":6,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 332\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"20 06 2014 00:00:00\",\"departTime\":\"20 06 2014 23:00:00\",\"arriveTime\":\"20 06 2014 00:35:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1194500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1292400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3474800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3649700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4003900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 303\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 05:25:00\",\"arriveTime\":\"23 06 2014 07:00:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":5,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":1,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"QG 800\",\"airlineCode\":\"QG\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 05:55:00\",\"arriveTime\":\"23 06 2014 07:15:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 305\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 06:30:00\",\"arriveTime\":\"23 06 2014 08:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 307\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 07:50:00\",\"arriveTime\":\"23 06 2014 09:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 309\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 08:50:00\",\"arriveTime\":\"23 06 2014 10:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"QG 802\",\"airlineCode\":\"QG\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 09:40:00\",\"arriveTime\":\"23 06 2014 11:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"H\",\"availableSeat\":6,\"fare\":833700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":6,\"fare\":888700.0,\"currency\":\"IDR\"},{\"code\":\"F\",\"availableSeat\":6,\"fare\":954700.0,\"currency\":\"IDR\"},{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 311\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 10:10:00\",\"arriveTime\":\"23 06 2014 11:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":2,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":1,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":3,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":3,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"QG 810\",\"airlineCode\":\"QG\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 10:40:00\",\"arriveTime\":\"23 06 2014 12:00:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"K\",\"availableSeat\":6,\"fare\":778700.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":6,\"fare\":833700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":6,\"fare\":888700.0,\"currency\":\"IDR\"},{\"code\":\"F\",\"availableSeat\":6,\"fare\":954700.0,\"currency\":\"IDR\"},{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 313\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 11:10:00\",\"arriveTime\":\"23 06 2014 12:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":7,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":1,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"QG 804\",\"airlineCode\":\"QG\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 11:35:00\",\"arriveTime\":\"23 06 2014 12:55:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"K\",\"availableSeat\":6,\"fare\":778700.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":6,\"fare\":833700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":6,\"fare\":888700.0,\"currency\":\"IDR\"},{\"code\":\"F\",\"availableSeat\":6,\"fare\":954700.0,\"currency\":\"IDR\"},{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 315\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 12:30:00\",\"arriveTime\":\"23 06 2014 14:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":5,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"QG 808\",\"airlineCode\":\"QG\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 13:15:00\",\"arriveTime\":\"23 06 2014 14:40:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"H\",\"availableSeat\":6,\"fare\":833700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":6,\"fare\":888700.0,\"currency\":\"IDR\"},{\"code\":\"F\",\"availableSeat\":6,\"fare\":954700.0,\"currency\":\"IDR\"},{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 317\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 13:30:00\",\"arriveTime\":\"23 06 2014 15:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 319\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 14:50:00\",\"arriveTime\":\"23 06 2014 16:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":4,\"fare\":1229500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":4,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 321\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 15:50:00\",\"arriveTime\":\"23 06 2014 17:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":1,\"fare\":1229500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":1,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":7,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 323\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 18:00:00\",\"arriveTime\":\"23 06 2014 19:35:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1229500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":9,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 325\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 18:40:00\",\"arriveTime\":\"23 06 2014 20:15:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":976500.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1229500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 327\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 19:30:00\",\"arriveTime\":\"23 06 2014 21:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":976500.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1229500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":8,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 329\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 20:30:00\",\"arriveTime\":\"23 06 2014 22:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":976500.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1229500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"QG 806\",\"airlineCode\":\"QG\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 20:55:00\",\"arriveTime\":\"23 06 2014 22:15:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"M\",\"availableSeat\":6,\"fare\":679700.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":6,\"fare\":723700.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":6,\"fare\":778700.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":6,\"fare\":833700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":6,\"fare\":888700.0,\"currency\":\"IDR\"},{\"code\":\"F\",\"availableSeat\":6,\"fare\":954700.0,\"currency\":\"IDR\"},{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 331\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 21:50:00\",\"arriveTime\":\"23 06 2014 23:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":855500.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1229500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"QG 814\",\"airlineCode\":\"QG\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 21:55:00\",\"arriveTime\":\"23 06 2014 23:15:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"N\",\"availableSeat\":6,\"fare\":646700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":6,\"fare\":679700.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":6,\"fare\":723700.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":6,\"fare\":778700.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":6,\"fare\":833700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":6,\"fare\":888700.0,\"currency\":\"IDR\"},{\"code\":\"F\",\"availableSeat\":6,\"fare\":954700.0,\"currency\":\"IDR\"},{\"code\":\"E\",\"availableSeat\":6,\"fare\":1031700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":1086700.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":6,\"fare\":1125200.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":6,\"fare\":1138400.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 333\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"CGK\",\"departDate\":\"23 06 2014 00:00:00\",\"departTime\":\"23 06 2014 22:50:00\",\"arriveTime\":\"23 06 2014 00:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":855500.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1229500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1327400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1398900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1429700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1448400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1464900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1480300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3509800.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3684700.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4038900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false}]";
		/*String bit48 = "[{\"pacFlights\":[{\"flightNo\":\"JT 34\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 04:30:00\",\"arriveTime\":\"30 08 2014 07:20:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":877300.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 400\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 05:40:00\",\"arriveTime\":\"30 08 2014 08:40:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":1,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":8,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":8,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 30\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 06:20:00\",\"arriveTime\":\"30 08 2014 09:10:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":811300.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 438\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 07:15:00\",\"arriveTime\":\"30 08 2014 10:15:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":3,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":4,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":8,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":8,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":3,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":4,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 32\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 07:20:00\",\"arriveTime\":\"30 08 2014 10:10:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":4,\"fare\":877300.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 402\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 07:45:00\",\"arriveTime\":\"30 08 2014 10:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":2,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":2,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":1,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":1,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 28\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 08:10:00\",\"arriveTime\":\"30 08 2014 11:00:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":2,\"fare\":1020300.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 404\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 09:40:00\",\"arriveTime\":\"30 08 2014 12:40:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":0,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 408\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 11:25:00\",\"arriveTime\":\"30 08 2014 14:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":0,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":0,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":0,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 22\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 11:30:00\",\"arriveTime\":\"30 08 2014 14:20:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":1,\"fare\":701300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":7,\"fare\":745300.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 36\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 12:30:00\",\"arriveTime\":\"30 08 2014 15:20:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":6,\"fare\":662800.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":7,\"fare\":745300.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 12\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 13:30:00\",\"arriveTime\":\"30 08 2014 16:20:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":7,\"fare\":662800.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":7,\"fare\":745300.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 410\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 14:00:00\",\"arriveTime\":\"30 08 2014 17:00:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"T\",\"availableSeat\":4,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":0,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 18\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 14:45:00\",\"arriveTime\":\"30 08 2014 17:35:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":4,\"fare\":618800.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":7,\"fare\":745300.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 420\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 16:15:00\",\"arriveTime\":\"30 08 2014 19:15:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":9,\"fare\":1237400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":9,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 24\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 16:50:00\",\"arriveTime\":\"30 08 2014 19:40:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":7,\"fare\":618800.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":7,\"fare\":745300.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 412\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 17:05:00\",\"arriveTime\":\"30 08 2014 20:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":9,\"fare\":1237400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 414\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 17:20:00\",\"arriveTime\":\"30 08 2014 20:20:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":9,\"fare\":1237400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 426\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 17:45:00\",\"arriveTime\":\"30 08 2014 20:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":9,\"fare\":1237400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 26\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 17:55:00\",\"arriveTime\":\"30 08 2014 20:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":7,\"fare\":618800.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":7,\"fare\":745300.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 16\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 18:55:00\",\"arriveTime\":\"30 08 2014 21:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":7,\"fare\":618800.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":7,\"fare\":745300.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 568\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 19:00:00\",\"arriveTime\":\"30 08 2014 23:00:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":1086300.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 418\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 19:05:00\",\"arriveTime\":\"30 08 2014 22:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":9,\"fare\":1237400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 422\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 20:30:00\",\"arriveTime\":\"30 08 2014 23:30:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":9,\"fare\":1237400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"JT 10\",\"airlineCode\":\"JT\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 20:40:00\",\"arriveTime\":\"30 08 2014 23:30:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":7,\"fare\":618800.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":7,\"fare\":745300.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"L\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"H\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"W\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"A\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":7,\"fare\":0.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 652\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 21:30:00\",\"arriveTime\":\"30 08 2014 00:30:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":9,\"fare\":1237400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 424\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"DPS\",\"departDate\":\"30 08 2014 00:00:00\",\"departTime\":\"30 08 2014 21:55:00\",\"arriveTime\":\"30 08 2014 00:55:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"V\",\"availableSeat\":9,\"fare\":1237400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1416700.0,\"currency\":\"IDR\"},{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1577300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1644400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":2,\"fare\":3662900.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":3845500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":4210700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true}]";

		PacAirItem[] mcArray = gson.fromJson(bit48, PacAirItem[].class);
    	List<PacAirItem> pacAirItems = Arrays.asList(mcArray);
    	System.out.println(pacAirItems);*/

/*
        //String str = "{\"departureAirportCode\":\"CGK\",\"ticketPrice\":920600,\"arrivalAirportCode\":\"SUB\",\"departureAirportFullName\":null,\"arrivalAirportFullName\":null,\"flightNumber\":\"GA 332\",\"etaConnecting\":\"10 07 2014 12:05:00\",\"isConnectingFlight\":true,\"aeroConnectingFlight\":\"{\\\"throughFare\\\":false,\\\"connectingFlightEta\\\":\\\"11 07 2014 09:20:00\\\",\\\"connectingFlightFrom\\\":\\\"SUB\\\",\\\"connectingFlightTo\\\":\\\"BPN\\\",\\\"connectingFlightDate\\\":\\\"11 07 2014 06:45:00\\\",\\\"connectingFlightFno\\\":\\\"GA 350\\\",\\\"connectingFlightEtd\\\":\\\"11 07 2014 06:45:00\\\",\\\"connectingFlightClassname\\\":\\\"H\\\",\\\"connectingFlightPrice\\\":711400,\\\"connectingFlightSeat\\\":9,\\\"id\\\":null,\\\"updated\\\":null,\\\"createdby\\\":null,\\\"created\\\":null,\\\"updatedby\\\":null}\",\"aeroConnectingFlight2\":\"{\\\"throughFare\\\":false,\\\"connectingFlightEta\\\":\\\"11 07 2014 12:05:00\\\",\\\"connectingFlightFrom\\\":\\\"BPN\\\",\\\"connectingFlightTo\\\":\\\"TRK\\\",\\\"connectingFlightDate\\\":null,\\\"connectingFlightFno\\\":\\\"GA 668\\\",\\\"connectingFlightEtd\\\":\\\"11 07 2014 11:05:00\\\",\\\"connectingFlightClassname\\\":\\\"H\\\",\\\"connectingFlightPrice\\\":745500,\\\"connectingFlightSeat\\\":8,\\\"id\\\":null,\\\"updated\\\":null,\\\"createdby\\\":null,\\\"created\\\":null,\\\"updatedby\\\":null}\",\"aeroFlightClasses\":[{\"value\":null,\"className\":\"H\",\"avaliableSeat\":1,\"classLabel\":null,\"classId\":\"H\",\"price\":920600,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"T\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"T\",\"price\":1542100,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Q\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"Q\",\"price\":1812700,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"N\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"N\",\"price\":1976600,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"K\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"K\",\"price\":2066800,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"M\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"M\",\"price\":2076700,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"B\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"B\",\"price\":2116300,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Y\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"Y\",\"price\":2146000,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"D\",\"avaliableSeat\":2,\"classLabel\":null,\"classId\":\"D\",\"price\":5551600,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"C\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"C\",\"price\":5835400,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"J\",\"avaliableSeat\":9,\"classLabel\":null,\"classId\":\"J\",\"price\":6401900,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"selectedClassId\":\"T\",\"selectedClass\":{\"value\":null,\"className\":\"H\",\"avaliableSeat\":1,\"classLabel\":null,\"classId\":\"T\",\"price\":920600,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":true,\"adultPassengerSummary\":{\"value\":\"Adult 1\",\"total\":920600,\"basic\":920600,\"service\":null,\"tax\":null,\"passengerType\":\"Adult\",\"iwjr\":null,\"pax\":1,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"childPassengerSummary\":{\"value\":\"Child 0\",\"total\":920600,\"basic\":920600,\"service\":null,\"tax\":null,\"passengerType\":\"Child\",\"iwjr\":null,\"pax\":0,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"infantPassengerSummary\":{\"value\":\"Infant 0\",\"total\":920600,\"basic\":920600,\"service\":null,\"tax\":null,\"passengerType\":\"Infant\",\"iwjr\":null,\"pax\":0,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"sessionId\":null,\"airlineId\":\"GA\",\"airlineName\":\"Garuda\",\"flightDate\":\"10 07 2014 05:35:00\",\"flightType\":\"connecting\",\"etd\":\"12 07 2014 23:06:00\",\"eta\":\"12 07 2014 23:06:00\",\"via\":\"-\",\"comission\":null,\"bookInfo\":null,\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"referenceNumber\":null,\"description\":null,\"bit22\":null,\"toAccountType\":null,\"cardData1\":null,\"billerName\":null,\"customerId\":null,\"billerCode\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"VOLT\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"25 06 2014 17:08:53\",\"productName\":null,\"feeIndicator\":null,\"save\":null,\"inputType\":null,\"referenceName\":null,\"providerName\":null,\"transactionDateString\":\"25/06/2014 17:08 \",\"productCode\":null,\"providerCode\":\"VOLT\",\"transactionType\":\"9m\",\"customerReference\":null,\"accountNumber\":\"1574087868\",\"traceNumber\":null}";
		String str = "{\"airlineId\":\"GA\",\"sessionId\":null,\"departureAirportCode\":\"CGK\",\"ticketPrice\":1542100,\"airlineName\":\"Garuda\",\"flightDate\":\"10 07 2014 05:35:00\",\"departureAirportFullName\":null,\"arrivalAirportFullName\":null,\"flightNumber\":\"GA 560\",\"etaConnecting\":\"10 07 2014 12:05:00\",\"isConnectingFlight\":true,\"aeroConnectingFlight\":{\"connectingFlightFrom\":\"BPN\",\"connectingFlightTo\":\"TRK\",\"connectingFlightDate\":\"10 07 2014 11:05:00\",\"connectingFlightFno\":\"GA 668\",\"connectingFlightEtd\":\"10 07 2014 11:05:00\",\"connectingFlightClassname\":\"H\",\"connectingFlightPrice\":745500,\"connectingFlightSeat\":9,\"throughFare\":false,\"connectingFlightEta\":\"10 07 2014 12:05:00\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"arrivalAirportCode\":\"BPN\",\"aeroConnectingFlight2\":null,\"aeroFlightClasses\":[{\"value\":null,\"className\":\"T\",\"avaliableSeat\":9,\"price\":1542100,\"classLabel\":null,\"classId\":\"T\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Q\",\"avaliableSeat\":9,\"price\":1812700,\"classLabel\":null,\"classId\":\"Q\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"N\",\"avaliableSeat\":9,\"price\":1976600,\"classLabel\":null,\"classId\":\"N\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"K\",\"avaliableSeat\":9,\"price\":2066800,\"classLabel\":null,\"classId\":\"K\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"M\",\"avaliableSeat\":9,\"price\":2076700,\"classLabel\":null,\"classId\":\"M\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"B\",\"avaliableSeat\":9,\"price\":2116300,\"classLabel\":null,\"classId\":\"B\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Y\",\"avaliableSeat\":9,\"price\":2146000,\"classLabel\":null,\"classId\":\"Y\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"D\",\"avaliableSeat\":2,\"price\":5551600,\"classLabel\":null,\"classId\":\"D\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"C\",\"avaliableSeat\":9,\"price\":5835400,\"classLabel\":null,\"classId\":\"C\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"J\",\"avaliableSeat\":9,\"price\":6401900,\"classLabel\":null,\"classId\":\"J\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"selectedClassId\":\"T\",\"selectedClass\":{\"value\":null,\"className\":\"T\",\"avaliableSeat\":9,\"price\":1542100,\"classLabel\":null,\"classId\":\"T\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":true,\"adultPassengerSummary\":{\"value\":\"Adult 1\",\"total\":1542100,\"service\":null,\"basic\":1542100,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Adult\",\"pax\":1,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"childPassengerSummary\":{\"value\":\"Child 1\",\"total\":1542100,\"service\":null,\"basic\":1542100,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Child\",\"pax\":1,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"infantPassengerSummary\":{\"value\":\"Infant 0\",\"total\":1542100,\"service\":null,\"basic\":1542100,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Infant\",\"pax\":0,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"flightType\":\"connecting\",\"etd\":\"12 07 2014 15:31:00\",\"eta\":\"12 07 2014 15:31:00\",\"via\":\"-\",\"comission\":null,\"bookInfo\":null,\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"description\":null,\"referenceNumber\":null,\"transactionType\":\"9m\",\"cardData1\":null,\"bit22\":null,\"billerCode\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"VOLT\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"billerName\":null,\"customerId\":null,\"customerReference\":null,\"accountNumber\":\"1574087868\",\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"25 06 2014 19:59:23\",\"productName\":null,\"feeIndicator\":null,\"referenceName\":null,\"providerName\":null,\"save\":null,\"inputType\":null,\"transactionDateString\":\"25/06/2014 19:59 \",\"toAccountType\":null,\"traceNumber\":null}";

		//\"connectingFlightEta\":\"10 07 2014 12:05:00\"
		Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
		AeroFlightView departFlight = gson.fromJson(str, AeroFlightView.class);
		
        //AeroFlightView departFlight = PojoJsonMapper.fromJson(str, AeroFlightView.class);
        System.out.println("departFlight=" + departFlight);
*/
        //String conn = "{\"throughFare\":false,\"connectingFlightEta\":\"11 07 2014 09:20:00\",\"connectingFlightFrom\":\"SUB\",\"connectingFlightTo\":\"BPN\",\"connectingFlightDate\":\"11 07 2014 06:45:00\",\"connectingFlightFno\":\"GA 350\",\"connectingFlightEtd\":\"11 07 2014 06:45:00\",\"connectingFlightClassname\":\"H\",\"connectingFlightPrice\":711400,\"connectingFlightSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}";
//        String conn = "{\"throughFare\":false,\"connectingFlightEta\":\"11 07 2014 12:05:00\",\"connectingFlightFrom\":\"BPN\",\"connectingFlightTo\":\"TRK\",\"connectingFlightDate\":null,\"connectingFlightFno\":\"GA 668\",\"connectingFlightEtd\":\"11 07 2014 11:05:00\",\"connectingFlightClassname\":\"H\",\"connectingFlightPrice\":745500,\"connectingFlightSeat\":8,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}";
//        AeroConnectingFlight connFlight = PojoJsonMapper.fromJson(conn, AeroConnectingFlight.class);
//        System.out.println("connFlight=" + connFlight);

        String input = "{\"sessionId\":null,\"passengers\":[{\"value\":\"null null\",\"parent\":null,\"country\":\"INDONESIA\",\"paspor\":null,\"passengerLastName\":null,\"passengerFirstName\":null,\"passengerType\":\"Adult\",\"passengerTitle\":null,\"passengerDob\":\"11 12 2014 12:16:45\",\"passengerIdCard\":null,\"expirePaspor\":null,\"countryPaspor\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"totalAdult\":1,\"aeroCustomer\":null,\"departureAirportCode\":\"CGK\",\"destinationAirportCode\":\"SUB\",\"departureFlight\":null,\"isDepartOnly\":false,\"returnFlight\":null,\"totalChildren\":0,\"isCheckingDepart\":null,\"departAirlineName\":null,\"returnAirlineName\":null,\"destinationAirportName\":\"SURABAYA\",\"totalInfant\":0,\"departureAirportName\":\"JAKARTA\",\"ticketPrice\":0,\"ticketComission\":0,\"assuranceComission\":0,\"agentMargin\":10000.0,\"totalAgentComission\":0,\"returnAirlineCode\":\"QZ,GA\",\"departAirlineCode\":\"QZ,GA\",\"assurancePrice\":0,\"totalAgentPrice\":0,\"totalCustomerPrice\":0,\"assuranceType\":null,\"assurancePolis\":null,\"pacBook\":null,\"departDate\":\"20 12 2014 00:00:00\",\"returnDate\":\"29 12 2014 00:00:00\",\"agentPrice\":0,\"bookId\":null,\"responseCode\":null,\"total\":null,\"description\":null,\"currencyCode\":\"360\",\"referenceNumber\":null,\"providerName\":null,\"transactionDateString\":\"11/12/2014 12:16 \",\"billerName\":null,\"billerCode\":null,\"customerId\":102114,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"I1516288\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"cardData1\":null,\"transactionType\":\"9e\",\"customerReference\":null,\"accountNumber\":\"1574087868\",\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"11 12 2014 12:16:45\",\"productName\":null,\"feeIndicator\":null,\"referenceName\":null,\"toAccountType\":\"00\",\"bit22\":null,\"save\":false,\"inputType\":null,\"traceNumber\":null}";

        Gson gson1 = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss")
                .serializeNulls()
//                .registerTypeAdapter(AeroTicketingView.class, new AeroTicketingViewAdapter())
                .create();

        AeroTicketingView aeroView = gson.fromJson(input, AeroTicketingView.class);
        String withNull = gson.toJson(aeroView);
        String withoutNull = gson1.toJson(aeroView);

        System.out.println("withNull=" + withNull);
        System.out.println("withoutNull=" + withoutNull);
		
	}
}
