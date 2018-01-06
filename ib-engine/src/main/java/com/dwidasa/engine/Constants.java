package com.dwidasa.engine;

import java.math.BigDecimal;




/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/1/11
 * Time: 6:18 PM
 */
public final class Constants {
    private Constants() {
    }

    /**
     * Number of columns for specified table, being used for chainRow in ChainedRowMapper class.
     */
    public static final int ACCOUNT_TYPE_LENGTH = 7;
    public static final int BALANCE_LENGTH = 8;
    public static final int BILLER_LENGTH = 9;
    public static final int BILLER_PRODUCT_LENGTH = 8;
    public static final int CELLULAR_PREFIX_LENGTH = 8;
    public static final int CURRENCY_LENGTH = 8;
    public static final int CUSTOMER_LENGTH = 18;
    public static final int CUSTOMER_ACCOUNT_LENGTH = 13;
    public static final int CUSTOMER_DATA_LENGTH = 12;
    public static final int CUSTOMER_DEVICE_LENGTH = 12;
    public static final int CUSTOMER_REGISTER_LENGTH = 13;
    public static final int CUSTOMER_SESSION_LENGTH = 9;
    public static final int EXCHANGE_RATE_LENGTH = 9;
    public static final int FTP_LENGTH = 13;
    public static final int INBOX_LENGTH = 9;
    public static final int INBOX_CUSTOMER_LENGTH = 8;
    public static final int ISO_BITMAP_LENGTH = 9;
    public static final int LOCATION_LENGTH = 14;
    public static final int MENU_LENGTH = 9;
    public static final int MERCHANT_LENGTH = 7;//
    public static final int PARAMETER_LENGTH = 8;
    public static final int PERIODIC_TASK_LENGTH = 12;
    public static final int PERIODIC_TASK_HISTORY_LENGTH = 12;
    public static final int PRODUCT_LENGTH = 7;
    public static final int PRODUCT_DENOMINATION_LENGTH = 9;	//8
    public static final int PROVIDER_LENGTH = 10;
    public static final int PROVIDER_DENOMINATION_LENGTH = 9;
    public static final int PROVIDER_PRODUCT_LENGTH = 9; //8
    public static final int RESPONSE_CODE_LENGTH = 8;
    public static final int ROLE_LENGTH = 7;
    public static final int ROLE_MENU_LENGTH = 7;
    public static final int TRANSACTION_LENGTH = 38;
    public static final int TRANSACTION_DATA_LENGTH = 8;
    public static final int TRANSACTION_HISTORY_LENGTH = 13;
    public static final int TRANSACTION_LIMIT_LENGTH = 9;
    public static final int TRANSACTION_STAGE_LENGTH = 7;
    public static final int TREASURY_STAGE_LENGTH = 10;
    public static final int TRANSACTION_TYPE_LENGTH = 8;
    public static final int TRANSFER_BATCH_LENGTH = 15;
    public static final int TRANSFER_QUEUE_LENGTH = 15;
    public static final int TRANSFER_RECEIVER_LENGTH = 11;
    public static final int USER_LENGTH = 14;
    public static final int KIOSK_PRINTER_LENGTH = 14;
    public static final int ACTIVITY_CUSTOMER_LENGTH = 11;
    

    public static final String CACHE_PARAMETER = "CACHE_PARAMETER";
    
    public static final String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyz123456789-";
    
    /**
     * Context variable to host map containing biller object by transaction type.
     */
    public static final String BILLER_BY_TRANSACTION_TYPE = "BILLER_BY_TRANSACTION_TYPE";
    
    public static final String BILLER_BY_TRANSACTION_TYPE_MERCHANT = "BILLER_BY_TRANSACTION_TYPE_MERCHANT";

    /**
     * Context variable to host map containing biller's product by transaction type
     * and biller code.
     */
    public static final String PRODUCT_BY_BILLER = "PRODUCT_BY_BILLER";

    /**
     * Context variable to host map containing product's denomination by transaction type,
     * biller code, and product code.
     */
    public static final String DENOMINATION_BY_PRODUCT = "DENOMINATION_BY_PRODUCT";

    /**
     * Context variable to host map containing provider denomination by transaction type,
     * biller code, product code, and denomination.
     */
    public static final String PROVIDER_BY_DENOMINATION = "PROVIDER_BY_DENOMINATION";
    public static final String SELECTED_CR_AIRPORT = "SELECTED_CR_AIRPORT";

    /**
     * Context variable to host map containing provider product by transaction type,
     * biller code, and product code.
     */
    public static final String PROVIDER_BY_PRODUCT = "PROVIDER_BY_PRODUCT";

    /**
     * Context variable to host map containing form config by form name.
     */
    public static final String FORM_CONFIG_BY_FORM = "FORM_CONFIG_BY_FORM";

    /**
     * Context variable to host map containing parameter config by parameter name.
     */
    public static final String PARAMETER_BY_NAME = "PARAMETER_BY_NAME";

    /**
     * Context variable to host map containing menu by its id.
     */
    public static final String MENU_MAP_BY_ID = "MENU_MAP_BY_ID";
    public static final String MENU_MAP_BY_ID2 = "MENU_MAP_BY_ID2";
    public static final String MENU_MAP_BY_ID_MERCHANT = "MENU_MAP_BY_ID_MERCHANT";
    public static final String MENU_MAP_BY_ID_PAC = "MENU_MAP_BY_ID_PAC"; 

    /**
     * Context variable to host map containing currency by its code.
     */
    public static final String CURRENCY_MAP_BY_CODE = "CURRENCY_MAP_BY_CODE";
    
    public static final String ACCOUNT_EXCEPTION_MAP_BY_ACCOUNT_NUMBER = "ACCOUNT_EXCEPTION_MAP_BY_ACCOUNT_NUMBER";

    public static final String LIST_SMS_TOKEN_PREFIX = "LIST_SMS_TOKEN_PREFIX";
    public static final String LIST_MOBILE_REGISTRATION_PREFIX = "LIST_MOBILE_REGISTRATION_PREFIX";

    public static final String FEATURE_CODE_MAP = "FEATURE_CODE_MAP";

    public static final String IGATE_BILLER_PRODUCT_MAP = "IGATE_BILLER_PRODUCT_MAP";

    public static final String CELLULAR_PREFIX_BY_ID = "CELLULAR_PREFIX_BY_ID";
    
    public static final String TRAIN_STATION = "TRAIN_STATION";

    /**
     * Active and inactive status used in status field.
     */
    public static final int ACTIVE_STATUS = 1;
    public static final int INACTIVE_STATUS = 0;

    /**
     * Transaction type code, database dependent constants.
     */
    public static final String PLN_PAYMENT_REP_CODE = "01";                 //-- PLN Postpaid
    public static final String TELCO_PAYMENT_REP_CODE = "02";               //-- HP Postpaid
    public static final String TRANSPORTATION_PAYMENT_REP_CODE = "06";      //-- Train
    public static final String ENTERTAINMENT_PAYMENT_REP_CODE = "07";       //-- TV
    public static final String INTERNET_PAYMENT_REP_CODE = "08";
    public static final String TELKOM_PAYMENT_REP_CODE= "09";
    public static final String PLN_PURCHASE_REP_CODE = "0a";                //-- PLN Prepaid
    public static final String PLN_PURCHASE_ADVICE_CODE = "0c";                //-- PLN Prepaid
    public static final String NONTAGLIST_PAYMENT_REP_CODE = "0b";
    public static final String LAST_10_TRANSACTION_CODE = "27";//"20";
    public static final String ATM_BLOCKING_INQ_CODE = "21";
    public static final String ACCOUNT_BALANCE_CODE = "26";
    public static final String MINI_STATMENT_CODE = "27";
    public static final String TRANSFER_INQ_CODE = "29";
    public static final String TRANSFER_TREASURY_INQ_CODE = "2b";
    
    public static final String INQUIRY_LOTTERY = "9x";					//Undian
    public static final String INQUIRY_NOMOR_UNDIAN = "9y";
    
    public static final String DOMPETKU_CASH_IN = "9z";					//Dompetku
    public static final String DOMPETKU_CASH_OUT = "9u";
    public static final String DOMPETKU_TRANSFER_TOKEN = "9t";
    
    public static final String MULTI_REKENING = "zz";
    
    public static final String KAI_GET_SCHEDULE_ONLY = "K0";  //KAI
    public static final String KAI_GET_AVB_ONLY = "K1";  //KAI
    public static final String KAI_GET_FARE = "K2";
    public static final String KAI_BOOKING_WITH_ARV_INFO = "K3";
    public static final String KAI_CANCEL_BOOK = "K4";
    public static final String KAI_PAYMENT = "K5";
    public static final String KAI_GET_SEAT_NULL_PER_SUBCLASS = "K6";
    public static final String KAI_MANUAL_SEAT = "K7";
    
    public static final String STATUS_CASH_IN = "CashIn";
    public static final String STATUS_CASH_OUT = "CashOut";
    public static final String STATUS_CASH_TRANSFER_TOKEN = "TransferToken";
    
    public static final String PLN_PAYMENT_INQ_CODE = "31";
    public static final String PLN_PURCHASE_INQ_CODE = "3a";                //-- PLN Prepaid
    public static final String NONTAGLIST_PAYMENT_INQ_CODE = "3b";
    public static final String TELCO_PAYMENT_INQ_CODE = "32";
    public static final String MULTIFINANCE_PAYMENT_INQ_CODE = "33";
    public static final String TRANSPORTATION_PAYMENT_INQ_CODE = "36";      //-- Train
    public static final String ENTERTAINMENT_PAYMENT_INQ_CODE = "37";       //-- TV
    public static final String INTERNET_PAYMENT_INQ_CODE = "38";
    public static final String TELKOM_PAYMENT_INQ_CODE = "39";              //-- Telkom
    public static final String VOUCHER_PURCHASE_CHK_CODE = "42";            //-- TOP UP
    public static final String REGISTRATION_CODE = "54";    
    public static final String ATM_BLOCKING_CODE = "55";
    public static final String CASHIN_DELIMA_INQ_CODE = "61";
    public static final String CASHIN_DELIMA_CODE = "62";
    public static final String CASHOUT_DELIMA_INQ_CODE = "63";
    public static final String CASHOUT_DELIMA_CODE = "64";
    public static final String CASHOUT_DELIMA_SMS_VERIFICATION = "14";	//60
    public static final String REFUND_DELIMA_INQ_CODE = "65";
    public static final String REFUND_DELIMA_CODE = "66";
    public static final String CASHIN_DELIMA_CHK_CODE = "45";   //67
    public static final String CASHOUT_DELIMA_CHK_CODE = "68";
    public static final String REFUND_DELIMA_CHK_CODE = "69";
    public static final String CASHIN_DELIMA_REP_CODE = "05";   //6a
    public static final String CASHOUT_DELIMA_REP_CODE = "6b";
    public static final String REFUND_DELIMA_REP_CODE = "6c";
    public static final String CASHTOBANK_INQ_CODE = "6d";
    public static final String CASHTOBANK_POS_CODE = "6e";
    public static final String CASHTOBANK_REP_CODE = "6f";
    public static final String CASHTOBANK_CHK_CODE = "6g";
    public static final String CASHOUT_ELMO_INQ_CODE = "6h";
    public static final String CASHOUT_ELMO_POS_CODE = "6i";
    public static final String CASHOUT_ELMO_REP_CODE = "6j";
    public static final String CASHOUT_ELMO_CHK_CODE = "6k";
    
    public static final String TRANSFER_SAME_CODE = "71";                   //-- Same card transfer
    public static final String TRANSFER_CODE = "72";
    public static final String TRANSFER_BATCH = "TB";
    public static final String TRANSFER_TREASURY_CODE = "73";
    public static final String TRANSFER_TARIK_TUNAI = "74";
    public static final String TRANSFER_SETOR_TUNAI = "75";
//    public static final String TRANSFER_ALTO = "77";
    public static final String LOGIN_TYPE = "1a";
    public static final String LOGOUT_TYPE = "1b";
    public static final String SSPP_TEMENOS = "2c";
    public static final String SSPP_TEMENOS_UPDATE = "2d";
    
    //
    public static final String Tambah_Daftar = "TD";
    public static final String TAMBAH_DAFTAR_PROVIDER = "ATMB"; //ATMB / ALTO

    public static final class SMS_TOKEN {
    	public static final String TRANS_TYPE = "1c";
    }
    public static final class ATMB {
	    public static final String TT_INQUIRY = "40";
	    public static final String TT_POSTING = "41";
	    public static final String TT_CEK_STATUS = "67";
        public static final String PROVIDER_CODE = "ATMB";
        public static final String NON_CARD_52 = "0000000000000000";
        public static final String TERMINAL_ID = "77700001";
    }
    public static final class ALTO {
	    public static final String TT_INQUIRY = "4a";
	    public static final String TT_POSTING = "4b";
        public static final String PROVIDER_CODE = "ALTO";
        public static final String NON_CARD_52 = "0000000000000000";
        public static final String TERMINAL_ID = "77700001";
    }
    public static final class TRANSFER_AMOUNT {
    	public static final BigDecimal MIN_PER_TRX = new BigDecimal("10000");
    	public static final BigDecimal MAX_PER_TRX = new BigDecimal("10000000");
    	public static final BigDecimal MAX_IN_ONE_DAY = new BigDecimal("25000000");
    }
    
    public static final class TRANSFER_VIRTUAL_ACCOUNT{
    	public static final String VIRTUAL_ACCOUNT_INQ_CODE = "2v";
    	public static final String VIRTUAL_ACCOUNT_POS_CODE = "VP";
    }
    
    public static final class WATER {
        public static final class TRANSACTION_TYPE {
            public static final String INQUIRY = "30";
            public static final String POSTING = "80";
            public static final String REPRINT = "00";
        }
        public static final String BILLER_ID = "001";
        public static final String PROJECT_ID = "029";
        public static final class TRANSLATION_CODE {
            public static final String INQUIRY = "01";
            public static final String POSTING = "02";
            public static final String REPRINT = "03";
        }
        public static final String FROM_ACCOUNT = "10";
        public static final class TO_ACCOUNT {
            public static final String INQUIRY = "00";
            public static final String POSTING = "00";
            public static final String REPRINT = "99";
        }
        public static final class BILLER_CODE {
        	public static final String PALYJA = "PLJ";
            public static final String AETRA = "ATR";
            public static final String PAMSurabaya = "ASBY";
            public static final String PAMSemarang = "ASMR";
            public static final String PAM_BILLER = "00001";
            public static final String PAM_BILLER_BANDUNG = "ABND";
            public static final String PAMBandung = "ABND";
        }
        public static final class BILLER_NAME {
        	public static final String PALYJA = "PALYJA";
            public static final String AETRA = "AETRA";
            public static final String PAMSurabaya = "PDAM Surabaya";
            public static final String PAMSemarang = "PDAM Kota Semarang";
            public static final String PAMBandung = "PDAM Kab Bandung";
        }
        public static final class PRODUCT_CODE{
        	public static final String PAM_SEMARANG = "0004";
        	public static final String PAM_SURABAYA = "0003";
        	public static final String PAM_BANDUNG = "0005";
        }
    }
    public static final class SMS_REGISTRATION {
        public static final String TRANS_TYPE = "52";
        public static final String BILLER_ID = "10001";
        public static final String PAYEE_ID = "0000";
        public static final String PRODUCT_ID = "0012";
    }
    
    public static final class AUTODEBET_PLN_REGISTRATION{
    	public static final String TRANS_TYPE_AUTODEBET = "44";
    }
    
    public static final class AUTODEBET_TELKOM_REGISTRATION{
    	public static final String TRANS_TYPE_AUTODEBET = "4t";
    }
    
    public static final class AUTODEBET_PLN_UNREGISTRATION{
    	public static final String TRANS_TYPE_AUTODEBET = "up";
    }
    
    public static final class AUTODEBET_TELKOM_UNREGISTRATION{
    	public static final String TRANS_TYPE_AUTODEBET = "ut";
    }
    
    public static final class ESTATEMENT{
    	public static final String TRANS_TYPE_REGISTRATION = "RE";
    }
    
    public static final class VOUCHER_GAME {
        public static final String TRANS_TYPE_INQUIRY = "3c";
        public static final String TRANS_TYPE_POSTING = "9b";
        public static final String TRANS_TYPE_CHECKSTATUS = "4a";
        public static final String ROUTING_CODE = "032";
        public static final String BILLER_CODE = "009";   //PAC
        public static final String PAYEE_ID = "0026";   //0026 PAC
        public static final String PRODUCT_ID = "0001";
    }
    public static final String PLN_PAYMENT_CODE = "81";
    public static final String TELCO_PAYMENT_CODE = "82";                   //-- HP Postpaid
    public static final String MULTIFINANCE_PAYMENT_CODE = "83";
    public static final String INSURANCE_PAYMENT_CODE = "84";
    public static final String EDUCATION_PAYMENT_CODE = "85";
    public static final String TRANSPORTATION_PAYMENT_CODE = "86";          //-- Train
    public static final String ENTERTAINMENT_PAYMENT_CODE = "87";           //-- TV
    public static final String INTERNET_PAYMENT_CODE = "88";
    public static final String TELKOM_PAYMENT_CODE = "89";                  //-- Telkom
    public static final String NONTAGLIST_PAYMENT_CODE = "8b";
    public static final String PLN_PURCHASE_CODE = "9a";                    //-- PLN Prepaid
    public static final String VOUCHER_PURCHASE_CODE = "92";                //-- TOP UP
    public static final String VOUCHER_GAME_PURCHASE_CODE = "9b";                //-- Voucher Game
    
    
    
    //MNCLIVE
    public static final class MNCLIFE {
    	public static final String PROVIDER_CODE = "MNC";
        public static final String Mnc_Live_Posting_Pembelian = "84";
        public static final String Mnc_Live_Cek_Status = "94";
        public static final String Mnc_Live_Reprint = "04";
        public static final String Mnc_Live_Provider = "PACT";
        
    }
    
 
    
    
    //-- Obsolate transaction type code, we do not provide these features
    public static final String CC_PAYMENT_CODE = "00";
    public static final String CC_PAYMENT_INQ_CODE = "00";
    public static final String PLANE_PAYMENT_CODE = "00";
    public static final String PLANE_PAYMENT_INQ_CODE = "00";
    
    public static final String mncLiveMBillerId = "182";

    /**
     * Default value for max distance in meter.
     */
    public static final double MAX_DISTANCE = 2000;

    /**
     * Default value for max size, number or rows being returned.
     */
    public static final int MAX_SIZE = 10;

    /**
     * Device type.
     */
    public static final String BLACKBERRY = "10";
    public static final String ANDROID = "11";
    public static final String IPHONE = "12";
    public static final String BB10 = "16";

    /**
     * Success response code.
     */
    public static final String SUCCESS_CODE = "00";
    /**
     * Timeout response code.
     */
    public static final String TIMEOUT_CODE = "50";

    /**
     * Location type id, database dependent field.
     */
    public static final Long LOCATION_TYPE_BRANCH = 1L;
    public static final Long LOCATION_TYPE_ATM_ADM = 2L;
    public static final Long LOCATION_TYPE_MERCHANT = 3L;
    public static final Long LOCATION_TYPE_KIOSK = 4L;

    /**
     * Possible execution type.
     */
    public static final String NOW_ET = "NOW";
    public static final String POSTDATED_ET = "POST";
    public static final String PERIODIC_ET = "PERIOD";

    /**
     * Possible status of a transaction.
     */
    public static final String SUCCEED_STATUS = "SUCCEED";
    public static final String PENDING_STATUS = "PENDING";
    public static final String FAILED_STATUS = "FAILED";
    public static final String CANCELED_STATUS = "CANCELED";
    public static final String QUEUED_STATUS = "QUEUED";
    public static final String EXECUTED_STATUS = "EXECUTED";
    public static final String PARTIAL_STATUS = "PARTIAL"; //only transfer batch


    /**
     * Transaction type financial/non financial
     */
    public static final class TRANSACTION_TYPE {

        public static final Integer NON_FINANCIAL = 0;
        public static final Integer FINANCIAL = 1;

    }

    /**
     * Addition status for transaction_stage table
     */
    public static final String DELIVERED_STATUS = "DELIVERED";
    public static final String HANDLED_STATUS = "HANDLED";

    /**
     * Provider code, database dependent.
     */
    public static final String INDOSMART_CODE = "0010";
    public static final String MKN_CODE = "0012";
    public static final String AKSES_NUSANTARA_CODE = "0017";
    public static final String AKSES_CIPTA_CODE = "0018";

    /**
     * Account type code taken from m_account_type, database dependent.
     */
    public static final String TABUNGAN_CODE = "10";
    public static final String GIRO_CODE = "20";

    /**
     * Provide code with special treatment, database dependent.
     */
    public static final String PROVIDER_FINNET_CODE = "A004";
    public static final String TREASURY_PROVIDER_CODE = "A005";
    public static final String PROVIDER_ARTAJASA_CODE = "ARJA";
    public static final String TELKOM_VISION = "A08";
    public static final String CENTRIN_TV = "001";
    public static final String FLEXI_POSTPAID = "016";
    public static final String AORATV = "A21";

    public static final String TRANSLATION_TELKOM_REPRINT = "0110301001001";    //Telkom,Telkomvision,Flexi Postpaid, inet speedy reprint
    public static final String ACC_10_TRANSAKSI_AKHIR = "02604";
    public static final String TRANSLATION_LOTTERY = "02603";
    public static final String TRANSLATION_MUTASIREK = "01611";

    public static class TV_CODE {
	    public static final String INDOVISION = "A09";
	    public static final String TOPTV = "A10";
	    public static final String OKEVISION = "A11";
    }

    public static class TRANSLATION_CODE {
	    public static final String TV_INQUIRY = "01114000801001008";
	    //indovision, okevisio, toptv
	    public static final String TV_INQUIRY_INDOVISION_OKE_TOPTV = "01114000801090001";
	    public static final String TV_POSTING = "01108000801001008";
	    public static final String TV_POSTING_INDOVISION_OKE_TOPTV = "01108000801090001";
	    public static final String TV_REPRINT = "01109000801001008";
	    public static final String TV_REPRINT_INDOVISION_OKE_TOPTV = "01109000801090001";
    }
    
    /**
     * Task period type
     */
    public static class TASK_PERIOD_TYPE {
	    public static final String BOD = "BOD";
	    public static final String EOD = "EOD";
	    public static final String BOM = "BOM";
	    public static final String EOM = "EOM";
    }
    
    /**
     * Possible status of periodic process.
     */
    public static final class PERIODIC_STATUS {
	    public static final String PROGRESS = "P";
	    public static final String SUCCESS = "S";
	    public static final String WAIT = "W";
	    public static final String FAIL = "F";
    }

    /**
     * 8 byte server encrypted key to encrypt sensitive data in database
     * This key should not be stored,
     * hence this code should be replaced with user input during server activation
     */
    public static final String SERVER_SECRET_KEY = "0102030405060708";
    public static final String SERVER_SECRET_KEY_CARD_NUMBER = "1234567890";
//        public static final String SERVER_SECRET_KEY = "0404040404040404";

    //Registrasi Mobile dari EDC punya key sendiri, sebelumnya didecrypt di MX, IGate tidak provide
    public static final String EDC_SERVER_SECRET_KEY = "0202020202020202";
    
    public static class BPRKS {
	    public static final String CODE = "688";
	    public static final String NAME = "BPR KS";
    }

//    public static String ALTO_PROVIDER_CODE = "ALTO";

    public static class INDUSTRY_CODE {
        public static String UTILITY_WATER = "00";
        public static String UTILITY_ELECTRIC = "01";
        public static String TELCO_PREPAID_POSTPAID = "02";
        public static String MULTI_FINANCE = "03 Multi Finance";
        public static String INSURANCE = "04";
        public static String EDUCATION = "05";
        public static String TRANSPORTATION = "06";
        public static String ENTERTAINMENT = "07";
        public static String CC_PERSONALLOAN_DISTRIBUTION = "08";
        public static String TAX = "09";
        public static String ON_US_BANKING = "10";
        public static String CASH_REMITTANCE = "11";
        public static String TRANSFER = "13";
    }
    
    public static class TIKET_KERETA_DJATI{
    	public static String INQ_KERETA_DJATI = "9v";
    	public static String POS_KERETA_DJATI = "9W";
    	public static String CEK_KERETA_DJATI = "9X";
    	public static String INQ_KOTA_ASAL_KRAMAT_DJATI = "8v";
    	public static String INQ_KOTA_TUJUAN_KRAMAT_DJATI = "8w";
    }
    
    public static class PAYMENT_KARTU_KREDIT_BNI{
    	public static String PAYMENT_BNI = "9B";
    }
    
    public static class COLUMBIA{
    	public static String INQ_COLUMBIA = "CI";
    	public static String PAYMENT_COLUMBIA = "CP";
    }
    
    public static class TIKETUX{
    	public static String INQ_TIKETUX = "8x";
    	public static String POS_TIKETUX = "8Y";
    	public static String INQ_KOTAKEBERANGKATAN = "8y";
    	public static String INQ_KURSI = "8k";
    	public static String INQ_KOTATUJUAN = "8z";
    	public static String JENIS_LAYANAN = "TIKETUX";
    }
    
    public static class MULTIFINANCE_NEW{
    	public static String INQ_MULTI_FINANCE = "az";
    	public static String PAYMENT_MULTI_FINANCE = "Ab";
    	public static String INQ_MULTI_FINANCE_MNC = "aa";
    	public static String PAYMENT_MULTI_FINANCE_MNC = "Ac";
    }

    public static class PAYEE_ID {  //sama dengan provider code
        public static String FINNET = "A004";
        public static String INDOSMART = "0010";
        public static String MKN = "0012";
        public static String AKSES_NUSANTARA = "0017";
        public static String AKSES_CIPTA_SOLUSI = "0018";
        public static String SERA = "0020";
        public static String SERA_FOR_ALL = "0000";
        public static String AKSES_INDO_SEMESTA = "0023";
        public static String AKSES_GLOBALINDO = "0024";
    }

//    public static String SERA_CODE = "0020";
    public static class PREPAID_PRODCODE {
        public static String SIMPATI_PRODCODE = "004";
        public static String KARTUAS_PRODCODE = "005";
        public static String XL_V_REGULAR_PRODCODE = "007";
        public static String XL_V_XTRA_PRODCODE = "008";
        public static String FINNET_TELKOMSEL = "FTLK";
    }
    public static class BILLER_CODE {
        public static String ESIA_AHA = "ESAPR";
        public static String AXIS_PRABAYAR = "AXSPR";
        public static String THREE_PRABAYAR = "THRPR";
        public static String SMARTFREN_PREPAID = "036";
    }
    public static class POSTPAID_PRODCODE {
        public static String TELKOMSEL_KATU_HALO = "003";
        public static String XL_PASCABAYAR = "006";
        public static String MOBI8_FREN = "010";
        public static String ESIA_PASCA = "014";
        public static String FLEXI_PASCA = "016";
        public static String INDOSAT_MATRIX = "018";
        public static String INDOSAT_STARONE_PASCA = "025";
        public static String AXIS_PASCA = "027";
        public static String THREE_PASCA = "029";
        public static String SMARTFREN_PASCA = "035";
    }

    public static class DENOMINATION {
	    public static String VOUCHER_5K = "5000";
        public static String VOUCHER_10K = "10000";
        public static String VOUCHER_20K = "20000";
        public static String VOUCHER_25K = "25000";
        public static String VOUCHER_50K = "50000";
        public static String VOUCHER_100K = "100000";
        public static String VOUCHER_150K = "150000";
        public static String VOUCHER_200K = "200000";
    }

    public static class TRANSLATION_PROCESS_CODE {
        public static class SERA {
            public static String PREPAID_POSTING = "01";
            public static String PREPAID_CEK_STATUS = "02";
            public static String POSTPAID_INQUIRY = "03";
            public static String POSTPAID_POSTING = "04";
            public static String POSTPAID_REPRINT = "05";
        }
    }
    public static class PO_SERVICE_ENTRY_MODE { //bit 22
        public static class PAN_EXPIRATION_DATE {
            public static String UNKNOWN_OR_NOT_APPLICABLE = "00";
            public static String MANUAL_OR_KEY_ENTRY = "01";
            public static String MAGNETIC_STRIPE = "02";
            public static String BAR_CODE = "03";
            public static String OCR = "04";
            public static String INTEGRATED_CIRCUIT_CARD_CCV = "05";
            public static String MAGNETIC_STRIPE_READ = "90";
            public static String PROXIMITY_TRANSACTION_ORIGINATED = "91";
            public static String INTEGRATED_CIRCUIT_CARD_READ = "95";
        }
        public static class PIN_ENTRY_CAPABILITY {
            public static String UNKNOWN = "0";
            public static String PIN_ENTRY_CAPABILITY = "1";
            public static String NO_PIN_ENTRY_CPABILITY = "2";
        }
    }

//    public static class SERVER_TYPE {
//        public static String MOBILE = "1";
//        public static String KIOSK = "2";
//        public static String IB = "3";
//    }

    public static class MPARAMETER_NAME {
        public static String SSPP_ITM_IP = "SSPP_ITM_IP";
        public static String SSPP_ITM_PORT = "SSPP_ITM_PORT";
        public static String PREFIX_MERCHANT_IB = "PREFIX_MERCHANT_IB";
        public static String MINIMAL_VOUCHER_INDIVIDUAL = "MINIMAL_VOUCHER_INDIVIDUAL";
        public static String SMS_TOKEN_PREFIX = "SMS_TOKEN_PREFIX";
        public static String TREASURY_TEMP_ACCOUNT = "TREASURY_TEMP_ACCOUNT";
        public static String TOKEN_AGENT_IP = "TOKEN_AGENT_IP";
        public static String TOKEN_AGENT_PORT = "TOKEN_AGENT_PORT";
        public static String TOKEN_TIMEOUT = "TOKEN_TIMEOUT";
        public static String TOKEN_AGENT_ID = "TOKEN_AGENT_ID";
        public static String TOKEN_AGENT_KEY = "TOKEN_AGENT_KEY";
        
        public static String TOKEN_ADMIN_ID = "TOKEN_ADMIN_ID";
        public static String TOKEN_ADMIN_KEY = "TOKEN_ADMIN_KEY";
        public static String TOKEN_ADMIN_PORT = "TOKEN_ADMIN_PORT";
        
        public static String SERVER_IB_URL = "SERVER_IB_URL";
        public static String KOMISI_TIKET_PESAWAT = "KOMISI_TIKET_PESAWAT";
        public static String TICKET_FOLDER = "TICKET_FOLDER";
    }
    
    public static String getTerminalIdAtmb(String terminalId, String accountNo) {
        if (terminalId != null) {
            if (terminalId.length() > 3 && terminalId.substring(0, 3).equals("KSK")) {
                return "999" + terminalId.substring(3);
            } else if (terminalId.equals("IBS")) { // IBS
                return ATMB.TERMINAL_ID;    // "777" + "00001";
            } else if (terminalId.equals("MBS")) { // MBS
                return "888" + "00001";
            } else {    //ini untuk mobile sbg merchant
                return accountNo.substring(accountNo.length() - 8);
            }

        }
        return terminalId;
    }
    
//    public static final String TERMINAL_ID_DEFAULT = "MBS";
    public static final String RESI_DATETIME = "dd/MM/yyyy HH:mm ";
    
    public static class SSPP {
    	public static String RC01 = "Bukan Rekening Tabungan";
    	public static String RC02 = "Rekening Tidak Terdaftar";
    	public static String RC03 = "Currency Rekening Tidak Terdaftar";
    	public static String RC04 = "Kode Format Printer tidak terdaftar (Internal Code)";
    	public static String RC05 = "Record gagal diupdate";
    	public static String RC20 = "Message Length Not Valid";
    	public static String RC21 = "Message ID not valid for this Transaction";
    	public static String RCS1 = "Silakan ke Kantor Cabang Untuk Mendapatkan Buku yang Baru";
    }
    
    /**
     * Transfer type
     */

    public static final String CURRENCY_CODE = "360";
    public static final String CURRENCY_CODE_IDR = "IDR";

    public static class PORTFOLIO {
    	public static String TRANS_TYPE = "2a";
    	public static String CASA = "CASA";
    	public static String DEPOSITO = "DEPOSITO";
    	public static String LOAN = "LOAN";
    }

    public static final String IBS_SERVER = "IBS";

    public static class STATUS {
    	public static String YES = "Y";
    	public static String NO = "N";
    	public static Integer ONE = 1;
    	public static Integer ZERO = 0;
    }
    public static class HARD_TOKEN {
    	public static String STATUS_AVAILABLE = "AVA";
    	public static String STATUS_SELECTED = "SEL";
    	public static String STATUS_LINKED = "LIN";   
    	public static String STATUS_ACTIVE = "ACT"; 
    	public static String STATUS_TERMINATED = "TRM";
    	public static String STATUS_IMPORTED = "IMP";
    	public static String STATUS_LOST = "LOS";   
    }
    public static class MERCHANT_TYPE {
    	public static String MOBILE = "6014";
    	public static String KIOSK = "6015";
    	public static String IB = "6017";    	
    }
    public static class TERMINALID_DEFAULT {
    	public static String MOBILE = "MBS";
    	public static String IB = "IBS";    	
    }
    public static class GSP {
    	public static final String PROVIDER_CODE = "0001";
    }
    public static class SYB {
    	public static final String PROVIDER_CODE = "0002";
    }
    public static class CUSTOMER{
    	public static String TOKEN_ACTIVATED = "Y";
    	public static String FISRT_LOGIN = "Y";
    }

    public static class CUSTOMER_TYPE {
        public static final Integer INDIVIDUAL = 0;
        public static final Integer MERCHANT = 1;
    }

    public static class DATE_FORMAT {
    	public static final String ddMMMyyyy = "dd MMM yyyy";
    	public static final String COMPLETE_FORMAT = "EEE, dd MMM yyyy";
    	public static final String HHMM = "hh:mm";
    }
}