package com.qyhstech.core.threads;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 重新包装ThreadPoolTaskExecutor为TTL
 *
 * @see <a href="https://github.com/alibaba/transmittable-thread-local/issues/173">参考</a>
 */
@SuppressWarnings("all")
public class TtlThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    /**
     * 错误提示语
     */
    private static final String ERROR_MESSAGE = "task不能为空";

    @Override
    public void execute(Runnable task) {
        super.execute(Objects.requireNonNull(TtlRunnable.get(task), ERROR_MESSAGE));
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        super.execute(Objects.requireNonNull(TtlRunnable.get(task), ERROR_MESSAGE), startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(Objects.requireNonNull(TtlRunnable.get(task), ERROR_MESSAGE));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(Objects.requireNonNull(TtlCallable.get(task), ERROR_MESSAGE));
    }

//    @Override
//    public ListenableFuture<?> submitListenable(Runnable task) {
//        return super.submitListenable(Objects.requireNonNull(TtlRunnable.get(task), ERROR_MESSAGE));
//    }
//
//    @Override
//    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
//        return super.submitListenable(Objects.requireNonNull(TtlCallable.get(task), ERROR_MESSAGE));
//    }
}
