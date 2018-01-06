package com.dwidasa.ib.pages.purchase;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.IReportDataDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.model.airline.PacBook;
import com.dwidasa.engine.model.airline.PacRetrieve;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.view.AeroTicketingSearchBookingViewService;
import com.dwidasa.engine.util.ListUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.RequestGlobals;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AeroTicketingFlightBook {
	private static Logger logger = Logger.getLogger( AeroTicketingFlightBook.class );
    @Persist private AeroTicketingView aeroTicketingView;

	@Inject private ThreadLocale threadLocale;
    @Inject private OtpManager otpManager;
    @Inject private CacheManager cacheManager;
    @Inject private SessionManager sessionManager;
    @Inject private AeroTicketingSearchBookingViewService aeroTicketingSearchBookingService;
    @Inject private ExtendedProperty extendedProperty;
    @Inject private IReportDataDao iReportDataDao;
    @Inject private RequestGlobals requestGlobals;

    @Property private DateFormat mediumFormat = new SimpleDateFormat(Constants.DATEFORMAT.DD_MMM_YYYY, threadLocale.getLocale());
    @Property private DateFormat completeFormat = new SimpleDateFormat(Constants.DATEFORMAT.COMPLETE_FORMAT, threadLocale.getLocale());
    @Property private DateFormat longFormat = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
    @Property private DateFormat hhmm = new SimpleDateFormat(Constants.DATEFORMAT.HH_MM, threadLocale.getLocale());

	@Property private NumberFormat formatter = NumberFormat.getNumberInstance(threadLocale.getLocale());
    @Property private TokenView tokenView;
    @Property private int tokenType;
    @Property private String hiddenJsonAeroView;
    @Property private String hiddenJsonDepartFlight;
    @Property private String hiddenJsonReturnFlight;


    @Property private String hiddenDepartFrom;  @Property private String hiddenDepartTo;
    @Property private String hiddenDepartConnectingFrom;    @Property private String hiddenDepartConnectingTo;
    @Property private String hiddenDepartConnecting2From;    @Property private String hiddenDepartConnecting2To;
    @Property private String hiddenReturnFrom;  @Property private String hiddenReturnTo;
    @Property private String hiddenReturnConnectingFrom;    @Property private String hiddenReturnConnectingTo;
    @Property private String hiddenReturnConnecting2From;    @Property private String hiddenReturnConnecting2To;

    @InjectPage
    private AeroTicketingFlightResult aeroTicketingFlightResult;

    @InjectPage
    private AeroTicketingInput aeroTicketingInput;
    
    @InjectComponent
    private Form form;

    private boolean fromNext;

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
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

        AeroFlightView departFlightConnecting = aeroTicketingView.getDepartureFlight();

        logger.info("sessionManager.getAeroticketViewSession()=" + sessionManager.getAeroticketViewSession());
    	sessionManager.setSessionLastPage(AeroTicketingFlightBook.class.toString());
        setTokenType();

        //tambah penjagaan bookingCode diset, karena dari aeroticket kadang2 dapat bookingCode = "-"
        if (aeroTicketingView.getDepartureFlight().getBookInfo() == null) {
            AeroBookInfo departBookInfo = new AeroBookInfo();
            departBookInfo.setBookCode("-");
            departBookInfo.setStatus("FAILED");
            aeroTicketingView.getDepartureFlight().setBookInfo(departBookInfo);
        }
        if (isReturnFlight() && aeroTicketingView.getReturnFlight() != null && aeroTicketingView.getReturnFlight().getBookInfo() == null ) {
            AeroBookInfo returnBookInfo = new AeroBookInfo();
            returnBookInfo.setBookCode("-");
            returnBookInfo.setStatus("FAILED");
            aeroTicketingView.getReturnFlight().setBookInfo(returnBookInfo);
        }

        hiddenJsonAeroView = PojoJsonMapper.toJson(aeroTicketingView);
        hiddenJsonDepartFlight = PojoJsonMapper.toJson(aeroTicketingView.getDepartureFlight());
        hiddenJsonReturnFlight = PojoJsonMapper.toJson(aeroTicketingView.getReturnFlight());
        fromNext = false;

    }

    private void addAssurance(BigDecimal val) {
        aeroTicketingView.getDepartureFlight().setTicketPrice(aeroTicketingView.getDepartureFlight().getTicketPrice().add(val));
        int totalPass = aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalAdult() ;
        BigDecimal totalAssurance = val.multiply(new BigDecimal(totalPass));
        aeroTicketingView.getDepartureFlight().setTotal(aeroTicketingView.getDepartureFlight().getTotal().add(totalAssurance) );
        if (!aeroTicketingView.getIsDepartOnly()) {
            aeroTicketingView.getReturnFlight().setTicketPrice(aeroTicketingView.getReturnFlight().getTicketPrice().add(val));
            aeroTicketingView.getReturnFlight().setTotal(aeroTicketingView.getReturnFlight().getTotal().add(totalAssurance) );
        }
        aeroTicketingView.setTotal(aeroTicketingView.getTotal().add(totalAssurance));
    }

    void onValidateFromForm() {
        sessionManager.setAeroticketViewSession(null);
        logger.info("2 sessionManager.getAeroticketViewSession()=" + sessionManager.getAeroticketViewSession());
        try {
            if (!form.getHasErrors()) {
                if (aeroTicketingView.getProviderCode().equals(AirConstants.VOLTRAS.PROVIDER.CODE)) {
                    PacBook pacBook = aeroTicketingView.getPacBook();
                    PacRetrieve retrieveDepart = pacBook.getRetrieveDepart();
                    PacRetrieve retrieveReturn = pacBook.getRetrieveReturn();
                    if (fromNext) {
                        if (otpManager.validateToken(aeroTicketingView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
                            aeroTicketingView.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.TICKET);
                            aeroTicketingView = aeroTicketingSearchBookingService.getVoltrasTicketing(aeroTicketingView);
                            if (pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
                                aeroTicketingView = aeroTicketingSearchBookingService.getVoltrasTicketing(aeroTicketingView);
                            }
//                            //untuk tes, pake cancel
//                            aeroTicketingView.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.CANCEL);
//                            aeroTicketingView = aeroTicketingSearchBookingService.getVoltrasCancel(aeroTicketingView);
//                            if (pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
//                                aeroTicketingView.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.CANCEL);
//                                aeroTicketingView = aeroTicketingSearchBookingService.getVoltrasCancel(aeroTicketingView);
//                            }
                            aeroTicketingSearchBookingService.savingCustRegIReport(aeroTicketingView);
                        }
                    } else {
                        aeroTicketingView.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.CANCEL);
                        aeroTicketingView = aeroTicketingSearchBookingService.getVoltrasCancel(aeroTicketingView);
                        if (pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
                            aeroTicketingView.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.CANCEL);
                            aeroTicketingView = aeroTicketingSearchBookingService.getVoltrasCancel(aeroTicketingView);
                        }
                    }
                } else {
                    if (fromNext) {
                        aeroTicketingView.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE);
                        logger.info("aeroTicketingView.getDepartureFlight().getEtd()=" + aeroTicketingView.getDepartureFlight().getEtd());
                        logger.info("aeroTicketingView.getDepartureFlight().getEta()=" + aeroTicketingView.getDepartureFlight().getEta());
                        System.out.println("aeroTicketingView.getDepartureFlight().getEtd()=" + aeroTicketingView.getDepartureFlight().getEtd());
                        System.out.println("aeroTicketingView.getDepartureFlight().getEta()=" + aeroTicketingView.getDepartureFlight().getEta());
                        aeroTicketingView = aeroTicketingSearchBookingService.getAeroIssue(aeroTicketingView);
                    } else {
                        //
                    }
                }
                savingForIReport();
                generateFileOnServer();
            }
        } catch (BusinessException e) {
        	form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    private void savingForIReport() {
        //save untuk ireport logo yang sesuai
        AeroFlightView departFlight = aeroTicketingView.getDepartureFlight();
        IReportData iReportData = new IReportData();
        iReportData.setCustomerId(aeroTicketingView.getCustomerId());
        iReportData.setTransactionType(aeroTicketingView.getTransactionType());
        iReportData.setTransactionDate(aeroTicketingView.getTransactionDate());
        iReportData.setReferenceKey(departFlight.getBookInfo().getBookCode());
        iReportData.setReportType(IReportData.REPORT_LOGO);

        ServletContext context = requestGlobals.getHTTPServletRequest().getSession().getServletContext();
        System.out.println("context=" + context);
        String bprksPath = context.getRealPath("/bprks/img/bprks_blue.png");
        String airlinePath = context.getRealPath("/bprks/img/aero/aero/");
        System.out.println("bprksPath=" + bprksPath);
        System.out.println("airlinePath=" + airlinePath);
        Airline airlineDepart;
        if (aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            airlineDepart = cacheManager.getAirlineById(departFlight.getAirlineId());
        } else {
            airlineDepart = cacheManager.getAirlineByCode(departFlight.getAirlineId());
        }
        iReportData.setData1(AirConstants.DEPARTURE);
        iReportData.setData2(bprksPath);
        iReportData.setData3(airlinePath + "/" + airlineDepart.getImage());

        DateFormat sdf = new SimpleDateFormat(Constants.DATEFORMAT.COMPLETE_FORMAT);
        iReportData.setData4(sdf.format(departFlight.getFlightDate()));
        AeroCustomer aeroCustomer = aeroTicketingView.getAeroCustomer();
        iReportData.setData5(aeroCustomer.getFirstName() + " " + aeroCustomer.getLastName());
        iReportData.setData6(aeroCustomer.getCustomerPhone());
        iReportData.setData7(aeroCustomer.getCustomerEmail());

        iReportData.setCreated(new Date());
        iReportData.setCreatedby(aeroTicketingView.getCustomerId());
        iReportData.setUpdated(new Date());
        iReportData.setUpdatedby(aeroTicketingView.getCustomerId());
        iReportDataDao.save(iReportData);
        if (!aeroTicketingView.getIsDepartOnly()) {
            AeroFlightView returnFlight = aeroTicketingView.getReturnFlight();

            IReportData iReportData2 = new IReportData();
            iReportData2.setCustomerId(aeroTicketingView.getCustomerId());
            iReportData2.setTransactionType(aeroTicketingView.getTransactionType());
            iReportData2.setTransactionDate(aeroTicketingView.getTransactionDate());
            iReportData2.setReferenceKey(returnFlight.getBookInfo().getBookCode());
            iReportData2.setReportType(IReportData.REPORT_LOGO);

            System.out.println("context=" + context);
            Airline airlineReturn;
            if (aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
                airlineReturn = cacheManager.getAirlineById(returnFlight.getAirlineId());
            } else {
                airlineReturn = cacheManager.getAirlineByCode(returnFlight.getAirlineId());
            }
            iReportData2.setData1(AirConstants.RETURN);
            iReportData2.setData2(bprksPath);
            iReportData2.setData3(airlinePath + "/" + airlineReturn.getImage());

            iReportData2.setData4(sdf.format(returnFlight.getFlightDate()));
            iReportData2.setData5(aeroCustomer.getFirstName() + " " + aeroCustomer.getLastName());
            iReportData2.setData6(aeroCustomer.getCustomerPhone());
            iReportData2.setData7(aeroCustomer.getCustomerEmail());

            iReportData2.setCreated(new Date());	iReportData2.setCreatedby(aeroTicketingView.getCustomerId());
            iReportData2.setUpdated(new Date());    iReportData2.setUpdatedby(aeroTicketingView.getCustomerId());
            iReportDataDao.save(iReportData2);
        }
    }

    private void generateFileOnServer() {
        Date dt = aeroTicketingView.getTransactionDate();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        Date plusOne = c.getTime();

        try {
            DateFormat yyyyMMdd = new SimpleDateFormat(Constants.Short_FormatDt);
            String formattedDate = yyyyMMdd.format(aeroTicketingView.getTransactionDate());
            String path = getClass().getResource("/pdfs/RPlaneTicket.jasper").getPath();
            String dirPath = path.substring(0, path.length() - "RPlaneTicket.jasper".length());

            ServletContext context = requestGlobals.getHTTPServletRequest().getSession().getServletContext();
            String dirPathResult = context.getRealPath("/bprks/tickets/" + formattedDate);
            File theDir = new File(dirPathResult);
            try {
                theDir.mkdirs();
                System.out.println("DIR created");
            } catch (Exception ex) {
                System.out.println("ex.getMessage()=" + ex.getMessage());
            }

            Map hmap = new HashMap();
            logger.info("dt=" + dt + " plusOne=" + plusOne + " booking code=" + aeroTicketingView.getDepartureFlight().getBookInfo().getBookCode());
            logger.info("customerId=" + aeroTicketingView.getCustomerId() + " dirPath=" + dirPath);
            hmap.put("customerId", aeroTicketingView.getCustomerId());
            hmap.put("transactionType", aeroTicketingView.getTransactionType());	//AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE
            hmap.put("transactionDate", dt);
            hmap.put("transactionDatePlusOne", plusOne);
            hmap.put("bookingCodeDepart", aeroTicketingView.getDepartureFlight().getBookInfo().getBookCode());
            if (!aeroTicketingView.getIsDepartOnly() || aeroTicketingView.getReturnFlight() != null) {
                hmap.put("bookingCodeReturn", aeroTicketingView.getReturnFlight().getBookInfo().getBookCode());
            }
            hmap.put("reportType", IReportData.REPORT_LOGO);
            hmap.put("SUBREPORT_DIR", dirPath);

            AeroCustomer aeroCustomer = aeroTicketingView.getAeroCustomer();
            logger.info("aeroCustomer=" + aeroCustomer);
            String fullName = aeroCustomer.getFirstName().replaceAll(" ", "_") + "_" + aeroCustomer.getLastName().replaceAll(" ", "_");
            logger.info("fullName=" + fullName);

            DataSource ds = (DataSource) ServiceLocator.getService("dataSource");
            Connection conn = ds.getConnection();

            AeroBookInfo departBook = aeroTicketingView.getDepartureFlight().getBookInfo();
            try {
                String ticketName = "ticket_" + formattedDate + "_" + departBook.getBookCode() + "_" + fullName + ".pdf";
                String destinationFile = theDir + "/" + ticketName;
                JasperRunManager.runReportToPdfFile(path, destinationFile, hmap, conn);
            } catch (JRException jex) {
                System.out.println("jex=" + jex.toString() + " jex=" + jex.getMessage());
            } catch (Exception e) {
                e.printStackTrace(System.out);
                logger.error(e);
            } finally {
//                if (conn!=null)
//                {
//                    conn.close();
//                }
            }

        } catch (Exception ex) {
            logger.info("ex=" + ex.getMessage());
            System.out.println("ex=" + ex.getMessage());
        }
    }

    //@DiscardAfter
    public Object onSuccess() {
//        PacBook pacBook = aeroTicketingView.getPacBook();
//        PacRetrieve retrieveDepart = pacBook.getRetrieveDepart();
//        PacRetrieve retrieveReturn = pacBook.getRetrieveReturn();
        if (fromNext) {

            aeroTicketingFlightResult.setAeroTicketingView(aeroTicketingView);
            return aeroTicketingFlightResult;
        }
        else {
            return aeroTicketingInput;
        }
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

    public boolean isConfirmedBook() {
        boolean isConfirmed = true;
        if (!isVoltras()) {
            AeroBookInfo departBookInfo = aeroTicketingView.getDepartureFlight().getBookInfo();
            if (departBookInfo != null && !departBookInfo.getStatus().toUpperCase().equals("CONFIRMED")) {
                isConfirmed = false;
            } else if (aeroTicketingView.getReturnFlight()!= null) {
                AeroBookInfo returnBookInfo = aeroTicketingView.getReturnFlight().getBookInfo();
                if (returnBookInfo != null && !returnBookInfo.getStatus().toUpperCase().equals("CONFIRMED")) {
                    isConfirmed = false;
                }
            }
        }
    	return isConfirmed;
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

    public boolean isDepartConnecting() {
        return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight() != null;
    }
    public boolean isDepartConnecting2() {
        return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight2() != null;
    }
    public boolean isReturnConnecting() {
        return aeroTicketingView.getReturnFlight().getAeroConnectingFlight() != null;
    }
    public boolean isReturnConnecting2() {
        return aeroTicketingView.getReturnFlight().getAeroConnectingFlight2() != null;
    }

    public boolean isVoltrasAndInsurance() { return isVoltras() && (aeroTicketingView.getAssuranceType() != null); }

    public boolean isInsurance() {
        return (aeroTicketingView.getAssuranceType() != null);
    }

    public boolean isAeroticketing() { return aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE); }

    public boolean isAeroChildPassenger() {
    	return isAeroticketing() && aeroTicketingView.getTotalChildren().intValue() > 0;
    }

    public boolean isAeroInfantPassenger() {
        return isAeroticketing() && aeroTicketingView.getTotalInfant().intValue() > 0;
    }

    public boolean isAeroticketAndInsurance() { return isAeroticket() && (aeroTicketingView.getAssuranceType() != null); }

}
