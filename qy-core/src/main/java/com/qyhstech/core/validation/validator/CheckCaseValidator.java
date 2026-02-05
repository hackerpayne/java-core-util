package com.qyhstech.core.validation.validator;

import com.qyhstech.core.domain.enums.QyCaseModeEnum;
import com.qyhstech.core.validation.annotation.CaseCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Hibernate大小写校验器
 */
public class CheckCaseValidator implements ConstraintValidator<CaseCheck, String> {
    private QyCaseModeEnum caseMode;

    @Override
    public void initialize(CaseCheck caseCheck) {
        this.caseMode = caseCheck.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }

        if (caseMode == QyCaseModeEnum.UPPER) {
            return s.equals(s.toUpperCase());
        } else {
            return s.equals(s.toLowerCase());
        }
    }


}
