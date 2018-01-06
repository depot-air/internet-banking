package com.dwidasa.ib.pages.purchase;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.AeroBookInfo;
import com.dwidasa.engine.model.AeroPassengerSummary;
import com.dwidasa.engine.model.Airline;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.airline.PacBook;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.view.AeroTicketingPriceDetailViewService;
import com.dwidasa.engine.service.view.AeroTicketingSearchBookingViewService;
import com.dwidasa.engine.service.view.AeroTicketingSearchDetailViewService;
import com.dwidasa.engine.util.ListUtils;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

//@Import(library = {"context:layout/javascript/jquery-1.6.2.min.js"})
public class AeroTicketingFlightInsurance {
	private static Logger logger = Logger.getLogger( AeroTicketingFlightInsurance.class );
    @Persist
    private AeroTicketingView aeroTicketingView;

	@Inject
	private ThreadLocale threadLocale;

    @Property
    private DateFormat dd_mm_yyyy = new SimpleDateFormat(com.dwidasa.ib.Constants.SHORT_FORMAT, threadLocale.getLocale());
    @Property
    private DateFormat hhmm = new SimpleDateFormat(com.dwidasa.ib.Constants.DATEFORMAT.HH_MM, threadLocale.getLocale());
    @Property
    private DateFormat mediumFormat = new SimpleDateFormat(com.dwidasa.ib.Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    
    @InjectPage
    private AeroTicketingFlightBook aeroTicketingFlightBook;

    @Property
    private String transactionType;

    @Property
    private int tokenType;

	@Property
	private NumberFormat formatter = NumberFormat.getNumberInstance(threadLocale.getLocale());

    @Inject private SessionManager sessionManager;
    @Inject private CacheManager cacheManager;

    @Inject private AeroTicketingSearchBookingViewService aeroTicketingSearchBookingViewService;

    @InjectComponent private Form form;

    @Inject private Messages message;

    @Inject private ExtendedProperty extendedProperty;

    @Property private String chooseValue;
    @Property private String hiddenAeroTicketingView;
    @Property private String sinarmasAssurance;

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

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(AeroTicketingFlightInsurance.class.toString());
        hiddenAeroTicketingView = PojoJsonMapper.toJson(aeroTicketingView);
        chooseValue = "Tidak";
    }

    void onValidateFromForm() {
        if (chooseValue == null) {
            form.recordError("Silahkan Menentukan Menggunakan Asuransi Atau Tidak");
        }

        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
        AeroTicketingView view = gson.fromJson(hiddenAeroTicketingView, AeroTicketingView.class);
        aeroTicketingView.setAssuranceType(view.getAssuranceType());
        aeroTicketingView.setAssurancePrice(view.getAssurancePrice() != null? view.getAssurancePrice() : BigDecimal.ZERO);
        aeroTicketingView.setTotal(view.getTotal());
        logger.info("1. aeroTicketingView.getTotal()=" + aeroTicketingView.getTotal());
        try {
            if (!form.getHasErrors()) {
                aeroTicketingView.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.SEARCH_BOOK_V2);
                aeroTicketingView = aeroTicketingSearchBookingViewService.getSearchBooking(aeroTicketingView);

                logger.info("2. aeroTicketingView.getTotal()=" + aeroTicketingView.getTotal());
                AeroBookInfo bookInfo = aeroTicketingView.getDepartureFlight().getBookInfo();
                if (bookInfo != null) {
                    System.out.println("bookInfo.getBookCode()" + bookInfo.getBookCode());
                    System.out.println("bookInfo.getStatus()" + bookInfo.getStatus());
                }
                if (aeroTicketingView.getReturnFlight() != null) {
                    AeroBookInfo retBookInfo = aeroTicketingView.getReturnFlight().getBookInfo();
                    if (retBookInfo != null) {
                        System.out.println("retBookInfo.getBookCode()" + retBookInfo.getBookCode());
                        System.out.println("retBookInfo..getStatus()" + retBookInfo.getStatus());
                    }
                }
                if (aeroTicketingView.getProviderCode().equals(AirConstants.VOLTRAS.PROVIDER.CODE)) {
                    aeroTicketingView.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.RETRIEVE);
                    PacBook pacBook = aeroTicketingView.getPacBook();
                    aeroTicketingView = aeroTicketingSearchBookingViewService.getVoltrasRetrieve(aeroTicketingView);  //getBookingCode for departure
                    logger.info("aeroTicketingView.getAssuranceType()=" + aeroTicketingView.getAssuranceType() + " aeroTicketingView.getAssurancePrice()=" + aeroTicketingView.getAssurancePrice());
                    if (!aeroTicketingView.getIsDepartOnly() && pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
                        aeroTicketingView = aeroTicketingSearchBookingViewService.getVoltrasRetrieve(aeroTicketingView);  //getBookingCode for return
                    }
                    logger.info("3. aeroTicketingView.getTotal()=" + aeroTicketingView.getTotal());
                    //jika 1 maskapai maka hanya dapat 1 kode booking saja meskipun depart-return
                    if (!aeroTicketingView.getIsDepartOnly() && aeroTicketingView.getDepartAirlineCode().equals(aeroTicketingView.getReturnAirlineCode()) ) {
                        AeroBookInfo departBookInfo = aeroTicketingView.getDepartureFlight().getBookInfo();
                        AeroBookInfo returnBookInfo = new AeroBookInfo();
                        returnBookInfo.setBookCode(departBookInfo.getBookCode());
                        returnBookInfo.setTimeLimit(departBookInfo.getTimeLimit());
                        returnBookInfo.setStatus(departBookInfo.getStatus());
                        aeroTicketingView.getReturnFlight().setBookInfo(returnBookInfo);

                        //set pnrId return = pnrId depart
                        aeroTicketingView.getPacBook().setPnrIdReturn(aeroTicketingView.getPacBook().getPnrIdDepart());
                    }
                } else {

                }
            }
        } catch (BusinessException e) {
        	form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

//	@DiscardAfter
    public Object onSuccess() {
        logger.info("3. aeroTicketingView.getTotal()=" + aeroTicketingView.getTotal());
        if (aeroTicketingView.getDepartureFlight().getChildPassengerSummary() == null) {
            AeroFlightView departFlight = aeroTicketingView.getDepartureFlight();
            AeroPassengerSummary childPassengerSummary =  new AeroPassengerSummary();
            childPassengerSummary.setPax(0);
            departFlight.setChildPassengerSummary(childPassengerSummary);
            aeroTicketingView.setDepartureFlight(departFlight);
        }
        if (aeroTicketingView.getDepartureFlight().getInfantPassengerSummary() == null) {
            AeroFlightView departFlight = aeroTicketingView.getDepartureFlight();
            AeroPassengerSummary infantPassengerSummary =  new AeroPassengerSummary();
            infantPassengerSummary.setPax(0);
            departFlight.setInfantPassengerSummary(infantPassengerSummary);
            aeroTicketingView.setDepartureFlight(departFlight);
        }
        aeroTicketingFlightBook.setAeroTicketingView(aeroTicketingView);
        return aeroTicketingFlightBook;
    }

    void pageReset() {
    	
    }

	public AeroTicketingView getAeroTicketingView() {
		return aeroTicketingView;
	}

	public void setAeroTicketingView(AeroTicketingView aeroTicketingView) {
		this.aeroTicketingView = aeroTicketingView;
	}

    private String getAirlineCodeById(String theId) {
        if (aeroTicketingView.getProviderCode() != null && aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            Airline airline = cacheManager.getAirlineById(theId);
            if (airline != null) {
                return airline.getAirlineCode();
            }
        }
        return theId;
   	}

    public String getDepartLogo(){
        String code = aeroTicketingView.getDepartAirlineCode();
        if (aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            code = getAirlineCodeById(code);
        }
        String logo = ListUtils.getLogo(code);
        return logo;
    }

    public String getReturnLogo(){
        String code = aeroTicketingView.getReturnAirlineCode();
        if (aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            code = getAirlineCodeById(code);
        }
        String logo = ListUtils.getLogo(code);
        return logo;
    }

    public boolean isAdultPassenger() {
        return aeroTicketingView.getTotalAdult() > 0;
    }
    public boolean isChildPassenger() {
        return aeroTicketingView.getTotalChildren() > 0;
    }
    public boolean isInfantPassenger() {
        return aeroTicketingView.getTotalInfant() > 0;
    }

    public boolean isReturnFlight() { return !aeroTicketingView.getIsDepartOnly(); }

    public boolean isAeroticket() { return aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE); }

    public boolean isVoltras() { return !isAeroticket(); }

    public boolean isDepartConnecting() { return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight() != null; }
    public boolean isDepartConnecting2() { return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight2() != null; }
    public boolean isReturnConnecting() { return aeroTicketingView.getReturnFlight().getAeroConnectingFlight() != null; }
    public boolean isReturnConnecting2() { return aeroTicketingView.getReturnFlight().getAeroConnectingFlight2() != null; }

    public boolean isAeroChildPassenger() {
    	return isAeroticket() && aeroTicketingView.getTotalChildren().intValue() > 0;
    }

    public boolean isAeroInfantPassenger() {
        return isAeroticket() && aeroTicketingView.getTotalInfant().intValue() > 0;
    }

}
