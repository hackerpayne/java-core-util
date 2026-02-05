package com.qyhstech.core.json.anno;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qyhstech.core.json.deserializer.BigDecimalDeSerializer;
import com.qyhstech.core.json.serializer.BigDecimalSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解指定数据为BigDecimal格式
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = BigDecimalSerializer.class)
@JsonDeserialize(using = BigDecimalDeSerializer.class)
public @interface QyBigDecimalFormat {

    /**
     * 转换为指定的形式
     *
     * @return
     */
    String value() default "#####0.00";
}