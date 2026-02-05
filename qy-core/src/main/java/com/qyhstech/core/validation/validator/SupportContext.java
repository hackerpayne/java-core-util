package com.qyhstech.core.validation.validator;

import com.qyhstech.core.validation.annotation.AssertFalseOn;
import com.qyhstech.core.validation.annotation.AssertTrueOn;
import com.qyhstech.core.validation.annotation.NotNullOn;
import jakarta.validation.ConstraintValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertFalseValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 **/
@SuppressWarnings("all")
public class SupportContext {
    public static final Map<String, ConstraintValidator> map = new HashMap<>();

    public static ConstraintValidator getValidator(String className) {
        return map.get(className);
    }

    static {
        map.put(NotNullOn.class.getName(), new NotNullValidator());
        map.put(AssertTrueOn.class.getName(), new AssertTrueValidator());
        map.put(AssertFalseOn.class.getName(), new AssertFalseValidator());
    }

}
