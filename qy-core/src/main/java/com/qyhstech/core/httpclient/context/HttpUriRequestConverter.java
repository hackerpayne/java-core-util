package com.qyhstech.core.httpclient.context;

import com.qyhstech.core.domain.dto.ModelProxy;
import com.qyhstech.core.httpclient.bean.QyHttpGlobalSetting;
import com.qyhstech.core.httpclient.bean.QyHttpRequest;
import com.qyhstech.core.http.QyCookieUtil;
import org.apache.hc.client5.http.protocol.HttpClientContext;

/**
 * 上下文转换，用于实现每个Client不同的Proxy代理IP请求
 */
public class HttpUriRequestConverter {

    /**
     * 转换Http上下文
     *
     * @param request 请求Request
     * @param site    Site网站信息
     * @param proxy   代理IP
     * @return
     */
    public HttpClientRequestContext convert(QyHttpRequest request, QyHttpGlobalSetting site, ModelProxy proxy) {
        HttpClientRequestContext httpClientRequestContext = new HttpClientRequestContext();
        //httpClientRequestContext.setHttpUriRequest(convertHttpUriRequest(request, site, proxy));
        httpClientRequestContext.setHttpClientContext(convertHttpClientContext(request, site, proxy));
        return httpClientRequestContext;
    }

    /**
     * @param request
     * @param site
     * @param proxy
     * @return
     */
    private HttpClientContext convertHttpClientContext(QyHttpRequest request, QyHttpGlobalSetting site, ModelProxy proxy) {
        HttpClientContext httpContext = new HttpClientContext();
//        if (proxy != null && proxy.getUsername() != null) {
//            AuthState authState = new AuthState();
////            authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword()));
//            authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword()));
////            httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
//            httpContext.setAttribute(HttpClientContext.AUTH_CACHE, authState);
//        }
        if (request.getCookies() != null && !request.getCookies().isEmpty()) {
            var cookieStore = QyCookieUtil.setCookieToCookieStore(request.getCookies(), request.getUrl());
            httpContext.setCookieStore(cookieStore);
        }
        return httpContext;
    }

//    /**
//     * @param request
//     * @param site
//     * @param proxy
//     * @return
//     */
//    private HttpUriRequest convertHttpUriRequest(QyHttpRequest request, QyHttpGlobalSetting site, ModelProxy proxy) {
//        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(QyUrlUtil.fixIllegalCharacterInUrl(request.getUrl()));
//        if (site.getHeaders() != null) {
//            for (Map.Entry<String, String> headerEntry : site.getHeaders().entrySet()) {
//                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
//            }
//        }
//
//        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
//        if (null != site) {
//            requestConfigBuilder.setConnectionRequestTimeout(site.getTimeOut(), TimeUnit.SECONDS)
////                    .setSocketTimeout(site.getTimeOut())
//                    .setConnectTimeout(site.getTimeOut(), TimeUnit.SECONDS).setCookieSpec("standard");
//        }
//
//        if (proxy != null) {
//            requestConfigBuilder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
//        }
////        requestBuilder.setConfig(requestConfigBuilder.build());
//        HttpUriRequest httpUriRequest = (HttpUriRequest) requestBuilder.build();
//        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
//            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
//                httpUriRequest.addHeader(header.getKey(), header.getValue());
//            }
//        }
//        return httpUriRequest;
//    }
//
//    /**
//     * 设置请求方式是GET、POST还是DELETE等
//     *
//     * @param request
//     * @return
//     */
//    private RequestBuilder selectRequestMethod(HttpRequest request) {
//        String method = request.getMethod();
//        if (method == null || method.equalsIgnoreCase(QyHttpConstant.Method.GET)) {
//            //default get
//            return RequestBuilder.get();
//        } else if (method.equalsIgnoreCase(QyHttpConstant.Method.POST)) {
//            return addFormParams(RequestBuilder.post(), request);
//        } else if (method.equalsIgnoreCase(QyHttpConstant.Method.HEAD)) {
//            return RequestBuilder.head();
//        } else if (method.equalsIgnoreCase(QyHttpConstant.Method.PUT)) {
//            return addFormParams(RequestBuilder.put(), request);
//        } else if (method.equalsIgnoreCase(QyHttpConstant.Method.DELETE)) {
//            return RequestBuilder.delete();
//        } else if (method.equalsIgnoreCase(QyHttpConstant.Method.TRACE)) {
//            return RequestBuilder.trace();
//        }
//        throw new IllegalArgumentException("Illegal HTTP Method " + method);
//    }
//
//    /**
//     * @param requestBuilder
//     * @param request
//     * @return
//     */
//    private RequestBuilder addFormParams(RequestBuilder requestBuilder, HttpRequest request) {
//        if (request.getRequestBody() != null) {
////            ByteArrayEntity entity = new ByteArrayEntity(request.getRequestBody().getBody(), request.getRequestBody().getContentType());
//////            entity.setContentType(request.getRequestBody().getContentType());
////            requestBuilder.setEntity(entity);
//        }
//        return requestBuilder;
//    }

}
