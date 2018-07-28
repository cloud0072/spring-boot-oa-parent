package com.caolei.system.utils;

import java.util.*;

/**
 * 对象增强
 */
public class EntityUtils {
    /**
     * 无法实例化，保证只能使用静态方法
     */
    private EntityUtils() {
    }

    /**
     *
     * @param obj
     * @param orNull
     * @param <T>
     * @return
     */
    public static <T> T orNull(T obj, T orNull) {
        return obj == null ? orNull : obj;
    }

    public static boolean isNull(Object o) {
        return o == null || "".equals(o) || "null".equals(o);
    }

}
