package com.qyhstech.spring.restclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 返回的响应流只能读取一次，所以需要重新封装一个类，实现ClientHttpResponse接口，重写getBody方法，返回一个InputStream，
 */
public class RepeatReadClientHttpRequestWrapper implements ClientHttpResponse {

    private ClientHttpResponse response;

    private byte[] bodyData = null;

    public RepeatReadClientHttpRequestWrapper(ClientHttpResponse response) throws IOException {
        this.response = response;
        // 立即读取并缓存响应体数据
        this.bodyData = response.getBody().readAllBytes();
    }

    @Override
    public HttpStatusCode getStatusCode() throws IOException {
        return response.getStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return response.getStatusText();
    }

    @Override
    public void close() {
        response.close();
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(bodyData);
    }

    @Override
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }

    /**
     * 获取响应体字符串
     */
    public String getBodyAsString() {
        if (bodyData == null || bodyData.length == 0) {
            return "";
        }
        return new String(bodyData, StandardCharsets.UTF_8);
    }
}