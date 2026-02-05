package com.qyhstech.core.threads;

import com.qyhstech.core.dates.QyDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;


@Slf4j
class QyFutureFactoryTest {

//    @BeforeClass
//    public static void setupLogger() {
//        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY,"logback.xml");
//    }

    QyFutureFactory futureFactory = new QyFutureFactory();
    QyFutureFactory futureFactory2 = new QyFutureFactory(Executors.newFixedThreadPool(10));

    @Test
    void addVoidTask() {
        // 无返回值的任务
        IntStream.rangeClosed(1, 100).forEach(pos -> {
            futureFactory2.addVoidTask(() -> {
                log.info("test,{}", QyDate.nowTime());
            });
        });
        futureFactory2.doVoidTasks();
    }

    @Test
    void doVoidTasks() {
    }

    @Test
    void addSupplyTask() {

        // 带返回值的任务
        IntStream.rangeClosed(1, 10).forEach(pos -> {
            futureFactory.addSupplyTask(() -> {
                log.info("test,{}", QyDate.nowTime());
                return QyDate.nowTime();
            });
        });
        List<String> listResult = futureFactory.doSupplyTasks();
        listResult.forEach(System.out::println);
    }

    @Test
    void doSupplyTasks() {
    }
}