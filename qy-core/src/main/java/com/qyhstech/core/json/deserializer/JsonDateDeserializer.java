package com.qyhstech.core.json.deserializer;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * SpringBoot 中默认接收的日期自动转换不支持yyyy-MM-dd HH:mm:ss格式
 * 使用方法：
 *
 * @JsonDeserialize(using = JsonDateDeserializer.class)
 * private Date startTime;
 */
//@JsonComponent
public class JsonDateDeserializer extends JsonDeserializer<Date> {
//    private String[] patterns = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateAsString = jsonParser.getText();
        Date parseDate = null;
        try {
//            parseDate = DateUtils.parseDate(dateAsString, QyDatePattern.NORMAL_PATTERS);
            parseDate = DateUtil.parseDate(dateAsString);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getCause());
        }
        return parseDate;
    }
}