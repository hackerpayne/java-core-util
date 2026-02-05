package com.qyhstech.spring.restclient;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.junit.jupiter.api.Test;

class QyBaseRestClientTest {

    @Test
    void initRestClientIfNecessary() {
    }

    @Test
    void rebuildRestClient() {
    }

    @Test
    void buildRestClient() {
    }

    @Test
    void configureAuth() {
    }

    @Test
    void configureInterceptors() {
    }

    @Test
    void configureDefaults() {
    }

    @Test
    void customizeBuilder() {
    }

    @Test
    void makeRequest() {
    }

    @Test
    void testMakeRequest() {
    }

    @Test
    void testMakeRequest1() {
    }

    @Test
    void processResponse() {
    }

    @Test
    void doGet() {
    }

    @Test
    void testDoGet() {
    }

    @Test
    void testDoGet1() {
    }

    @Test
    void testDoGet2() {
    }

    @Test
    void testDoGet3() {
    }

    @Test
    void get() {
    }

    @Test
    void testGet() {
    }

    @Test
    void doPost() {
    }

    @Test
    void testDoPost() {
    }

    @Test
    void doPostForm() {
    }

    @Test
    void doPut() {
    }

    @Test
    void doDelete() {
    }

    @Test
    void upload() {
    }

    @Test
    void downloadFile() {
    }

    @Test
    void testDownloadFile() {
    }

    @Test
    void downloadFileExt() {
    }

    @Test
    void testDownloadFileExt() {
    }

    @Test
    void setDebugMode() {
    }

    @Test
    void updateAuthToken() {
    }

    @Test
    void getRestClient() {
    }

    @Test
    void getAuthToken() {
    }

    @Test
    void getDownloadBaseFolder() {
    }

    @Test
    void getDebug() {
    }

    @Test
    void getBaseUrl() {
    }

    @Test
    void getDownloadBaseUrl() {
    }

    @Test
    void getConnectTimeout() {
    }

    @Test
    void getReadTimeout() {
    }

    @Test
    void setRestClient() {
    }

    @Test
    void setAuthToken() {
    }

    @Test
    void setDownloadBaseFolder() {
    }

    @Test
    void setDebug() {
    }

    @Test
    void setBaseUrl() {
        String downloadBaseUrl = "https://test.com/static/";
        String relativePath = "/20251124/ComfyUI_00600_.mp4.mp4";
        String fullUrl = URLUtil.completeUrl(downloadBaseUrl, StrUtil.removePrefix(relativePath, "/"));
        System.out.println(StrUtil.format("Full URL: {}", fullUrl));
    }

    @Test
    void setDownloadBaseUrl() {
    }

    @Test
    void setConnectTimeout() {
    }

    @Test
    void setReadTimeout() {
    }
}