package com.qyhstech.core.threads;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.threads.retry.RetryResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 轮询工具类，方便进行任务轮询
 */
@Slf4j
public class QyPoller {

    /**
     * 默认最大等待时间
     */
    private static final Integer DEFAULT_POLL_MAX_WAIT = 60;

    /**
     * 默认最小轮询间隔
     */
    private static final Integer DEFAULT_POLL_INTERVAL = 3;

    /**
     * 使用默认配置调用
     *
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> T poll(PollCallback<T> callback) {
        return poll(DEFAULT_POLL_MAX_WAIT, DEFAULT_POLL_INTERVAL, "", callback);
    }

    /**
     *
     * @param maxWaitSeconds
     * @param intervalSeconds
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> T poll(Integer maxWaitSeconds, Integer intervalSeconds, PollCallback<T> callback) {
        return poll(maxWaitSeconds, intervalSeconds, "", callback);
    }

    /**
     * 通用轮询方法
     *
     * @param maxWaitSeconds  最长等待时间（秒）
     * @param intervalSeconds 轮询间隔（秒）
     * @param taskName        日志中展示的任务名称
     * @param callback        每次轮询时执行的检查逻辑，返回非 null 即表示成功
     * @return 轮询结果，超时则返回 null
     */
    public static <T> T poll(Integer maxWaitSeconds, Integer intervalSeconds, String taskName, PollCallback<T> callback) {
        long startTime = System.currentTimeMillis();
        maxWaitSeconds = Objects.nonNull(maxWaitSeconds) ? maxWaitSeconds : 3;
        intervalSeconds = Objects.nonNull(intervalSeconds) ? intervalSeconds : 1;
        taskName = StrUtil.isNotEmpty(taskName) ? taskName : "未命名任务";
        long maxWaitMillis = maxWaitSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < maxWaitMillis) {
            long waited = (System.currentTimeMillis() - startTime) / 1000;
            log.info("[{}] 等待中... ({}/{}秒)", taskName, waited, maxWaitSeconds);

            try {
                // 让业务方决定：如果有结果就返回，没有结果就返回 null
                T result = callback.check();
                if (result != null) {
                    log.info("[{}] 轮询成功，返回结果", taskName);
                    return result;
                }
            } catch (Exception e) {
                log.error("[{}] 轮询异常", taskName, e);
                // 看需求决定要不要直接 break / throw，这里选择继续重试
            }

            // 间隔等待
            ThreadUtil.sleep(intervalSeconds * 1000L);
        }

        log.warn("[{}] 轮询超时", taskName);
        return null;
    }

    /**
     * 默认最大重试次数
     */
    private static final Integer DEFAULT_RETRY_MAX_TIMES = 3;

    /**
     * 默认重试间隔
     */
    private static final Integer DEFAULT_RETRY_INTERVAL = 1;

    /**
     * 使用默认配置进行重试
     *
     * @param callback 重试回调，接收当前重试次数参数，返回 RetryResult 包含成功状态和结果数据
     * @param <T>      结果数据类型
     * @return 重试结果，成功时返回数据，失败时返回 null
     */
    public static <T> T retry(RetryCallback<T> callback) {
        return retry(DEFAULT_RETRY_MAX_TIMES, DEFAULT_RETRY_INTERVAL, "", callback);
    }

    /**
     * 指定重试次数和间隔进行重试
     *
     * @param maxTimes        最大重试次数
     * @param intervalSeconds 重试间隔（秒）
     * @param callback        重试回调，接收当前重试次数参数，返回 RetryResult 包含成功状态和结果数据
     * @param <T>             结果数据类型
     * @return 重试结果，成功时返回数据，失败时返回 null
     */
    public static <T> T retry(Integer maxTimes, Integer intervalSeconds, RetryCallback<T> callback) {
        return retry(maxTimes, intervalSeconds, "", callback);
    }

    /**
     * 通用重试方法
     *
     * @param maxTimes        最大重试次数
     * @param intervalSeconds 重试间隔（秒）
     * @param taskName        日志中展示的任务名称
     * @param callback        每次重试时执行的逻辑，接收当前重试次数参数（0表示第一次执行），返回 RetryResult 包含成功状态和结果数据
     * @param <T>             结果数据类型
     * @return 重试结果，成功时返回数据，失败时返回 null
     */
    public static <T> T retry(Integer maxTimes, Integer intervalSeconds, String taskName, RetryCallback<T> callback) {
        maxTimes = Objects.nonNull(maxTimes) ? maxTimes : 3;
        intervalSeconds = Objects.nonNull(intervalSeconds) ? intervalSeconds : 1;
        taskName = StrUtil.isNotEmpty(taskName) ? taskName : "常规任务";

        log.info("[{}] 开始重试，最大重试次数: {}", taskName, maxTimes);

        for (int currentAttempt = 0; currentAttempt <= maxTimes; currentAttempt++) {
            try {
                if (currentAttempt > 0) {
                    log.info("[{}] 第{}次重试 ({}/{})", taskName, currentAttempt, currentAttempt, maxTimes);
                    // 间隔等待
                    ThreadUtil.sleep(intervalSeconds * 1000L);
                }

                // 执行重试逻辑
                RetryResult<T> result = callback.execute(currentAttempt);
                if (result.isSuccess()) {
                    log.info("[{}] 重试成功，任务完成", taskName);
                    return result.getData();
                }

                if (currentAttempt < maxTimes) {
                    log.warn("[{}] 本次执行失败，准备第{}次重试", taskName, currentAttempt + 1);
                }
            } catch (Exception e) {
                log.error("[{}] 第{}次执行异常", taskName, currentAttempt + 1, e);
                if (currentAttempt >= maxTimes) {
                    log.error("[{}] 重试次数已用尽，任务失败", taskName);
                    return null;
                }
            }
        }

        log.warn("[{}] 重试次数已用尽，任务失败", taskName);
        return null;
    }

}
