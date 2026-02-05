package com.qyhstech.spring.annotation;

import com.qyhstech.spring.configuration.QySpringConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动启用 SpringContextUtils
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({QySpringConfiguration.class})
@Documented
public @interface EnableQySpring {
}