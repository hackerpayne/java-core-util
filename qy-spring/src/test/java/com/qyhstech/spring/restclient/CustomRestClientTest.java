package com.qyhstech.spring.restclient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRestClientTest extends QyBaseRestClient {


    /**
     *
     * @param saveFolder
     * @param baseUrl
     */
    public CustomRestClientTest(String saveFolder, String baseUrl) {
        // 初始化实例
        this.setDownloadBaseFolder(saveFolder); // 设置保存文件的根目录
        this.setDownloadBaseUrl(baseUrl); // 设置下载文件的根URL，拼接完整的下载图片路径用

        this.initRestClientIfNecessary();
    }

}
