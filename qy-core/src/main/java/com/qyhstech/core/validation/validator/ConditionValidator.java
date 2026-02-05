package com.qyhstech.core.validation.validator;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.qyhstech.core.validation.annotation.AssertFalseOn;
import com.qyhstech.core.validation.annotation.AssertTrueOn;
import com.qyhstech.core.validation.annotation.EnableConditionValid;
import com.qyhstech.core.validation.annotation.NotNullOn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 通过EL表达式来判断条件是否是需要执行的
 */
@Slf4j
public class ConditionValidator implements ConstraintValidator<EnableConditionValid, Object> {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final EvaluationContext elContext = new StandardEvaluationContext();

    @Override
    public boolean isValid(Object validatedBean, ConstraintValidatorContext context) {
        fillBean(validatedBean);
        Field[] fields = ReflectUtil.getFields(validatedBean.getClass());
        boolean res = true;
        for (Field field : fields) {
            //获取当前字段
            Object fieldValue = ReflectUtil.getFieldValue(validatedBean, field.getName());
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof NotNullOn) {
                    NotNullOn notNullOn = (NotNullOn) annotation;
                    res = res && isValid(NotNullOn.class.getName(), notNullOn.message(), context, notNullOn.on(), field.getName(), fieldValue);
                }
                if (annotation instanceof AssertTrueOn) {
                    AssertTrueOn assertTrueOn = (AssertTrueOn) annotation;
                    res = res && isValid(AssertTrueOn.class.getName(), assertTrueOn.message(), context, assertTrueOn.on(), field.getName(), fieldValue);
                }
                if (annotation instanceof AssertFalseOn) {
                    AssertFalseOn assertTrueOn = (AssertFalseOn) annotation;
                    res = res && isValid(AssertFalseOn.class.getName(), assertTrueOn.message(), context, assertTrueOn.on(), field.getName(), fieldValue);
                }
            }

        }
        return res;
    }

    private boolean isValid(String name, String message, ConstraintValidatorContext context, String on, String fieldName, Object fieldValue) {
        Boolean res = true;
        if (parseEl(on)) {
            ConstraintValidator validator = SupportContext.getValidator(name);
            if (!validator.isValid(fieldValue, context)) {
                res = false;
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(fieldName)
                        .addConstraintViolation();
            }
        }
        return res;
    }


    private void fillBean(Object object) {
        Map<String, Object> map = BeanUtil.beanToMap(object);
        map.forEach(elContext::setVariable);
    }

    protected Boolean parseEl(String el) {
        Expression expression = parser.parseExpression(el);
        Object value = expression.getValue(elContext);
        return Boolean.valueOf(value.toString());
    }
}
