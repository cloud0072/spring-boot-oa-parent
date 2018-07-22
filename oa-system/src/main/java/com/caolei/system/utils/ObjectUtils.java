package com.caolei.system.utils;

/**
 * 对象增强
 */
public class ObjectUtils {
    /**
     * 无法实例化，保证只能使用静态方法
     */
    private ObjectUtils(){

    }

    public static <T> T nvl(T obj, T orNull) {
        return obj == null ? orNull : obj;
    }

    public static boolean isNull(Object o) {
        return o == null || "".equals(o) || "null".equals(o);
    }


}
