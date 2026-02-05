package com.qyhstech.spring.annotation;

import com.qyhstech.spring.configuration.QyTaskExecutorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动启用 线程池管理类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(QyTaskExecutorConfiguration.class)
@Documented
public @interface EnableQyTaskExecutor {
}