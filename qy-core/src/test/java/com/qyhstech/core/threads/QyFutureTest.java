package com.qyhstech.core.threads;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.json.QyJackson;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

class QyFutureTest {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    void doBatchJob() {
        // 构造数据
        List<String> testJobs = QyList.empty();
        IntStream.rangeClosed(1, 22).forEach(page -> testJobs.add("test" + page));

        QyFuture.doJobSplitBatchSimple(testJobs, (job) -> {
            System.out.println("批次数据量为：" + QyJackson.toJsonString(job));
            return null;
        }, 4);
    }

    @Test
    void doJobList() {

        // 构造数据
        List<String> testJobs = QyList.empty();
        IntStream.rangeClosed(1, 20).forEach(page -> testJobs.add("test" + page));

//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        List<Integer> jobResult = QyFuture.doJobList(executorService, testJobs, itemJob -> RandomUtil.randomInt());
        System.out.println(jobResult);
        executorService.shutdown();
    }

    @Test
    void doJobList2() {
        // 构造数据
        List<String> testJobs = QyList.empty();
        IntStream.rangeClosed(1, 20).forEach(page -> testJobs.add("test" + page));

        List<String> jobResult = QyFuture.doJobList(testJobs, item -> {
            ThreadUtil.sleep(1000);
            System.out.println("test:" + item);
            return item;
        });
        System.out.println(jobResult);
    }

    @Test
    void doJobList3() {
        // 构造数据
        List<String> testJobs = QyList.empty();
        IntStream.rangeClosed(1, 100).forEach(page -> testJobs.add("test" + page));

        System.out.println(LocalDateTime.now());
        List<String> jobResult = QyFuture.doJobList(executorService, testJobs, item -> {
            ThreadUtil.sleep(1000);
            System.out.println(LocalDateTime.now() + "test:" + item);
            if (item == "test10") {
                ThreadUtil.sleep(3000);
            }
            return item;
        }, false);
        System.out.println(LocalDateTime.now());
        System.out.println(jobResult);
    }

    @Test
    void completeFutureGet() {
    }

    @Test
    void doJob() {
        Future<String> future = QyFuture.doJob(executorService, () -> "test");
        System.out.println(QyFuture.futureGet(future));
    }

    @Test
    void doJobBatchTest() {
        List<Future<String>> priceFutureList = QyList.empty();
        List<String> productList = QyList.empty();
        for (String product : productList) {
            priceFutureList.add(QyFuture.doJob(executorService, () -> product));
        }
        for (int i = 0; i < productList.size(); i++) {
            //            productList.get(i).set(QyFuture.futureGet(priceFutureList.get(i)));
        }
        System.out.println();
    }

    @Test
    void futureGet() {
    }


}