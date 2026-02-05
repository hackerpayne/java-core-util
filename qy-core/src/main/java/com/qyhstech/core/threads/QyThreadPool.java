package com.qyhstech.core.threads;

import cn.hutool.core.thread.NamedThreadFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 自定义某个线程池，在退出程序时自动销毁
 * 也可以使用Bean的方式创建
 */
@Slf4j
public class QyThreadPool {

    /**
     * 线程池
     */
    private ExecutorService executor;

    /**
     * 初始化10个线程
     */
    @PostConstruct
    void init() {

        ThreadFactory namedThreadFactory = new NamedThreadFactory("common-pool--%d", false);

        // 通用线程池
        executor = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 提交新的消费者
     *
     * @param shutdownThread
     */
    public void submitConsumerPool(Runnable shutdownThread) {
        executor.execute(shutdownThread);
    }

    /**
     * 程序关闭,关闭线程池
     */
    @PreDestroy
    void fin() {
        shutdown();
    }

    /**
     * 安全的线程池关闭方法
     */
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }

        try {
            if (executor != null && !executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                log.info("CommonThreadPool退出超时，exiting uncleanly");
            }
        } catch (InterruptedException e) {
            log.info("CommonThreadPool退出中断, exiting uncleanly");
        }
    }


}
