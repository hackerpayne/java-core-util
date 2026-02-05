package com.qyhstech.core.function;

/**
 * 可重试操作的接口
 * @param <T>
 */
@FunctionalInterface
public interface RetryableOperation<T> {
    T execute() throws Exception;
}