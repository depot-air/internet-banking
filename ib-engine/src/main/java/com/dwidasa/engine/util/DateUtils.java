package com.dwidasa.engine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static java.util.Calendar.MONTH;

/**
 * Convenience class to deal with date.
 *
 * @author rk
 */
public final class DateUtils {
    /**
     * Method to preserve date part only and removing time part.
     * @param input date to truncate
     * @return date without time part
     */
    public static Date truncate(Date input){
        Calendar cal = Calendar.getInstance();
        cal.setTime(input);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * This method will be used in a query where sql predicate used date range, specifically
     * generating lower bound of a range before bind it to query.
     * @param input date to generate
     * @return start date from specified input date
     */
    public static Date generateStart(Date input) {
        return truncate(input);
    }

    /**
     * This method will be used in a query where sql predicate used date range, specifically
     * generating upper bound of a range before bind it to query. Implementation of this method will add
     * one day to input date therefore, used &lt; operator inside the query.
     * @param input date to generate
     * @return end date from specified input date
     */
    public static Date generateEnd(Date input) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(input);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * N days backward from input date.
     * @param input input date
     * @param n how many days from input date backward
     * @return n day backward from input date
     */
    public static Date before(Date input, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(input);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DATE, -1*n);
        return cal.getTime();
    }

    /**
     * N days forward from input date.
     * @param input input date
     * @param n how many days from input date forward
     * @return n day forward from input date
     */
    public static Date after(Date input, int n) {
        return before(input, -1*n);
    }

    /**
     * Inclusive range check whether value specified between start and end date.
     * @param value date to check
     * @param start start date
     * @param end end date
     * @return true if in between
     */
    public static Boolean inRange(Date value, Date start, Date end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    /**
     * Generate current date with specified time. Time provided will comply to format parameter.
     * @param time time
     * @param format format used to parse time
     * @return current date with time set as specified
     */
    public static Date today(String time, String format) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd" + format);
        Date result = null;

        try {
            result = sdf2.parse(sdf1.format(new Date()) + time);
        } catch (ParseException e) {
        }

        return result;
    }


    /**
     * Format date for receipt
     *
     * @param dates
     * @return
     */
    public static String formatDateForReceipt(Date... dates) {

        StringBuffer ret = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");

        for (int i = 0; i < dates.length; i++){
            Date date = dates[i];
            if (i != 0 && date != null){
                ret.append(',');
            }

            if (date != null){
                ret.append(sdf.format(date));
            }

        }

        return  ret.toString();
    }
    
    public static String getYYYYMMDD(Date date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	return sdf.format(date);
    }

    public static String getYYYY_MM_DD(Date date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	return sdf.format(date);
    }

    public static String getDD_YYYY_MM(Date date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	return sdf.format(date);
    }
    
    public static Integer compareOnlyDate(Date dateOne, Date dateTwo) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Long firstDate = Long.parseLong(sdf.format(dateOne));
    	Long secondDate = Long.parseLong(sdf.format(dateTwo));
    	return firstDate.compareTo(secondDate);
    }
    
    //Tambahan
    public static String getyyyyMMMDDHHss(Date date){
    	 SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
    	 return sdf.format(date);
    }

    public static Date getDateMonthsBefore(int months) {
    	Calendar calendar = Calendar.getInstance();
        calendar.add( MONTH ,  months );
        return calendar.getTime();
        //return aDate.compareTo( calendar.getTime() ) < 0;
    }
    
    public static String getDDMMMYYYY(Date date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
    	return sdf.format(date);
    }
    
    public static String getHHMMSS(Date date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	return sdf.format(date);
    }
    
    public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	try {
    		Date threeMonthsBefore = getDateMonthsBefore(-3);
    		System.out.println(threeMonthsBefore);
    		Date twoYearsBefore = getDateMonthsBefore(-2 * 12);
    		System.out.println(twoYearsBefore);
//			Date yesterday = sdf.parse("20131218");
//			Date today = sdf.parse("20131219");
//	    	Date now = new Date();
//	    	System.out.println("yesterday vs now=" + yesterday.compareTo(now));
//	    	System.out.println("today vs now=" + today.compareTo(now));
//	    	System.out.println("today compareOnlyDtae now=" + compareOnlyDate(today, now));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
