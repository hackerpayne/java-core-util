package com.qyhstech.core.threads;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QyRetryTest {

    @Test
    void retry() {

        try {
            // 调用需要重试的方法，最多重试3次
            String result = QyRetry.retry(3, () -> {
                // 模拟操作，随机抛出异常
                if (Math.random() < 0.5) {
                    ThreadUtil.sleep(500);
                    throw new Exception("操作失败，重试中...");
                }
                return "操作成功";

            });
            System.out.println("成功获得结果: " + result);
        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
        }

    }


    @Test
    void retry2() {

        try {
            // 调用需要重试的方法，最多重试3次
            String result = QyRetry.retry(3, () -> {
                // 模拟操作，随机抛出异常
                if (Math.random() < 0.5) {
                    ThreadUtil.sleep(500);
                    throw new Exception("操作失败，重试中...");
                }
                return "操作成功";

            });
            System.out.println("成功获得结果: " + result);
        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
        }

    }

}