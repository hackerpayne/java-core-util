package com.qyhstech.core.number;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单号生成工具
 */
public class QyOrderGenerator {

    /**
     * 时间数值
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 序列号
     */
    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    /**
     * 序列号生成器，格式为：年月日时分秒+序列号，默认最大支持每秒9999单
     *
     * @return
     */
    public static synchronized String generate() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        // 序列号每日重置，最大支持每秒9999单
        long seq = SEQUENCE.incrementAndGet() % 10000;
        return String.format("%s%04d", timestamp, seq);
    }

    /**
     * 生成随机数，格式为：年月日时分秒+6位随机数
     *
     * @return
     */
    public static String generateRand() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        // 添加6位随机数（可重复时）
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return timestamp + random;
    }

}
