package com.qyhstech.core.domain.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestStatusEnum implements QyBaseEnum<Integer> {
    DISABLE(0, "停用"),
    ENABLE(1, "启用");

    @EnumValue
    @JsonValue
    private final Integer value;

    private final String name;
}
