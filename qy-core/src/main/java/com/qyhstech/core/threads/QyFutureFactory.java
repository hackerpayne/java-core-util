package com.qyhstech.core.threads;

import cn.hutool.core.collection.CollUtil;
import com.qyhstech.core.collection.QyList;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

@Slf4j
public class QyFutureFactory {

    /**
     * 存储消费型任务
     */
    private final List<CompletableFuture<Void>> consumerTaskList = new CopyOnWriteArrayList<>();
    /**
     * 存储供给型任务
     */
    private final List<CompletableFuture<Object>> supplyTaskList = new CopyOnWriteArrayList<>();

    private ExecutorService executors = null;

    public QyFutureFactory() {
    }

    public QyFutureFactory(ExecutorService executors) {
        this.executors = executors;
    }

    /**
     * 添加消费型任务
     *
     * @param task
     */
    public void addVoidTask(IExecutorVoidTask task) {
        if (Objects.nonNull(this.executors)) {
            consumerTaskList.add(CompletableFuture.runAsync(task::doRun, this.executors));
        } else {
            consumerTaskList.add(CompletableFuture.runAsync(task::doRun));
        }
    }

    /**
     * 执行消费型任务
     */
    @SuppressWarnings("unchecked")
    public void doVoidTasks() {
        if (CollUtil.isNotEmpty(consumerTaskList)) {
            CompletableFuture<Void>[] completableFutures = consumerTaskList.toArray(new CompletableFuture[0]);
            /// 全部并行执行结束时结束任务
            CompletableFuture.allOf(completableFutures).join();
        }
    }

    /**
     * 添加供给型任务
     *
     * @param task
     */
    public void addSupplyTask(IExecutorSupplyTask task) {
        if (Objects.nonNull(this.executors)) {
            supplyTaskList.add(CompletableFuture.supplyAsync(task::doRun, this.executors));
        } else {
            supplyTaskList.add(CompletableFuture.supplyAsync(task::doRun));
        }
    }

    /**
     * 执行供给型任务
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> doSupplyTasks() {
        List<T> list = QyList.empty();
        if (CollUtil.isNotEmpty(supplyTaskList)) {
            CompletableFuture<T>[] completableFutures = supplyTaskList.toArray(new CompletableFuture[0]);
            /// 全部并行执行结束时结束任务
            CompletableFuture.allOf(completableFutures).join();

            Arrays.stream(completableFutures).forEach(c -> {
                try {
                    T o = c.get();
                    list.add(o);
                } catch (Exception ex) {
                    log.error("doSupplyTasks发生异常", ex);
                }
            });
        }
        return list;
    }

    @FunctionalInterface
    public interface IExecutorVoidTask {
        /**
         * 执行消费任务
         */
        void doRun();
    }

    @FunctionalInterface
    public interface IExecutorSupplyTask {
        /**
         * 执行供给任务
         *
         * @return
         */
        Object doRun();
    }

}
