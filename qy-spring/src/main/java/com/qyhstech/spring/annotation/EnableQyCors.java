package com.qyhstech.spring.annotation;

import com.qyhstech.spring.configuration.CorsAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动启用 CORSConfiguration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CorsAutoConfiguration.class)
@Documented
public @interface EnableQyCors {
}