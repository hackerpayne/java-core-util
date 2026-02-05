package com.qyhstech.core.httpclient.strategy;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * 解决豆瓣http采集时的连接池使用问题，参考：https://www.cnblogs.com/aisam/p/7652680.html
 */
public class MyConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {


    private Integer timeOut = 20;

    public MyConnectionKeepAliveStrategy() {

    }

    public MyConnectionKeepAliveStrategy(Integer timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public TimeValue getKeepAliveDuration(HttpResponse response, HttpContext context) {
        return TimeValue.of(this.timeOut, TimeUnit.SECONDS); // 20 seconds,because tomcat default keep-alive timeout is 20s
    }
}
