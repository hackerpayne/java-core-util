package com.qyhstech.spring.restclient;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

/**
 * API客户端实现示例
 */
@Slf4j
public class QyApiRestClient extends QyBaseRestClient {

    public QyApiRestClient() {
        super();
    }

    /**
     * @param baseFolder 文件保存目录
     * @param authToken  AuthToken头
     * @param baseUrl    BaseUrl下载和请求的基准URL
     */
    public QyApiRestClient(String baseFolder, String authToken, String baseUrl) {
        super(baseFolder, authToken, baseUrl);
    }

    @Override
    protected void configureDefaults(RestClient.Builder builder) {
        super.configureDefaults(builder);
        // 添加API特定的默认配置
        builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        builder.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    @Override
    protected void configureAuth(RestClient.Builder builder) {
        super.configureDefaults(builder);

        if (StrUtil.isNotEmpty(this.authToken)) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.authToken);
        }
    }


    @Override
    protected void customizeBuilder(RestClient.Builder builder) {
        super.customizeBuilder(builder);
        // 可以添加API特定的拦截器或配置
    }

    /**
     * Get请求
     *
     * @param endpoint
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T getData(String endpoint, Class<T> responseType) {
        ResponseEntity<T> response = doGet(endpoint, responseType);
        return response.getBody();
    }

    /**
     * Post请求
     *
     * @param endpoint
     * @param data
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T postData(String endpoint, Object data, Class<T> responseType) {
        ResponseEntity<T> response = doPost(endpoint, data, responseType);
        return response.getBody();
    }
}