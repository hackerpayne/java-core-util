package com.qyhstech.core.validation.validator;

import com.qyhstech.core.QyObj;
import com.qyhstech.core.validation.annotation.TimeIntervalCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

/**
 * 判断时间是否在间隔内
 */
public class CheckTimeIntervalValidator implements ConstraintValidator<TimeIntervalCheck, String> {

    private String startTime;

    private String endTime;

    @Override
    public void initialize(TimeIntervalCheck timeIntervalCheck) {
        this.startTime = timeIntervalCheck.startTime();
        this.endTime = timeIntervalCheck.endTime();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value) return true;

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);// 获取对象属性值
        Object start = beanWrapper.getPropertyValue(startTime);
        Object end = beanWrapper.getPropertyValue(endTime);
        if (null == start || null == end) {
            return true;
        }
        int result = QyObj.compare((Date) end, (Date) start);
        return result > 0;
    }
}
