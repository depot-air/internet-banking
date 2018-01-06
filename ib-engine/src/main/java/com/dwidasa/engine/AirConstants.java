package com.dwidasa.engine;

public final class AirConstants {
    private AirConstants() {
    }
    public static final String PIPE = "|";
    public static final class AEROTICKETING {
    	public static final class PROVIDER {
    		public static final String CODE = "AERO";
    		public static final String NAME = "AEROTICKETING";
    	}    	
    	public static final class TRANSACTION_TYPE {
    		public static final String POSTING_SEARCH = "9e";
    		public static final String POSTING_SEARCH_MULTI = "9f";
    		public static final String GET_ARRIVAL_AIRPORT = "9g";
    		public static final String GET_DEPARTURE_AIRPORT = "9h";
    		public static final String GET_AIRPORT = "9i";
    		public static final String GET_AIRLINES_CONNECT = "9j";
    		public static final String GET_AIRLINES = "9k";
    		public static final String SEARCH_DETAIL = "9l";
    		public static final String PRICE_DETAIL = "9m";
    		public static final String REBOOK_V2 = "9n";
    		public static final String SEARCH_BOOK_V2 = "9o";
    		public static final String ISSUE = "9p";
    	}

        public static final String CITY = "CITY";
        public static final String AIRPORT = "AIRPORT";
        public static final String AIRLINE = "AIRLINE";

    	public static final class AIRLINE_ID {
    		public static final String AIRASIA_ID = "1";
    		public static final String BATAVIA_ID = "2";
    		public static final String CITILINK_ID = "3";
    		public static final String GARUDA_ID = "4";
    		public static final String MERPATI_ID = "5";
    		public static final String SRIWIJAYA_ID = "6";
    		public static final String LIONAIR_ID = "7";
    		public static final String KALSTAR_ID = "8";
    		public static final String GARUDAEXEC_ID = "9";
    		public static final String TIGER_ID = "11";
    	}
    	public static final class AIRLINE_CONNECT {
    		public static final String OFF = "off";
    		public static final String ON = "on";
    	}
    	public static final class FLIGHT_TYPE {
    		public static final String DIRECT = "direct";
    		public static final String TRANSIT = "transit";
    		public static final String CONNECTIING = "connecting";
    	}
    	public static final class DEPART_RETURN {
        	public static final String DEPART_CODE = "1";	//also for oneway flight only
        	public static final String RETURN_CODE = "2";	//also for return flight
        	public static final String DEPART_DESC = "depart";	//also for oneway flight only
        	public static final String RETURN_DESC = "return";	//also for return flight
        }
    }
    public static final class AIRPORT {
    	public static final class CODE {
    		
    	}
    }
    public static final class AIRLINE {
    	public static final class FLIGHT_CODE {
    		public static final String LION_AIR = "JT";
    		public static final String AIRASIA_INDONESIA = "QZ";
			public static final String MV_AVIASTAR = "MV Aviastar";
			public static final String SRIWIJAYA_AIR = "SJ";
			public static final String CITILINK = "QG";
			public static final String GIA = "GA";
			public static final String KALSTAR = "KD";
			public static final String BATAVIA_AR = "Y6";
			public static final String MERPATI = "MZ";
			public static final String TRIGANA_AIR = "IL";
			public static final String SKY_AVIATION = "SY";
			public static final String MANDALA_AIRLINE = "RI";
    	}
    	public static final class DB_CODE {
    		public static final String BILLER = "AIR";
    		public static final String BILLER_PRODUCT = "PSWT";
    	}
    }

    public static final class VOLTRAS {
    	public static final class PROVIDER {
    		public static final String CODE = "VOLT";
    		public static final String NAME = "VOLTRAS";
    	}
    	public static final class TRANSACTION_TYPE {
    		public static final String CANCEL = "9q";
    		public static final String RETRIEVE = "9r";
    		public static final String TICKET = "9s";
    		public static final String OFFICE_INFORMATION = "9t";
    	}
    }
    
    public static final String DEPARTURE = "DEPARTURE";
    public static final String RETURN = "RETURN";
}
