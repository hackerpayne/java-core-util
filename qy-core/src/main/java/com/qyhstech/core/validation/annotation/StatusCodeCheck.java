package com.qyhstech.core.validation.annotation;

import com.qyhstech.core.domain.enums.QyStatusCode;
import com.qyhstech.core.validation.validator.StatusCodeCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 使用时：
 * public enum ThirdPartyPlatformEnum implements StatusCode<String> {}
 * 替换出信息，然后
 *
 * @StatusCodeCheck(message = "无效的第三方平台类型", value = ThirdPartyPlatformEnum.class)
 * private String thirdPartyPlatform;
 * 即可对枚举信息进行判断
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = StatusCodeCheckValidator.class)
@Documented
public @interface StatusCodeCheck {

    // 指定校验失败时的异常信息，后面会详细说明
    String message() default "{com.qyhstech.core.model.enums.QyStatusCode.message}";

    // 分组，如同一个实体类的字段有些情况需要该校验，有些情况不需要，则可通过指定分组实现
    Class<?>[] groups() default {};

    // 指定错误的级别，一般不会用
    Class<? extends Payload>[] payload() default {};

    // 自定义的属性
    Class<? extends QyStatusCode> value();
}

