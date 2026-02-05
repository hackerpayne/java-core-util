package com.qyhstech.core.validation.annotation;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.regex.QyRegexPatterns;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

/**
 * 身份证格式校验
 */
@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdcardValidator.IdCardValidatorInner.class)
public @interface IdcardValidator {

    String message() default "不是有效的身份证号码";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 身份证号码校验器
     */
    class IdCardValidatorInner implements ConstraintValidator<IdcardValidator, String> {

        private Pattern moneyPattern = QyRegexPatterns.CITIZEN_ID;

        @Override
        public void initialize(IdcardValidator money) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext arg1) {
            return !StrUtil.isEmpty(value) && moneyPattern.matcher(value).matches();
        }

    }
}