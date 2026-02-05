package com.qyhstech.core.threads;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动统计费时
 * 使用方法：
 * try (QcPerfTracker.TimerContext ignored = QcPerfTracker.start()) {
 *         return userMapper.selectList(wrapper);
 *     }
 */
@Slf4j
public class QcPerfTracker {
    private final long startTime;
    private final String methodName;

    private QcPerfTracker(String methodName) {
        this.startTime = System.currentTimeMillis();
        this.methodName = methodName;
    }

    public static TimerContext start() {
        return new TimerContext(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    public static class TimerContext implements AutoCloseable {
        private final QcPerfTracker tracker;

        private TimerContext(String methodName) {
            this.tracker = new QcPerfTracker(methodName);
        }

        @Override
        public void close() {
            long executeTime = System.currentTimeMillis() - tracker.startTime;
            if (executeTime > 500) {
                log.warn("慢查询告警：方法 {} 耗时 {}ms", tracker.methodName, executeTime);
            }
        }
    }
}
