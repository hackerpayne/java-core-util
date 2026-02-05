package com.qyhstech.core.validation.annotation;

import com.qyhstech.core.domain.enums.QyCaseModeEnum;
import com.qyhstech.core.validation.validator.CheckCaseValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Hibernate Validator 大小写校验工具
 * 使用时：@CaseCheckValidAnnotation(value = CaseModeEnum.LOWER,message = "userName必须是小写")
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckCaseValidator.class)
@Documented
//@Repeatable(CaseCheck.List.class)
public @interface CaseCheck {

    String message() default ""; // 配置默认错误提示消息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 获取验校值
     *
     * @return
     */
    QyCaseModeEnum value();

    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CaseCheck[] value();
    }

}

