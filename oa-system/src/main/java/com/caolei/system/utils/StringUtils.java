package com.caolei.system.utils;


/**
 * @author cloud0072
 * @date 2018/6/12 22:39
 */
public class StringUtils {

    public static boolean isEmpty(String input) {
        return input == null || "".equals(input);
    }

    public static boolean isNull(Object o) {
        return o == null || "".equals(o) || "null".equals(o);
    }

}
