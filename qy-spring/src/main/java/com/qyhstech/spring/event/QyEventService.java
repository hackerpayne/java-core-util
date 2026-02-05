package com.qyhstech.spring.event;

/**
 * Spring内部自带的事件发布器
 */
public interface QyEventService {

    /**
     * 发布事件
     *
     * @param qyEvent
     */
    <T> void publishEvent(QyEvent<T> qyEvent);

}