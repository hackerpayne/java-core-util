package com.qyhstech.core.validation.annotation;

import com.qyhstech.core.validation.validator.GenderCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 性别约束
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = GenderCheckValidator.class)
public @interface GenderCheck {

    String message() default "性别有误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}