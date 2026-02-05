package com.qyhstech.core.threads;


import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池工具
 */
@Slf4j
public class QyThread extends ThreadUtil {

    /**
     * 创建ThreadExecutor线程池
     *
     * @param prefix       线程名称前缀
     * @param corePoolSize 线程池活跃的线程数
     * @param maxPoolSize  线程池最大活跃的线程数
     * @param queueSize    线程池队列最大线程数
     * @return
     */
    public static ThreadPoolTaskExecutor createThreadExecutor(String prefix, Integer corePoolSize, Integer maxPoolSize, Integer queueSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(corePoolSize);//线程池活跃的线程数
        //executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(maxPoolSize);//线程池最大活跃的线程数
        executor.setThreadNamePrefix(prefix);//线程名称前缀
        executor.setKeepAliveSeconds(30 * 60); // 线程池维护线程所允许的空闲时间
        executor.setAllowCoreThreadTimeOut(true); // 核心线程池也会请0

        //线程池拒绝机制
        //        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.setQueueCapacity(queueSize);  // 线程池队列
        executor.initialize();
        return executor;
    }

    /**
     * 创建线程池
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @return
     */
    public static ThreadPoolExecutor newExecutor(int corePoolSize, int maximumPoolSize) {
        return newExecutor("custom", corePoolSize, maximumPoolSize);
    }

    /**
     * 获得一个新的线程池<br>
     * 如果maximumPoolSize =》 corePoolSize，在没有新任务加入的情况下，多出的线程将最多保留60s
     *
     * @param prefix          线程池名称前缀
     * @param corePoolSize    初始线程池大小
     * @param maximumPoolSize 最大线程池大小
     * @return {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newExecutor(String prefix, int corePoolSize, int maximumPoolSize) {

        ThreadFactory namedThreadFactory = new NamedThreadFactory(prefix + "-thread-%d", false);

        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 获得一个新的线程池<br>
     * 传入阻塞系数，线程池的大小计算公式为：CPU可用核心数 / (1 - 阻塞因子)<br>
     * Blocking Coefficient(阻塞系数) = 阻塞时间／（阻塞时间+使用CPU的时间）<br>
     * 计算密集型任务的阻塞系数为0，而IO密集型任务的阻塞系数则接近于1。
     * <p>
     * see: http://blog.csdn.net/partner4java/article/details/9417663
     *
     * @param blockingCoefficient 阻塞系数，阻塞因子介于0~1之间的数，阻塞因子越大，线程池中的线程数越多。
     * @return {@link ThreadPoolExecutor}
     * @since 3.0.6
     */
    public static ThreadPoolExecutor newExecutorByBlockingCoefficient(float blockingCoefficient) {
        if (blockingCoefficient > 1 || blockingCoefficient <= 0) {
            throw new IllegalArgumentException("[blockingCoefficient] must between 0 and 1, or equals 0.");
        }

        // 最佳的线程数 = CPU可用核心数 / (1 - 阻塞系数)
        int poolSize = (int) (Runtime.getRuntime().availableProcessors() / (1 - blockingCoefficient));

        return new ThreadPoolExecutor(poolSize, poolSize, //
                0L, TimeUnit.MILLISECONDS, //
                new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 停止线程池
     *
     * @param pool
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        shutdownAndAwaitTermination(pool, 120);
    }

    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍人超時，則強制退出.
     * 另对在shutdown时线程本身被调用中断做了处理.
     *
     * @param pool
     * @param timeoutSeconds
     */
    public static boolean shutdownAndAwaitTermination(ExecutorService pool, Integer timeoutSeconds) {
        if (pool == null || timeoutSeconds == null || timeoutSeconds <= 0) {
            return false;
        }

        if (pool.isShutdown()) {
            return true;
        }

        try {
            // 第一步：调用shutdown，停止接收新任务
            pool.shutdown();

            // 第二步：等待已提交任务完成
            if (pool.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                return true;
            }

            // 第三步：如果还没完成，调用shutdownNow强制终止
            log.warn("Pool did not terminate after {} seconds, forcing shutdown...", timeoutSeconds);
            List<Runnable> droppedTasks = pool.shutdownNow();
            if (!droppedTasks.isEmpty()) {
                log.warn("Dropped {} tasks during forced shutdown", droppedTasks.size());
            }

            // 最后一次等待
            if (!pool.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                log.error("Pool did not terminate after forced shutdown and {} seconds wait", timeoutSeconds);
                return false;
            }
            return true;

        } catch (InterruptedException ie) {
            log.warn("Shutdown interrupted", ie);
            List<Runnable> droppedTasks = pool.shutdownNow();
            if (!droppedTasks.isEmpty()) {
                log.warn("Dropped {} tasks due to interruption", droppedTasks.size());
            }
            Thread.currentThread().interrupt();
            return false;
        }
    }


    /**
     * 打印线程异常信息
     *
     * @param r
     * @param t
     */
    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            log.error(t.getMessage(), t);
        }
    }

}
