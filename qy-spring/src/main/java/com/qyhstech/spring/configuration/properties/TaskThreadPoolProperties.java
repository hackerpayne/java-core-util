package com.qyhstech.spring.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异步任务线程池配置
 */
//@Configuration
@ConfigurationProperties(prefix = "qy.thread-pool")
@Data
public class TaskThreadPoolProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = 5;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 50;

    /**
     * 线程池维护线程所允许的空闲时间
     * 当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
     */
    private int keepAliveSeconds = 60;

    /**
     * 队列长度
     */
    private int queueCapacity = 200;

    /**
     * 线程名称前缀
     */
    private String threadNamePrefix = "ThreadPool-AsyncTask-";


}