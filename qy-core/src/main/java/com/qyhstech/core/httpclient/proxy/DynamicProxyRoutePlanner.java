package com.qyhstech.core.httpclient.proxy;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.HttpContext;

/**
 * 如果需要随时切换 proxy, 需要自己实现一个 HttpRoutePlanner
 * 使用方法
 * HttpHost proxy = new HttpHost("127.0.0.1", 1080);
 * DynamicProxyRoutePlanner routePlanner = new DynamicProxyRoutePlanner(proxy);
 * CloseableHttpClient httpclient = HttpClients.custom()
 *     .setRoutePlanner(routePlanner)
 *     .build();
 * // 换代理
 * routePlanner.setProxy(new HttpHost("192.168.0.1", 1081));
 */
public class DynamicProxyRoutePlanner implements HttpRoutePlanner {
    private DefaultProxyRoutePlanner planner;

    public DynamicProxyRoutePlanner(HttpHost host) {
        planner = new DefaultProxyRoutePlanner(host);
    }

    public void setProxy(HttpHost host) {
        planner = new DefaultProxyRoutePlanner(host);
    }

    public HttpRoute determineRoute(HttpHost target, HttpContext context) throws HttpException {
        return planner.determineRoute(target, context);
    }
}