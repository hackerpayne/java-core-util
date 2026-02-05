package com.qyhstech.core.httpclient.core;

import com.qyhstech.core.encode.QyCharset;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Slf4j
class QyHttpClientGeneratorTest {

    @Test
    public void testGetHtml() throws IOException, ParseException {
        CloseableHttpClient qyHttpClientGenerator = QyHttpClientGenerator.custom().build();

        HttpGet httpGet = new HttpGet("https://www.baidu.com");
        CloseableHttpResponse response = qyHttpClientGenerator.execute(httpGet);
        String html = EntityUtils.toString(response.getEntity(), QyCharset.UTF_8);
        System.out.println("请求完成，结果为：" + html);
    }

}