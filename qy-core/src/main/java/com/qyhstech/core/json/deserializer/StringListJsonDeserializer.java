package com.qyhstech.core.json.deserializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 单个或多个字符串转为列表，可以提交一个数据或者一些列表同时转换
 * 使用时：@JsonDeserialize(using = StringListDeserializer.class)
 */
public class StringListJsonDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        List<String> result = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode element : node) {
                if (element.isTextual()) {
                    String text = element.asText("").trim();
                    if (StrUtil.isNotEmpty(text)) {
                        result.add(text);
                    }
                }
            }
        } else if (node.isTextual()) {
            String text = node.asText("").trim();
            if (StrUtil.isNotEmpty(text)) {
                result.add(text);
            }
        } else {
            // fallback：把其他节点类型转为字符串
            result.add(node.toString());
        }

        return result;
    }
}
