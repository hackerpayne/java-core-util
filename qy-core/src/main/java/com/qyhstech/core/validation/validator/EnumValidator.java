package com.qyhstech.core.validation.validator;

import com.qyhstech.core.validation.annotation.EnumValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 枚举校验注解的校验规则实现
 * 使用时：@EnumValid(enumClass = SexEnum.class, message = "输入性别不合法")
 */
public class EnumValidator implements ConstraintValidator<EnumValid, Object> {

    private Class clazz;

    private String validField;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        clazz = constraintAnnotation.enumClass();
        validField = constraintAnnotation.field();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null || "".equals(object)) {
            return true;
        }

        if (!clazz.isEnum()) {
            return false;
        }

        Class<Enum> enumClass = (Class<Enum>) clazz;
        //获取所有枚举实例
        Enum[] enumConstants = enumClass.getEnumConstants();

        // 需要比对的字段
        Field field = enumClass.getDeclaredField(validField);
        field.setAccessible(true);

        for (Enum constant : enumConstants) {
            // 取值final修饰
            Object validValue = field.get(constant);
            if (validValue == null) {
                Method method = enumClass.getMethod(validField);
                validValue = method.invoke(constant);
            }

            if (validValue instanceof Number) {
                validValue = ((Number) validValue).intValue();
                object = ((Number) object).intValue();
            }
            if (Objects.equals(validValue, object)) {
                return true;
            }
        }
        return false;
    }
}