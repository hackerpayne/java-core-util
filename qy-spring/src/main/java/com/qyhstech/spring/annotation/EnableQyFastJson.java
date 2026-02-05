package com.qyhstech.spring.annotation;

import com.qyhstech.spring.configuration.FastJsonAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自动启用 FastJsonConfiguration
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Import(FastJsonAutoConfiguration.class)
@Documented
public @interface EnableQyFastJson {
}