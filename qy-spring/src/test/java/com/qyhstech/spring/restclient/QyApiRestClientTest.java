package com.qyhstech.spring.restclient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QyApiRestClientTest {

    @Test
    void configureDefaults() {
    }

    @Test
    void customizeBuilder() {
    }

    @Test
    void getData() {
        QyApiRestClient client = new QyApiRestClient();
        String html = client.getData("https://docs.spring.io/spring-boot/api/java/index.html", String.class);
        System.out.println(html);
    }

    @Test
    void postData() {
    }
}