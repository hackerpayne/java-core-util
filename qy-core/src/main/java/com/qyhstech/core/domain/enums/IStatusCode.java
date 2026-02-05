package com.qyhstech.core.domain.enums;

/**
 * 通用的状态枚举类
 */
public interface IStatusCode {
    /**
     * 获取提示码
     *
     */
    Integer getCode();

    /**
     * 获取消息提示
     *
     */
    String getDesc();
}
