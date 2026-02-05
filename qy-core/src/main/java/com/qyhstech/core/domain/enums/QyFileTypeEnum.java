package com.qyhstech.core.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qyhstech.core.domain.base.QyModelNameValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 上传文件的类型枚举
 */
@Getter
public enum QyFileTypeEnum {
    IMAGE("img", "图片文件"),
    DOCUMENT("doc", "文档文件"),
    MUSIC("music", "音乐文件"),
    VIDEO("video", "视频文件"),
    OTHER("other", "其它文件"),
    ;

    @EnumValue // 声明数据库存的是这个值
    @JsonValue
    private final String value;
    private final String name;

    QyFileTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static QyFileTypeEnum getItem(String value) {
        return Arrays.stream(values()).filter(item -> item.value.equals(value)).findAny().orElse(null);
    }

    public static List<QyModelNameValue> getOptions() {
        return Arrays.stream(values()).map(item -> new QyModelNameValue(item.name, item.value)).toList();
    }

}
