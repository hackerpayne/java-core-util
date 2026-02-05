package com.qyhstech.core.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 密码相同匹配，需要添加到类上进行注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.PasswordCheckValidatorInner.class)
public @interface PasswordMatchValidator {

    String message() default "密码格式不匹配{pattern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PasswordCheckValidatorInner implements ConstraintValidator<PasswordMatchValidator, Object> {
        @Override
        public void initialize(PasswordMatchValidator constraintAnnotation) {
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
            String password = (String) beanWrapper.getPropertyValue("password");
            String checkPassword = (String) beanWrapper.getPropertyValue("checkPassword");
            return password != null && password.equals(checkPassword);
        }
    }

}
