package com.qyhstech.core.json.deserializer;

/**
 * 使用时：
 * @JsonDeserialize(using = IntListJsonDeserializer.class) // 同时支持单个和多个ID参数
 */
public class IntListJsonDeserializer extends NumberListDeserializer<Integer> {
    public IntListJsonDeserializer() {
        super(Integer::parseInt);
    }
}