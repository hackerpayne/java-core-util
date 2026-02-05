package com.qyhstech.core.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * HTTP请求类型枚举
 */
@Getter
@AllArgsConstructor
public enum QyRequestMethodEnum {

    /**
     * 搜寻 @AnonymousGetMapping
     */
    GET("GET"),

    /**
     * 搜寻 @AnonymousPostMapping
     */
    POST("POST"),

    /**
     * 搜寻 @AnonymousPutMapping
     */
    PUT("PUT"),

    /**
     * 搜寻 @AnonymousPatchMapping
     */
    PATCH("PATCH"),

    /**
     * 搜寻 @AnonymousDeleteMapping
     */
    DELETE("DELETE"),

    /**
     * 否则就是所有 Request 接口都放行
     */
    ALL("ALL");

    /**
     * Request 类型
     */
    private final String value;

    /**
     * 根据Code查找内容
     *
     * @param value
     * @return
     */
    public static QyRequestMethodEnum getItem(String value) {
        return Stream.of(values()).filter(item -> item.value.equals(value)).findAny().orElse(ALL);
    }
}
