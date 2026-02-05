package com.qyhstech.spring.configuration;

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ExecutorService已经被ttl处理过了，可以直接注入到系统中使用
 *
 */
@RequiredArgsConstructor
@Slf4j
public class QyThreadPoolConfiguration {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Bean
    public ExecutorService ttlThreadPool() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】ExecutorService包装为TTL服务 >>>>>>>>>>>>>>>>>>");
        ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
        return TtlExecutors.getTtlExecutorService(threadPoolExecutor);
    }
}
