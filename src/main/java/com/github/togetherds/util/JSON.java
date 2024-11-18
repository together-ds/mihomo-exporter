package com.github.togetherds.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.spi.CDI;

public class JSON {
    public static <T> T toBean(String string, JavaType valueType) {
        ObjectMapper om = CDI.current().select(ObjectMapper.class).get();
        try {
            return om.readValue(string, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T toBean(String string, TypeReference<T> typeReference) {
        ObjectMapper om = CDI.current().select(ObjectMapper.class).get();
        try {
            return om.readValue(string, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T toBean(String string, Class<T> clazz) {
        ObjectMapper om = CDI.current().select(ObjectMapper.class).get();
        try {
            return om.readValue(string, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
