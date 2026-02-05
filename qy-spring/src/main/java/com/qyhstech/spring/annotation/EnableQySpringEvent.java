package com.qyhstech.spring.annotation;

import com.qyhstech.spring.configuration.QySpringEventConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动启用 Spring Event事件
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({QySpringEventConfiguration.class})
@Documented
public @interface EnableQySpringEvent {
}