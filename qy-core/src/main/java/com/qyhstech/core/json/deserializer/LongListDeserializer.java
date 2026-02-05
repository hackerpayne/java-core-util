package com.qyhstech.core.json.deserializer;

/**
 * 让提交的数据同时支持单个和多个ID，并同时支持字符串和数字
 * 用法: @JsonDeserialize(using = IdListDeserializer.class) // 同时支持单个和多个ID参数
 */
public class LongListDeserializer extends NumberListDeserializer<Long> {

    public LongListDeserializer() {
        super(Long::parseLong);
    }
}
