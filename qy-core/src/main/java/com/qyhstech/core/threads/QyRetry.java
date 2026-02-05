package com.qyhstech.core.threads;

import com.qyhstech.core.function.RetryableOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QyRetry {

    /**
     * 重试机制
     * @param retries
     * @param operation
     * @return
     * @param <T>
     * @throws Exception
     */
    public static <T> T retry(int retries, RetryableOperation<T> operation) throws Exception {
        Exception lastException = null;
        for (int i = 0; i < retries; i++) {
            try {
                return operation.execute();
            } catch (Exception e) {
                lastException = e;
                log.info("QyRetry重试任务：第 {} 次重试失败: {}", i + 1, e.getMessage());
            }
        }
        throw lastException; // 如果所有重试都失败，抛出最后一个异常
    }


}
