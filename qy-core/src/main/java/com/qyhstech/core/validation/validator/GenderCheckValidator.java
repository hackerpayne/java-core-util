package com.qyhstech.core.validation.validator;

import com.qyhstech.core.validation.annotation.GenderCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 性别校验器
 */
public class GenderCheckValidator implements ConstraintValidator<GenderCheck, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && (value.equals("男") || value.equals("女"));
    }

}
