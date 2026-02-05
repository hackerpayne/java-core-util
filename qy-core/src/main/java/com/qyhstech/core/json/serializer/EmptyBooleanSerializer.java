package com.qyhstech.core.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 自定义序列化
 */
public class EmptyBooleanSerializer extends JsonSerializer<Object> {
    public static final JsonSerializer<Object> INSTANCE = new EmptyBooleanSerializer();

    private EmptyBooleanSerializer() {
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeBoolean(false);
    }
}