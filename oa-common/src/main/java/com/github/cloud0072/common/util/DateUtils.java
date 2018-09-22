package com.github.cloud0072.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

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
@Slf4j
public class DateUtils {

    private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat datePathFormat = new SimpleDateFormat("yyyyMM/dd");
    private static final MultiValuedMap<DayOfWeek, String> weekMap = new ArrayListValuedHashMap<DayOfWeek, String>() {{
        putAll(DayOfWeek.MONDAY, Arrays.asList("一", "周一", "星期一"));
        putAll(DayOfWeek.TUESDAY, Arrays.asList("二", "周二", "星期二"));
        putAll(DayOfWeek.WEDNESDAY, Arrays.asList("三", "周三", "星期三"));
        putAll(DayOfWeek.THURSDAY, Arrays.asList("四", "周四", "星期四"));
        putAll(DayOfWeek.FRIDAY, Arrays.asList("五", "周五", "星期五"));
        putAll(DayOfWeek.SATURDAY, Arrays.asList("六", "周六", "星期六"));
        putAll(DayOfWeek.SUNDAY, Arrays.asList("日", "周日", "星期日"));
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
        log.info("parseDayOfWeek - > {}", chinese);
        //java自带的日期枚举类，同类的还有 Month 等
        return weekMap.entries().stream().filter(entry -> entry.getValue().contains(chinese)).findFirst()
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
