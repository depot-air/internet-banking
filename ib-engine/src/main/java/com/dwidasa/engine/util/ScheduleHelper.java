package com.dwidasa.engine.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Helper class to generate date based on specified option.
 *
 * @author rk
 */
public class ScheduleHelper {
    private ScheduleHelper() {
    }

    /**
     * Generate next occurrence of periodic execution  
     * @param periodType (1:every x days, 2:once in a week, 3: once in a month)
     * @param periodValue
     * @param endDate (ending date boundary, inclusive)
     * @return
     */
    public static Date getNextDate(Integer periodType, Integer periodValue, Date endDate) {
    	Date result = null;
    	if (periodType.intValue() == 1) { //every x days
    		Date from = DateUtils.truncate(new Date());
            endDate = DateUtils.truncate(endDate);
            from = DateUtils.after(from, periodValue);
            if (from.compareTo(endDate) <= 0) {
                result = from;
            }
    	} else if (periodType.intValue() == 2) { //once in a week
    		Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, periodValue);
            Date from = DateUtils.truncate(cal.getTime());
            Date now = DateUtils.truncate(new Date());
            endDate = DateUtils.truncate(endDate);

            if (from.compareTo(now) <= 0) {
                cal.setTime(from);
                cal.add(Calendar.DATE, Calendar.DAY_OF_WEEK);
                from = cal.getTime();
            }

            if (from.compareTo(endDate) <= 0) {
                result = from;
            }
    	} else if (periodType.intValue() == 3) { //once in a month 
    		Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, periodValue);
            Date from = DateUtils.truncate(cal.getTime());
            Date now = DateUtils.truncate(new Date());
            endDate = DateUtils.truncate(endDate);
            
            if (from.compareTo(now) <= 0) {
                cal.setTime(from);
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, periodValue);
                from = cal.getTime();
            }
            
            cal.setTime(from);
            if (cal.get(Calendar.DAY_OF_MONTH) != periodValue.intValue()) {
            	cal.set(Calendar.DAY_OF_MONTH, periodValue);
            	from = cal.getTime();
            } 

            if (from.compareTo(endDate) <= 0) {
                result = from;
            }
    	}
    	
    	return result;
    }

    /**
     * Generate dates sequence such that the difference of any two successive members is periodValue,
     * with current date and endDate as the boundary. This boundary is inclusive.
     * @param periodValue difference in day
     * @param endDate ending date
     * @return list of date
     */
    public static List<Date> differenceOption(Integer periodValue, Date endDate) {
        List<Date> dates = new ArrayList<Date>();
        Date from = DateUtils.truncate(new Date());
        endDate = DateUtils.truncate(endDate);

        for (;;) {
            from = DateUtils.after(from, periodValue);
            if (from.compareTo(endDate) > 0) {
                break;
            }

            dates.add(from);
        }

        return dates;
    }

    /**
     * Generate dates sequence such that the resulted sequence has the same day as periodValue.
     * Day of week starting from sunday, as we refer the implementation of {@link Calendar}.
     * The boundary of generated sequence of date will be current date and endDate. This boundary is inclusive.
     * @param periodValue day of week
     * @param endDate ending date
     * @return list of date
     */
    public static List<Date> dayOption(Integer periodValue, Date endDate) {
        List<Date> dates = new ArrayList<Date>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, periodValue);
        Date from = DateUtils.truncate(cal.getTime());
        Date now = DateUtils.truncate(new Date());
        endDate = DateUtils.truncate(endDate);

        if (from.compareTo(now) < 0) {
            cal.setTime(from);
            cal.add(Calendar.DATE, Calendar.DAY_OF_WEEK);
            from = cal.getTime();
        }

        for (;;) {
            if (from.compareTo(endDate) > 0) {
                break;
            }

            dates.add(from);

            cal.setTime(from);
            cal.add(Calendar.DATE, Calendar.DAY_OF_WEEK);
            from = cal.getTime();
        }

        return dates;
    }


    /**
     * Generate dates sequence such that the resulted sequence has the same DAY_OF_MONTH as periodValue.
     * The boundary of generated sequence of date will be current date and endDate. This boundary is inclusive.
     * @param periodValue day of week
     * @param endDate ending date
     * @return list of date
     */
    public static List<Date> dateOption(Integer periodValue, Date endDate) {
        List<Date> dates = new ArrayList<Date>();

        Date from = DateUtils.truncate(new Date());
        Date now = from;
        endDate = DateUtils.truncate(endDate);

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        for (;;) {
            cal.set(Calendar.DATE, periodValue);
            cal.set(Calendar.MONTH, month++);
            cal.set(Calendar.YEAR, year);

            if (month == 13) {
                year++;
                month = 1;
            }

            from = cal.getTime();
            if (cal.get(Calendar.DATE) != periodValue || from.compareTo(now) < 0) {
                continue;
            }
            else if (from.compareTo(endDate) > 0) {
                break;
            }

            dates.add(from);
        }

        return dates;
    }
}
