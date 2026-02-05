package com.qyhstech.core.threads;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class LatchUtilsTest {

    @Test
    void testBasicFunctionality() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // 使用新的实例化模式
        LatchUtils latch = new LatchUtils();

        System.out.println("主流程开始，准备分发异步任务...");
        System.out.println("实例ID: " + latch.getInstanceId());

        // 注册异步任务
        latch.submitTask(executorService, () -> {
            System.out.println("开始获取用户信息...");
            ThreadUtil.sleep(1000);
            System.out.println("获取用户信息成功！");
        });

        latch.submitTask(executorService, () -> {
            System.out.println("开始获取订单信息...");
            ThreadUtil.sleep(1500);
            System.out.println("获取订单信息成功！");
        });

        latch.submitTask(executorService, () -> {
            System.out.println("开始获取商品信息...");
            ThreadUtil.sleep(500);
            System.out.println("获取商品信息成功！");
        });

        assertEquals(3, latch.getTaskCount(), "任务数量应该为3");
        assertFalse(latch.isExecuted(), "执行前状态应该为false");

        System.out.println("所有异步任务已提交，主线程开始等待...");

        boolean allDone = latch.waitFor(5, TimeUnit.SECONDS);

        assertTrue(allDone, "所有任务应该完成");
        assertTrue(latch.isExecuted(), "执行后状态应该为true");

        System.out.println("所有任务执行完成，主流程继续...");
        executorService.shutdown();
    }

    @Test
    void testConcurrentInstances() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int threadCount = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completeLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        // 多线程并发使用不同的 LatchUtils 实例
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    // 等待所有线程就绪
                    startLatch.await();

                    // 每个线程使用独立的 LatchUtils 实例
                    LatchUtils latch = new LatchUtils();

                    // 提交任务
                    latch.submitTask(executorService, () -> {
                        ThreadUtil.sleep(100);
                        System.out.println("线程 " + threadId + " 任务完成");
                    });

                    latch.submitTask(executorService, () -> {
                        ThreadUtil.sleep(50);
                        System.out.println("线程 " + threadId + " 任务2完成");
                    });

                    // 等待完成
                    boolean success = latch.waitFor(2, TimeUnit.SECONDS);
                    if (success) {
                        successCount.incrementAndGet();
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completeLatch.countDown();
                }
            }).start();
        }

        // 启动所有线程
        startLatch.countDown();

        // 等待所有线程完成
        assertTrue(completeLatch.await(5, TimeUnit.SECONDS), "所有线程应该在超时前完成");
        assertEquals(threadCount, successCount.get(), "所有线程都应该成功完成");

        System.out.println("并发测试完成，成功线程数: " + successCount.get());
        executorService.shutdown();
    }

    @Test
    void testExceptionHandling() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        LatchUtils latch = new LatchUtils();

        // 提交正常任务
        latch.submitTask(executorService, () -> {
            ThreadUtil.sleep(100);
            System.out.println("正常任务完成");
        });

        // 提交异常任务
        latch.submitTask(executorService, () -> {
            throw new RuntimeException("测试异常");
        });

        latch.submitTask(executorService, () -> {
            ThreadUtil.sleep(50);
            System.out.println("另一个正常任务完成");
        });

        // 即使有异常，也应该返回true（任务执行完成）
        boolean allDone = latch.waitFor(2, TimeUnit.SECONDS);
        assertTrue(allDone, "即使有任务异常，其他任务也应该完成");

        executorService.shutdown();
    }

    @Test
    void testReusePrevention() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        LatchUtils latch = new LatchUtils();

        latch.submitTask(executorService, () -> System.out.println("任务完成"));

        // 第一次执行应该成功
        assertTrue(latch.waitFor(1, TimeUnit.SECONDS), "第一次执行应该成功");

        // 第二次提交任务应该抛出异常
        assertThrows(IllegalStateException.class, () -> {
            latch.submitTask(executorService, () -> System.out.println("不应该执行"));
        }, "已执行的实例不能提交新任务");

        // 第二次等待应该抛出异常
        assertThrows(IllegalStateException.class, () -> {
            latch.waitFor(1, TimeUnit.SECONDS);
        }, "已执行的实例不能重复等待");

        executorService.shutdown();
    }

    @Test
    void testNullValidation() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        LatchUtils latch = new LatchUtils();

        // 测试null executor
        assertThrows(IllegalArgumentException.class, () -> {
            latch.submitTask(null, () -> System.out.println("测试"));
        }, "null executor应该抛出异常");

        // 测试null runnable
        assertThrows(IllegalArgumentException.class, () -> {
            latch.submitTask(executorService, null);
        }, "null runnable应该抛出异常");

        executorService.shutdown();
    }

    @Test
    void testEmptyTaskList() {
        LatchUtils latch = new LatchUtils();

        assertEquals(0, latch.getTaskCount(), "空任务列表应该返回0");

        // 空任务列表应该立即返回true
        boolean result = latch.waitFor(1, TimeUnit.SECONDS);
        assertTrue(result, "空任务列表应该立即返回true");
        assertTrue(latch.isExecuted(), "即使没有任务，状态也应该标记为已执行");
    }
}