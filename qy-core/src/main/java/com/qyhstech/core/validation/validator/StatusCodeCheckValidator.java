package com.qyhstech.core.validation.validator;

import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.enums.QyStatusCode;
import com.qyhstech.core.validation.annotation.StatusCodeCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对枚举状态码的校验
 */
public class StatusCodeCheckValidator implements ConstraintValidator<StatusCodeCheck, Object> {

    /**
     * 枚举缓存
     */
    private static final Map<Class<? extends QyStatusCode>, List<QyStatusCode>> CACHE_MAP = new ConcurrentHashMap<>(64);
    private Class<? extends QyStatusCode> enumClass;

    @Override
    public void initialize(StatusCodeCheck constraintAnnotation) {
        this.enumClass = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) {
            return false;
        }
        if (!enumClass.isEnum()) {
            throw new RuntimeException("StatusCode 的实现类必须是枚举类型");
        }
        List<QyStatusCode> qyStatusCodeList = CACHE_MAP.computeIfAbsent(enumClass, (key) -> {
            try {
                Method method = key.getDeclaredMethod("values");
                QyStatusCode[] qyStatusCodes = (QyStatusCode[]) method.invoke(null);
                return Stream.of(qyStatusCodes).collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return QyList.empty();
        });
        for (QyStatusCode qyStatusCode : qyStatusCodeList) {
            if (qyStatusCode.getCode().equals(object)) {
                return true;
            }
        }
        return false;
    }
}
