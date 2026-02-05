package com.qyhstech.spring.context;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 策略工厂配置注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface StaticStrategyFactory {
    /**
     * 策略接口类型
     */
    Class<?> strategyClass();

    /**
     * 策略类型枚举
     */
    Class<? extends Enum<? extends StrategyType>> enumClass();

    /**
     * 工厂名称
     */
    String name() default "";
}