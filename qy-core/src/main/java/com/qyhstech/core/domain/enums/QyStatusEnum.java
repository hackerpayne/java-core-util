package com.qyhstech.core.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qyhstech.core.domain.base.QyModelNameValue;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

/**
 * 通用的状态码设计，0停用，1启用
 */
@Getter
public enum QyStatusEnum implements IStatus {

    DISABLE(0, "停用"),
    ENABLE(1, "启用"),
    ;

    @EnumValue // 声明数据库存的是这个值
    @JsonValue
    private final Integer value;

    private final String name;

    QyStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static QyStatusEnum getItem(Integer value) {
        return Stream.of(values()).filter(item -> item.value.equals(value)).findAny().orElse(null);
    }

    /**
     * 获取所有的数据列表
     *
     * @return
     */
    public static List<QyModelNameValue> getOptions() {
        return Stream.of(values()).map(item -> new QyModelNameValue(item.name, item.value)).toList();
    }


}
