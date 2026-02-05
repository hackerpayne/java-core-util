package com.qyhstech.core.threads;

import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * TTL包装类, 通过此类包装过的线程池均具备ttl能力
 */
public class QyTtl {

    public static ExecutorService wrap(ThreadPoolExecutor pool) {
        return TtlExecutors.getTtlExecutorService(pool);
    }

}
