package com.qyhstech.core.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 多个方法，随机选一个执行，并返任何一个结果返回。
 * 场景：3个下载图片的平台，一个一个尝试，失败用下一个，直到有结果返回
 *
 * @param <P>
 * @param <T>
 */
public class QyRandCaller<P, T> {

    private final List<Function<P, T>> methods = new ArrayList<>();
    private final Random random = new Random();

    /**
     * 添加方法
     *
     * @param method
     */
    public void addMethod(Function<P, T> method) {
        methods.add(method);
    }

    /**
     * 如果没有结果校验器，默认判断为Null即可
     *
     * @param param
     * @return
     */
    public T getRandomResult(P param) {
        return getRandomResult(param, null);
    }

    /**
     * 顺序随机模式（一个个尝试直到有结果）
     *
     * @param param     入参
     * @param validator 结果校验
     * @return
     */
    public T getRandomResult(P param, Predicate<T> validator) {
        Predicate<T> effectiveValidator = (validator != null) ? validator : (Objects::nonNull);

        List<Function<P, T>> candidates = new ArrayList<>(methods);
        while (!candidates.isEmpty()) {
            int index = random.nextInt(candidates.size());
            Function<P, T> method = candidates.remove(index);
            T result = method.apply(param);
            if (effectiveValidator.test(result)) {
                return result;
            }
        }
        return null;
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public T getRandomResultConcurrent(P param) throws Exception {
        return getRandomResultConcurrent(param, null);
    }

    /**
     * 并发模式（所有任务一起执行，取最快有效结果）
     *
     * @param param
     * @param validator
     * @return
     * @throws Exception
     */
    public T getRandomResultConcurrent(P param, Predicate<T> validator) throws Exception {
        Predicate<T> effectiveValidator = (validator != null) ? validator : (Objects::nonNull);

        ExecutorService executor = Executors.newFixedThreadPool(methods.size());
        try {
            List<Callable<T>> tasks = new ArrayList<>();
            for (Function<P, T> method : methods) {
                tasks.add(() -> {
                    T result = method.apply(param);
                    return effectiveValidator.test(result) ? result : null;
                });
            }
            return executor.invokeAny(tasks); // 返回最快的非 null 结果
        } finally {
            executor.shutdownNow();
        }
    }

}
