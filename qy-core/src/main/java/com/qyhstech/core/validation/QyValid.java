package com.qyhstech.core.validation;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.validation.BeanValidationResult;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.spring.QySpringContext;
import jakarta.validation.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.validator.HibernateValidator;

import java.util.List;
import java.util.Set;

/**
 * 使用Hibernate Validator的常用工具类，也可以扩展ValidationUtil
 * 手动进行校验
 * 自定义校验规则：https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-customconstraints
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QyValid {

    //    private static Validator validator = getHibernateFastValidator(true);
    private static final Validator validator = QySpringContext.getBean(Validator.class);

    /**
     * 开启快速结束模式 failFast (true)
     *
     * @param failFast 是否开启快速返回错误模式
     * @return
     */
    public static Validator getHibernateFastValidator(boolean failFast) {
        try (ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(failFast)  // 是否启用快速校验模式，即失败一个即退出
//                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory()) {
            return validatorFactory.getValidator();
        }
    }

    /**
     * 校验对象
     *
     * @param t      校验目标
     * @param groups 校验分组
     * @return
     */
    public static <T> void validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> validate = validator.validate(t, groups);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException("参数校验异常", validate);
        }
    }

    /**
     * 校验bean的某一个属性
     *
     * @param t            要校验的目标
     * @param propertyName 要校验的属性
     * @param groups       校验分组
     * @param <T>
     */
    public static <T> void validateProperty(T t, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<T>> validate = validator.validateProperty(t, propertyName, groups);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException("参数校验异常", validate);
        }
    }

    /**
     * 将校验结果转换为List列表
     *
     * @param violationSet
     * @param <T>
     * @return
     */
    public static <T> List<String> toResult(Set<ConstraintViolation<T>> violationSet) {
        List<String> msgList = QyList.empty();
        boolean hasError = violationSet != null && !violationSet.isEmpty();
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                msgList.add(violation.getMessage());
            }
        }
        return msgList;
    }

    /**
     * 校验结果合并
     *
     * @param result
     * @return
     */
    public static String getValidMessageStr(BeanValidationResult result) {
        return getValidMessageStr(result, ",");
    }

    /**
     * 校验结果合并
     *
     * @param result
     * @param joinStr
     * @return
     */
    public static String getValidMessageStr(BeanValidationResult result, String joinStr) {
        return StrUtil.join(joinStr, getValidMessage(result));
    }

    /**
     * 校验结果转换为List
     *
     * @param result
     * @return
     */
    public static List<String> getValidMessage(BeanValidationResult result) {
        return result.getErrorMessages()
                .stream().map(BeanValidationResult.ErrorMessage::getMessage).toList();
    }

}
