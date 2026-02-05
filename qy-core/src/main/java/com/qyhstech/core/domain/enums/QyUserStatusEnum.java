package com.qyhstech.core.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qyhstech.core.domain.base.QyModelNameValue;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

/**
 * 通用的激活枚举类
 */
@Getter
public enum QyUserStatusEnum implements IStatus {

    UNACTIVE(0, "未激活"),
    ACTIVE(1, "已激活"),
    BLACKLIST(2, "黑名单"),
    ;

    @EnumValue // 声明数据库存的是这个值
    @JsonValue
    private final Integer value;

    private final String name;

    QyUserStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static QyUserStatusEnum getItem(Integer value) {
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
