package com.qyhstech.core.threads;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class QyThreadV {

    /**
     * 虚拟线程池
     */
    private final static ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * 执行Runnable任务
     *
     * @param runnable
     */
    public static void executeVirtual(Runnable runnable) {
        executorService.execute(runnable);
    }

    /**
     * 使用指定线程也消费数据列表
     * 使用信用量严格控制速率
     *
     * @param dataList    数据列表
     * @param consumer    消费者
     * @param totalThread 限制线程数，默认为CPU核数
     * @param <T>
     */
    public static <T> void executeVirtual(List<T> dataList, Consumer<T> consumer, int totalThread) {
        Semaphore semaphore = new Semaphore(totalThread);
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 假设有1000个查询任务
            for (T data : dataList) {
                executor.submit(() -> {
                    try {
                        // 获取许可
                        semaphore.acquire();
                        try {
                            // 执行数据库查询
                            consumer.accept(data);
                        } finally {
                            // 释放许可
                            semaphore.release();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }

    /**
     * 带计数器执行
     *
     * @param runnable
     * @param countDownLatch
     */
    public static void executeVirtual(Runnable runnable, CountDownLatch countDownLatch) {
        executorService.execute(() -> {
            try {
                runnable.run();
            } finally {
                countDownLatch.countDown();
            }
        });
    }

    /**
     * 提交Future任务
     *
     * @param task
     * @param <T>
     * @return
     */
    public static <T> Future<T> submitVirtual(Callable<T> task) {
        return executorService.submit(task);
    }

}
