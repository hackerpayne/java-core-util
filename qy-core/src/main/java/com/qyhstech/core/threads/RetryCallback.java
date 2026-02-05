package com.qyhstech.core.threads;

import com.qyhstech.core.threads.retry.RetryResult;

@FunctionalInterface
public interface RetryCallback<T> {
    /**
     * 执行重试逻辑
     *
     * @param currentAttempt 当前重试次数，从 0 开始（0 表示第一次执行，1 表示第一次重试，以此类推）
     * @return 返回 RetryResult 对象，包含是否成功和结果数据
     * @throws Exception 执行过程中可能抛出的异常
     */
    RetryResult<T> execute(int currentAttempt) throws Exception;

}