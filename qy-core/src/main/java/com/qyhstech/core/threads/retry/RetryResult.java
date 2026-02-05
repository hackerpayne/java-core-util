package com.qyhstech.core.threads.retry;


import lombok.Data;

/**
 * 重试结果包装类
 *
 * @param <T> 结果数据类型
 */

@Data
public class RetryResult<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 数据内容
     */
    private T data;

    private RetryResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     * 创建成功结果
     *
     * @param data 结果数据
     * @return 成功的 RetryResult
     */
    public static <T> RetryResult<T> success(T data) {
        return new RetryResult<>(true, data);
    }

    /**
     * 创建失败结果，需要重试
     *
     * @return 失败的 RetryResult
     */
    public static <T> RetryResult<T> failure() {
        return new RetryResult<>(false, null);
    }

    /**
     * 是否成功
     *
     * @return true 表示成功，false 表示需要重试
     */
    public boolean isSuccess() {
        return success;
    }

}