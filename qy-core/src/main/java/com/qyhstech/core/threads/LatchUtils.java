package com.qyhstech.core.threads;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * LatchUtils - 轻量级异步任务协调工具
 * 核心设计：实例化模式，避免线程安全问题
 * 使用示例：
 *   LatchUtils latch = new LatchUtils();
 *   latch.submitTask(executor, task1);
 *   latch.submitTask(executor, task2);
 *   boolean success = latch.waitFor(5, TimeUnit.SECONDS);
 */
public class LatchUtils {

    private final List<TaskInfo> taskList;
    private final String instanceId;
    private volatile boolean executed;

    /**
     * 构造函数 - 创建新的任务协调实例
     */
    public LatchUtils() {
        this.taskList = new LinkedList<>();
        this.instanceId = UUID.randomUUID().toString().substring(0, 8);
        this.executed = false;
    }

    /**
     * 提交异步任务
     *
     * @param executor 指定线程池执行任务
     * @param runnable 任务逻辑
     * @throws IllegalStateException 如果实例已经执行过
     */
    public void submitTask(Executor executor, Runnable runnable) {
        if (executed) {
            throw new IllegalStateException("LatchUtils实例已经执行，不能重复提交任务。实例ID: " + instanceId);
        }
        if (executor == null) {
            throw new IllegalArgumentException("Executor不能为null");
        }
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable任务不能为null");
        }

        taskList.add(new TaskInfo(executor, runnable));
    }

    /**
     * 获取当前提交的任务数量
     *
     * @return 任务数量
     */
    public int getTaskCount() {
        return taskList.size();
    }

    /**
     * 触发所有任务执行，并同步等待完成
     *
     * @param timeout  最大等待时间
     * @param timeUnit 时间单位
     * @return 是否在超时前全部完成
     * @throws IllegalStateException 如果实例已经执行过
     */
    public boolean waitFor(long timeout, TimeUnit timeUnit) {
        if (executed) {
            throw new IllegalStateException("LatchUtils实例已经执行，不能重复等待。实例ID: " + instanceId);
        }

        if (taskList.isEmpty()) {
            executed = true;
            return true;
        }

        // 标记为已执行，防止重复使用
        executed = true;

        CountDownLatch latch = new CountDownLatch(taskList.size());
        List<Exception> exceptions = new LinkedList<>();

        for (TaskInfo taskInfo : taskList) {
            try {
                taskInfo.executor.execute(() -> {
                    try {
                        taskInfo.runnable.run();
                    } catch (Exception e) {
                        // 记录异常但不影响其他任务
                        synchronized (exceptions) {
                            exceptions.add(e);
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            } catch (Exception e) {
                // 提交任务失败，减少计数并记录异常
                latch.countDown();
                synchronized (exceptions) {
                    exceptions.add(new RuntimeException("任务提交失败: " + e.getMessage(), e));
                }
            }
        }

        try {
            boolean completed = latch.await(timeout, timeUnit);

            // 如果有异常，打印警告日志
            if (!exceptions.isEmpty()) {
                System.err.println("LatchUtils[" + instanceId + "] 执行过程中发生 " +
                    exceptions.size() + " 个异常:");
                exceptions.forEach(e -> e.printStackTrace());
            }

            return completed;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 检查实例是否已执行
     *
     * @return 是否已执行
     */
    public boolean isExecuted() {
        return executed;
    }

    /**
     * 获取实例ID（用于调试）
     *
     * @return 实例ID
     */
    public String getInstanceId() {
        return instanceId;
    }

    // 内部任务封装类 - 使用不可变设计确保线程安全
    private static final class TaskInfo {
        private final Executor executor;
        private final Runnable runnable;

        TaskInfo(Executor executor, Runnable runnable) {
            this.executor = executor;
            this.runnable = runnable;
        }
    }
}