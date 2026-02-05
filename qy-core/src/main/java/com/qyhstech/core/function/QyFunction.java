package com.qyhstech.core.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 定义函数式接口
 *
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface QyFunction<T, R> extends Function<T, R>, Serializable {
}