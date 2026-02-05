package com.qyhstech.core.httpclient.strategy;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.protocol.RedirectStrategy;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.net.URI;

/**
 * 支持post 302跳转策略实现类
 * HttpClient默认跳转：httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
 * 上述代码在post/redirect/post这种情况下不会传递原有请求的数据信息。所以参考了下SeimiCrawler这个项目的重定向策略。
 * 原代码地址：https://github.com/zhegexiaohuozi/SeimiCrawler/blob/master/project/src/main/java/cn/wanghaomiao/seimi/http/hc/SeimiRedirectStrategy.java
 */
@Slf4j
public class CustomRedirectStrategy implements RedirectStrategy {

//    @Override
//    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException {
//        URI uri = getLocationURI(request, response, context);
//        String method = request.getRequestLine().getMethod().toLowerCase();
//        if ("post".equalsIgnoreCase(method)) {
//            try {
//                HttpRequestWrapper httpRequestWrapper = (HttpRequestWrapper) request;
//                httpRequestWrapper.setUri(uri);
//                httpRequestWrapper.removeHeaders("Content-Length");
//                return httpRequestWrapper;
//            } catch (Exception e) {
//                log.error("强转为HttpRequestWrapper出错");
//            }
//            return new HttpPost(uri);
//        } else {
//            return new HttpGet(uri);
//        }
//    }

    @Override
    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException {
        return false;
    }

    @Override
    public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException {
        return null;
    }
}
