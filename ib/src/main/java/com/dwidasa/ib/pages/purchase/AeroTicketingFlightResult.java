package com.dwidasa.ib.pages.purchase;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.model.airline.PacBook;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.view.AeroTicketingSearchBookingViewService;
import com.dwidasa.engine.util.ListUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.RequestGlobals;

import javax.sql.DataSource;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AeroTicketingFlightResult {
	private static Logger logger = Logger.getLogger( AeroTicketingFlightResult.class );
    @Persist
    private AeroTicketingView aeroTicketingView;


	@Inject
	private ThreadLocale threadLocale;

    @Inject private RequestGlobals requestGlobals;

    @Property private DateFormat mediumFormat = new SimpleDateFormat(Constants.DATEFORMAT.DD_MMM_YYYY, threadLocale.getLocale());
    @Property private DateFormat completeFormat = new SimpleDateFormat(com.dwidasa.ib.Constants.DATEFORMAT.COMPLETE_FORMAT, threadLocale.getLocale());
    @Property private DateFormat longFormat = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    @Property private DateFormat hhmm = new SimpleDateFormat(com.dwidasa.ib.Constants.DATEFORMAT.HH_MM, threadLocale.getLocale());

	@Property
	private NumberFormat formatter = NumberFormat.getNumberInstance(threadLocale.getLocale());

    @Property
    private String hiddenJsonAeroView;

    @Property
    private String hiddenJsonDepartFlight;

    @Property private String hiddenJsonReturnFlight;
    @Property private String urlTicket;

    @Property private String hiddenDepartFrom;  @Property private String hiddenDepartTo;
    @Property private String hiddenDepartConnectingFrom;    @Property private String hiddenDepartConnectingTo;
    @Property private String hiddenDepartConnecting2From;    @Property private String hiddenDepartConnecting2To;
    @Property private String hiddenReturnFrom;  @Property private String hiddenReturnTo;
    @Property private String hiddenReturnConnectingFrom;    @Property private String hiddenReturnConnectingTo;
    @Property private String hiddenReturnConnecting2From;    @Property private String hiddenReturnConnecting2To;

    @Property private String additionalEmail;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @InjectComponent
    private Form form;

	@Inject
	private AeroTicketingSearchBookingViewService aeroTicketingSearchBookingService;

    @Inject
    private ExtendedProperty extendedProperty;

    @Inject private ParameterDao parameterDao;

    private boolean fromNext;

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void onPrepare() {
      
    }

    public void setupRender() {

        //set Nama bandara untuk kepentingan UI
        //aeroTicketingView.departureFlight.departureAirportFullName

        AeroFlightView departFlight = aeroTicketingView.getDepartureFlight();
        Airport departAirportFrom = cacheManager.getAirport(departFlight.getDepartureAirportCode());
        hiddenDepartFrom = departAirportFrom.getAirportCity() + " - " + departAirportFrom.getAirportName();
        Airport departAirportTo = cacheManager.getAirport(departFlight.getArrivalAirportCode());
        hiddenDepartTo = departAirportTo.getAirportCity() + " - " + departAirportTo.getAirportName();

        if (isDepartConnecting()) {
            AeroConnectingFlight departConnecting = departFlight.getAeroConnectingFlight();
            Airport departConnectingFrom = cacheManager.getAirport(departConnecting.getConnectingFlightFrom());
            hiddenDepartConnectingFrom = departConnectingFrom.getAirportCity() + " - " + departConnectingFrom.getAirportName();
            Airport departConnectingTo = cacheManager.getAirport(departConnecting.getConnectingFlightTo());
            hiddenDepartConnectingTo = departConnectingTo.getAirportCity() + " - " + departConnectingTo.getAirportName();
        }
        if (isDepartConnecting2()) {
            AeroConnectingFlight departConnecting2 = departFlight.getAeroConnectingFlight2();
            Airport departConnecting2From = cacheManager.getAirport(departConnecting2.getConnectingFlightFrom());
            hiddenDepartConnecting2From = departConnecting2From.getAirportCity() + " - " + departConnecting2From.getAirportName();
            Airport departConnecting2To = cacheManager.getAirport(departConnecting2.getConnectingFlightTo());
            hiddenDepartConnecting2To = departConnecting2To.getAirportCity() + " - " + departConnecting2To.getAirportName();
        }
        if (isDepartReturn()) {
            AeroFlightView returnFlight = aeroTicketingView.getReturnFlight();
            Airport returnAirportFrom = cacheManager.getAirport(returnFlight.getDepartureAirportCode());
            hiddenReturnFrom = returnAirportFrom.getAirportCity() + " - " + returnAirportFrom.getAirportName();
            Airport returnAirportTo = cacheManager.getAirport(returnFlight.getArrivalAirportCode());
            hiddenReturnTo = returnAirportTo.getAirportCity() + " - " + returnAirportTo.getAirportName();

            if (isReturnConnecting()) {
                AeroConnectingFlight returnConnecting = returnFlight.getAeroConnectingFlight();
                Airport returnConnectingFrom = cacheManager.getAirport(returnConnecting.getConnectingFlightFrom());
                hiddenReturnConnectingFrom = returnConnectingFrom.getAirportCity() + " - " + returnConnectingFrom.getAirportName();
                Airport returnConnectingTo = cacheManager.getAirport(returnConnecting.getConnectingFlightTo());
                hiddenReturnConnectingTo = returnConnectingTo.getAirportCity() + " - " + returnConnectingTo.getAirportName();
            }
            if (isReturnConnecting2()) {
                AeroConnectingFlight returnConnecting2 = returnFlight.getAeroConnectingFlight2();
                Airport returnConnecting2From = cacheManager.getAirport(returnConnecting2.getConnectingFlightFrom());
                hiddenReturnConnecting2From = returnConnecting2From.getAirportCity() + " - " + returnConnecting2From.getAirportName();
                Airport returnConnecting2To = cacheManager.getAirport(returnConnecting2.getConnectingFlightTo());
                hiddenReturnConnecting2To = returnConnecting2To.getAirportCity() + " - " + returnConnecting2To.getAirportName();
            }
        }

    	sessionManager.setSessionLastPage(AeroTicketingFlightResult.class.toString());

        hiddenJsonAeroView = PojoJsonMapper.toJson(aeroTicketingView);
        hiddenJsonDepartFlight = PojoJsonMapper.toJson(aeroTicketingView.getDepartureFlight());
        hiddenJsonReturnFlight = PojoJsonMapper.toJson(aeroTicketingView.getReturnFlight());
        fromNext = false;

        DateFormat yyyyMMdd = new SimpleDateFormat(Constants.Short_FormatDt);
        String formattedDate = yyyyMMdd.format(aeroTicketingView.getTransactionDate());
        AeroBookInfo departBook = aeroTicketingView.getDepartureFlight().getBookInfo();
        AeroCustomer aeroCustomer = aeroTicketingView.getAeroCustomer();
        String fullName = aeroCustomer.getFirstName().replaceAll(" ", "_") + "_" + aeroCustomer.getLastName().replaceAll(" ", "_");

        Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.SERVER_IB_URL);

        String str = ip.getParameterValue() + "/bprks/tickets/" + formattedDate;
        System.out.println("str=" + str);
        String ticketName = "ticket_" + formattedDate + "_" + departBook.getBookCode() + "_" + fullName + ".pdf";
        urlTicket = str + "/" + ticketName;

    }

//	@DiscardAfter
    public Object onSuccess() {
        if (fromNext) { //kirim email
            List<AeroPassenger> passengers = aeroTicketingView.getPassengers();
            AeroPassenger firstPassenger = passengers.get(0);
            System.out.println("firstPassenger.getPassengerType()=" + firstPassenger.getPassengerType());

            DateFormat yyyyMMdd = new SimpleDateFormat(Constants.Short_FormatDt);
            String formattedDate = yyyyMMdd.format(aeroTicketingView.getTransactionDate());
            AeroBookInfo departBook = aeroTicketingView.getDepartureFlight().getBookInfo();
            AeroCustomer aeroCustomer = aeroTicketingView.getAeroCustomer();
            String fullName = aeroCustomer.getFirstName().replaceAll(" ", "_") + "_" + aeroCustomer.getLastName().replaceAll(" ", "_");

//            String realPath = requestGlobals.getHTTPServletRequest().getSession().getServletContext().getRealPath("/");
//            System.out.println("realPath=" + realPath);

//            String str = realPath + "/bprks/tickets/" + formattedDate;
//            System.out.println("str=" + str);
            Parameter folderParam = cacheManager.getParameter(com.dwidasa.engine.Constants.MPARAMETER_NAME.TICKET_FOLDER);
            String dirPathResult = folderParam.getParameterValue() + "/" + formattedDate;
            

            String ticketName = "ticket_" + formattedDate + "_" + departBook.getBookCode() + "_" + fullName + ".pdf";
            aeroTicketingView.setCardData2(dirPathResult + "/" + ticketName);

            if (additionalEmail == null) additionalEmail = "";
            aeroTicketingView.setCardData1(additionalEmail);

            String res = aeroTicketingSearchBookingService.sendEmail(aeroTicketingView);
            System.out.println("email res=" + res);
            logger.info("email res=" + res);

            return this;
        }

        return this;
    }


    void pageReset() {
    	
    }

	public AeroTicketingView getAeroTicketingView() {
		return aeroTicketingView;
	}

	public void setAeroTicketingView(AeroTicketingView aeroTicketingView) {
		this.aeroTicketingView = aeroTicketingView;
	}

    void onSelectedFromNext() {
   		fromNext = true;
   		logger.info("fromNext=" + fromNext);
   	}

    public boolean isDepartReturn() {
    	return !aeroTicketingView.getIsDepartOnly();
    }

    public boolean isAeroticket() {
    	return !aeroTicketingView.getProviderCode().equals(AirConstants.VOLTRAS.PROVIDER.CODE);
    }
    public boolean isVoltras() {
    	return aeroTicketingView.getProviderCode().equals(AirConstants.VOLTRAS.PROVIDER.CODE);
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

    public boolean isReturnFlight() { return !aeroTicketingView.getIsDepartOnly(); }

    public boolean isDepartConnecting() { return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight() != null; }
    public boolean isDepartConnecting2() { return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight2() != null; }
    public boolean isReturnConnecting() { return aeroTicketingView.getReturnFlight().getAeroConnectingFlight() != null; }
    public boolean isReturnConnecting2() { return aeroTicketingView.getReturnFlight().getAeroConnectingFlight2() != null; }

    public BigDecimal getTicketPrice(){
        return aeroTicketingView.getTotal().subtract(aeroTicketingView.getAssurancePrice());
    }
    public BigDecimal getTicketInsurance(){
        return aeroTicketingView.getAssurancePrice();
    }
    public boolean isVoltrasAndInsurance() { return isVoltras() && (aeroTicketingView.getAssuranceType() != null); }

    public boolean isInsurance() { return (aeroTicketingView.getAssuranceType() != null); }

    public boolean isAeroticketing() { return aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE); }

    public boolean isAeroChildPassenger() {
    	return isAeroticketing() && aeroTicketingView.getTotalChildren().intValue() > 0;
    }

    public boolean isAeroInfantPassenger() {
        return isAeroticketing() && aeroTicketingView.getTotalInfant().intValue() > 0;
    }

    public boolean isAeroticketAndInsurance() { return isAeroticket() && (aeroTicketingView.getAssuranceType() != null); }

}
