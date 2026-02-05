package com.qyhstech.core.validation.annotation;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.PasswordValidatorInner.class)
public @interface PasswordValidator {

    /**
     * 必须的属性
     * 显示 校验信息
     * 利用 {} 获取 属性值，参考了官方的message编写方式
     *
     * @see org.hibernate.validator 静态资源包里面 message 编写方式
     */
    String message() default "密码格式不匹配{pattern}";

    /**
     * 必须的属性
     * 用于分组校验
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 格式
     *
     * @return
     */
    String pattern() default "";

    class PasswordValidatorInner implements ConstraintValidator<PasswordValidator, String> {

        private String pattern = "";

        @Override
        public void initialize(PasswordValidator passwordValidator) {
            this.pattern = passwordValidator.pattern();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return StrUtil.isNotBlank(value) && Pattern.compile(pattern).matcher(value).matches();
        }
    }
}
