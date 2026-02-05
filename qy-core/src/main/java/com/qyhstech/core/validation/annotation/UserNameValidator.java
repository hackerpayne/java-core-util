package com.qyhstech.core.validation.annotation;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.regex.QyRegexPatterns;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用户名格式校验：中文、英文大小写、数字。不包括特殊符号
 */
@Target(value = {FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UserNameValidator.UserNameValidatorInner.class)
public @interface UserNameValidator {

    String message() default "用户名格式无效";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 身份证号码校验器
     */
    class UserNameValidatorInner implements ConstraintValidator<UserNameValidator, String> {

        @Override
        public void initialize(UserNameValidator validator) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext arg1) {
            return !StrUtil.isEmpty(value) && QyRegexPatterns.USER_NAME.matcher(value).matches();
        }

    }
}