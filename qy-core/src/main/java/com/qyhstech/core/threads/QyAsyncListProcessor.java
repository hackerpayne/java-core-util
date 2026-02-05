package com.qyhstech.core.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class QyAsyncListProcessor<T, R> {

    // 处理单个元素的函数式接口
    @FunctionalInterface
    public interface AsyncProcessor<T, R> {
        R process(T item) throws Exception;
    }

    /**
     * 异步处理列表并返回结果
     * @param params
     * @param processor
     * @return
     */
    public List<R> processListAsync(List<T> params, AsyncProcessor<T, R> processor) {
        // 使用虚拟线程执行器
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 存储所有Future对象
            List<Future<R>> futures = new ArrayList<>();

            // 提交任务到虚拟线程
            for (T param : params) {
                Future<R> future = executor.submit(() -> processor.process(param));
                futures.add(future);
            }

            // 等待并收集所有结果
            List<R> results = new ArrayList<>();
            for (Future<R> future : futures) {
                try {
                    results.add(future.get());
                } catch (Exception e) {
                    // 处理异常情况
                    throw new RuntimeException("Error processing item", e);
                }
            }

            return results;
        }
    }

    /**
     * 使用Stream API的替代实现
     * @param params
     * @param processor
     * @return
     */
    public List<R> processListAsyncStream(List<T> params, AsyncProcessor<T, R> processor) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return params.stream()
                    .map(param -> executor.submit(() -> processor.process(param)))
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException("Error processing item", e);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }
}