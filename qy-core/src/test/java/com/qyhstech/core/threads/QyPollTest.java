package com.qyhstech.core.threads;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
class QyPollTest {

    @Test
    void poll() {

        String taskId = UUID.fastUUID().toString();
        AtomicReference<Integer> count = new AtomicReference<>(1);
        String result = QyPoller.poll(30, 1, "视频轮询-" + taskId, () -> {
                    count.updateAndGet(v -> v + 1);
                    if (count.get() > 10) {
                        return "success";
                    }
                    // 还没准备好，继续轮询
                    return null;
                }
        );

        System.out.println(result);
    }

    @Test
    void testPoll() {
    }

    @Test
    void testPoll1() {
    }
}