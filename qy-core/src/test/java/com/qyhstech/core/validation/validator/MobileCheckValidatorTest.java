package com.qyhstech.core.validation.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

class MobileCheckValidatorTest {

    private static ExecutableValidator executableValidator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        executableValidator = factory.getValidator().forExecutables();
    }

    public void setMobile(String mobile) {
        // to do
    }

    @Test
    public void manufacturerIsNull() throws NoSuchMethodException {
        MobileCheckValidatorTest mobileTest = new MobileCheckValidatorTest();

        Method method = MobileCheckValidatorTest.class.getMethod("setMobile", String.class);
        Object[] parameterValues = {"1111111"};
        Set<ConstraintViolation<MobileCheckValidatorTest>> violations = executableValidator.validateParameters(
                mobileTest, method, parameterValues);

        violations.forEach(violation -> System.out.println(violation.getMessage()));
    }

}