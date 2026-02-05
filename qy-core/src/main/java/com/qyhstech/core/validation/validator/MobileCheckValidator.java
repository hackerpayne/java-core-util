package com.qyhstech.core.validation.validator;

import com.qyhstech.core.validation.annotation.MobileCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 手机号校验工具类
 */
public class MobileCheckValidator implements ConstraintValidator<MobileCheck, String> {

    /**
     * 手机验证规则
     */
    private Pattern pattern;

    @Override
    public void initialize(MobileCheck mobile) {
        pattern = Pattern.compile(mobile.regexp());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return pattern.matcher(value).matches();
    }
}
