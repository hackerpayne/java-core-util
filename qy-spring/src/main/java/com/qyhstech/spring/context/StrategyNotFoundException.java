package com.qyhstech.spring.context;

/**
 * 策略未找到异常
 */
public class StrategyNotFoundException extends RuntimeException {
    public StrategyNotFoundException(String message) {
        super(message);
    }

    public StrategyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}