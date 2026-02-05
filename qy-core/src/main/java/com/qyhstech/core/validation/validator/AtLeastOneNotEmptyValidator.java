package com.qyhstech.core.validation.validator;


import com.qyhstech.core.reflect.QyReflect;
import com.qyhstech.core.validation.annotation.AtLeastOneNotEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

/**
 * 至少一个数据不为空处理器
 */
@Slf4j
public class AtLeastOneNotEmptyValidator implements ConstraintValidator<AtLeastOneNotEmpty, Object> {
    private String[] fields;

    @Override
    public void initialize(AtLeastOneNotEmpty atLeastOneNotEmpty) {
        this.fields = atLeastOneNotEmpty.fields();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) {
            return true;
        }
        try {
            for (String fieldName : fields) {
                if (!isBlank.apply(object, object.getClass().getDeclaredField(fieldName))) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("AtLeastOneNotEmptyValidator校验失败", e);
            return false;
        }
    }

    /**
     * 判断数据是否为空，字符串判断空值，其余的判断null值
     */
    private static BiFunction<Object, Field, Boolean> isBlank = (object, field) ->
    {
        log.info("NotBothBlankValidator field type {}", field.getType());
        try {
            field.setAccessible(true);
            return (String.class.isAssignableFrom(field.getType()) ? StringUtil.isBlank((String) field.get(object)) : field.get(object) == null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    };

}