package com.qyhstech.spring.event.impl;

import com.qyhstech.spring.event.QyEvent;
import com.qyhstech.spring.event.QyEventService;
import org.springframework.context.ApplicationContext;

//@Component
public class QySpringEventServiceImpl implements QyEventService {

    private final ApplicationContext applicationContext;

    //    @Autowired
    public QySpringEventServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 发送一个继承QyEvent的事件
     *
     * @param qyEvent
     * @param <T>
     */
    @Override
    public <T> void publishEvent(QyEvent<T> qyEvent) {
        applicationContext.publishEvent(qyEvent);
    }
}
