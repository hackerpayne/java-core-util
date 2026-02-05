package com.qyhstech.spring.annotation;

import com.qyhstech.core.spring.QySpringContext;
import com.qyhstech.spring.configuration.CorsAutoConfiguration;
import com.qyhstech.spring.configuration.HibernateValidatorAutoConfiguration;
import com.qyhstech.spring.configuration.JacksonAutoConfiguration;
import com.qyhstech.spring.advice.QyGlobalExceptionAdvice;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动启用 SpringContextUtils
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({QySpringContext.class, QyGlobalExceptionAdvice.class, CorsAutoConfiguration.class, JacksonAutoConfiguration.class, HibernateValidatorAutoConfiguration.class})
@Documented
public @interface EnableQySpringAll {
}