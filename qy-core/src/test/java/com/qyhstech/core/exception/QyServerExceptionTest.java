package com.qyhstech.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@Slf4j
class QyServerExceptionTest {

    @Test
    void getCode() {
    }

    @Test
    void getMsg() {
    }

    @Test
    void setCode() {
    }

    @Test
    void setMsg() {
    }

    @Test
    void testToString() {
        QyServerException exception = new QyServerException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
        System.out.println("发生异常的明细为：");
        System.out.println(exception);
    }
}