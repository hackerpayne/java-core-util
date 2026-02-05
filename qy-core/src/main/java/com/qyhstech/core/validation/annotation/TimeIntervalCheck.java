package com.qyhstech.core.validation.annotation;

import com.qyhstech.core.validation.validator.CheckTimeIntervalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, TYPE_USE, METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckTimeIntervalValidator.class)
@Documented
//@Repeatable(TimeIntervalCheck.List.class)
public @interface TimeIntervalCheck {

    String startTime() default "from";
    String endTime() default "to";

    String message() default "{org.hibernate.validator.referenceguide.chapter06.CheckCase.}"+"message}";

    // 分组，如同一个实体类的字段有些情况需要该校验，有些情况不需要，则可通过指定分组实现
    Class<?>[] groups() default {};

    // 指定错误的级别，一般不会用
    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        TimeIntervalCheck[] value();
    }

}
