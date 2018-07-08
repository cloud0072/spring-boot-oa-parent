package com.caolei.system.utils;

/**
 * 对象增强
 */
public class ObjectUtils {

    public static <T> T nvl(T obj, T orNull) {
        return obj == null ? orNull : obj;
    }

}
