package com.qyhstech.core.threads;

import org.junit.jupiter.api.Test;

import java.util.List;

class QyAsyncListProcessorTest {

    @Test
    void processListAsync() {

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        QyAsyncListProcessor<Integer, String> processor = new QyAsyncListProcessor<>();

        // 定义处理逻辑
        QyAsyncListProcessor.AsyncProcessor<Integer, String> task = num -> {
            Thread.sleep(100); // 模拟耗时操作
            return "Processed: " + num;
        };

        // 使用普通循环方式
        List<String> results1 = processor.processListAsync(numbers, task);
        System.out.println("Results 1: " + results1);

        // 使用Stream方式
        List<String> results2 = processor.processListAsyncStream(numbers, task);
        System.out.println("Results 2: " + results2);
    }

    @Test
    void processListAsyncStream() {
    }
}