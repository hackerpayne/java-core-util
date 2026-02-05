package com.qyhstech.core.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NumberListDeserializer<T extends Number> extends JsonDeserializer<List<T>> {

    private final Function<String, T> parser;

    public NumberListDeserializer(Function<String, T> parser) {
        this.parser = parser;
    }

    @Override
    public List<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        List<T> result = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode element : node) {
                T value = convertNode(p, element);
                if (value != null) {
                    result.add(value);
                }
            }
        } else {
            T value = convertNode(p, node);
            if (value != null) {
                result.add(value);
            }
        }

        return result;
    }

    private T convertNode(JsonParser p, JsonNode node) throws JsonMappingException {
        // 节点是 null -> 忽略
        if (node == null || node.isNull()) {
            return null;
        }

        // 直接数字
        if (node.isNumber()) {
            return parser.apply(node.asText());
        }

        // 字符串
        if (node.isTextual()) {
            String text = node.asText().trim();
            if (text.isEmpty()) {
                return null; // 空字符串忽略
            }
            try {
                return parser.apply(text);
            } catch (NumberFormatException e) {
                throw JsonMappingException.from(p, "无法转换为数字类型: " + text);
            }
        }

        // 其他类型 -> 不支持
        throw JsonMappingException.from(p, "不支持的数字格式: " + node);
    }
}
