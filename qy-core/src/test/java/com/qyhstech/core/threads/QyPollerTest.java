package com.qyhstech.core.threads;

import com.qyhstech.core.domain.dto.ModelArticle;
import com.qyhstech.core.threads.retry.RetryResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class QyPollerTest {

    @Test
    void poll() {
    }

    @Test
    void testPoll() {
    }

    @Test
    void testPoll1() {
    }

    @Test
    void retry() {
    }

    @Test
    void testRetry() {

        ModelArticle articleVo = QyPoller.retry(currentAttempt -> {
            try {
                ModelArticle tmpArticle = new ModelArticle();
                tmpArticle.setBody("this is a test");

                if (currentAttempt < 2) {
                    return RetryResult.failure();
                }

                return RetryResult.success(tmpArticle);
            } catch (Exception e) {
                return RetryResult.failure();
            }
        });

        System.out.println(articleVo);
    }

    @Test
    void testRetry1() {
    }
}