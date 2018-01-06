package com.dwidasa.admin;


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
     * Selected language used for Locale setter.
     */
    public static final String SELECTED_LANGUAGE = "com.dwidasa.admin.SELECTED_LANGUAGE";

    /**
     * Page size define how many rows will be displayed in a grid component.
     */
    public static final int PAGE_SIZE = 20;

    /**
     * Predefined date format.
     */
    public static final String SHORT_FORMAT = "dd-MM-yyyy";
    public static final String MEDIUM_FORMAT = "dd-MMM-yyyy";
    public static final String LONG_FORMAT = "dd-MMM-yyyy HH:mm:ss";
    public static final String YMD_FORMAT = "yyyyMMdd";
    
    
    public static final class Role {
    	public static final Long SUPERUSER = 1L;
    	public static final Long ADMIN = 2L;
		public static final Long TREASURY = 3L;
		public static final Long DAY_ADMIN = 4L;
		public static final Long NIGHT_ADMIN = 5L;
	}
    
    public static final class RoleName {
    	public static final String SUPERUSER = "Super User";		
    	public static final String ADMIN = "Admin";
		public static final String TREASURY = "Treasury";
		public static final String DAY_ADMIN = "Day Admin";
		public static final String NIGHT_ADMIN = "Night Admin";
	}
}
