package com.qyhstech.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 策略工厂里面用于快速指定策略名称时使用，
 * 使用时：@QyStrategyAnno("redis")
 * 在添加到工厂时，可以用：这样好指定策略名称，而不使用默认的类名
 * strategies.stream().collect(Collectors.toMap(s -> s.getClass().getAnnotation(QyStrategyAnno.class).value(), Function.identity()));
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//@Component
public @interface QyStrategyAnno {
    String value();
}
