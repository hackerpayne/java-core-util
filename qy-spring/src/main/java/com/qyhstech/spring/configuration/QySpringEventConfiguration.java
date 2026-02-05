package com.qyhstech.spring.configuration;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.qyhstech.spring.event.QyEventService;
import com.qyhstech.spring.event.impl.QySpringEventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 配置，加载EventBus事件总线
 */
@Configuration
@EnableAsync // 自动开启异步处理
@Slf4j
public class QySpringEventConfiguration {

    private final ApplicationContext applicationContext;

    @Autowired
    public QySpringEventConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public QyEventService qySpringEventService() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】注入 QyEventService 实现自定义事件 >>>>>>>>>>>>>>>>>>");
        return new QySpringEventServiceImpl(applicationContext);
    }

    /**
     * 指定线程池，专用于事件订阅和发布
     *
     * @return
     */
    @Bean(name = "eventTaskExecutor")
    public Executor eventTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("qy-eventExecutor-");
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setMaxPoolSize(5);

        // 增加 TaskDecorator 属性的配置,以支持异步获取Request对象
        //        taskExecutor.setTaskDecorator(new QyRequestTaskDecorator());

        taskExecutor.initialize();
        return TtlExecutors.getTtlExecutor(taskExecutor);// 使用TTL包装
    }

    /**
     * 为SpringEvent指定线程池
     * 注意beanName必须为applicationEventMulticaster；下面的源码中你将看到
     *
     * @param beanFactory
     * @return
     */
    @Bean(name = AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
    public SimpleApplicationEventMulticaster eventMulticaster(BeanFactory beanFactory) {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        eventMulticaster.setTaskExecutor(eventTaskExecutor());
        return eventMulticaster;
    }

}
