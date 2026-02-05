package com.qyhstech.core.threads;

import com.qyhstech.core.upload.QyUploadFileDto;
import org.junit.jupiter.api.Test;

class QyRandCallerTest {

    @Test
    void addMethod() {
    }

    private QyUploadFileDto test1(String url) {
        QyUploadFileDto test = new QyUploadFileDto();
        test.setSourceUrl("执行 testA 参数: " + url);
        return test;
    }

    private QyUploadFileDto test2(String url) {
        QyUploadFileDto test = new QyUploadFileDto();
        test.setSourceUrl("执行 testB 参数: " + url);
        return test;
    }

    private QyUploadFileDto test3(String url) {
        QyUploadFileDto test = new QyUploadFileDto();
        test.setSourceUrl("执行 testC 参数: " + url);
        return test;
    }

    @Test
    void getRandomResult() {
        QyRandCaller<String, QyUploadFileDto> caller = new QyRandCaller<>();

        caller.addMethod(this::test1);
        caller.addMethod(this::test2);
        caller.addMethod(this::test3);

        // 顺序随机
        QyUploadFileDto result1 = caller.getRandomResult("参数123", r -> r != null && !r.getSourceUrl().isEmpty());
        System.out.println("顺序随机结果: " + result1);

    }

    @Test
    void getRandomResultConcurrent() throws Exception {
        QyRandCaller<String, String> caller = new QyRandCaller<>();

        caller.addMethod(p -> {
            System.out.println("执行 testA 参数: " + p);
            return "A";
        });
        caller.addMethod(p -> {
            System.out.println("执行 testB 参数: " + p);
            return "B";
        });
        caller.addMethod(p -> {
            System.out.println("执行 testC 参数: " + p);
            return "C: " + p;
        });

        // 并发最快
        String result2 = caller.getRandomResultConcurrent("参数456", r -> r != null && !r.isEmpty());
        System.out.println("并发最快结果: " + result2);
    }
}