package com.qyhstech.spring.exception;

import java.lang.annotation.*;

/**
 * 指定参数校验的异常码
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface QyUseStatusCode {

    /**
     * 异常对应的错误码.
     *
     * @return 异常对应的错误码
     */
    int code() default 1;
}
