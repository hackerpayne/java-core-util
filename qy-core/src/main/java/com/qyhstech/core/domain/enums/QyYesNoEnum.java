package com.qyhstech.core.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qyhstech.core.domain.base.QyModelNameValue;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 0为否，1为是的枚举类型
 */
@Getter
public enum QyYesNoEnum implements IStatus {

    NO(0, "否"),
    YES(1, "是");

    @EnumValue // 声明数据库存的是这个值
    @JsonValue
    private final Integer value;

    private final String desc;

    QyYesNoEnum(Integer value, String name) {
        this.value = value;
        this.desc = name;
    }

    public static QyYesNoEnum getItem(Integer value) {
        return Stream.of(values()).filter(item -> item.value.equals(value)).findAny().orElse(null);
    }

    public static List<QyModelNameValue> getOptions() {
        return Stream.of(values()).map(item -> new QyModelNameValue(item.desc, item.value)).collect(Collectors.toList());
    }
}
