package org.github.jhy.chat.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author jihongyuan
 * @date 2023/2/8 11:22
 */
public class JSON {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T parseObject(String obj, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(obj, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String toString(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
