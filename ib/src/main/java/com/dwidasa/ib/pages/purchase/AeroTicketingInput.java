package com.dwidasa.ib.pages.purchase;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.model.airline.PacBook;
import com.dwidasa.engine.service.AirportService;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.view.AeroTicketingSearchBookingViewService;
import com.dwidasa.engine.util.ListUtils;
import com.lowagie.text.pdf.codec.Base64;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.view.AeroTicketingViewService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

import javax.sql.DataSource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;

public class AeroTicketingInput {
	private static final String PILIH_MASKAPAI_PENERBANGAN = "Pilih Maskapai Penerbangan";
	private static final String PILIH_KOTA_KEBERANGKATAN = "Pilih Kota Keberangkatan";
	private static final String PILIH_KOTA_TUJUAN = "Pilih Kota Tujuan";
	private static Logger logger = Logger.getLogger( AeroTicketingInput.class );
	
    @Persist
    @Property
    private AeroTicketingView aeroTicketingView;
    private List<AeroFlightView> flights;

    @InjectPage private AeroTicketingFlightList aeroTicketingFlightList;

    @Property
    private String transactionType;

    @Property
    private String chooseValue;
    
    @Property private String hiddenAirasia;
    @Property private String hiddenBatavia;
    @Property private String hiddenCitilink;
    @Property private String hiddenGaruda;
    @Property private String hiddenGarudaExec;
    @Property private String hiddenKalstar;
    @Property private String hiddenLionair;
    @Property private String hiddenTiger;
    @Property private String hiddenMerpati;
    @Property private String hiddenSriwijaya;
    @Property private String hiddenXpress;

    @Property private String hiddenAirasiaMalaysia;
    @Property private String hiddenAviaStar;
    @Property private String hiddenBatikAir;
    @Property private String hiddenMalindo;
    @Property private String hiddenNamAir;
    @Property private String hiddenSkyAviation;
    @Property private String hiddenThaiLion;
    @Property private String hiddenTigerAirSing;
    @Property private String hiddenTriganaAir;
    @Property private String hiddenWingsAir;

    //untuk menampilkan logo ketika tombol back
    @Property private String hiddenSelectedAirlines;
    @Property private String hiddenAeroTicketingView;

//    @Property @Persist private String hiddenOftenDepart;
//    @Property @Persist private String hiddenOftenReturn;
//    @Property @Persist private String hiddenOftenRoute;

    @Property
    private boolean saveBoxValue;
    
    @Property
    private int tokenType;

    @Property private String sDepartDate;
    @Property private String sReturnDate;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private OtpManager otpManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages message;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private RequestGlobals requestGlobals;

    @Property
    private SelectModel departAirlineModel;
    
    @Property
    private SelectModel airportModel;

    @Property
    private List<String> adultModel;

    @Property
    private List<String> childrenModel;

    @Property
    private SelectModel providerModel;
    
    @Property
    private Object currentKey;

    @Property private String hiddenServerIp;
    @Property private String hiddenCustomerId;
    
    @Inject private ExtendedProperty extendedProperty;
    @Inject private AeroTicketingSearchBookingViewService aeroTicketingSearchBookingService;
    @Inject private AirportService airportService;
    @Inject private CustomerRegisterService customerRegisterService;
    @Inject private ParameterDao parameterDao;

    public String getDateFieldFormat() {
        return com.dwidasa.ib.Constants.SHORT_FORMAT;
    }

    private void buildDepartAirlineModel() {
    	//departAirlineModel = genericSelectModelFactory.create(cacheManager.getAirlines());
    }

    private void buildAirportModel() {
        //airportModel = genericSelectModelFactory.create(cacheManager.getAirports());

//        List<Airport> airportsDepart = airportService.getAirports(sessionManager.getLoggedCustomerView().getId(),
//                AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, AirConstants.AEROTICKETING.PROVIDER.CODE, "FROM");
//        List<Airport> airportsReturn = airportService.getAirports(sessionManager.getLoggedCustomerView().getId(),
//                AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, AirConstants.AEROTICKETING.PROVIDER.CODE, "TO");
//        List<CustomerRegister> crs = customerRegisterService.getAirportsFromTo("" + sessionManager.getLoggedCustomerView().getId(),
//                AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, AirConstants.AEROTICKETING.PROVIDER.CODE, "FROM");
//        hiddenOftenDepart = PojoJsonMapper.toJson((airportsDepart == null) ? "" : airportsDepart);
//        hiddenOftenReturn = PojoJsonMapper.toJson((airportsReturn == null) ? "" : airportsReturn);
//        hiddenOftenRoute =  PojoJsonMapper.toJson((crs == null) ? "" : crs);
        Parameter ip = parameterDao.get(Constants.MPARAMETER_NAME.SERVER_IB_URL);
        hiddenServerIp = ip.getParameterValue();
        hiddenCustomerId = "" + sessionManager.getLoggedCustomerView().getId();
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
        AeroTicketingView aeroView = sessionManager.getAeroticketViewSession();
        if (sessionManager.getSessionLastPage().equals(AeroTicketingFlightBook.class.toString()) && aeroView != null) {
            if (aeroView.getProviderCode().equals(AirConstants.VOLTRAS.PROVIDER.CODE) &&
                    aeroView.getPacBook() != null) {
                PacBook pacBook = aeroView.getPacBook();
                aeroView.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.CANCEL);
                aeroView = aeroTicketingSearchBookingService.getVoltrasCancel(aeroView);
                logger.info("pacBook.getPnrIdDepart()=" + pacBook.getPnrIdDepart());
                logger.info("pacBook.getPnrIdReturn()=" + pacBook.getPnrIdReturn());
                if (pacBook != null && pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
                    aeroView.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.CANCEL);
                    aeroView = aeroTicketingSearchBookingService.getVoltrasCancel(aeroView);
                }
            }
            aeroTicketingView = null;
        } else {
            if (aeroView == null) aeroTicketingView = null;
        }
    	sessionManager.setSessionLastPage(AeroTicketingInput.class.toString());
        chooseValue = "return";
        logger.info("sessionManager.getAeroticketViewSession()=" + sessionManager.getAeroticketViewSession());

        if (aeroTicketingView == null) {
        	aeroTicketingView = new AeroTicketingView();
            //set margin / komisi di awal, sehingga harga yg muncul di awal sudah termasuk margin / komisi
            Parameter pcomission = parameterDao.get(Constants.MPARAMETER_NAME.KOMISI_TIKET_PESAWAT);
            BigDecimal commission = BigDecimal.ZERO;
            try {
                commission = BigDecimal.valueOf(Double.parseDouble(pcomission.getParameterValue()));
            } catch (Exception ex) {
                //do nothing
            }
            aeroTicketingView.setAgentMargin(commission);
            setSearchView();
            hiddenSelectedAirlines = "";
        } else {
            //set logo dan tanggal jika sudah ada nilainya (dari tekan tombol back)
            System.out.println("aeroTicketingView.getDepartDate()=" + aeroTicketingView.getDepartDate());
            System.out.println("aeroTicketingView.getReturnDate()=" + aeroTicketingView.getReturnDate());
            System.out.println("aeroTicketingView.getDepartAirlineCode()=" + aeroTicketingView.getDepartAirlineCode());

            DateFormat sdf = new SimpleDateFormat(getDateFieldFormat());
            if (aeroTicketingView.getDepartDate() != null) {
                sDepartDate = sdf.format(aeroTicketingView.getDepartDate());
            }
            if (aeroTicketingView.getReturnDate() != null) {
                sReturnDate = sdf.format(aeroTicketingView.getReturnDate());
            }

            if (aeroTicketingView.getProviderCode() != null && aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
                if (aeroTicketingView.getDepartAirlineCode() != null) {
                    if (aeroTicketingView.getDepartAirlineCode().contains(",")) {
                        String[] ids = aeroTicketingView.getDepartAirlineCode().split(",");
                        String codes = "";
                        for (String id : ids) {
                            codes += getAirlineCodeById(id) + ",";
                        }
                        hiddenSelectedAirlines = codes.substring(0, codes.length() - 1);
                    } else {
                        hiddenSelectedAirlines = getAirlineCodeById(aeroTicketingView.getDepartAirlineCode());
                    }
                }
            } else {
                hiddenSelectedAirlines = aeroTicketingView.getDepartAirlineCode();  //ex:QZ,QG,GA
            }
            hiddenAeroTicketingView = PojoJsonMapper.toJson(aeroTicketingView);
        }
        //buildDepartAirlineModel();
        buildAirportModel();
        buildProviderModel();
        adultModel = getNumbers(false);
        childrenModel = getNumbers(true);
        
        aeroTicketingView.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH);

    }

    private void setSearchView() {
    	aeroTicketingView.setTransactionDate(new Date());
    	aeroTicketingView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	aeroTicketingView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());

        aeroTicketingView.setMerchantType(sessionManager.getDefaultMerchantType());
        aeroTicketingView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        aeroTicketingView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        aeroTicketingView.setSave(saveBoxValue);
        aeroTicketingView.setToAccountType("00");
        aeroTicketingView.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH);
        aeroTicketingView.setIsDepartOnly(chooseValue != null && chooseValue.equalsIgnoreCase("return") ? false : true);
//        System.out.println("aeroTicketingView.getDepartureAirportCode()=" + aeroTicketingView.getDepartureAirportCode());
        if (aeroTicketingView.getDepartureAirportCode() != null && !aeroTicketingView.getDepartureAirportCode().equals("")) {
        	Airport departureAirport = cacheManager.getAirport(aeroTicketingView.getDepartureAirportCode());
            aeroTicketingView.setDepartureAirportName(departureAirport.getAirportCity());		//yang ditampikan city
        }
//        	aeroTicketingView.getDestinationAirportCode()=" + aeroTicketingView.getDestinationAirportCode());
        if (aeroTicketingView.getDestinationAirportCode() != null && !aeroTicketingView.getDestinationAirportCode().equals("")) {
            Airport destinationAirport = cacheManager.getAirport(aeroTicketingView.getDestinationAirportCode());
            aeroTicketingView.setDestinationAirportName(destinationAirport.getAirportCity());	//yang ditampikan city
        }
        aeroTicketingView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
        aeroTicketingView.setAccountNumber(sessionManager.getLoggedCustomerView().getAccountNumber());
        if (aeroTicketingView.getTotalAdult() == null) aeroTicketingView.setTotalAdult(1);
        if (aeroTicketingView.getTotalChildren() == null) aeroTicketingView.setTotalChildren(0);
        if (aeroTicketingView.getTotalInfant() == null) aeroTicketingView.setTotalInfant(0);
    }
    
    void onValidateFromForm() {

        aeroTicketingView.setIsDepartOnly(chooseValue != null && chooseValue.equalsIgnoreCase("return") ? false : true);
        DateFormat sdf = new SimpleDateFormat(getDateFieldFormat());
        System.out.println("sDepartDate=" + sDepartDate);
        if (sDepartDate == null || sDepartDate.equals("")) {
            form.recordError(message.get("departDate-required-message"));
        } else {
            try {
                aeroTicketingView.setDepartDate(sdf.parse(sDepartDate));
            } catch (ParseException e) {
                aeroTicketingView.setDepartDate(null);
            }
        }
        logger.info("aeroTicketingView.getDepartDate()=" + aeroTicketingView.getDepartDate());
        if (aeroTicketingView.getDepartDate() != null && DateUtils.compareOnlyDate(aeroTicketingView.getDepartDate(), new Date()) < 0) {
            form.recordError(message.get("departDate-mustafter-today-message"));
        }
        if (chooseValue != null && chooseValue.equalsIgnoreCase("return")) {
            if (sReturnDate == null || sReturnDate.equals("")) {
                form.recordError(message.get("returnDate-required-message"));
            } else {
                try {
                    aeroTicketingView.setReturnDate(sdf.parse(sReturnDate));
                } catch (ParseException e) {
                    aeroTicketingView.setReturnDate(null);
                }
            }

            if (aeroTicketingView.getDepartDate() != null && aeroTicketingView.getReturnDate() != null && (aeroTicketingView.getDepartDate().compareTo(aeroTicketingView.getReturnDate()) > 0 )) {
                form.recordError(message.get("returnDate-mustafter-message"));
            }

        }

    	String airlineCodes = "";	//di tml menggunakan code shg bisa digunakan oleh dealer lain selain aeroticketing
    	if (hiddenAirasia != null && hiddenAirasia.equals("QZ")) airlineCodes += getAirlineIdByCode(hiddenAirasia) + ",";
    	if (hiddenBatavia != null && hiddenBatavia.equals("Y6")) airlineCodes += getAirlineIdByCode(hiddenBatavia) + ",";
    	if (hiddenCitilink != null && hiddenCitilink.equals("QG")) airlineCodes += getAirlineIdByCode(hiddenCitilink) + ",";
    	if (hiddenGaruda != null && hiddenGaruda.equals("GA")) airlineCodes += getAirlineIdByCode(hiddenGaruda) + ",";
    	if (hiddenGarudaExec != null && hiddenGarudaExec.equals("GE")) airlineCodes += getAirlineIdByCode(hiddenGarudaExec) + ",";
    	if (hiddenKalstar != null && hiddenKalstar.equals("KD")) airlineCodes += getAirlineIdByCode(hiddenKalstar) + ",";
    	if (hiddenLionair != null && hiddenLionair.equals("JT")) airlineCodes += getAirlineIdByCode(hiddenLionair) + ",";
    	if (hiddenMerpati != null && hiddenMerpati.equals("MZ")) airlineCodes += getAirlineIdByCode(hiddenMerpati) + ",";
    	if (hiddenSriwijaya != null && hiddenSriwijaya.equals("SJ")) airlineCodes += getAirlineIdByCode(hiddenSriwijaya) + ",";
        if (hiddenXpress != null && hiddenXpress.equals("XP")) airlineCodes += getAirlineIdByCode(hiddenXpress) + ",";
    	if (hiddenTiger != null && hiddenTiger.equals("RI")) airlineCodes += getAirlineIdByCode(hiddenTiger) + ",";

        if (hiddenAirasiaMalaysia != null && hiddenAirasiaMalaysia.equals("AK")) airlineCodes += getAirlineIdByCode(hiddenAirasiaMalaysia) + ",";
        if (hiddenAviaStar != null && hiddenAviaStar.equals("MV")) airlineCodes += getAirlineIdByCode(hiddenAviaStar) + ",";
        if (hiddenBatikAir != null && hiddenBatikAir.equals("ID")) airlineCodes += getAirlineIdByCode(hiddenBatikAir) + ",";
        if (hiddenMalindo != null && hiddenMalindo.equals("OD")) airlineCodes += getAirlineIdByCode(hiddenMalindo) + ",";
        if (hiddenNamAir != null && hiddenNamAir.equals("XX")) airlineCodes += getAirlineIdByCode(hiddenNamAir) + ",";
        if (hiddenSkyAviation != null && hiddenSkyAviation.equals("SY")) airlineCodes += getAirlineIdByCode(hiddenSkyAviation) + ",";
        if (hiddenThaiLion != null && hiddenThaiLion.equals("SL")) airlineCodes += getAirlineIdByCode(hiddenThaiLion) + ",";
        if (hiddenTigerAirSing != null && hiddenTigerAirSing.equals("TR")) airlineCodes += getAirlineIdByCode(hiddenTigerAirSing) + ",";
        if (hiddenTriganaAir != null && hiddenTriganaAir.equals("IL")) airlineCodes += getAirlineIdByCode(hiddenTriganaAir) + ",";
        if (hiddenWingsAir != null && hiddenWingsAir.equals("IW")) airlineCodes += getAirlineIdByCode(hiddenWingsAir) + ",";

        if (airlineCodes.equals("")) {
            form.recordError(message.get("billerCode-required-message"));
        } else {
            //nilainya salah, hanya diisi saja,
            aeroTicketingView.setDepartAirlineCode(airlineCodes.substring(0, airlineCodes.length() - 1));
            aeroTicketingView.setReturnAirlineCode(aeroTicketingView.getDepartAirlineCode());
        }
    	if (aeroTicketingView.getDepartureAirportCode() == null || aeroTicketingView.getDepartureAirportCode().equals("") || aeroTicketingView.getDepartureAirportCode().equals(PILIH_KOTA_KEBERANGKATAN)) {
    		form.recordError(message.get("departureCity-required-message"));
    	}
    	if (aeroTicketingView.getDestinationAirportCode() == null || aeroTicketingView.getDestinationAirportCode().equals("") || aeroTicketingView.getDestinationAirportCode().equals(PILIH_KOTA_TUJUAN)) {
    		form.recordError(message.get("destinationCity-required-message"));
    	}
    	if (aeroTicketingView.getDepartureAirportCode() != null && aeroTicketingView.getDestinationAirportCode() != null && aeroTicketingView.getDepartureAirportCode().equals(aeroTicketingView.getDestinationAirportCode())) {
    		form.recordError(message.get("destinationCity-mustDifferent-message"));
    	}
        if (aeroTicketingView.getProviderCode() == null) {
            form.recordError(message.get("providerCode-required-message"));
        }
//    	System.out.println("chooseValue=" + chooseValue);

        if (aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalChildren() + aeroTicketingView.getTotalInfant() > 7) {
    		form.recordError(message.get("passenger-maximum-message"));
    	}
        if (aeroTicketingView.getTotalAdult() < aeroTicketingView.getTotalInfant() ) {
    		form.recordError(message.get("passenger-adult-mustbigger"));
    	}
        try {
            if (!form.getHasErrors() ) {
                setSearchView();
                setPassengers();

            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

	private String getAirlineIdByCode(String hiddenCode) {
        if (aeroTicketingView.getProviderCode() != null && aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            Airline airline = cacheManager.getAirlineByCode(hiddenCode);
            if (airline != null) return airline.getAirlineId();
        }
        return hiddenCode;
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

    public static List<String> getNumbers(boolean isWithZero) {
		List<String> numbers = new ArrayList<String>();
		if (isWithZero) numbers.add("0");
        numbers.add("1");     
        numbers.add("2");     
        numbers.add("3");     
        numbers.add("4");     
        numbers.add("5");     
        numbers.add("6");     
        numbers.add("7");
        return numbers;
    }
	//@DiscardAfter
    public Object onSuccess() {
        System.out.println("aeroTicketingView.getDepartAirlineCode()=" + aeroTicketingView.getDepartAirlineCode());
        System.out.println("aeroTicketingView.getDepartureAirportCode()=" + aeroTicketingView.getDepartureAirportCode());
        System.out.println("aeroTicketingView.getDestinationAirportCode()=" + aeroTicketingView.getDestinationAirportCode());
        System.out.println("aeroTicketingView.getTotalAdult()=" + aeroTicketingView.getTotalAdult());
        System.out.println("aeroTicketingView.getTotalChildren()=" + aeroTicketingView.getTotalChildren());
        System.out.println("aeroTicketingView.getTotalInfant()=" + aeroTicketingView.getTotalInfant());
        aeroTicketingFlightList.setAeroTicketingView(aeroTicketingView);
        return aeroTicketingFlightList;
/*
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        Date plusOne = c.getTime();

        try {
            String path = getClass().getResource("/pdfs/SPlaneTicket.jasper").getPath();
            String dirPath = path.substring(0, path.length() - "SPlaneTicket.jasper".length());

            Map hmap = new HashMap<String, Object>();
            hmap.put("flightType", AirConstants.DEPARTURE);
            hmap.put("customerId", Long.valueOf("102114"));
            hmap.put("transactionType", AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE);
            hmap.put("transactionDate", dt);
            hmap.put("transactionDatePlusOne", plusOne);
            hmap.put("bookingCode", "RCTJRK");
            hmap.put("reportType", IReportData.REPORT_LOGO);
            hmap.put("SUBREPORT_DIR", dirPath);

            requestGlobals.getHTTPServletResponse().setContentType("application/pdf");
            requestGlobals.getHTTPServletResponse().addHeader(
                    "Content-Disposition",
                    "attachment; filename=PO.pdf");
            Class.forName("org.postgresql.Driver");

            DataSource ds = (DataSource) ServiceLocator.getService("dataSource");
            Connection conn = ds.getConnection();
            byte[] bytes = JasperRunManager.runReportToPdf(getClass().getResourceAsStream("/pdfs/SPlaneTicket.jasper"), hmap, conn);  //new JREmptyDataSource()
            requestGlobals.getHTTPServletResponse().setContentLength(bytes.length);

            try {
              JasperRunManager.runReportToPdfStream( getClass().getResourceAsStream("/pdfs/SPlaneTicket.jasper"),
                      requestGlobals.getHTTPServletResponse().getOutputStream(), hmap, conn);
            } catch (JRException jex) {

                System.out.println("jex=" + jex.toString() + " jex=" + jex.getMessage());
            } catch (Exception e) {
                e.printStackTrace(System.out);
                logger.error(e);
            } finally {
                if (conn!=null)
                {
                    conn.close();
                }
            }


        } catch (Exception ex) {
            System.out.println("ex=" + ex.getMessage());
        }
        return null;
        */
    }

    private void setPassengers() {
        List<AeroPassenger> passengers = new ArrayList<AeroPassenger>();
        for (int i = 0; i < aeroTicketingView.getTotalAdult(); i++) {
            AeroPassenger aeroPassenger = new AeroPassenger();
            aeroPassenger.setPassengerType(AeroPassenger.PASSENGER_TYPE.ADULT);
            aeroPassenger.setPassengerDob(new Date());
            aeroPassenger.setCountry(ListUtils.INDONESIA);
            passengers.add(aeroPassenger);
        }
        for (int i = 0; i < aeroTicketingView.getTotalChildren(); i++) {
            AeroPassenger aeroPassenger = new AeroPassenger();
            aeroPassenger.setPassengerType(AeroPassenger.PASSENGER_TYPE.CHILD);
            aeroPassenger.setPassengerDob(new Date());
            aeroPassenger.setCountry(ListUtils.INDONESIA);
            passengers.add(aeroPassenger);
        }
        for (int i = 0; i < aeroTicketingView.getTotalInfant(); i++) {
            AeroPassenger aeroPassenger = new AeroPassenger();
            aeroPassenger.setPassengerType(AeroPassenger.PASSENGER_TYPE.INFANT);
            aeroPassenger.setPassengerDob(new Date());
            aeroPassenger.setCountry(ListUtils.INDONESIA);
            aeroPassenger.setPassengerTitle("Inf");
            passengers.add(aeroPassenger);
        }
        aeroTicketingView.setPassengers(passengers);
    }

    public Map<String,String> getMyMap() {
		List<Airline> airlines = cacheManager.getAirlines();
		Map<String,String> maps = new HashMap<String, String>();
		for (int i = 0; i < airlines.size(); i++) {
			Airline airline = airlines.get(i);
			maps.put(airline.getAirlineId(), airline.getAirlineName());
		}
		return maps;
	}
	
	public String getMapValue() {
	    return this.getMyMap().get(this.currentKey);
	}
	
    void pageReset() {
//        waterPaymentView = null;

    }

    private void buildProviderModel() {
        providerModel = genericSelectModelFactory.create(
                cacheManager.getProviders(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH,
                        AirConstants.AIRLINE.DB_CODE.BILLER_PRODUCT, AirConstants.AIRLINE.DB_CODE.BILLER_PRODUCT));
        if (providerModel.getOptions().size() > 0) {
            aeroTicketingView.setProviderCode(providerModel.getOptions().get(0).getValue().toString());        }
    }

}
