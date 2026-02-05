package com.qyhstech.core.validation.annotation;

import com.qyhstech.core.validation.validator.ConditionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 开启条件
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Constraint(validatedBy = ConditionValidator.class)
public @interface EnableConditionValid {
    @Deprecated
    String message() default "";

    @Deprecated
    Class<?>[] groups() default {};

    @Deprecated
    Class<? extends Payload>[] payload() default {};
}
