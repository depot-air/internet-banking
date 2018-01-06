package com.dwidasa.admin.common;

import java.util.Calendar;

/**
 * A representation of day of week.
 *
 * @author rk
 */
public enum DayOfWeek {
    SUNDAY(Calendar.SUNDAY), MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY), WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY), FRIDAY(Calendar.FRIDAY), SATURDAY(Calendar.SATURDAY);

    Integer name;

    DayOfWeek(Integer name) {
        this.name = name;
    }

    public String toString() {
        return String.valueOf(name);
    }
}
