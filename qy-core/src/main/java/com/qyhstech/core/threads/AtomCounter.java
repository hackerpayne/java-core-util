package com.qyhstech.core.threads;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子计数器
 */
@Slf4j
public class AtomCounter {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 加一个数
     */
    public void increment() {
        atomicInteger.incrementAndGet();
    }

    /**
     * 减一个数
     */
    public void decrement() {
        atomicInteger.decrementAndGet();
    }

    /**
     * 取值
     * @return
     */
    public Integer value() {
        return atomicInteger.get();
    }

}
