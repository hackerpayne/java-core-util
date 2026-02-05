package com.qyhstech.core.validation.annotation;

import com.qyhstech.core.validation.validator.MobileCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证手机号，空和正确的手机号都能验证通过<br/>
 * 正确的手机号由11位数字组成，第一位为1
 * 第二位为 3、4、5、7、8
 * <p>
 * 使用时：@MobileValidator(message = "手机号格式不正确")
 */
@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "1\\d{10}")
@Null
@Documented
@Constraint(validatedBy = {MobileCheckValidator.class})
@Target({METHOD, FIELD})
@Retention(RUNTIME)
//@Repeatable(MobileCheck.List.class)
public @interface MobileCheck {

    /**
     * 错误消息提示
     *
     * @return
     */
    String message() default "手机号格式不正确";

    String regexp() default "1\\d{10}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        MobileCheck[] value();
    }

}
