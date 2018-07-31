package com.caolei.system.utils;


import java.util.UUID;

/**
 * @author cloud0072
 * @date 2018/6/12 22:39
 */
public class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String input) {
        return input == null || "".equals(input);
    }

    /**
     * 首字母转小写
     *
     * @param string
     * @return
     */
    public static String toLowerCaseFirstOne(String string) {
        if (Character.isLowerCase(string.charAt(0)))
            return string;
        else
            return (new StringBuilder().append(Character.toLowerCase(string.charAt(0))).append(string.substring(1))).toString();
    }

    /**
     * 首字母转大写
     *
     * @param string
     * @return
     */
    public static String toUpperCaseFirstOne(String string) {
        if (Character.isUpperCase(string.charAt(0)))
            return string;
        else
            return (new StringBuilder().append(Character.toUpperCase(string.charAt(0))).append(string.substring(1))).toString();
    }

    /**
     * 生成32位UUID
     *
     * @return
     */
    public static String UUID32() {
        return UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
    }

    public static String extendName(String fileName) {
        String exendName = null;
        if (!StringUtils.isEmpty(fileName) && fileName.contains(".")) {
            exendName = fileName.substring(fileName.lastIndexOf("."));
        }
        return exendName;
    }
}
