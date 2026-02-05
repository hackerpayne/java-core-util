package com.qyhstech.core.httpclient.ext;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class QyHttpClientAsyncUtil {

    /**
     * 异步处理
     *
     * @param url
     * @return
     */
    public static String getAsync(String url) {
        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
            // 开始 http clinet
            httpclient.start();
            // 执行请求
            SimpleHttpRequest request1 = SimpleHttpRequests.get(url);
            Future<SimpleHttpResponse> future = httpclient.execute(request1, null);
            // 等待直到返回完毕
            SimpleHttpResponse response1 = future.get();
            //System.out.println("getAsync1:" + request1.getRequestUri() + "->" + response1.getCode());
            return response1.getBodyText();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
