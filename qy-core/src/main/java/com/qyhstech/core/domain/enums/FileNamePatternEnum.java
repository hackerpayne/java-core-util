package com.qyhstech.core.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 文件名的命名规则
 */
@Getter
public enum FileNamePatternEnum {
    UUID("uuid", "UUID生成的文件名"),
    UUID_RAND("uuid_rand", "UUID+6位随机数"),
    DATE("date", "日期"),
    DATE_RAND("date_rand", "日期加6位随机数"),
    DATETIME("datetime", "时间"),
    DATETIME_RAND("datetime_rand", "时间加6位随机数"),
    TIMESTAMP("timestamp", "时间戳"),
    TIMESTAMP_RAND("timestamp_rand", "时间戳加6位随机数"),
    ;

    @JsonValue
    private final String value;

    private final String name;

    FileNamePatternEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }
}
