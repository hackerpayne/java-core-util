package com.qyhstech.core.validation.annotation;

import com.qyhstech.core.validation.validator.AtLeastOneNotEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 至少一个数据不为空
 * 使用方法：
 *
 * @AtLeastOneNotEmpty(fields = {"name", "age"},message="userKey,deviceId不能同时为空")
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneNotEmptyValidator.class)
@Documented
public @interface AtLeastOneNotEmpty {
    String message() default "{至少有一个属性不可为空}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields();
}
