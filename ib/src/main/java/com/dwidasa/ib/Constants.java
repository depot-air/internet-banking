package com.dwidasa.ib;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/1/11
 * Time: 10:14 AM
 */
public final class Constants {
    private Constants() {
    }

    /**
     * Success indicator for enclosed operation. Used in web service.
     */
    public static final String OK = "1";

    /**
     * Success indicator of a transaction. Database dependent value.
     */
    public static final String SUCCESS_CODE = "00";

    /**
     * Merchant type for internet.
     */
//    public static final String MERCHANT_TYPE = "6014";
    /**
     * User Type
     */
    public static final String MERCHANT_EDC1 = "1571";
    public static final String MERCHANT_EDC2 = "1572";
    public static final String HIPERWALLET = "1575";
    
    /**
     * Default currency code.
     */
    public static final String CURRENCY_CODE = "360";
    public static final String CURRENCY_CODE_IDR = "IDR";

    /**
     * Transfer type
     */
    public static final int TRANSFER_NOW = 1;
    public static final int TRANSFER_POSTDATE = 2;
    public static final int TRANSFER_PERIODIC = 3;

    /**
     * Default page size for a grid
     */
    public static final int PAGE_SIZE = 5;

    /**
     * Token type
     */
    public static final int TOKEN_NOTHING = 0;
    public static final int TOKEN_RESPONSE_TOKEN = 1;
    public static final int TOKEN_CHALLENGE_TOKEN = 2;
    public static final int TOKEN_SOFTTOKEN = 3;
    public static final int TOKEN_SMS_TOKEN = 4;
    //public static final int TOKEN_CHALLENGE_TOKEN_INPUT = 5;

    /**
     * Selected language used for Locale setter.
     */
    public static final String SELECTED_LANGUAGE = "com.dwidasa.ib.SELECTED_LANGUAGE";

    /**
     * Separator for contributed encoders
     */
    public static final String SEPARATOR = "/";

    /**
     * Define default table rows.
     */
    public static final int DEFAULT_TABLE_ROWS = 10;

    /**
     * Predefined date format.
     */
    public static final String SHORT_FORMAT = "dd/MM/yyyy";
    public static final String Short_FormatDt = "yyyyMMdd";
    public static final String MEDIUM_FORMAT = "dd MMMM yyyy";
    public static final String MEDIUMTHREE_FORMAT = "dd MMM yy";
    public static final String LONG_FORMAT = "dd MMMM yyyy / HH:mm:ss";
    public static final String SHORT_DATE = "dd-MM-yyyy HH:mm ";
    public static final String RESI_DATETIME = "dd/MM/yyyy HH:mm ";
    public static class DATEFORMAT {
	    public static final String DDMMMYY = "ddMMMyy";
        public static final String DD_MMM_YYYY = "dd MMM yyyy";
	    public static final String DD_MM_YYYY = "dd-MM-yyyy";
	    public static final String DAY_HHMM = "EEE HH:mm";
	    public static final String DD_MM = "dd/MM";
	    public static final String COMPLETE_FORMAT = "EEEE, dd MMMM yyyy";
	    public static final String HH_MM = "HH:mm";
        public static final String YYYYMMDDHHmmss = "yyyyMMDDHHmmss";
    }
    
    /**
     * Transfer Type
     */
    public static class TRANSFER_TYPE {
	    public static final String OVERBOOKING = "overbooking";
	    public static final String TREASURY = "trasury";
	    public static final String ALTO = "alto";
	    public static final String ATMB = "atmb";
    }
    
    /**
     * Code for Delivery Channel
     */
    public static class CHANNEL {
    	public static final String MB = "MB";
    	public static final String IB = "IB";
    }
    
    public static final int RECORDS_PER_PAGE = 22;
    public static class GSP {
    	public static final String PROVIDER_CODE = "0001";	//SYB = 0002
    }
    public static final String EXCEPTION_OVERRIDE_MESSAGE = "Timeout";
    public static final String LASTPAGE_SESSION = "lastPage";
    public static final String AEROTICKET_VIEW_SESSION = "AeroticketingView Session";
    public static final String DEMO_INDEX = "demoIndex";
    public static final String DEMO_BIT48 = "demoBit48";
    public static final String DEMO_BIT120 = "demoBit120";
    public static final String DEMO_SSPP_UPDATE = "demoSsppUpdate";

    public static class SESSION {
    	public static final String SMS_TOKEN = "SMS_TOKEN";
    	public static final String TOKEN_TYPE = "TOKEN_TYPE";
    	public static final String SMS_TOKEN_SENT = "SMS_TOKEN_SENT";
    	public static final String CHALLENGE = "TOKEN_APPL1";
    }
    
    public static final String[] strDay = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
    public static final String[] strMonth = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

}
