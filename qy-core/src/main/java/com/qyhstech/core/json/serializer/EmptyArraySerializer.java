package com.qyhstech.core.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 自定义序列化，空数组转换
 */
public class EmptyArraySerializer extends JsonSerializer<Object> {
    public static final JsonSerializer<Object> INSTANCE = new EmptyArraySerializer();

    private EmptyArraySerializer() {
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        int[] a = {};
        jsonGenerator.writeArray(a, 0, 0);
    }
}