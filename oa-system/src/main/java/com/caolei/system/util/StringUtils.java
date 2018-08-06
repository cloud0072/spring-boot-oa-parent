package com.caolei.system.util;


import com.caolei.system.web.BaseLogger;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * @author cloud0072
 * @date 2018/6/12 22:39
 */
public class StringUtils implements BaseLogger {

    private static Logger logger;

    private StringUtils() {
        logger = logger();
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
        return Character.isLowerCase(string.charAt(0)) ? string : (new StringBuilder()
                .append(Character.toLowerCase(string.charAt(0))).append(string.substring(1))).toString();
    }

    /**
     * 首字母转大写
     *
     * @param string
     * @return
     */
    public static String toUpperCaseFirstOne(String string) {
        return Character.isLowerCase(string.charAt(0)) ? string : (new StringBuilder()
                .append(Character.toUpperCase(string.charAt(0))).append(string.substring(1))).toString();
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
        String extendName = null;
        if (!StringUtils.isEmpty(fileName) && fileName.contains(".")) {
            extendName = fileName.substring(fileName.lastIndexOf("."));
        }
        return extendName;
    }
}
