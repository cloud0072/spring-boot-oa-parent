package com.caolei.system.utils;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理时间的工具类
 *
 * @author cloud0072
 */
public class DateUtils {

    private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat datePathFormat = new SimpleDateFormat("yyyyMM" + File.separator + "dd");

    private DateUtils() {
    }

    public static String defaultDateFormat(Date date) {
        return defaultDateFormat.format(date);
    }

    public static SimpleDateFormat defaultDateFormat() {
        return defaultDateFormat;
    }

    public static String datePathFormat(Date date) {
        return datePathFormat.format(date);
    }

    public static SimpleDateFormat datePathFormat() {
        return datePathFormat;
    }

    public static Date of(java.sql.Date date) {
        return date;
    }

    public static Date of(Timestamp timestamp) {
        return timestamp;
    }

    public static long millisecond() {
        return System.currentTimeMillis();
    }

    public static long nanosecond() {
        return System.nanoTime();
    }

}
