package com.qyhstech.spring.annotation;

import com.qyhstech.spring.configuration.MvcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动启用 Web MVC配置
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MvcAutoConfiguration.class)
@Documented
public @interface EnableQyWebMvc {
}