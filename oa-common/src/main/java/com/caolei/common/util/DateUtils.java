package com.caolei.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 处理时间的工具类
 *
 * @author cloud0072
 */
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat datePathFormat = new SimpleDateFormat("yyyyMM" + File.separator + "dd");
    private static final Map<DayOfWeek, Set<String>> weekMap = new HashMap<DayOfWeek, Set<String>>() {{
        put(DayOfWeek.MONDAY, new HashSet<String>() {{
            addAll(Arrays.asList("一", "周一", "星期一"));
        }});
        put(DayOfWeek.TUESDAY, new HashSet<String>() {{
            addAll(Arrays.asList("二", "周二", "星期二"));
        }});
        put(DayOfWeek.WEDNESDAY, new HashSet<String>() {{
            addAll(Arrays.asList("三", "周三", "星期三"));
        }});
        put(DayOfWeek.THURSDAY, new HashSet<String>() {{
            addAll(Arrays.asList("四", "周四", "星期四"));
        }});
        put(DayOfWeek.FRIDAY, new HashSet<String>() {{
            addAll(Arrays.asList("五", "周五", "星期五"));
        }});
        put(DayOfWeek.SATURDAY, new HashSet<String>() {{
            addAll(Arrays.asList("六", "周六", "星期六"));
        }});
        put(DayOfWeek.SUNDAY, new HashSet<String>() {{
            addAll(Arrays.asList("日", "周日", "星期日"));
        }});
    }};

    private DateUtils() {
    }

    /**
     * 将中文转化为星期
     *
     * @param chinese
     * @return
     */
    public static DayOfWeek parseDayOfWeek(String chinese) {
        logger.info("parseDayOfWeek - > {}", chinese);
        //java自带的日期枚举类，同类的还有 Month 等
        return weekMap.entrySet().stream().filter(entry -> entry.getValue().contains(chinese)).findFirst()
                .orElseThrow(UnsupportedOperationException::new).getKey();
    }

    /**
     * 生成星期选择菜单
     *
     * @param style
     * @return
     */
    public static List<Map<String, String>> getDayOfWeekSelect(TextStyle style) {
        //style支持各种长短格式 后台接受select框的value值可以直接生成DayOfWeek枚举类
        return Stream.of(DayOfWeek.values()).map(week -> new HashMap<String, String>() {{
            put(week.name(), week.getDisplayName(style, Locale.CHINESE));
        }}).collect(Collectors.toList());
    }

    public static String parseToChinese(DayOfWeek dayOfWeek, TextStyle style) {
        return dayOfWeek.getDisplayName(style, Locale.CHINESE);
    }

    public static String parseToChinese(DayOfWeek dayOfWeek) {
        return parseToChinese(dayOfWeek, TextStyle.FULL);
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
