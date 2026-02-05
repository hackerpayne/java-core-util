package com.qyhstech.core.threads;

import cn.hutool.core.collection.CollUtil;
import com.qyhstech.core.collection.QyList;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class QyFuture {

    /**
     * 对列表的数据进行分批处理
     *
     * @param entityList 所有数据列表
     * @param jobFunc    需要执行的任务
     * @param batchSize  批次大小，200一批
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<?> doJobSplitBatchSimple(Collection<T> entityList, Function<List<T>, R> jobFunc, Integer batchSize) {
        if (CollUtil.isEmpty(entityList)) {
            return QyList.empty();
        }

        if (Objects.isNull(jobFunc)) {
            return QyList.empty();
        }
        if (Objects.isNull(batchSize) || batchSize <= 0) {
            batchSize = 200;
        }

        int size = entityList.size();
        int idxLimit = Math.min(batchSize, size);
        int i = 1;
        //保存单批提交的数据集合
        List<T> oneBatchList = QyList.empty();
        List<R> listResults = QyList.empty();
        for (Iterator<T> var7 = entityList.iterator(); var7.hasNext(); ++i) {
            T element = var7.next();
            oneBatchList.add(element);
            if (i == idxLimit) {

                // 对一批的数据进行处理
                listResults.add(jobFunc.apply(oneBatchList));

                //每次提交后需要清空集合数据
                oneBatchList.clear();

                idxLimit = Math.min(idxLimit + batchSize, size);
            }
        }
        if (CollUtil.isNotEmpty(oneBatchList)) {
            listResults.add(jobFunc.apply(oneBatchList));
        }

        return listResults;
    }

    /**
     * 拆成指定数量为一批，进行并行任务处理
     *
     * @param executorService 线程池
     * @param listSource      要处理的数据列表
     * @param consumer        要处理的类
     * @param batchSize       批次大小
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> doJobSplitBatch(ExecutorService executorService, List<T> listSource, Function<List<T>, List<R>> consumer, Integer batchSize) {

        List<R> resultList = QyList.empty();
        if (CollUtil.isEmpty(listSource)) {
            return resultList;
        }

        // 创建CompletableFuture列表，用于存储每个查询的结果
        List<CompletableFuture<List<R>>> listFutures = QyList.empty();

        // 每个CompletableFuture负责查询一部分数据
        for (int i = 0; i < listSource.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, listSource.size());
            List<T> subList = listSource.subList(i, endIndex);
            listFutures.add(CompletableFuture.supplyAsync(() -> consumer.apply(subList), executorService));
        }

        // 使用CompletableFuture的allOf方法等待所有查询完成
        CompletableFuture.allOf(listFutures.toArray(new CompletableFuture[0])).join();

        // 当所有查询完成时，对所有结果进行合并
        listFutures.stream().map(QyFuture::futureGet).forEach(resultList::addAll);
        return resultList;
    }

    /**
     * 批量处理任务
     *
     * @param listSource
     * @param consumer
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> doJobList(List<T> listSource, Function<T, R> consumer) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<R> listResults = doJobList(executorService, listSource, consumer, false);
        executorService.shutdown();
        return listResults;
    }

    /**
     * @param executorService
     * @param listSource
     * @param consumer
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> doJobList(ExecutorService executorService, List<T> listSource, Function<T, R> consumer) {
        return doJobList(executorService, listSource, consumer, false);
    }

    /**
     * 对List的数据进行处理，并且最后依然返回List，不过可以是另外的数据
     *
     * @param executorService
     * @param listSource      要处理的源数据
     * @param consumer        要处理的方法
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> doJobList(ExecutorService executorService, List<T> listSource, Function<T, R> consumer, boolean parallel) {
        List<R> resultList = QyList.empty();
        if (CollUtil.isEmpty(listSource)) {
            return resultList;
        }

        // 转为Future
        List<CompletableFuture<R>> listFutures = listSource.stream()
                .map(task -> CompletableFuture.supplyAsync(() -> consumer.apply(task), executorService))
                .toList();

        // 等待完成
        CompletableFuture.allOf(listFutures.toArray(new CompletableFuture[0])).join();

        // 合并结果
        if (parallel) {
            listFutures.parallelStream().map(QyFuture::futureGet).forEach(resultList::add);
        } else {
            listFutures.stream().map(QyFuture::futureGet).forEach(resultList::add);
        }
        return resultList;
    }

    /**
     * 执行任务
     *
     * @param executorService ExecutorService
     * @param callable        回调
     * @param <T>             返回的结果集Future泛型
     * @return Future泛型
     */
    public static <T> Future<T> doJob(ExecutorService executorService, Callable<T> callable) {
        return executorService.submit(callable);
    }

    /**
     * 获取结果集，执行时会阻塞直到有结果，中间的异常不会被静默
     *
     * @param future Future
     * @param <T>    返回的结果集泛型
     * @return T
     */
    public static <T> T futureGet(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ConcurrentUtil Exeception ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * CompletableFuture获取结果集，执行时会阻塞直到有结果，中间的异常不会被静默
     *
     * @param future
     * @param <T>
     * @return
     */
    public static <T> T futureGet(CompletableFuture<T> future) {
        return futureGet(future, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取结果集，执行时会阻塞直到有结果，中间的异常不会被静默
     *
     * @param future
     * @param <T>
     * @return
     */
    public static <T> T futureGet(CompletableFuture<T> future, long timeout, TimeUnit timeUnit) {
        try {
            if (timeout > 0) {
                return future.get(timeout, timeUnit);
            }
            return future.get();
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            log.error("ConcurrentUtil Exeception ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 组合多个CompletableFuture为一个CompletableFuture, 所有子任务全部完成，组合后的任务才会完成。带返回值，可直接get.
     *
     * @param futures
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<List<T>> futureGet(List<CompletableFuture<T>> futures) {
        //2.流式（总任务完成后，每个子任务join取结果，后转换为list）
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    /**
     * Stream流式类型futures转换成一个CompletableFuture, 所有子任务全部完成，组合后的任务才会完成。带返回值，可直接get.
     *
     * @param futures
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<List<T>> futureGet(Stream<CompletableFuture<T>> futures) {
        List<CompletableFuture<T>> futureList = futures.filter(Objects::nonNull).collect(Collectors.toList());
        return futureGet(futureList);
    }

    /**
     * 等待所有任务完成并返回
     *
     * @param futures
     * @param <T>
     * @return
     */
    public static <T> List<T> futureGetResult(List<CompletableFuture<T>> futures) {
        try {
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()))
                    .join();
        } catch (Exception e) {
            throw new RuntimeException("futureGet failed", e);
        }
    }


}
