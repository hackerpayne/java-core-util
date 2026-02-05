package com.qyhstech.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 条件为True时打开验证
 */
@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface AssertTrueOn {

    String on();

    String message() default "";
}

