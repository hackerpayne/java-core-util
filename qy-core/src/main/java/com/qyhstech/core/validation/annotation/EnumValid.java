package com.qyhstech.core.validation.annotation;

import com.qyhstech.core.validation.validator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举校验器：用法
 * @EnumValid(enumClass = SexEnum.class, message = "输入性别不合法")
 */
@Constraint(validatedBy = {EnumValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface EnumValid {

    /**
     * 不合法时 抛出异常信息
     */
    String message() default "值不合法";

    /**
     * 校验的枚举类
     *
     * @return
     */
    Class enumClass() default Enum.class;

    /**
     * 对应枚举类中需要比对的字段
     *
     * @return
     */
    String field() default "value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}