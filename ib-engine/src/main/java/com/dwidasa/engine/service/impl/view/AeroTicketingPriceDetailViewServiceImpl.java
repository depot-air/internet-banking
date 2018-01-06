package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.airline.PacAirFare;
import com.dwidasa.engine.model.airline.PacAirItem;
import com.dwidasa.engine.model.airline.PacResponseError;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.AeroTicketingPriceDetailViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("aeroTicketingPriceDetailViewService")
public class AeroTicketingPriceDetailViewServiceImpl implements AeroTicketingPriceDetailViewService {
	@Autowired
	private CacheManager cacheManager;

	private static Logger logger = Logger
			.getLogger(AeroTicketingPriceDetailViewServiceImpl.class);

	public AeroTicketingPriceDetailViewServiceImpl() {

	}

	private class AeroPriceDetailMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private AeroPriceDetailMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			AeroFlightView atv = (AeroFlightView) view;
//			String customData = PojoJsonMapper.toJson(atv);
            String customData = gson.toJson(atv);
			logger.info("customData=" + customData);
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
/*            	"{"airlineId":"3","airlineName":"Citilink","flightType":"direct","departureAirportCode":"SUB","arrivalAirportCode":"BPN","flightDate":"18 04 2014 00:00:00","flightNumber":"QG 630","etd":"18 04 2014 06:20:00","eta":"18 04 2014 08:50:00","via":"-","selectedClassId":"DQG1","comission":12950.0,"selectedClass":{"classId":"DQG1","className":"R"},"adultPassengerSummary":{"passengerType":"Adult","pax":1,"basic":259000.0,"tax":25900.0,"iwjr":1.0,"service":156400.0,"total":446300.0},"childPassengerSummary":{"passengerType":"Adult","pax":1,"basic":194250.0,"tax":19425.0,"iwjr":1.0,"service":156400.0,"total":375075.0},"total":821375.0}";
*/
            	String bit48 = transaction.getFreeData1();
            	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
    			AeroFlightView fromIgate = gson.fromJson(bit48, AeroFlightView.class);
    			flight.setAdultPassengerSummary(fromIgate.getAdultPassengerSummary());
    			flight.setChildPassengerSummary(fromIgate.getChildPassengerSummary());
    			flight.setInfantPassengerSummary(fromIgate.getInfantPassengerSummary());
    			logger.info("fromIgate.getChildPassengerSummary()=" + fromIgate.getChildPassengerSummary());
    			logger.info("fromIgate.getInfantPassengerSummary()=" + fromIgate.getInfantPassengerSummary());
    			if (fromIgate.getInfantPassengerSummary() != null) {
    				logger.info("fromIgate.getInfantPassengerSummary().getTotal()=" + fromIgate.getInfantPassengerSummary().getTotal());
    			}
    			flight.setComission(fromIgate.getComission());
    			flight.setTotal(fromIgate.getTotal());
    							
			} else {
				return Boolean.FALSE;

			}
			return Boolean.TRUE;
		}

	}

	@Override
	public AeroFlightView getPriceDetail(AeroFlightView view) {

		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
		t.setTranslationCode(null);
		t.setFreeData1(new String());

		CommLink link = new MxCommLink(t);
		MessageCustomizer customizer = new AeroPriceDetailMessageCustomizer();
		customizer.compose(view, t);

		link.sendMessage();
		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			customizer.decompose(view, t);
		} else {
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

	private class VoltrasFareMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private VoltrasFareMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			AeroTicketingView atv = (AeroTicketingView) view;
//			String customData = PojoJsonMapper.toJson(atv);
            String customData = gson.toJson(atv);
			logger.info("customData=" + customData);
			transaction.setFreeData1(customData);
		}

		/**
		 * {@inheritDoc}
		 */
		public Boolean decompose(Object view, Transaction transaction) {
			AeroTicketingView atv = (AeroTicketingView) view;
			
			if (transaction.getResponseCode().equals("00")) {
/*            	"{"airlineId":"3","airlineName":"Citilink","flightType":"direct","departureAirportCode":"SUB","arrivalAirportCode":"BPN","flightDate":"18 04 2014 00:00:00","flightNumber":"QG 630","etd":"18 04 2014 06:20:00","eta":"18 04 2014 08:50:00","via":"-","selectedClassId":"DQG1","comission":12950.0,"selectedClass":{"classId":"DQG1","className":"R"},"adultPassengerSummary":{"passengerType":"Adult","pax":1,"basic":259000.0,"tax":25900.0,"iwjr":1.0,"service":156400.0,"total":446300.0},"childPassengerSummary":{"passengerType":"Adult","pax":1,"basic":194250.0,"tax":19425.0,"iwjr":1.0,"service":156400.0,"total":375075.0},"total":821375.0}";
*/
            	String bit48 = transaction.getFreeData1();
            	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
            	AeroFlightView departFlight = atv.getDepartureFlight();
            	AeroFlightView returnFlight = null;
            	if (!atv.getIsDepartOnly()) {
            		returnFlight = atv.getReturnFlight();
            	}
            	
            	PacAirFare airFare = gson.fromJson(bit48, PacAirFare.class);
            	if (airFare.getSecondAirlineCode() == null || airFare.getSecondAirlineCode().equals("")) {
            		//hanya ada satu airFare
            		if (atv.getIsDepartOnly()) {
            			//berangkat saja
            			departFlight.setTotal(airFare.getFirstFare());
            		} 
            		atv.setTotal(airFare.getFirstFare());
            	} else {
            		departFlight.setTotal(airFare.getFirstFare());
            		if (atv.getIsDepartOnly()) {
            			atv.setTotal(airFare.getFirstFare());
            		} else {
            			returnFlight.setTotal(airFare.getSecondFare());
            			atv.setTotal(airFare.getFirstFare().add(airFare.getSecondFare()));
            		}
            	}
            	atv.setTotal(atv.getTotal().add(airFare.getInsurance()));				
			} else {
				return Boolean.FALSE;

			}
			return Boolean.TRUE;
		}

	}

	@Override
	public AeroTicketingView getVoltrasFare(AeroTicketingView view) {

		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
		t.setTranslationCode(null);
		t.setFreeData1(new String());

		CommLink link = new MxCommLink(t);
		MessageCustomizer customizer = new VoltrasFareMessageCustomizer();
		customizer.compose(view, t);

		link.sendMessage();
		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			customizer.decompose(view, t);
		} else {
			Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
        	if (view.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
        		PacResponseError pre = gson.fromJson(t.getFreeData1(), PacResponseError.class);
        		if (pre != null && pre.getCode() != null) throw new BusinessException("IB-1009", t.getResponseCode(), pre.getDescription());
        		else throw new BusinessException("IB-1009", t.getResponseCode());
        	} else {
        		throw new BusinessException("IB-1009", t.getResponseCode());
        	}
		}

		return view;
	}

	private class DepositInformationMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private DepositInformationMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			AeroTicketingView atv = new AeroTicketingView();
//			String customData = PojoJsonMapper.toJson(atv);
            String customData = gson.toJson(atv);
			logger.info("customData=" + customData);
			transaction.setFreeData1(customData);
		}

		/**
		 * {@inheritDoc}
		 */
		public Boolean decompose(Object view, Transaction transaction) {
			AeroTicketingView atv = (AeroTicketingView) view;
			
			if (transaction.getResponseCode().equals("00")) {
				String bit48 = transaction.getFreeData1();
				atv.setBit48(bit48);
			} else {
				return Boolean.FALSE;

			}
			return Boolean.TRUE;
		}

	}

	@Override
	public String getDepositInformation() {
		AeroTicketingView view = new AeroTicketingView();
		view.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.OFFICE_INFORMATION);
		view.setCardNumber("123456890123456");
		view.setAmount(BigDecimal.ZERO);
		view.setFeeIndicator("C");
		view.setFee(BigDecimal.ZERO);
		view.setTransactionDate(new Date());
		view.setMerchantType("6014");
		view.setTerminalId("IBS");
		view.setCurrencyCode("360");
		view.setCustomerReference("1");
		view.setAccountNumber("1234567890");
		view.setAccountType("10");
		view.setToAccountType("00");		
//      transaction.setCardData1(atv.getCardData1());
//      transaction.setCardData2(atv.getCardData2());
//      transaction.setBit22(atv.getBit22());
		view.setBillerCode("VOLT");
		view.setProductCode("VOLT");
		view.setProviderCode("VOLT");
		
		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
		t.setTranslationCode(null);
		t.setFreeData1(new String());

		CommLink link = new MxCommLink(t);
		MessageCustomizer customizer = new DepositInformationMessageCustomizer();
		customizer.compose(view, t);

		link.sendMessage();
		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			customizer.decompose(view, t);
			return view.getBit48();
		} else {
			Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
//        	if (view.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
        		PacResponseError pre = gson.fromJson(t.getFreeData1(), PacResponseError.class);
        		if (pre != null && pre.getCode() != null) throw new BusinessException("IB-1009", t.getResponseCode(), pre.getDescription());
        		else throw new BusinessException("IB-1009", t.getResponseCode());
//        	} else {
//        		throw new BusinessException("IB-1009", t.getResponseCode());
//        	}
		}
	}

	public static void main(String[] args) {
		/*String bit48 = "[{\"pacFlights\":[{\"flightNo\":\"GA 334\",\"airlineCode\":\"GA\",\"fromAirport\":\"BDO\",\"toAirport\":\"DPS\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 12:35:00\",\"arriveTime\":\"18 04 2014 15:15:00\"},{\"flightNo\":\"GA 347\",\"airlineCode\":\"GA\",\"fromAirport\":\"DPS\",\"toAirport\":\"SUB\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 19:25:00\",\"arriveTime\":\"18 04 2014 19:25:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":1002400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1100300.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1210300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1273000.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1346700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1424800.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1508400.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1524900.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":2203600.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2865800.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3141900.0,\"currency\":\"IDR\"}]},{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":576300.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":796300.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":837000.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":871100.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":877700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":884300.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":892000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":900800.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2020600.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":2215300.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 334\",\"airlineCode\":\"GA\",\"fromAirport\":\"BDO\",\"toAirport\":\"DPS\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 12:35:00\",\"arriveTime\":\"18 04 2014 15:15:00\"},{\"flightNo\":\"GA 439\",\"airlineCode\":\"GA\",\"fromAirport\":\"DPS\",\"toAirport\":\"CGK\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 16:15:00\",\"arriveTime\":\"18 04 2014 17:10:00\"},{\"flightNo\":\"GA 324\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 18:10:00\",\"arriveTime\":\"18 04 2014 19:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":1002400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1100300.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1210300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1273000.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1346700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1424800.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1508400.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1524900.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":2203600.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2865800.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3141900.0,\"currency\":\"IDR\"}]},{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1622400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":5,\"fare\":3626600.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":7,\"fare\":3975300.0,\"currency\":\"IDR\"}]},{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1194500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1292400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3481400.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3818000.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 334\",\"airlineCode\":\"GA\",\"fromAirport\":\"BDO\",\"toAirport\":\"DPS\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 12:35:00\",\"arriveTime\":\"18 04 2014 15:15:00\"},{\"flightNo\":\"GA 349\",\"airlineCode\":\"GA\",\"fromAirport\":\"DPS\",\"toAirport\":\"SUB\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 20:05:00\",\"arriveTime\":\"18 04 2014 20:00:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":1002400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1100300.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1210300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1273000.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1346700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1424800.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1508400.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1524900.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":2203600.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2865800.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3141900.0,\"currency\":\"IDR\"}]},{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"S\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":576300.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":796300.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":837000.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":871100.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":877700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":884300.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":892000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":900800.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2020600.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":2215300.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 334\",\"airlineCode\":\"GA\",\"fromAirport\":\"BDO\",\"toAirport\":\"DPS\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 12:35:00\",\"arriveTime\":\"18 04 2014 15:15:00\"},{\"flightNo\":\"GA 439\",\"airlineCode\":\"GA\",\"fromAirport\":\"DPS\",\"toAirport\":\"CGK\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 16:15:00\",\"arriveTime\":\"18 04 2014 17:10:00\"},{\"flightNo\":\"GA 326\",\"airlineCode\":\"GA\",\"fromAirport\":\"CGK\",\"toAirport\":\"SUB\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 19:30:00\",\"arriveTime\":\"18 04 2014 21:05:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":1002400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1100300.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1210300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1273000.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1346700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1424800.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1508400.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1524900.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":6,\"fare\":2203600.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2865800.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3141900.0,\"currency\":\"IDR\"}]},{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":0,\"fare\":1622400.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":0,\"fare\":1695000.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":0,\"fare\":1719200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1750000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1773100.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":5,\"fare\":3626600.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":7,\"fare\":3975300.0,\"currency\":\"IDR\"}]},{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1194500.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1292400.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1363900.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1394700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1413400.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1429900.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1445300.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":0,\"fare\":3481400.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3818000.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 361\",\"airlineCode\":\"GA\",\"fromAirport\":\"BDO\",\"toAirport\":\"SUB\",\"departDate\":\"18 04 2014 00:00:00\",\"departTime\":\"18 04 2014 13:35:00\",\"arriveTime\":\"18 04 2014 14:55:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":934200.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1081600.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1127800.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1219100.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1319200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1367600.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1412700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":9,\"fare\":2310300.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2862500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3139700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":true},{\"pacFlights\":[{\"flightNo\":\"GA 338\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"DPS\",\"departDate\":\"21 04 2014 00:00:00\",\"departTime\":\"21 04 2014 07:55:00\",\"arriveTime\":\"21 04 2014 10:00:00\"},{\"flightNo\":\"GA 335\",\"airlineCode\":\"GA\",\"fromAirport\":\"DPS\",\"toAirport\":\"BDO\",\"departDate\":\"21 04 2014 00:00:00\",\"departTime\":\"21 04 2014 11:10:00\",\"arriveTime\":\"21 04 2014 11:50:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":757800.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":796300.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":837000.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":871100.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":877700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":884300.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":892000.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":900800.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2020600.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":2215300.0,\"currency\":\"IDR\"}]},{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":1017400.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":1115300.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1225300.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1288000.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1361700.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1439800.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1523400.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1539900.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":3,\"fare\":2218600.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2880800.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3156900.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false},{\"pacFlights\":[{\"flightNo\":\"GA 360\",\"airlineCode\":\"GA\",\"fromAirport\":\"SUB\",\"toAirport\":\"BDO\",\"departDate\":\"21 04 2014 00:00:00\",\"departTime\":\"21 04 2014 11:25:00\",\"arriveTime\":\"21 04 2014 12:45:00\"}],\"pacFlightClassGroups\":[{\"pacFlightClasses\":[{\"code\":\"G\",\"availableSeat\":0,\"fare\":0.0,\"currency\":\"IDR\"},{\"code\":\"V\",\"availableSeat\":0,\"fare\":840300.0,\"currency\":\"IDR\"},{\"code\":\"T\",\"availableSeat\":9,\"fare\":949200.0,\"currency\":\"IDR\"},{\"code\":\"Q\",\"availableSeat\":9,\"fare\":1096600.0,\"currency\":\"IDR\"},{\"code\":\"N\",\"availableSeat\":9,\"fare\":1142800.0,\"currency\":\"IDR\"},{\"code\":\"K\",\"availableSeat\":9,\"fare\":1234100.0,\"currency\":\"IDR\"},{\"code\":\"M\",\"availableSeat\":9,\"fare\":1334200.0,\"currency\":\"IDR\"},{\"code\":\"B\",\"availableSeat\":9,\"fare\":1382600.0,\"currency\":\"IDR\"},{\"code\":\"Y\",\"availableSeat\":9,\"fare\":1427700.0,\"currency\":\"IDR\"},{\"code\":\"D\",\"availableSeat\":9,\"fare\":2325300.0,\"currency\":\"IDR\"},{\"code\":\"C\",\"availableSeat\":9,\"fare\":2877500.0,\"currency\":\"IDR\"},{\"code\":\"J\",\"availableSeat\":9,\"fare\":3154700.0,\"currency\":\"IDR\"}]}],\"boolDepart\":false}]";
		
		Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss")
				.create();
		PacAirItem[] mcArray = gson.fromJson(bit48, PacAirItem[].class);
		List<PacAirItem> pacAirItems = Arrays.asList(mcArray);
		System.out.println(pacAirItems);*/
		
		
		String bit48 = "{\"airlineId\":\"3\",\"airlineName\":\"Citilink\",\"flightType\":\"direct\",\"departureAirportCode\":\"BDO\",\"arrivalAirportCode\":\"DPS\",\"flightDate\":\"28 08 2014 00:00:00\",\"flightNumber\":\"QG 889\",\"etd\":\"28 08 2014 15:05:00\",\"eta\":\"28 08 2014 17:35:00\",\"via\":\"-\",\"selectedClassId\":\"DQG1\",\"comission\":10000.0,\"selectedClass\":{\"classId\":\"DQG1\",\"className\":\"O\"},\"adultPassengerSummary\":{\"passengerType\":\"Adult\",\"pax\":1,\"basic\":375000.0,\"tax\":37500.0,\"iwjr\":1.0,\"service\":108600.0,\"total\":531100.0},\"childPassengerSummary\":{\"passengerType\":\"Child\",\"pax\":1,\"basic\":281250.0,\"tax\":28125.0,\"iwjr\":1.0,\"service\":108600.0,\"total\":427975.0},\"infantPassengerSummary\":{\"passengerType\":\"Infant\",\"pax\":1,\"basic\":200000.0,\"tax\":20000.0,\"iwjr\":1.0,\"service\":0.0,\"total\":225000.0},\"total\":1184075.0}\n";
		Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
		AeroFlightView fromIgate = gson.fromJson(bit48, AeroFlightView.class);
		System.out.println("fromIgate=" + fromIgate);
		System.out.println("fromIgate.getInfantPassengerSummary()=" + fromIgate.getInfantPassengerSummary());
		System.out.println("fromIgate.getInfantPassengerSummary().getTotal()=" + fromIgate.getInfantPassengerSummary().getTotal());
	}

}

