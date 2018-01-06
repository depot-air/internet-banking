package com.dwidasa.engine.service.impl.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.IReportDataDao;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.dao.TransactionDataDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.AeroBookInfo;
import com.dwidasa.engine.model.AeroConnectingFlight;
import com.dwidasa.engine.model.AeroCustomer;
import com.dwidasa.engine.model.AeroPassenger;
import com.dwidasa.engine.model.AeroPassengerSummary;
import com.dwidasa.engine.model.Airport;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.IReportData;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.airline.PacBook;
import com.dwidasa.engine.model.airline.PacResponseError;
import com.dwidasa.engine.model.airline.PacRetrieve;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.engine.service.MessageMailer;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.AeroTicketingSearchBookingViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("aeroTicketingSearchBookingViewService")
public class AeroTicketingSearchBookingViewServiceImpl implements AeroTicketingSearchBookingViewService {
	private static final int FOURTEEN = 14;
	private static final int FIFTEEN = 15;
	@Autowired private CacheManager cacheManager;
	@Autowired protected TransactionDao transactionDao;
	@Autowired protected TransactionDataDao transactionDataDao;
	@Autowired protected IReportDataDao iReportDataDao;
    @Autowired protected MessageMailer mailer;
    @Autowired private CustomerRegisterService customerRegisterService;
    @Autowired private PaymentService paymentService;
    
	private static Logger logger = Logger.getLogger( AeroTicketingSearchBookingViewServiceImpl.class );

    private class AeroSearchBookMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private AeroSearchBookMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
            String customData = "";
                        
//            customData = PojoJsonMapper.toJson(atv);
            customData = gson.toJson(atv);
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
            	
            	{"departureAirportCode":"SUB","departDate":"30 07 2014 00:00:00","returnDate":"02 08 2014 00:00:00",
            	"isDepartOnly":false,"totalAdult":1,"totalChildren":0,"totalInfant":0,
            	"departureFlight":{"airlineId":"4","airlineName":"Garuda","flightType":"direct","departureAirportCode":"CGK","arrivalAirportCode":"SUB",
            	"flightDate":"30 07 2014 00:00:00","flightNumber":"GA302","etd":"30 07 2014 05:30:00","eta":"30 07 2014 07:05:00","via":"-",
            	"ticketPrice":1038900.0,"selectedClassId":"T","comission":5000.0,
            	"selectedClass":{"classId":"T","className":"T"},
            	"adultPassengerSummary":{"pax":1,"basic":899000.0,"tax":89900.0,"iwjr":5000.0,"service":40000.0,"total":1038900.0},
            	"bookInfo":{"bookCode":"-","timeLimit":"24 07 2014 14:04:41","status":"failed"},"total":1038900.0},
            	"returnFlight":{"airlineId":"4","airlineName":"Garuda","flightType":"direct","departureAirportCode":"SUB","arrivalAirportCode":"CGK",
            	"flightDate":"02 08 2014 00:00:00","flightNumber":"GA303","etd":"02 08 2014 05:25:00","eta":"02 08 2014 07:00:00","via":"-",
            	"ticketPrice":1403900.0,"selectedClassId":"N","comission":5000.0,"selectedClass":{"classId":"N","className":"N"},
            	"adultPassengerSummary":{"pax":1,"basic":1199000.0,"tax":119900.0,"iwjr":5000.0,"service":75000.0,"total":1403900.0},
            	"bookInfo":{"bookCode":"-","timeLimit":"24 07 2014 14:04:41","status":"failed"},"total":1403900.0},"ticketPrice":2442800.0,
            	"ticketComission":10000.0,"agentPrice":2432800.0,"agentMargin":0.0,"aeroCustomer":{"customerName":"DIDI SUGI",
            	"customerPhone":"628152233445    ","customerEmail":"imam.baihaqi1999@gmail.com"}}
*/
            	String bit48 = transaction.getFreeData1();
            	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
            	if (aero.getProviderCode().equals(AirConstants.VOLTRAS.PROVIDER.CODE)) {                	
                	PacBook pacBook = gson.fromJson(bit48, PacBook.class);            	
        			aero.setPacBook(pacBook);
            	} else {
            		AeroTicketingView fromIGate = gson.fromJson(bit48, AeroTicketingView.class);  
            		
            		logger.info("fromIGate.getBookId()=" + fromIGate.getBookId());
            		aero.setBookId(fromIGate.getBookId());
            		
            		AeroFlightView departFlight =  aero.getDepartureFlight();
            		departFlight.setAeroFlightClasses(null);	//biar rapi
            		departFlight.setBookInfo(fromIGate.getDepartureFlight().getBookInfo());
            		AeroPassengerSummary adultPassengerSummary = aero.getDepartureFlight().getAdultPassengerSummary();
            		if (adultPassengerSummary == null) adultPassengerSummary = new AeroPassengerSummary();
        			adultPassengerSummary.setPassengerType(AeroPassenger.PASSENGER_TYPE.ADULT);
            		adultPassengerSummary.setBasic(fromIGate.getDepartureFlight().getAdultPassengerSummary().getBasic());
            		adultPassengerSummary.setIwjr(fromIGate.getDepartureFlight().getAdultPassengerSummary().getIwjr());
            		adultPassengerSummary.setService(fromIGate.getDepartureFlight().getAdultPassengerSummary().getService());
            		adultPassengerSummary.setTax(fromIGate.getDepartureFlight().getAdultPassengerSummary().getTax());
            		adultPassengerSummary.setTotal(fromIGate.getDepartureFlight().getAdultPassengerSummary().getTotal());
            		departFlight.setAdultPassengerSummary(adultPassengerSummary);
            		if (fromIGate.getDepartureFlight().getChildPassengerSummary() != null) {
            			AeroPassengerSummary childPassengerSummary = aero.getDepartureFlight().getChildPassengerSummary();
            			if (childPassengerSummary == null) childPassengerSummary = new AeroPassengerSummary();
            			childPassengerSummary.setPassengerType(AeroPassenger.PASSENGER_TYPE.CHILD);
            			childPassengerSummary.setBasic(fromIGate.getDepartureFlight().getChildPassengerSummary().getBasic());
            			childPassengerSummary.setIwjr(fromIGate.getDepartureFlight().getChildPassengerSummary().getIwjr());
            			childPassengerSummary.setService(fromIGate.getDepartureFlight().getChildPassengerSummary().getService());
            			childPassengerSummary.setTax(fromIGate.getDepartureFlight().getChildPassengerSummary().getTax());
            			childPassengerSummary.setTotal(fromIGate.getDepartureFlight().getChildPassengerSummary().getTotal());
            			departFlight.setChildPassengerSummary(childPassengerSummary);	
            		}
            		if (fromIGate.getDepartureFlight().getInfantPassengerSummary() != null) {
            			AeroPassengerSummary infantPassengerSummary = aero.getDepartureFlight().getInfantPassengerSummary();
            			if (infantPassengerSummary == null) infantPassengerSummary = new AeroPassengerSummary();
            			infantPassengerSummary.setPassengerType(AeroPassenger.PASSENGER_TYPE.INFANT);
            			infantPassengerSummary.setBasic(fromIGate.getDepartureFlight().getInfantPassengerSummary().getBasic());
            			infantPassengerSummary.setIwjr(fromIGate.getDepartureFlight().getInfantPassengerSummary().getIwjr());
            			infantPassengerSummary.setService(fromIGate.getDepartureFlight().getInfantPassengerSummary().getService());
            			infantPassengerSummary.setTax(fromIGate.getDepartureFlight().getInfantPassengerSummary().getTax());
            			infantPassengerSummary.setTotal(fromIGate.getDepartureFlight().getInfantPassengerSummary().getTotal());
            			departFlight.setInfantPassengerSummary(fromIGate.getDepartureFlight().getInfantPassengerSummary());	
            		}
            		departFlight.setTicketPrice(fromIGate.getDepartureFlight().getTicketPrice());
            		departFlight.setTotal(fromIGate.getDepartureFlight().getTotal());
            		aero.setDepartureFlight(departFlight);
            		
            		if (fromIGate.getReturnFlight() != null) { 	//!aero.getIsDepartOnly()) {
            			AeroFlightView returnFlight = aero.getReturnFlight();
            			returnFlight.setAeroFlightClasses(null);	//biar rapi
                		returnFlight.setBookInfo(fromIGate.getReturnFlight().getBookInfo());
                		returnFlight.setAdultPassengerSummary(fromIGate.getReturnFlight().getAdultPassengerSummary());
                		returnFlight.setChildPassengerSummary(fromIGate.getReturnFlight().getChildPassengerSummary());
                		returnFlight.setInfantPassengerSummary(fromIGate.getReturnFlight().getInfantPassengerSummary());
                		returnFlight.setTicketPrice(fromIGate.getReturnFlight().getTicketPrice());
                		returnFlight.setTotal(fromIGate.getReturnFlight().getTotal());
                		aero.setReturnFlight(returnFlight);
            		}
//            		aero.setTotal(fromIGate.getTotal());
            	}
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }
    }

	@Override
	public AeroTicketingView getSearchBooking(AeroTicketingView view) {
		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroSearchBookMessageCustomizer();
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

    private class AeroVoltrasRetrieveMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private AeroVoltrasRetrieveMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
            PacBook pacBook = atv.getPacBook();
            String customData = "";
                        
//            customData = PojoJsonMapper.toJson(pacBook);
            customData = gson.toJson(pacBook);
            transaction.setFreeData1(customData);
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	AeroTicketingView aero = (AeroTicketingView) view;
            if (transaction.getResponseCode().equals("00")) {
            	String bit48 = transaction.getFreeData1();
            	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
            	PacRetrieve pacRetrieve = gson.fromJson(bit48, PacRetrieve.class);            	

            	AeroBookInfo bookInfo = new AeroBookInfo();
    			bookInfo.setStatus(pacRetrieve.getStatus());
    			bookInfo.setBookCode(pacRetrieve.getBookingCode());
    			bookInfo.setTimeLimit(pacRetrieve.getTimeLimit());
    			bookInfo.setAgentFare(pacRetrieve.getAgentFare());
    			bookInfo.setAgentInsurance(pacRetrieve.getAgentInsurance());
    			bookInfo.setPublishFare(pacRetrieve.getPublishFare());
    			bookInfo.setPublishInsurance(pacRetrieve.getPublishInsurance());
    			
    			PacBook pacBook = aero.getPacBook();
            	if (aero.getDepartureFlight().getBookInfo() != null && aero.getDepartureFlight().getBookInfo().getBookCode() != null) {            		
            		aero.getReturnFlight().setBookInfo(bookInfo);
            		pacBook.setRetrieveReturn(pacRetrieve);
            	} else {
            		aero.getDepartureFlight().setBookInfo(bookInfo);
            		pacBook.setRetrieveDepart(pacRetrieve);
            	}    			
            	aero.setPacBook(pacBook);
            	logger.info("aero.getAssuranceType()=" + aero.getAssuranceType());
            	if (aero.getAssuranceType() != null && !aero.getAssuranceType().equals("")) {
            		aero.setTicketPrice(pacRetrieve.getPublishFare());
//            		aero.setTotal(pacRetrieve.getPublishFare().add(pacRetrieve.getPublishInsurance()));
//            		aero.setAssurancePrice(pacRetrieve.getPublishInsurance());
            	}
            	logger.info("aero.getAssurancePrice()=" + aero.getAssurancePrice());
            	logger.info("aero.getTicketPrice()=" + aero.getTicketPrice() + " aero.getTotal()=" + aero.getTotal());
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }
    }

	@Override
	public AeroTicketingView getVoltrasRetrieve(AeroTicketingView view) {
		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroVoltrasRetrieveMessageCustomizer();
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

    private class AeroVoltrasTicketingMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private AeroVoltrasTicketingMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
            PacBook pacBook = atv.getPacBook();
            String customData = "";
                        
//            customData = PojoJsonMapper.toJson(pacBook);
            customData = gson.toJson(pacBook);
            transaction.setFreeData1(customData);
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	AeroTicketingView aero = (AeroTicketingView) view;
            if (transaction.getResponseCode().equals("00")) {
            	String bit48 = transaction.getFreeData1();
            	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
            	PacBook pacBook = gson.fromJson(bit48, PacBook.class);            	
    			aero.setPacBook(pacBook);		
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }
    }

	@Override
	public AeroTicketingView getVoltrasTicketing(AeroTicketingView view) {
		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroVoltrasTicketingMessageCustomizer();
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

        t.setExecutionType(Constants.NOW_ET);
        EngineUtils.setTransactionStatus(t);
        transactionDao.save(t);
    	transactionDataDao.save(EngineUtils.createTransactionData(view, t.getId()));
        
    	savingCustRegIReport(view);
        return view;
	}

    private class AeroVoltrasCancelMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private AeroVoltrasCancelMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
            PacBook pacBook = atv.getPacBook();
            String customData = "";
                        
//            customData = PojoJsonMapper.toJson(pacBook);
            customData = gson.toJson(pacBook);
            transaction.setFreeData1(customData);
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	AeroTicketingView aero = (AeroTicketingView) view;
            if (transaction.getResponseCode().equals("00")) {

            	String bit48 = transaction.getFreeData1();
            	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
            	PacBook pacBook = gson.fromJson(bit48, PacBook.class);
    			aero.setPacBook(pacBook);
    		
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }
    }

	@Override
	public AeroTicketingView getVoltrasCancel(AeroTicketingView view) {
		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroVoltrasCancelMessageCustomizer();
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
        
        t.setExecutionType(Constants.NOW_ET);
        EngineUtils.setTransactionStatus(t);
        transactionDao.save(t);
    	transactionDataDao.save(EngineUtils.createTransactionData(view, t.getId()));

        return view;
	}

	@Override
	public String sendEmail(AeroTicketingView view) {
		try {
			String res = mailer.sendTransactionMessageAero(view.getClass().getSimpleName()+".vm", view, view.getAeroCustomer());
			return res;
		} catch (Exception e) {
			return "FAIL, " + e.getMessage();
		}
		
	}

    private class AeroticketingIssuedMessageCustomizer implements MessageCustomizer {
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

		private AeroticketingIssuedMessageCustomizer() {
        }
        public void compose(BaseView view, Transaction transaction) {
            AeroTicketingView atv = (AeroTicketingView) view;
//            String customData = PojoJsonMapper.toJson(atv);
            String customData = gson.toJson(atv);
            transaction.setFreeData1(customData);
            logger.info("transaction.getTransactionAmount()=" + transaction.getTransactionAmount());
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	AeroTicketingView aero = (AeroTicketingView) view;
            if (transaction.getResponseCode().equals("00")) {

            	String bit48 = transaction.getFreeData1();
            	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
            	PacBook pacBook = gson.fromJson(bit48, PacBook.class);
    			aero.setPacBook(pacBook);
    		
            }
            else {
                return Boolean.FALSE;
                
            }
            return Boolean.TRUE;
        }
    }

	@Override
	public AeroTicketingView getAeroIssue(AeroTicketingView view) {
		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        t.setTranslationCode(null);
        t.setFreeData1(new String());

        CommLink link = new MxCommLink(t);
        MessageCustomizer customizer = new AeroticketingIssuedMessageCustomizer();
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

        t.setExecutionType(Constants.NOW_ET);
        EngineUtils.setTransactionStatus(t);
        transactionDao.save(t);
    	transactionDataDao.save(EngineUtils.createTransactionData(view, t.getId()));
        
    	savingCustRegIReport(view);
        return view;
	}


    private void savingSelectedAirport(AeroTicketingView aeroView) {
    	//1. get listCustReg yg sudah ada di table,
    	//2. jika sudah ada rute yg sama maka increment dan update
    	//3. jika belum ada dan jumlah < 15 maka insert
    	//4. jika ada 15, dan yg kesepuluh = 1x, maka delete yg terakhir, insert custReg penerbangan ini, set index = 14
    	//5. update keseluruhan index

    	List<CustomerRegister> customerRegisters = paymentService.getRegistersOrderBy(aeroView.getCustomerId(),
    			AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, AirConstants.AEROTICKETING.PROVIDER.CODE, " customer_reference ");
    	
    	
        CustomerRegister customerRegister = new CustomerRegister();
        customerRegister.setCustomerId(aeroView.getCustomerId());
        customerRegister.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH);
        
        customerRegister.setData1(AirConstants.AEROTICKETING.PROVIDER.CODE);
        customerRegister.setData2(aeroView.getDepartureAirportCode());
        customerRegister.setData3(aeroView.getDestinationAirportCode());
        customerRegister.setData4("1,1,1,1");	//counter From, To, fromTo, Route
        customerRegister.setCustomerReference("" + 1);	//index
        customerRegister.setCreated(new Date());
        customerRegister.setCreatedby(aeroView.getCustomerId());
        customerRegister.setUpdated(new Date());
        customerRegister.setUpdatedby(aeroView.getCustomerId());

		String sCounterMy = customerRegister.getData4();
		String [] arrCounterMy = sCounterMy.split(",");
		int counterFromMy = Integer.parseInt(arrCounterMy[0]);
		int counterToMy = Integer.parseInt(arrCounterMy[1]);
		int counterFromToMy = Integer.parseInt(arrCounterMy[2]);
		int counterRouteMy = Integer.parseInt(arrCounterMy[3]);

        boolean isNew = true;
    	for (CustomerRegister cr : customerRegisters) {
    		String sCounter = cr.getData4();
			String [] arrCounter = sCounter.split(",");
			int counterFrom = Integer.parseInt(arrCounter[0]);
			int counterTo = Integer.parseInt(arrCounter[1]);
			int counterFromTo = Integer.parseInt(arrCounter[2]);
			int counterRoute = Integer.parseInt(arrCounter[3]);
			
    		if (cr.getData2().equals(aeroView.getDepartureAirportCode()) && cr.getData3().equals(aeroView.getDestinationAirportCode())) {
    			counterFromMy = counterFrom + 1;
    			counterToMy = counterTo + 1;
    			counterRouteMy = counterRoute + 1;
    			
    			customerRegister = cr;
    			isNew = false;
    		} else if (cr.getData2().equals(aeroView.getDepartureAirportCode())) {    			
    			counterFrom += 1;
    			if (counterFrom > counterFromMy) {
    				counterFromMy = counterFrom;
    			} else {
    				counterFrom = counterFromMy;
    			}
    			cr.setData4("" + counterFrom + "," + counterTo + "," + counterFromTo + "," + counterRoute);
    			customerRegisterService.saveOrUpdate(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, cr);
    			
    		} else if (cr.getData3().equals(aeroView.getDestinationAirportCode())) {
    			counterTo += 1;
    			if (counterTo > counterToMy) {
    				counterToMy = counterTo;
    			} else {
    				counterTo = counterToMy;
    			}
    			cr.setData4("" + counterFrom + "," + counterTo + "," + counterFromTo + "," + counterRoute);
    			customerRegisterService.saveOrUpdate(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, cr);
    			
    		} else if (cr.getData2().equals(aeroView.getDestinationAirportCode()) || cr.getData3().equals(aeroView.getDepartureAirportCode())) { 
    			counterFromTo += 1;
    			cr.setData4("" + counterFrom + "," + counterTo + "," + counterFromTo + "," + counterRoute);
    			customerRegisterService.saveOrUpdate(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, cr); 
    		}
    	}
    	//check all counter & index and update other
    	
    	sCounterMy = "" + counterFromMy + "," + counterToMy + "," + counterFromToMy + "," + counterRouteMy;
		customerRegister.setData4(sCounterMy);
        if (isNew) {
        	if (customerRegisters.size() >= FIFTEEN) {
        		CustomerRegister lastCr = customerRegisters.get(FOURTEEN);
        		customerRegisterService.remove(lastCr.getId(), aeroView.getCustomerId());
        		customerRegister.setCustomerReference("" + FOURTEEN);
        	} else {
        		customerRegister.setCustomerReference("" + (customerRegisters.size() + 1) );	//pasti index terakhir
        	}
        } else {
        	int indexOrigin = Integer.parseInt(customerRegister.getCustomerReference());
        	//cek 1 index sebelumnya, jika counter X >= counter sebelumnya, maka index decrement, sebelumnya diencrement
        	//yg dicek adalah from, to, route dan fromTo
        	for(CustomerRegister cr : customerRegisters) {
        		if (!cr.getData2().equals(customerRegister.getData2()) || !cr.getData3().equals(customerRegister.getData3())) {
        			if (Integer.parseInt(cr.getCustomerReference()) == Integer.parseInt(customerRegister.getCustomerReference()) - 1) {
                		String sCounter = cr.getData4();
            			String [] arrCounter = sCounter.split(",");
            			int counterFrom = Integer.parseInt(arrCounter[0]);
            			int counterTo = Integer.parseInt(arrCounter[1]);
            			int counterFromTo = Integer.parseInt(arrCounter[2]);
            			int counterRoute = Integer.parseInt(arrCounter[3]);
            			if (counterRouteMy >= counterRoute || counterFromMy >= counterFrom || counterToMy >= counterTo || counterFromToMy > counterFromTo) {
            				int index = Integer.parseInt(cr.getCustomerReference());
            				customerRegister.setCustomerReference("" + (index));	//tuker
            				cr.setCustomerReference("" + (indexOrigin));	//tuker
            				customerRegisterService.saveOrUpdate(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, cr);
            			}
            			break;
            		}
        		}
        	}
        }
        customerRegisterService.saveOrUpdate(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, customerRegister);
    }

    private void savingPassengers(AeroTicketingView aeroView) {
    	//get all cr, jika total > 11, delete yg terakhir sebanyak yg sekarang, replace dg yg sekarang
    	List<CustomerRegister> crsAdult = customerRegisterService.getAeroPassangers(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, AeroPassenger.PASSENGER_TYPE.ADULT);
    	List<CustomerRegister> crsChild = customerRegisterService.getAeroPassangers(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, AeroPassenger.PASSENGER_TYPE.CHILD);
    	List<CustomerRegister> crsInfant = customerRegisterService.getAeroPassangers(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, AeroPassenger.PASSENGER_TYPE.INFANT);
		
		int totalNewAdult = 0;
		int totalNewChild = 0;
		int totalNewInfant = 0;
    	DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	List<AeroPassenger> passengers = aeroView.getPassengers();
    	for (AeroPassenger passenger : passengers) {
    		String custRef = passenger.getPassengerLastName().toUpperCase() + "," + passenger.getPassengerFirstName().toUpperCase();
    		logger.info("custRef=" + custRef);
    		CustomerRegister customerRegister;
    		CustomerRegister existing = customerRegisterService.get(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, custRef);
    		if (existing == null) {
    			if (passenger.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT)) totalNewAdult += 1;
    			else if (passenger.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.CHILD)) totalNewChild += 1;
    			else totalNewInfant += 1; 
    			customerRegister = new CustomerRegister();
    		    customerRegister.setCustomerId(aeroView.getCustomerId());
    		    customerRegister.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE);                
    		    customerRegister.setCustomerReference(custRef);
    		    customerRegister.setData1(AirConstants.VOLTRAS.PROVIDER.CODE);
    		    customerRegister.setData2(passenger.getPassengerType());
    		    customerRegister.setData3(passenger.getPassengerTitle());
    		    customerRegister.setData4("1");
    		    
    		    String data5 = sdf.format(passenger.getPassengerDob()) + "," + passenger.getCountry() + "," + passenger.getPassengerIdCard();
    		    logger.info("data5=" + data5);
    		    customerRegister.setData5(data5);
    		    customerRegister.setCreated(new Date());
    		    customerRegister.setCreatedby(aeroView.getCustomerId());
    		} else {
    			customerRegister = existing;
    			
    			int counter = Integer.parseInt(customerRegister.getData4()) + 1;
    			customerRegister.setData4("" + counter);
    		}
    		customerRegister.setUpdated(new Date());
    		customerRegister.setUpdatedby(aeroView.getCustomerId());
    		
    		logger.info("customerRegister=" + customerRegister);
    		customerRegisterService.saveOrUpdate(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, customerRegister);
    	}
	
    	if (crsAdult.size() + totalNewAdult > 11) {
			for (int i = 0; i < totalNewAdult; i++) {
				CustomerRegister cr = crsAdult.get(crsAdult.size() - (i+1) ); 
				customerRegisterService.remove(cr.getId(), aeroView.getCustomerId());
			}
    	}
    	if (crsChild.size() + totalNewChild > 11) {
			for (int i = 0; i < totalNewChild; i++) {
				CustomerRegister cr = crsChild.get(crsChild.size() - (i+1) ); 
				customerRegisterService.remove(cr.getId(), aeroView.getCustomerId());
			}
    	}
    	if (crsInfant.size() + totalNewInfant > 11) {
			for (int i = 0; i < totalNewInfant; i++) {
				CustomerRegister cr = crsInfant.get(crsInfant.size() - (i+1) ); 
				customerRegisterService.remove(cr.getId(), aeroView.getCustomerId());
			}
    	}
    }

    private void savingContactDetail(AeroTicketingView aeroView) {
    	AeroCustomer aeroCustomer = aeroView.getAeroCustomer();
    	
		String custRef = aeroCustomer.getLastName().toUpperCase() + "," + aeroCustomer.getFirstName().toUpperCase();
		logger.info("custRef=" + custRef);	
		CustomerRegister customerRegister;
		CustomerRegister existing = customerRegisterService.get(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, custRef);
		if (existing == null) {
			//get all cr, jika > 12, delete yg terakhir, replace dg yg sekarang
			List<CustomerRegister> crs = customerRegisterService.getAeroPassangers(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, "Contact");
			if (crs.size() >= 11) {    				
				CustomerRegister cr = crs.get(crs.size() - 1); 
				customerRegisterService.remove(cr.getId(), aeroView.getCustomerId());
			}
			
			customerRegister = new CustomerRegister();
		    customerRegister.setCustomerId(aeroView.getCustomerId());
		    customerRegister.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE);                
		    customerRegister.setCustomerReference(custRef);
		    customerRegister.setData1(AirConstants.VOLTRAS.PROVIDER.CODE);
		    customerRegister.setData2("Contact");
		    customerRegister.setData3(aeroCustomer.getTitle());
		    customerRegister.setData4("1");
		    
		    String data5 = aeroCustomer.getCustomerPhone() + "," + aeroCustomer.getCustomerEmail();
		    logger.info("data5=" + data5);
		    customerRegister.setData5(data5);
		    customerRegister.setCreated(new Date());
		    customerRegister.setCreatedby(aeroView.getCustomerId());
		} else {
			customerRegister = existing;
			
			int counter = Integer.parseInt(customerRegister.getData4()) + 1;
			customerRegister.setData4("" + counter);
		}
		customerRegister.setUpdated(new Date());
		customerRegister.setUpdatedby(aeroView.getCustomerId());
		
		logger.info("customerRegister=" + customerRegister);
		customerRegisterService.saveOrUpdate(aeroView.getCustomerId(), AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, customerRegister);
		
    }

    private void savingIReportData(AeroTicketingView aeroView) {
    	DateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT.COMPLETE_FORMAT);
    	DateFormat hhmm = new SimpleDateFormat(Constants.DATE_FORMAT.HHMM);
    	DateFormat ddMMMMyyyy = new SimpleDateFormat(Constants.DATE_FORMAT.ddMMMyyyy);
    	
    	//save flight
    	AeroFlightView departFlight = aeroView.getDepartureFlight();    	
    	savingFlight(aeroView, sdf, hhmm, departFlight, AirConstants.DEPARTURE);
    	
    	if (!aeroView.getIsDepartOnly()) {
    		AeroFlightView returnFlight = aeroView.getReturnFlight();    	
        	savingFlight(aeroView, sdf, hhmm, returnFlight, AirConstants.RETURN);		
    	}
		
    	//save passenger
    	for (int i = 0; i < aeroView.getPassengers().size(); i++) {
    		AeroPassenger passenger = aeroView.getPassengers().get(i);
    		IReportData iReportData = new IReportData();
        	iReportData.setCustomerId(aeroView.getCustomerId());
        	iReportData.setTransactionType(aeroView.getTransactionType());
        	iReportData.setTransactionDate(aeroView.getTransactionDate());
        	iReportData.setReferenceKey(departFlight.getBookInfo().getBookCode());
        	iReportData.setReportType(IReportData.REPORT_PASSENGER);
        	
        	iReportData.setData1("" + (i + 1));
        	iReportData.setData2(passenger.getPassengerTitle());
        	
        	iReportData.setData3(passenger.getPassengerFirstName() + " " + passenger.getPassengerLastName());
        	iReportData.setData4(ddMMMMyyyy.format(passenger.getPassengerDob()));
        	iReportData.setData5(passenger.getPassengerIdCard());
        	iReportData.setCreated(new Date());		iReportData.setCreatedby(aeroView.getCustomerId());
        	iReportData.setUpdated(new Date()); 	iReportData.setUpdatedby(aeroView.getCustomerId());
        	iReportDataDao.save(iReportData);
    	}
    	
    }

	private void savingFlight(AeroTicketingView aeroView, DateFormat sdf,
			DateFormat hhmm, AeroFlightView aeroFlight, String departOrReturn) {
		IReportData iReportData = new IReportData();
    	iReportData.setCustomerId(aeroView.getCustomerId());
    	iReportData.setTransactionType(aeroView.getTransactionType());
    	iReportData.setTransactionDate(aeroView.getTransactionDate());
    	iReportData.setReferenceKey(aeroFlight.getBookInfo().getBookCode());
    	iReportData.setReportType(IReportData.REPORT_FLIGHT);
    	
    	iReportData.setData1(departOrReturn);
    	Airport from = cacheManager.getAirport(aeroFlight.getDepartureAirportCode());
    	Airport to = cacheManager.getAirport(aeroFlight.getArrivalAirportCode());
    	String fromAirport = from.getAirportCity() + ", " + from.getAirportName() + " (" + from.getAirportCode() + ")";
    	String toAirport = to.getAirportCity() + ", " + to.getAirportName() + " (" + to.getAirportCode() + ")";
    	iReportData.setData2(fromAirport + " - " + toAirport);
    	String flightDate = sdf.format(aeroFlight.getFlightDate());
    	iReportData.setData3(flightDate);
    	
    	String etdEta = hhmm.format(aeroFlight.getEtd()) + " - " + hhmm.format(aeroFlight.getEta());
    	iReportData.setData4(etdEta);
    	iReportData.setData5(aeroFlight.getFlightNumber());
    	iReportData.setData6(aeroFlight.getSelectedClassId());
    	
    	iReportData.setCreated(new Date());	iReportData.setCreatedby(aeroView.getCustomerId());
    	iReportData.setUpdated(new Date()); iReportData.setUpdatedby(aeroView.getCustomerId());
    	iReportDataDao.save(iReportData);
    	
    	AeroConnectingFlight connectingFlight = aeroFlight.getAeroConnectingFlight();
    	if (connectingFlight != null) {
    		IReportData iReportData2 = new IReportData();
        	iReportData2.setCustomerId(aeroView.getCustomerId());
        	iReportData2.setTransactionType(aeroView.getTransactionType());
        	iReportData2.setTransactionDate(aeroView.getTransactionDate());
        	iReportData2.setReferenceKey(aeroFlight.getBookInfo().getBookCode());
        	iReportData2.setReportType(IReportData.REPORT_FLIGHT);
        	
        	iReportData2.setData1(departOrReturn);
        	from = cacheManager.getAirport(connectingFlight.getConnectingFlightFrom());
        	to = cacheManager.getAirport(connectingFlight.getConnectingFlightTo());
        	fromAirport = from.getAirportCity() + " (" + from.getAirportCode() + ")";
        	toAirport = to.getAirportCity() + " (" + to.getAirportCode() + ")";
        	iReportData2.setData2(fromAirport + " - " + toAirport);
        	flightDate = sdf.format(connectingFlight.getConnectingFlightDate());
        	iReportData2.setData3(flightDate);
        	
        	etdEta = hhmm.format(connectingFlight.getConnectingFlightEtd()) + " - " + hhmm.format(connectingFlight.getConnectingFlightEta());
        	iReportData2.setData4(etdEta);
        	iReportData2.setData5(connectingFlight.getConnectingFlightFno());
        	iReportData2.setData6(connectingFlight.getConnectingFlightClassname());
        	
        	iReportData2.setCreated(new Date());	iReportData2.setCreatedby(aeroView.getCustomerId());
        	iReportData2.setUpdated(new Date()); 	iReportData2.setUpdatedby(aeroView.getCustomerId());	
        	iReportDataDao.save(iReportData2);
    	}
    	AeroConnectingFlight connectingFlight2 = aeroFlight.getAeroConnectingFlight2();
    	if (connectingFlight2 != null) {
    		IReportData iReportData3 = new IReportData();
        	iReportData3.setCustomerId(aeroView.getCustomerId());
        	iReportData3.setTransactionType(aeroView.getTransactionType());
        	iReportData3.setTransactionDate(aeroView.getTransactionDate());
        	iReportData3.setReferenceKey(aeroFlight.getBookInfo().getBookCode());
        	iReportData3.setReportType(IReportData.REPORT_FLIGHT);
        	
        	iReportData3.setData1(departOrReturn);
        	from = cacheManager.getAirport(connectingFlight2.getConnectingFlightFrom());
        	to = cacheManager.getAirport(connectingFlight2.getConnectingFlightTo());
        	fromAirport = from.getAirportCity() + " (" + from.getAirportCode() + ")";
        	toAirport = to.getAirportCity() + " (" + to.getAirportCode() + ")";
        	iReportData3.setData2(fromAirport + " - " + toAirport);
        	flightDate = sdf.format(connectingFlight2.getConnectingFlightDate());
        	iReportData3.setData3(flightDate);
        	
        	etdEta = hhmm.format(connectingFlight2.getConnectingFlightEtd()) + " - " + hhmm.format(connectingFlight2.getConnectingFlightEta());
        	iReportData3.setData4(etdEta);
        	iReportData3.setData5(connectingFlight2.getConnectingFlightFno());
        	iReportData3.setData6(connectingFlight2.getConnectingFlightClassname());
        	iReportData3.setCreated(new Date());	iReportData3.setCreatedby(aeroView.getCustomerId());
        	iReportData3.setUpdated(new Date()); 	iReportData3.setUpdatedby(aeroView.getCustomerId());
        	iReportDataDao.save(iReportData3);
    	}
	}
	
	public boolean savingCustRegIReport(AeroTicketingView view) {

    	savingSelectedAirport(view);
    	savingPassengers(view);
    	savingContactDetail(view);
    	//insert ke table r_ireport_data
    	//generate pdf report
    	savingIReportData(view);
		
		return true;
	}
	
    public static void main(String[] args) {
        /*
		Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
		String bit48 = "{\"departureAirportCode\":\"DPS\",\"departDate\":\"15 08 2014 00:00:00\",\"isDepartOnly\":true,\"totalAdult\":1,\"totalChildren\":1,\"totalInfant\":1,\"departureFlight\":{\"airlineId\":\"3\",\"airlineName\":\"Citilink\",\"flightType\":\"direct\",\"departureAirportCode\":\"BDO\",\"arrivalAirportCode\":\"DPS\",\"flightDate\":\"15 08 2014 00:00:00\",\"flightNumber\":\"QG 889\",\"etd\":\"15 08 2014 15:05:00\",\"eta\":\"15 08 2014 17:35:00\",\"via\":\"-\",\"ticketPrice\":1569075.0,\"selectedClassId\":\"K\",\"comission\":10000.0,\"selectedClass\":{\"classId\":\"K\",\"className\":\"K\"},\"adultPassengerSummary\":{\"pax\":1,\"basic\":575000.0,\"tax\":57500.0,\"iwjr\":5000.0,\"service\":108600.0,\"total\":751100.0},\"childPassengerSummary\":{\"pax\":1,\"basic\":431250.0,\"tax\":43125.0,\"iwjr\":5000.0,\"service\":108600.0,\"total\":592975.0},\"infantPassengerSummary\":{\"pax\":1,\"basic\":200000.0,\"tax\":20000.0,\"iwjr\":5000.0,\"service\":0.0,\"total\":225000.0},\"bookInfo\":{\"bookCode\":\"-\",\"timeLimit\":\"12 08 2014 11:18:28\",\"status\":\"failed\"},\"total\":1599075.0},\"ticketPrice\":1569075.0,\"ticketComission\":10000.0,\"agentPrice\":1559075.0,\"agentMargin\":0.0,\"aeroCustomer\":{\"customerName\":\"DIDI SUGI\",\"customerPhone\":\"628152233445    \",\"customerEmail\":\"imam.baihaqi1999@gmail.com\"},\"bookId\":\"TiSMU7yuS7ExAPJO7S9ITCLvvEBmeyDvq0xNQdPvnPc\",\"total\":1599075.0}";
		AeroTicketingView fromIGate = gson.fromJson(bit48, AeroTicketingView.class);
		System.out.println(fromIGate.getDepartureFlight().getBookInfo());
		//System.out.println(fromIGate.getReturnFlight().getBookInfo());
		System.out.println(fromIGate.getDepartureFlight().getBookInfo().getBookCode() + " - " + fromIGate.getDepartureFlight().getBookInfo().getStatus());

		System.out.println("fromIGate.getDepartureFlight().getInfantPassengerSummary().getBasic()=" + fromIGate.getDepartureFlight().getInfantPassengerSummary().getBasic());
		System.out.println("fromIGate.getDepartureFlight().getInfantPassengerSummary()" + fromIGate.getDepartureFlight().getInfantPassengerSummary());
		System.out.println("fromIGate.getDepartureFlight().getInfantPassengerSummary().getBasic()=" + fromIGate.getDepartureFlight().getInfantPassengerSummary().getBasic());
		*/


        int counter = Integer.parseInt("89745689545") + 1;
        System.out.println("counter=" + counter);
	}

}
