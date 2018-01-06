package com.dwidasa.engine.service.impl.view;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.airline.AirSearchFlightDetail;
import com.dwidasa.engine.model.airline.PacResponseError;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.AeroTicketingSearchDetailViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("aeroTicketingSearchDetailViewService")
public class AeroTicketingSearchDetailViewServiceImpl implements AeroTicketingSearchDetailViewService {
	@Autowired
	private CacheManager cacheManager;

	private static Logger logger = Logger.getLogger( AeroTicketingSearchDetailViewServiceImpl.class );

    public AeroTicketingSearchDetailViewServiceImpl() {

    }

    private class AeroSearchDetailMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private AeroSearchDetailMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
            String customData = "";
            
            AirSearchFlightDetail airDetail = new AirSearchFlightDetail();
            AeroFlightView departFlight = atv.getDepartureFlight();
            AeroFlightView returnFlight = atv.getReturnFlight();
            airDetail.setDepartFlight(departFlight);
            airDetail.setReturnFlight(returnFlight);
            airDetail.setWithInsurance(false);
            
//            customData = PojoJsonMapper.toJson(airDetail);
            customData = gson.toJson(airDetail);
            transaction.setFreeData1(customData);
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	AeroTicketingView aero = (AeroTicketingView) view;
            if (transaction.getResponseCode().equals("00")) {
/*            	"JSRS6278790011900001   9l1000000000000000040815004504011015004504086014C0000000000000091407700I1516288AERO.SRC.DET,0001,0001        1574087868         
            	{\"departureAirportCode\":\"BPN\",\"departDate\":\"10 04 2014 00:00:00\",\"returnDate\":\"13 04 2014 00:00:00\",
            	\"isDepartOnly\":false,\"totalAdult\":2,\"totalChildren\":1,\"totalInfant\":0,
            	\"departureFlight\":{\"airlineId\":\"3\",\"airlineName\":\"Citilink\",\"flightType\":\"direct\",\"departureAirportCode\":\"SUB\",
            	\"arrivalAirportCode\":\"BPN\",\"flightDate\":\"10 04 2014 00:00:00\",\"flightNumber\":\"QG 630\",\"etd\":\"08 04 2014 15:02:06\",
            	\"eta\":\"08 04 2014 15:02:06\",\"selectedClassId\":\"Q\",\"comission\":21750.0,
            	\"adultPassengerSummary\":{\"passengerType\":\"Adult\",\"pax\":2,\"basic\":440000.0,\"tax\":44000.0,\"iwjr\":5000.0,\"service\":156400.0,\"total\":1290800.0},
            	\"childPassengerSummary\":{\"passengerType\":\"Adult\",\"pax\":1,\"basic\":330000.0,\"tax\":33000.0,\"iwjr\":5000.0,\"service\":156400.0,\"total\":524400.0},
            	\"total\":1361450.0},
            	\"returnFlight\":{\"airlineId\":\"3\",\"airlineName\":\"Citilink\",\"flightType\":\"direct\",\"departureAirportCode\":\"BPN\",
            	\"arrivalAirportCode\":\"SUB\",\"flightDate\":\"13 04 2014 00:00:00\",\"flightNumber\":\"QG 631\",\"etd\":\"08 04 2014 15:02:06\",
            	\"eta\":\"08 04 2014 15:02:06\",\"selectedClassId\":\"N\",\"comission\":33000.0,\"total\":1815200.0}}\n";
*/
            	//{"returnFlight":null,
            	//"departFlight":{"sessionId":"F_jTBvfupAOjKKOyw-shHW-b7sfW52O0xazAZNYttJ4","airlineId":"1",
            	//"airlineName":"Airasia","departureAirportCode":"CGK","ticketPrice":661600,"flightType":"direct",
            	//"arrivalAirportCode":"SUB","departureAirportFullName":null,"arrivalAirportFullName":null,
            	//"flightNumber":"QZ 7680","etd":"15 08 2014 07:40:00","eta":"15 08 2014 08:55:00",
            	//"etaConnecting":"15 08 2014 08:55:00","via":"-","flightDate":"15 08 2014 07:40:00",
            	//"isConnectingFlight":false,"aeroConnectingFlight":null,"aeroConnectingFlight2":null,
            	//"aeroFlightClasses":[{"value":null,"className":"Promo","price":661600,"classId":"DQZ1",
            	//"classLabel":null,"avaliableSeat":5,"id":null,"updated":null,"created":null,"createdby":null,
            	//"updatedby":null},{"value":null,"className":"Reguler","price":841600,"classId":"DQZ2",
            	//"classLabel":null,"avaliableSeat":5,"id":null,"updated":null,"created":null,"createdby":null,
            	//"updatedby":null}],"selectedClassId":"DQZ1","comission":null,
            	//"selectedClass":{"value":null,"className":"Promo","price":661600,"classId":"DQZ1","classLabel":null,
            	//"avaliableSeat":5,"id":null,"updated":null,"created":null,"createdby":null,"updatedby":null},
            	//"isDepartureFlight":true,"adultPassengerSummary":{"value":"Adult 1","total":661600,"service":null,
            	//"basic":661600,"pax":1,"passengerType":"Adult","tax":null,"iwjr":null,"id":null,"updated":null,
            	//"created":null,"createdby":null,"updatedby":null},"childPassengerSummary":{"value":"Child 0",
            	//"total":661600,"service":null,"basic":661600,"pax":0,"passengerType":"Child","tax":null,"iwjr":null,"id":null,"updated":null,
            	//"created":null,"createdby":null,"updatedby":null},"infantPassengerSummary":{"value":"Infant 0","total":661600,"service":null,"basic":661600,"pax":0,"passengerType":"Infant","tax":null,"iwjr":null,"id":null,"updated":null,"created":null,"createdby":null,"updatedby":null},"bookInfo":null,"responseCode":null,"total":null,"description":null,"currencyCode":null,"referenceNumber":null,"billerName":null,"transactionType":"9m","billerCode":null,"customerReference":null,"accountNumber":"1574087868","customerId":null,"productCode":null,"providerCode":"AERO","cardNumber":"6278790011900001","terminalId":"AERO","accountType":"10","merchantType":"6014","amount":null,"transactionDate":"05 08 2014 13:31:09","productName":null,"fee":null,"feeIndicator":null,"cardData2":null,"bit48":null,"referenceName":null,"toAccountType":null,"cardData1":null,"bit22":null,"providerName":null,"transactionDateString":"05/08/2014 13:31 ","save":null,"inputType":null,"traceNumber":null},"withInsurance":false,"sessionId":null,"token":null,"providerCode":null,"appType":null}

            	String bit48 = transaction.getFreeData1();
            	logger.info("bit48=" + bit48);
            	
            	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
    			AeroTicketingView fromIgate = gson.fromJson(bit48, AeroTicketingView.class);
//    			aero.setDepartureAirportCode(fromIgate.getDepartureAirportCode());
//    			aero.setDepartDate(fromIgate.getDepartDate());
//    			aero.setReturnDate(fromIgate.getReturnDate());
//    			aero.setIsDepartOnly(fromIgate.getIsDepartOnly());
//    			aero.setTotalAdult(fromIgate.getTotalAdult());
//    			aero.setTotalChildren(fromIgate.getTotalChildren());
//    			aero.setTotalInfant(fromIgate.getTotalInfant());
    			
    			logger.info("aero.getDepartureFlight()=" + aero.getDepartureFlight());
    			AeroFlightView departFlight = fromIgate.getDepartureFlight();
    			logger.info("departFlight=" + departFlight);
    			logger.info("departFlight.getAdultPassengerSummary()=" + departFlight.getAdultPassengerSummary());
    			logger.info("departFlight.getAdultPassengerSummary().getTotal()=" + departFlight.getAdultPassengerSummary().getTotal());
    			aero.setDepartureFlight(departFlight); //sudah termasuk adultPassengerSummary, child, infant

    			if (!fromIgate.getIsDepartOnly()) {
        			AeroFlightView returnFlight = fromIgate.getReturnFlight();
        			logger.info("returnFlight=" + returnFlight);
        			logger.info("returnFlight.getAdultPassengerSummary()=" + returnFlight.getAdultPassengerSummary());
        			logger.info("returnFlight.getAdultPassengerSummary().getTotal()=" + returnFlight.getAdultPassengerSummary().getTotal());
        			aero.setReturnFlight(returnFlight);
    			}
    			
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }
		
    }

	@Override
	public AeroTicketingView getSeachDetail(AeroTicketingView view) {
        Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        logger.info("view.getTransactionType()=" + view.getTransactionType());
        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroSearchDetailMessageCustomizer();
        logger.info("t.getTransactionType()=" + t.getTransactionType());
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
		String bit48 = "{\"departureAirportCode\":\"DPS\",\"departDate\":\"25 08 2014 00:00:00\",\"isDepartOnly\":true,\"totalAdult\":1,\"totalChildren\":1,\"totalInfant\":1,\"departureFlight\":{\"airlineId\":\"3\",\"airlineName\":\"Citilink\",\"flightType\":\"direct\",\"departureAirportCode\":\"BDO\",\"arrivalAirportCode\":\"DPS\",\"flightDate\":\"25 08 2014 00:00:00\",\"flightNumber\":\"QG 889\",\"etd\":\"12 08 2014 14:17:26\",\"eta\":\"12 08 2014 14:17:26\",\"selectedClassId\":\"L\",\"comission\":10000.0,\"adultPassengerSummary\":{\"passengerType\":\"Adult\",\"pax\":1,\"basic\":515000.0,\"tax\":51500.0,\"iwjr\":5000.0,\"service\":108600.0,\"total\":685100.0},\"childPassengerSummary\":{\"passengerType\":\"Adult\",\"pax\":1,\"basic\":200000.0,\"tax\":20000.0,\"iwjr\":5000.0,\"service\":0.0,\"total\":225000.0},\"total\":1453575.0}}";
		
//    	List<PacAirItem> pacAirItems = PojoJsonMapper.fromJson(bit48, new TypeReference<List<PacAirItem>>() {} );
		Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
		AeroTicketingView atv = gson.fromJson(bit48, AeroTicketingView.class);
    	System.out.println(atv.getDepartureFlight());
    	System.out.println("atv.getTotal()=" + atv.getTotal());
    	System.out.println("atv.getTerminalId()=" + atv.getTerminalId());
    	System.out.println("atv.getCustomerReference()=" + atv.getCustomerReference());    	
    	System.out.println("atv.getDepartureFlight().getEtd()=" + atv.getDepartureFlight().getEtd());
    	System.out.println("atv.getDepartureFlight().getEta()=" + atv.getDepartureFlight().getEta());
	}
}
