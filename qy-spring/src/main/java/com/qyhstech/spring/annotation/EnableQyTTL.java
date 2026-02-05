package com.qyhstech.spring.annotation;

import com.qyhstech.spring.configuration.QyThreadPoolConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自动启用 TTL服务包装系统工具
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Import({ QyThreadPoolConfiguration.class})
@Documented
public @interface EnableQyTTL {
}