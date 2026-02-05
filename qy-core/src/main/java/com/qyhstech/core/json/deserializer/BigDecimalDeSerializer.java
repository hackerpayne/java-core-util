package com.qyhstech.core.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 自动把带逗号的字符串转换为金额形式
 */
//@JsonComponent
public class BigDecimalDeSerializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return new BigDecimal(jsonParser.getText().replaceAll(",", ""));
    }
}