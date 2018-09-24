package com.github.cloud0072.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JSONUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readValue(String string, Class<T> clazz) {
        try {
            return objectMapper.readValue(string, clazz);
        } catch (IOException e) {
            log.warn("read to Object error", e);
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * 对象转换成json
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String objectToJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to Json error", e);
            throw new UnsupportedOperationException(e);
        }
    }

}
