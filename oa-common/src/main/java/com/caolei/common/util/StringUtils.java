package com.caolei.common.util;


import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author cloud0072
 * @date 2018/6/12 22:39
 */
@Slf4j
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

    /**
     * 获取扩展名
     *
     * @param fileName
     * @return
     */
    public static String extendName(String fileName) {
        String extendName = null;
        if (!StringUtils.isEmpty(fileName) && fileName.contains(".")) {
            extendName = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extendName;
    }

    /**
     * 验证是否是32位的uuid格式
     * @param lastParam
     * @return
     */
    public static boolean isUUID32(String lastParam) {
        if (!StringUtils.isEmpty(lastParam)) {
            return lastParam.matches("^[0-9a-fA-F]{32}$");
        }

        return false;
    }
}
