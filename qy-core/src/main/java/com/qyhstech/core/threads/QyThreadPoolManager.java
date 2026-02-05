package com.qyhstech.core.threads;

import cn.hutool.core.thread.NamedThreadFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 每个任务独立的线程池
 */
public class QyThreadPoolManager {
    // 每个任务，都有自己单独的线程池
    private static Map<String, ExecutorService> executors = new ConcurrentHashMap<>();

    /**
     * 初始化一个线程池
     *
     * @param poolName 线程池名称
     * @param poolSize 线程池大小
     * @return
     */
    private static ExecutorService init(String poolName, int poolSize) {

        ThreadFactory namedThreadFactory = new NamedThreadFactory("ppol-" + poolName, false);

        return new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                namedThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 获取线程池
     *
     * @param poolName 线程池名称
     * @param poolSize 线程池大小
     * @return
     */
    public static ExecutorService getOrInitExecutors(String poolName, int poolSize) {
        ExecutorService executorService = executors.get(poolName);
        if (null == executorService) {
            synchronized (QyThreadPoolManager.class) {
                executorService = executors.get(poolName);
                if (null == executorService) {
                    executorService = init(poolName, poolSize);
                    executors.put(poolName, executorService);
                }
            }
        }
        return executorService;
    }

    /**
     * 回收线程资源
     *
     * @param poolName
     */
    public static void releaseExecutors(String poolName) {
        ExecutorService executorService = executors.remove(poolName);
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
