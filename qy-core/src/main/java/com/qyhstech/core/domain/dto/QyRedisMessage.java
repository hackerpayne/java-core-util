package com.qyhstech.core.domain.dto;

public interface QyRedisMessage<T> {

    /**
     * 消息ID
     */
    String getId();

    /**
     * 消息类型
     */
    String getMessageType();

    /**
     * 消息内容
     */
    T getMessage();
}
