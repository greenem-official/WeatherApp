package com.mireaweb.app.util;

import java.sql.Date;
import java.util.Calendar;

/**
 * Useful stuff for working on Date object
 */

public class DateUtil {
    public static Date today() {
        return new java.sql.Date(new java.util.Date().getTime());
    }

    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return new Date(c.getTimeInMillis());
    }

    public static Date subtractDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -days);
        return new Date(c.getTimeInMillis());
    }

    public static Date addMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return new Date(c.getTimeInMillis());
    }

    public static Date subtractMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -months);
        return new Date(c.getTimeInMillis());
    }

    public static Date addYears(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return new Date(c.getTimeInMillis());
    }

    public static Date subtractYears(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -years);
        return new Date(c.getTimeInMillis());
    }

    public static void main(String[] args) {
        Date today = today();
        Date sevenDaysAgo = subtractDays(today, 7);
        System.out.println(sevenDaysAgo);
    }
}
