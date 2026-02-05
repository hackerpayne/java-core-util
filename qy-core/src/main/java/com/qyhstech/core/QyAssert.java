package com.qyhstech.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.domain.enums.IStatusCode;
import com.qyhstech.core.exception.QyBizException;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 断言工具类，集成 Hutool 加自定义
 */
public class QyAssert extends Assert {

    // ===================== 通用断言方法 =====================

    /**
     * 通用断言方法，根据条件判断是否抛出异常
     *
     * @param condition         条件判断函数
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIf(boolean condition, Supplier<? extends T> exceptionSupplier) throws T {
        if (condition) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 通用断言方法，根据对象和条件判断是否抛出异常
     *
     * @param obj               要校验的对象
     * @param validator         校验函数
     * @param exceptionSupplier 异常提供者
     * @param <E>               对象类型
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <E, T extends Throwable> void throwIf(E obj, Function<E, Boolean> validator, Supplier<? extends T> exceptionSupplier) throws T {
        if (validator.apply(obj)) {
            throw exceptionSupplier.get();
        }
    }

    // ===================== 基于条件的断言 =====================

    /**
     * 条件为真时抛出异常
     *
     * @param condition 条件
     * @param message   错误消息
     */
    public static void throwIfTrue(boolean condition, String message) {
        throwIf(condition, () -> new QyBizException(message));
    }

    /**
     * 条件为真时抛出异常
     *
     * @param condition         条件
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfTrue(boolean condition, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(condition, exceptionSupplier);
    }

    /**
     * 条件为真时抛出异常
     *
     * @param condition 条件
     * @param status    状态码
     */
    public static void throwIfTrue(boolean condition, IStatusCode status) {
        throwIf(condition, () -> new QyBizException(status));
    }

    /**
     * 条件为真时抛出异常
     *
     * @param condition 条件
     * @param status    状态码
     * @param params    参数
     */
    public static void throwIfTrue(boolean condition, IStatusCode status, Object... params) {
        throwIf(condition, () -> new QyBizException(status.getCode(), StrUtil.format(status.getDesc(), params)));
    }

    /**
     * 条件为假时抛出异常
     *
     * @param condition 条件
     * @param message   错误消息
     */
    public static void throwIfFalse(boolean condition, String message) {
        throwIf(!condition, () -> new QyBizException(message));
    }

    /**
     * 条件为假时抛出异常
     *
     * @param condition         条件
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfFalse(boolean condition, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(!condition, exceptionSupplier);
    }

    /**
     * 条件为假时抛出异常
     *
     * @param condition 条件
     * @param status    状态码
     */
    public static void throwIfFalse(boolean condition, IStatusCode status) {
        throwIf(!condition, () -> new QyBizException(status));
    }

    /**
     * 条件为假时抛出异常
     *
     * @param condition 条件
     * @param status    状态码
     * @param params    参数
     */
    public static void throwIfFalse(boolean condition, IStatusCode status, Object... params) {
        throwIf(!condition, () -> new QyBizException(status.getCode(), StrUtil.format(status.getDesc(), params)));
    }

    // ===================== 空值检查断言 =====================

    /**
     * 对象为null时抛出异常
     *
     * @param obj     对象
     * @param message 错误消息
     */
    public static void throwIfNull(Object obj, String message) {
        throwIf(obj == null, () -> new QyBizException(message));
    }

    /**
     * 对象为null时抛出异常
     *
     * @param obj               对象
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfNull(Object obj, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(obj == null, exceptionSupplier);
    }

    /**
     * 对象为null时抛出异常
     *
     * @param obj    对象
     * @param status 状态码
     */
    public static void throwIfNull(Object obj, IStatusCode status) {
        throwIf(obj == null, () -> new QyBizException(status));
    }

    /**
     * 对象为null时抛出异常
     *
     * @param obj    对象
     * @param status 状态码
     * @param params 参数
     */
    public static void throwIfNull(Object obj, IStatusCode status, Object... params) {
        throwIf(obj == null, () -> new QyBizException(status.getCode(), StrUtil.format(status.getDesc(), params)));
    }

    // ===================== 字符串空值检查断言 =====================

    /**
     * 字符串为空时抛出异常
     *
     * @param str     字符串
     * @param message 错误消息
     */
    public static void throwIfEmpty(String str, String message) {
        throwIf(StrUtil.isEmpty(str), () -> new QyBizException(message));
    }

    /**
     * 字符串为空时抛出异常
     *
     * @param str               字符串
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfEmpty(String str, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(StrUtil.isEmpty(str), exceptionSupplier);
    }

    /**
     * 字符串为空时抛出异常
     *
     * @param str    字符串
     * @param status 状态码
     */
    public static void throwIfEmpty(String str, IStatusCode status) {
        throwIf(StrUtil.isEmpty(str), () -> new QyBizException(status));
    }

    /**
     * 字符串为空时抛出异常
     *
     * @param str    字符串
     * @param status 状态码
     * @param params 参数
     */
    public static void throwIfEmpty(String str, IStatusCode status, Object... params) {
        throwIf(StrUtil.isEmpty(str), () -> new QyBizException(status.getCode(), StrUtil.format(status.getDesc(), params)));
    }

    /**
     * 字符串不为空时抛出异常
     *
     * @param str     字符串
     * @param message 错误消息
     */
    public static void throwIfNotEmpty(String str, String message) {
        throwIf(StrUtil.isNotEmpty(str), () -> new QyBizException(message));
    }

    /**
     * 字符串不为空时抛出异常
     *
     * @param str               字符串
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfNotEmpty(String str, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(StrUtil.isNotEmpty(str), exceptionSupplier);
    }

    /**
     * 字符串不为空时抛出异常
     *
     * @param str    字符串
     * @param status 状态码
     */
    public static void throwIfNotEmpty(String str, IStatusCode status) {
        throwIf(StrUtil.isNotEmpty(str), () -> new QyBizException(status));
    }

    /**
     * 字符串不为空时抛出异常
     *
     * @param str    字符串
     * @param status 状态码
     * @param params 参数
     */
    public static void throwIfNotEmpty(String str, IStatusCode status, Object... params) {
        throwIf(StrUtil.isNotEmpty(str), () -> new QyBizException(status.getCode(), StrUtil.format(status.getDesc(), params)));
    }

    // ===================== 集合空值检查断言 =====================

    /**
     * 集合为空时抛出异常
     *
     * @param collection 集合
     * @param message    错误消息
     */
    public static void throwIfEmpty(Collection<?> collection, String message) {
        throwIf(CollUtil.isEmpty(collection), () -> new QyBizException(message));
    }

    /**
     * 集合为空时抛出异常
     *
     * @param collection        集合
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfEmpty(Collection<?> collection, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(CollUtil.isEmpty(collection), exceptionSupplier);
    }

    /**
     * 集合为空时抛出异常
     *
     * @param collection 集合
     * @param status     状态码
     */
    public static void throwIfEmpty(Collection<?> collection, IStatusCode status) {
        throwIf(CollUtil.isEmpty(collection), () -> new QyBizException(status));
    }

    /**
     * 集合为空时抛出异常
     *
     * @param collection 集合
     * @param status     状态码
     * @param params     参数
     */
    public static void throwIfEmpty(Collection<?> collection, IStatusCode status, Object... params) {
        throwIf(CollUtil.isEmpty(collection), () -> new QyBizException(status.getCode(), StrUtil.format(status.getDesc(), params)));
    }

    /**
     * 集合不为空时抛出异常
     *
     * @param collection 集合
     * @param message    错误消息
     */
    public static void throwIfNotEmpty(Collection<?> collection, String message) {
        throwIf(CollUtil.isNotEmpty(collection), () -> new QyBizException(message));
    }

    /**
     * 集合不为空时抛出异常
     *
     * @param collection        集合
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfNotEmpty(Collection<?> collection, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(CollUtil.isNotEmpty(collection), exceptionSupplier);
    }

    /**
     * 集合不为空时抛出异常
     *
     * @param collection 集合
     * @param status     状态码
     */
    public static void throwIfNotEmpty(Collection<?> collection, IStatusCode status) {
        throwIf(CollUtil.isNotEmpty(collection), () -> new QyBizException(status));
    }

    /**
     * 集合不为空时抛出异常
     *
     * @param collection 集合
     * @param status     状态码
     * @param params     参数
     */
    public static void throwIfNotEmpty(Collection<?> collection, IStatusCode status, Object... params) {
        throwIf(CollUtil.isNotEmpty(collection), () -> new QyBizException(status.getCode(), StrUtil.format(status.getDesc(), params)));
    }

    // ===================== 相等性检查断言 =====================

    /**
     * 对象相等时抛出异常
     *
     * @param obj1    对象1
     * @param obj2    对象2
     * @param message 错误消息
     */
    public static void throwIfEquals(Object obj1, Object obj2, String message) {
        throwIf(Objects.equals(obj1, obj2), () -> new QyBizException(message));
    }

    /**
     * 对象相等时抛出异常
     *
     * @param obj1              对象1
     * @param obj2              对象2
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfEquals(Object obj1, Object obj2, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(Objects.equals(obj1, obj2), exceptionSupplier);
    }

    /**
     * 对象相等时抛出异常
     *
     * @param obj1   对象1
     * @param obj2   对象2
     * @param status 状态码
     */
    public static void throwIfEquals(Object obj1, Object obj2, IStatusCode status) {
        throwIf(Objects.equals(obj1, obj2), () -> new QyBizException(status));
    }

    /**
     * 对象不相等时抛出异常
     *
     * @param obj1    对象1
     * @param obj2    对象2
     * @param message 错误消息
     */
    public static void throwIfNotEquals(Object obj1, Object obj2, String message) {
        throwIf(!Objects.equals(obj1, obj2), () -> new QyBizException(message));
    }

    /**
     * 对象不相等时抛出异常
     *
     * @param obj1              对象1
     * @param obj2              对象2
     * @param exceptionSupplier 异常提供者
     * @param <T>               异常类型
     * @throws T 抛出的异常
     */
    public static <T extends Throwable> void throwIfNotEquals(Object obj1, Object obj2, Supplier<? extends T> exceptionSupplier) throws T {
        throwIf(!Objects.equals(obj1, obj2), exceptionSupplier);
    }

    /**
     * 对象不相等时抛出异常
     *
     * @param obj1   对象1
     * @param obj2   对象2
     * @param status 状态码
     */
    public static void throwIfNotEquals(Object obj1, Object obj2, IStatusCode status) {
        throwIf(!Objects.equals(obj1, obj2), () -> new QyBizException(status));
    }

    // ===================== 对象验证断言 =====================

    /**
     * 对象验证通过时抛出异常
     *
     * @param obj          要校验的对象
     * @param validator    验证函数
     * @param message      错误消息
     * @param <T>          对象类型
     */
    public static <T> void throwIfValidate(T obj, Function<T, Boolean> validator, String message) {
        throwIf(obj, validator, () -> new QyBizException(message));
    }

    /**
     * 对象验证通过时抛出异常
     *
     * @param obj               要校验的对象
     * @param validator         验证函数
     * @param exceptionSupplier 异常提供者
     * @param <T>               对象类型
     * @param <F>               异常类型
     * @throws F 抛出的异常
     */
    public static <T, F extends Throwable> void throwIfValidate(T obj, Function<T, Boolean> validator, Supplier<? extends F> exceptionSupplier) throws F {
        throwIf(obj, validator, exceptionSupplier);
    }

    /**
     * 对象验证通过时抛出异常
     *
     * @param obj        要校验的对象
     * @param validator  验证函数
     * @param statusCode 状态码
     * @param <T>        对象类型
     */
    public static <T> void throwIfValidate(T obj, Function<T, Boolean> validator, IStatusCode statusCode) {
        throwIf(obj, validator, () -> new QyBizException(statusCode));
    }

    /**
     * 对象验证未通过时抛出异常
     *
     * @param obj       要校验的对象
     * @param validator 验证函数
     * @param message   错误消息
     * @param <T>       对象类型
     */
    public static <T> void throwIfNotValidate(T obj, Function<T, Boolean> validator, String message) {
        throwIf(obj, (t) -> !validator.apply(t), () -> new QyBizException(message));
    }

}