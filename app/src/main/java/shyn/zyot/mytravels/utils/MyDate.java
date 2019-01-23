package shyn.zyot.mytravels.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyDate {
    private static final String DATE_PATTERN_DEFAULT = "yyyyMMddHHmmss";
    private static final String DATE_PATTERN_DATE = "yyyy-MM-dd";
    private static final String DATE_PATTERN_MIN = "yyyy-MM-dd HH:mm";
    private static final String DATE_PATTERN_TIME_MIN = "HH:mm";
    private static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_PATTERN_DEFAULT, Locale.getDefault());
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat(DATE_PATTERN_DATE, Locale.getDefault());
    private static final SimpleDateFormat SDF_MIN = new SimpleDateFormat(DATE_PATTERN_MIN, Locale.getDefault());
    private static final SimpleDateFormat SDF_TIME_MIN = new SimpleDateFormat(DATE_PATTERN_TIME_MIN, Locale.getDefault());

    static {
        SDF.setTimeZone(TimeZone.getDefault());
        SDF_DATE.setTimeZone(TimeZone.getDefault());
        SDF_MIN.setTimeZone(TimeZone.getDefault());
        SDF_TIME_MIN.setTimeZone(TimeZone.getDefault());
    }

    public static String getString(Date date) {
        return SDF.format(date);
    }

    public static String getString(long date) {
        return getString(new Date(date));
    }

    public static long getTime(String date) {
        try {
            return SDF.parse(date).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Formats a Date into a date string with the default format yyyy-MM-dd.
     *
     * @param date the date value to be formatted into a date string.
     * @return the formatted date string.
     */
    public static String getDateString(Date date) {
        return SDF_DATE.format(date);
    }

    /**
     * Formats a time value in milliseconds into a date string with the default format yyyy-MM-dd.
     *
     * @param date the milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return the formatted date string.
     */
    public static String getDateString(long date) {
        return getDateString(new Date(date));
    }

    public static String getDateString(String date) {
        try {
            return getDateString(SDF.parse(date));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Formats a Date into a datetime string with the default format yyyy-MM-dd HH:mm.
     *
     * @param date the date value to be formatted into a date string.
     * @return the formatted datetime string.
     */
    public static String getDateTimeMinString(Date date) {
        return SDF_MIN.format(date);
    }

    /**
     * Formats a time value in milliseconds into a date string with the default format yyyy-MM-dd HH:mm.
     *
     * @param date the milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return the formatted date string.
     */
    public static String getDateTimeMinString(long date) {
        return getDateTimeMinString(new Date(date));
    }

    public static String getDateTimeMinString(String date) {
        try {
            return getDateTimeMinString(SDF.parse(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getTimeMinString(Date date) {
        return SDF_TIME_MIN.format(date);
    }

    public static String getTimeMinString(long date) {
        return getTimeMinString(new Date(date));
    }

    public static String getTimeMinString(String date) {
        try {
            return getTimeMinString(SDF.parse(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static long getCurrentTime() {
        return getCurrentCalendar().getTimeInMillis();
    }

    public static String getCurrentDate() {
        return getDateString(getCurrentCalendar().getTimeInMillis());
    }
}
