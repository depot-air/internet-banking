package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.airline.PacBook;
import com.dwidasa.engine.service.view.AeroTicketingPriceDetailViewService;
import com.dwidasa.engine.service.view.AeroTicketingSearchDetailViewService;
import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.service.view.AeroTicketingViewService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.codehaus.jackson.type.TypeReference;

//@Import(library = {"context:layout/javascript/jquery-1.6.2.min.js"})
public class AeroTicketingFlightList {
	private static Logger logger = Logger.getLogger( AeroTicketingFlightList.class );
    @Persist
    private AeroTicketingView aeroTicketingView;

    @Property
    private String hiddenServerIp;

    @Property
    @Persist
    private String hiddenAeroTicketingView;

    @Property private String hiddenAeroTicketingViewInit;

	@Inject
	private ThreadLocale threadLocale;

    @InjectPage
    private AeroTicketingFlightDetail aeroTicketingFlightDetail;

    @Property
    private AeroFlightView selectedDepartFlight;

    @Property
    private AeroFlightView selectedReturnFlight;

    @Property
    private AeroFlightClass selectedClass;

	@Property
	private EvenOdd evenOdd;

    private DateFormat ddmm = new SimpleDateFormat(com.dwidasa.ib.Constants.DATEFORMAT.DD_MM, threadLocale.getLocale());
    private DateFormat mediumFormat = new SimpleDateFormat(com.dwidasa.ib.Constants.MEDIUM_FORMAT, threadLocale.getLocale());

    @Property
    private DateFormat hhmm = new SimpleDateFormat(com.dwidasa.ib.Constants.DATEFORMAT.HH_MM, threadLocale.getLocale());

    @Property
    private boolean saveBoxValue;
    
    @Property
    private int tokenType;

	@Property
	private NumberFormat formatter = NumberFormat.getNumberInstance(threadLocale.getLocale());

//    @Property
//    private String chooseValue;

    @Inject private SessionManager sessionManager;
    @Inject private CacheManager cacheManager;
    @Inject private Messages message;
    @Inject private GenericSelectModelFactory genericSelectModelFactory;
    @Inject private AeroTicketingViewService aeroTicketingService;
    @Inject private AeroTicketingPriceDetailViewService priceDetailViewService;
    @Inject private AeroTicketingSearchDetailViewService aeroTicketingSearchDetailViewService;
    @Inject private ParameterDao parameterDao;

    @InjectComponent
    private Form form;

    @Persist
    private Boolean showDepartureTable;
    @Persist
    private Boolean showReturnTable;
    @Persist
    private Boolean showSummaryDepart;
    @Persist
    private Boolean showSummaryReturn;

    private boolean fromNext;
    private boolean fromSort;

    public String getDateFieldFormat() {
        return com.dwidasa.ib.Constants.SHORT_FORMAT;
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void onPrepare() {
      
    }

    public void setupRender() {
        if (sessionManager.getSessionLastPage().equals(AeroTicketingInput.class.toString())) {
            form.clearErrors();
        }
        sessionManager.setAeroticketViewSession(aeroTicketingView);
        logger.info("sessionManager.getAeroticketViewSession()=" + sessionManager.getAeroticketViewSession());
        hiddenAeroTicketingView = "";

        sessionManager.setSessionLastPage(AeroTicketingFlightList.class.toString());

        if (aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE) && aeroTicketingView.getDepartAirlineCode().contains(",")) {
            aeroTicketingView.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH_MULTI);
        } else {
            aeroTicketingView.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH);
        }

        hiddenAeroTicketingViewInit = PojoJsonMapper.toJson(aeroTicketingView);

        Parameter ip = parameterDao.get(Constants.MPARAMETER_NAME.SERVER_IB_URL);
        hiddenServerIp = ip.getParameterValue();
//        hiddenTotalPrice = aeroTicketingView.getTotal();

        evenOdd = new EvenOdd();
	    evenOdd.setEven(false);
	    if (showDepartureTable == null) showDepartureTable = true;
	    if (showReturnTable == null) showReturnTable = true;
	    if (showSummaryDepart == null) showSummaryDepart = false;
	    if (showSummaryReturn == null) showSummaryReturn = false;

	    aeroTicketingView.setTicketPrice(getTicketPriceTotal());
	    aeroTicketingView.setTicketComission(getComissionTotal());
	    aeroTicketingView.setAgentPrice(getTicketPriceTotal().add(getComissionTotal()));

        hiddenAeroTicketingView = PojoJsonMapper.toJson(aeroTicketingView);
//        logger.info("hiddenAeroTicketingView=" + hiddenAeroTicketingView);
//        System.out.println("aeroTicketingView.getTotal()=" + aeroTicketingView.getTotal());
        fromNext = false;
        fromSort = false;
//        chooseValue = "Tidak";
    }

    private void parseAtribute(AeroFlightView flight) {
        flight.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.PRICE_DETAIL);
        flight.setBillerCode(aeroTicketingView.getBillerCode());
        flight.setProductCode(aeroTicketingView.getProductCode());
        flight.setProviderCode(aeroTicketingView.getProviderCode());
        flight.setTerminalId(aeroTicketingView.getProviderCode());
        flight.setMerchantType(aeroTicketingView.getMerchantType());
        flight.setAccountNumber(aeroTicketingView.getAccountNumber());
        flight.setAccountType(aeroTicketingView.getAccountType());
        flight.setCardNumber(aeroTicketingView.getCardNumber());
        flight.setTransactionDate(aeroTicketingView.getTransactionDate());
    }

    void onActionFromDayBeforeYesterday() {
        Date newDate = DateUtils.before(aeroTicketingView.getDepartDate(), 2);
        if (!aeroTicketingView.getIsDepartOnly() && newDate.compareTo(aeroTicketingView.getReturnDate()) > 0 ) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        } else {
    	    this.aeroTicketingView.setDepartDate(newDate);
        }
    }
	void onActionFromYesterday(){
        Date newDate = DateUtils.before(aeroTicketingView.getDepartDate(), 1);
        if (!aeroTicketingView.getIsDepartOnly() && newDate.compareTo(aeroTicketingView.getReturnDate()) > 0 ) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        } else {
            this.aeroTicketingView.setDepartDate(newDate);
        }
	}
    void onActionFromToday(){
        //Do Nothing
    }
	void onActionFromTomorrow(){
        Date newDate = DateUtils.after(aeroTicketingView.getDepartDate(), 1);
        if (!aeroTicketingView.getIsDepartOnly() && newDate.compareTo(aeroTicketingView.getReturnDate()) > 0 ) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        } else {
            this.aeroTicketingView.setDepartDate(newDate);
        }
	}
	void onActionFromDayAfterTomorrow(){
        Date newDate = DateUtils.after(aeroTicketingView.getDepartDate(), 2);
        if (!aeroTicketingView.getIsDepartOnly() && newDate.compareTo(aeroTicketingView.getReturnDate()) > 0 ) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        } else {
            this.aeroTicketingView.setDepartDate(newDate);
        }
	}

    void onActionFromDayBeforeYesterdayReturn() {
        Date newDate = DateUtils.before(aeroTicketingView.getDepartDate(), 2);
        if (!aeroTicketingView.getIsDepartOnly() && newDate.compareTo(aeroTicketingView.getDepartDate()) < 0 ) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        } else {
            this.aeroTicketingView.setReturnDate(newDate);
        }
    }
	void onActionFromYesterdayReturn(){
        Date newDate = DateUtils.before(aeroTicketingView.getReturnDate(), 1);
        if (!aeroTicketingView.getIsDepartOnly() && newDate.compareTo(aeroTicketingView.getDepartDate()) < 0 ) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        } else {
            this.aeroTicketingView.setReturnDate(newDate);
        }
	}
    void onActionFromTodayReturn(){
        //Do Nothing
    }
	void onActionFromTomorrowReturn(){
    Date newDate = DateUtils.after(aeroTicketingView.getReturnDate(), 1);
        if (!aeroTicketingView.getIsDepartOnly() && newDate.compareTo(aeroTicketingView.getDepartDate()) < 0 ) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        } else {
            this.aeroTicketingView.setReturnDate(newDate);
        }
	}
	void onActionFromDayAfterTomorrowReturn(){
        Date newDate = DateUtils.after(aeroTicketingView.getReturnDate(), 2);
        if (!aeroTicketingView.getIsDepartOnly() && newDate.compareTo(aeroTicketingView.getDepartDate()) < 0 ) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        } else {
            this.aeroTicketingView.setReturnDate(newDate);
        }
	}

	void onSelectedFromNext() {
		fromNext = true;
		logger.info("fromNext=" + fromNext);
	}

    void onValidateFromForm() {
        aeroTicketingView = PojoJsonMapper.fromJson(hiddenAeroTicketingView, AeroTicketingView.class);
        logger.info("1. aeroTicketingView.getDepartureFlight().getAeroConnectingFlight()=" + aeroTicketingView.getDepartureFlight().getAeroConnectingFlight());
        if (aeroTicketingView.getTotal() == null || aeroTicketingView.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            form.recordError("Silahkan Memilih Penerbangan Terlebih Dahulu");
        }
        System.out.println("aeroTicketingView.getTotal()=" + aeroTicketingView.getTotal() + "aeroTicketingView.getDepartureFlight().getAeroConnectingFlight()=" + aeroTicketingView.getDepartureFlight().getAeroConnectingFlight());

/*
        AeroFlightView departFlight = PojoJsonMapper.fromJson(hiddenSelectedDepart, AeroFlightView.class);
        System.out.println("departFlight.getAeroConnectingFlight()=" + departFlight.getAeroConnectingFlight());
        aeroTicketingView.setDepartureFlight(departFlight);
        System.out.println("aeroTicketingView.getDepartureFlight().getAeroConnectingFlight()=" + aeroTicketingView.getDepartureFlight().getAeroConnectingFlight());
        aeroTicketingView.setDepartAirlineCode(departFlight.getAirlineId());    //voltras berisi kode maskapai
        System.out.println("aeroTicketingView.getDepartAirlineCode()=" + aeroTicketingView.getDepartAirlineCode());
*/

        try {
            if (!form.getHasErrors()) {
                if (aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
                    aeroTicketingView.setSessionId(aeroTicketingView.getDepartureFlight().getSessionId());
                    aeroTicketingView.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.SEARCH_DETAIL);
                    aeroTicketingView = aeroTicketingSearchDetailViewService.getSeachDetail(aeroTicketingView);
                    logger.info("aeroTicketingView.getSessionId()=" + aeroTicketingView.getSessionId());
                } else {    //voltras
                    aeroTicketingView.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.PRICE_DETAIL);
                }
            }
        } catch (BusinessException e) {
        	form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }
	//@DiscardAfter
    public Object onSuccess() {
        logger.info("aeroTicketingView.getDepartAirlineCode()=" + aeroTicketingView.getDepartAirlineCode());
        logger.info("aeroTicketingView.getDepartureFlight().getAirlineId()=" + aeroTicketingView.getDepartureFlight().getAirlineId());
        aeroTicketingView.setDepartAirlineCode(aeroTicketingView.getDepartureFlight().getAirlineId());
        if (!aeroTicketingView.getIsDepartOnly()) {
            aeroTicketingView.setReturnAirlineCode(aeroTicketingView.getReturnFlight().getAirlineId());
        }
        sessionManager.setAeroticketViewSession(aeroTicketingView);
        if (fromNext) {
            Airport departArrivalAirport = cacheManager.getAirport(aeroTicketingView.getDepartureFlight().getArrivalAirportCode());
            aeroTicketingView.getDepartureFlight().setArrivalAirportFullName(departArrivalAirport.getAirportCity());
            logger.info("aeroTicketingView.getDepartureFlight().arrivalAirportFullName" + aeroTicketingView.getDepartureFlight().getArrivalAirportFullName());

            //refresh city return arrivalAirport
            if (aeroTicketingView.getReturnFlight() != null) {
                Airport returnArrivalAirport = cacheManager.getAirport(aeroTicketingView.getReturnFlight().getArrivalAirportCode());
                aeroTicketingView.getReturnFlight().setArrivalAirportFullName(returnArrivalAirport.getAirportCity());
                logger.info("aeroTicketingView.returnFlight.arrivalAirportFullName" + aeroTicketingView.getReturnFlight().getArrivalAirportFullName());
            }

            aeroTicketingFlightDetail.setAeroTicketingView(aeroTicketingView);
            logger.info("aeroTicketingFlightDetail.getAeroTicketingView()=" + aeroTicketingFlightDetail.getAeroTicketingView().getTransactionType());

            logger.info("2. aeroTicketingView.getDepartureFlight().getAeroConnectingFlight()=" + aeroTicketingView.getDepartureFlight().getAeroConnectingFlight());
            return aeroTicketingFlightDetail;
        }
        return this;
    }

    void pageReset() {
//        waterPaymentView = null;
    }

    public String getDayBeforeYesterday(){
    	Date theDay = DateUtils.before(aeroTicketingView.getDepartDate(), 2);
        return ddmm.format(theDay);
    }
    
    public String getYesterday(){
    	Date yesterday = DateUtils.before(aeroTicketingView.getDepartDate(), 1);
        return ddmm.format(yesterday);
    }

    public String getToday(){
        return ddmm.format(aeroTicketingView.getDepartDate());
    }

    public String getTodayMedium(){
        return mediumFormat.format(aeroTicketingView.getDepartDate());
    }
    
    public String getTomorrow(){
    	Date tomorrow = DateUtils.after(aeroTicketingView.getDepartDate(), 1);
        return ddmm.format(tomorrow);
    }
    
    public String getDayAfterTomorrow(){
    	Date theDay = DateUtils.after(aeroTicketingView.getDepartDate(), 2);
        return ddmm.format(theDay);
    }

    public String getDayBeforeYesterdayReturn(){
    	Date theDay = DateUtils.before(aeroTicketingView.getReturnDate(), 2);
        return ddmm.format(theDay);
    }

    public String getYesterdayReturn(){
    	Date yesterday = DateUtils.before(aeroTicketingView.getReturnDate(), 1);
        return ddmm.format(yesterday);
    }

    public String getTodayReturn(){
        return ddmm.format(aeroTicketingView.getReturnDate());
    }
    public String getTodayMediumReturn(){
        return mediumFormat.format(aeroTicketingView.getReturnDate());
    }

    public String getTomorrowReturn(){
    	Date tomorrow = DateUtils.after(aeroTicketingView.getReturnDate(), 1);
        return ddmm.format(tomorrow);
    }
    
    public String getDayAfterTomorrowReturn(){
    	Date theDay = DateUtils.after(aeroTicketingView.getReturnDate(), 2);
        return ddmm.format(theDay);
    }
    
	public AeroTicketingView getAeroTicketingView() {
		return aeroTicketingView;
	}

	public void setAeroTicketingView(AeroTicketingView aeroTicketingView) {
		this.aeroTicketingView = aeroTicketingView;
	}

    public boolean isSelectDeparture() {
    	return showDepartureTable;
    }
    public boolean isSelectReturn() {    	
    	return showReturnTable;
    }
    public boolean isDepartReturn() {    	
    	return !aeroTicketingView.getIsDepartOnly();
    }
    public boolean isAfterSelectDepart() {    	
    	return showSummaryDepart;
    }
    public boolean isAfterSelectReturn() {    	
    	return showSummaryReturn;
    }

    private AeroFlightView getDepartureFlight() {
		return aeroTicketingView.getDepartureFlight();
	}
	private AeroFlightView getReturnFlight() {
		return aeroTicketingView.getReturnFlight();
	}
	
    //depart
    public String getDepartDetailFlightNumber(){
    	return getDepartureFlight() == null ? "" : getDepartureFlight().getAirlineName() + " " + getDepartureFlight().getFlightNumber(); 
    }
    public String getDepartDetailFromTo(){
    	return getDepartureFlight() == null ? "" : aeroTicketingView.getDepartureAirportCode() + " " + hhmm.format(getDepartureFlight().getEtd()) + " >> " + aeroTicketingView.getDestinationAirportCode() + " " + hhmm.format(getDepartureFlight().getEta()); 
    }
    public String getDepartAdultLabel(){
    	return getDepartureFlight() == null ? "" : "Dewasa(" + getDepartureFlight().getAdultPassengerSummary().getPax() + ")"; 
    }
    public String getDepartAdultPriceValue(){
    	return getDepartureFlight() == null ? "" : formatter.format(getDepartureFlight().getAdultPassengerSummary().getTotal()); 
    }
    public boolean isChildPassengerDepart() {
    	if (getDepartureFlight() != null) {
	    	AeroPassengerSummary departChildSummary = getDepartureFlight().getChildPassengerSummary();
	    	return (departChildSummary != null) && (departChildSummary.getPax() != null) && (departChildSummary.getPax().intValue() > 0);
    	}
    	return false;
    }
    public boolean isInfantPassengerDepart() {
    	if (getDepartureFlight() != null) {
        	AeroPassengerSummary departInfantSummary = getDepartureFlight().getInfantPassengerSummary();
        	return (departInfantSummary != null) && (departInfantSummary.getPax() != null) && (departInfantSummary.getPax().intValue() > 0);
    	}
    	return false;
    }
    public String getDepartChildLabel(){
    	return getDepartureFlight() == null ? "" : "Anak-anak(" + getDepartureFlight().getChildPassengerSummary().getPax() + ")"; 
    }
    public String getDepartChildPriceValue(){
    	return getDepartureFlight() == null ? "" : !isChildPassengerDepart() ? "" : formatter.format(getDepartureFlight().getChildPassengerSummary().getTotal()); 
    }
    public String getDepartInfantLabel(){
    	return getDepartureFlight() == null ? "" : "Bayi(" + getDepartureFlight().getInfantPassengerSummary().getPax() + ")"; 
    }
    public String getDepartInfantPriceValue(){
    	return getDepartureFlight() == null ? "" : !isInfantPassengerDepart() ? "" : formatter.format(getDepartureFlight().getInfantPassengerSummary().getTotal()); 
    }

    public String getDepartPriceTotal(){
    	if (getDepartureFlight() != null) {
    		return formatter.format(getDepartureFlight().getTicketPrice());
    	}
    	return "0";
    }
    
    //return
    public String getReturnDetailFlightNumber(){
    	return getReturnFlight() == null ? "" : getReturnFlight().getAirlineName() + " " + getReturnFlight().getFlightNumber(); 
    }
    public String getReturnDetailFromTo(){
    	return getReturnFlight() == null ? "" : aeroTicketingView.getDestinationAirportCode() + " " + hhmm.format(getReturnFlight().getEtd()) + " >> " + aeroTicketingView.getDepartureAirportCode() + " " + hhmm.format(getReturnFlight().getEta()); 
    }
    public String getReturnAdultLabel(){
    	return getReturnFlight() == null ? "" : "Dewasa(" + getReturnFlight().getAdultPassengerSummary().getPax() + ")"; 
    }
    public String getReturnAdultPriceValue(){
    	return getReturnFlight() == null ? "" : formatter.format(getReturnFlight().getAdultPassengerSummary().getTotal()); 
    }
    public boolean isChildPassengerReturn() {
    	if (getReturnFlight() != null) {
	    	AeroPassengerSummary returnChildSummary = getReturnFlight().getChildPassengerSummary();
	    	return (returnChildSummary != null) && (returnChildSummary.getPax() != null) && (returnChildSummary.getPax().intValue() > 0);
    	}
    	return false;
    }
    public boolean isInfantPassengerReturn() {
    	if (getReturnFlight() != null) {
        	AeroPassengerSummary returnInfantSummary = getReturnFlight().getInfantPassengerSummary();
        	return (returnInfantSummary != null) && (returnInfantSummary.getPax() != null) && (returnInfantSummary.getPax().intValue() > 0);
    	}
    	return false;
    }
    public String getReturnChildLabel(){
    	return getReturnFlight() == null ? "" : "Anak-anak(" + getReturnFlight().getChildPassengerSummary().getPax() + ")"; 
    }

    public String getReturnChildPriceValue(){
    	return getReturnFlight() == null ? "" : !isChildPassengerReturn() ? "" : formatter.format(getReturnFlight().getChildPassengerSummary().getTotal()); 
    }
    public String getReturnInfantLabel(){
    	return getReturnFlight() == null ? "" : "Bayi(" + getReturnFlight().getInfantPassengerSummary().getPax() + ")"; 
    }
    public String getReturnInfantPriceValue(){
    	return getReturnFlight() == null ? "" : !isInfantPassengerReturn() ? "" : formatter.format(getReturnFlight().getInfantPassengerSummary().getTotal()); 
    }

    public String getReturnPriceTotal(){
    	if (getReturnFlight() != null) {
    		return formatter.format(getReturnFlight().getTicketPrice());
    	}
    	return "0";
    }
    
    public BigDecimal getTicketPriceTotal() {
    	BigDecimal departPrice = BigDecimal.ZERO;
    	BigDecimal returnPrice = BigDecimal.ZERO;
    	if (getDepartureFlight() != null && getDepartureFlight().getTicketPrice() != null) {
    		departPrice = getDepartureFlight().getTicketPrice();
    	}
    	if (getReturnFlight() != null && getReturnFlight().getTicketPrice() != null) {
    		returnPrice = getReturnFlight().getTicketPrice();
    	}
    	return departPrice.add(returnPrice);
    }
    public BigDecimal getComissionTotal() {
    	BigDecimal departComission = BigDecimal.ZERO;
    	BigDecimal returnComission = BigDecimal.ZERO;
    	if (getDepartureFlight() != null && getDepartureFlight().getComission() != null) {
    		departComission = getDepartureFlight().getComission();
    	}
    	if (getReturnFlight() != null && getReturnFlight().getComission() != null) {
    		returnComission = getReturnFlight().getComission();
    	}
    	return departComission.add(returnComission);
    }

    public boolean isReturnFlight() {
        return !aeroTicketingView.getIsDepartOnly();
    }

    public boolean isReturnFlight2() {
        return !aeroTicketingView.getIsDepartOnly();
    }

    public boolean isAeroticketing() {
    	return aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE);
    }

    public boolean isAeroChildPassenger() {
    	return isAeroticketing() && aeroTicketingView.getTotalChildren().intValue() > 0;
    }

    public boolean isAeroInfantPassenger() {
        return isAeroticketing() && aeroTicketingView.getTotalInfant().intValue() > 0;
    }

}

