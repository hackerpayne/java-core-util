package com.qyhstech.spring.annotation;

import com.qyhstech.spring.advice.QyGlobalExceptionAdvice;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动启用 全局异常处理 功能
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(QyGlobalExceptionAdvice.class)
@Documented
@Inherited
public @interface EnableQyWebGlobalException {
}