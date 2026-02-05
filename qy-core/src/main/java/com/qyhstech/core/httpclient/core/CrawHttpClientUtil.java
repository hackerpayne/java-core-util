package com.qyhstech.core.httpclient.core;

import com.qyhstech.core.domain.dto.ModelProxy;
import com.qyhstech.core.domain.constant.QyHttpMethodConst;
import com.qyhstech.core.httpclient.bean.QyHttpGlobalSetting;
import com.qyhstech.core.httpclient.bean.QyHttpRequest;
import com.qyhstech.core.httpclient.bean.QyHttpResult;
import com.qyhstech.core.httpclient.context.HttpClientRequestContext;
import com.qyhstech.core.httpclient.context.HttpUriRequestConverter;
import com.qyhstech.core.httpclient.proxy.ProxyProvider;
import com.qyhstech.core.httpclient.tool.QyHttpEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.protocol.RedirectLocations;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支持爬虫形式的高级HttpClient
 * 返回和请求统一 使用HttpResult和HttpRequest类包装
 * https://hc.apache.org/httpcomponents-client-5.2.x/migration-guide/migration-to-classic.html
 */
@Slf4j
public class CrawHttpClientUtil {

    private final Map<String, CloseableHttpClient> httpClients = new HashMap();

    private QyHttpClientGenerator httpClientGenerator = new QyHttpClientGenerator();

    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();

    private ProxyProvider proxyProvider;

    public CrawHttpClientUtil() {
    }

    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }

    public void setHttpUriRequestConverter(HttpUriRequestConverter httpUriRequestConverter) {
        this.httpUriRequestConverter = httpUriRequestConverter;
    }

    /**
     * 根据Site从池里获取新的HttpClient
     *
     * @param site
     * @return
     */
    private CloseableHttpClient getHttpClient(QyHttpGlobalSetting site) {
        if (site == null) {
            return this.httpClientGenerator.build(null);
        } else {
            String domain = site.getDomain();
            CloseableHttpClient httpClient = this.httpClients.get(domain);
            if (httpClient == null) {
                synchronized (this) {
                    httpClient = this.httpClients.get(domain);
                    if (httpClient == null) {
                        httpClient = this.httpClientGenerator.build(site);
                        this.httpClients.put(domain, httpClient);
                    }
                }
            }

            return httpClient;
        }
    }

    /**
     * 下载页面请求
     *
     * @param request
     * @param httpSetting
     * @return
     */
    public QyHttpResult download(QyHttpRequest request, QyHttpGlobalSetting httpSetting) {

        if (httpSetting == null) {
            throw new NullPointerException("task or site can not be null");
        }

        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = this.getHttpClient(httpSetting);
        ModelProxy proxy = this.proxyProvider != null ? this.proxyProvider.getProxy(httpSetting) : null;
        HttpClientRequestContext requestContext = this.httpUriRequestConverter.convert(request, httpSetting, proxy);
        QyHttpResult page = new QyHttpResult();

        QyHttpResult var9;
        try {

            httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());

//            List<URI> locationList = requestContext.getHttpClientContext().getRedirectLocations();
            RedirectLocations locationList = requestContext.getHttpClientContext().getRedirectLocations();

            if (locationList != null && locationList.size() > 0) {
                page.setLocationUrl(locationList.get(locationList.size() - 1).toString());
            }

            // 返回码大于400的，都是错误 ，需要重试请求
            if (httpResponse.getCode() > 400) {
                log.warn("download page {} error,Status could not be {}", request.getUrl(), httpResponse.getCode());
                page.setDownloadSuccess(false);
                return page;
            }

            page = QyHttpEntityUtil.handleResponse(request, request.getCharset() != null ? request.getCharset() : httpSetting.getCharset(), httpResponse);

            log.info("downloading page success {}", request.getUrl());
            QyHttpResult var8 = page;
            return var8;
        } catch (IOException var13) {

            // 代理里面出现SocketTimeOut太多了，屏蔽掉异常说明
            if (var13 instanceof SocketTimeoutException) {
                log.warn("download page {} error, SocketTimeout", request.getUrl());
            } else {
                log.warn("download page {} error", request.getUrl(), var13);
            }
            var9 = page;
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }

            if (this.proxyProvider != null && proxy != null) {
                this.proxyProvider.returnProxy(proxy, page, httpSetting);
            }

        }

        return var9;

    }

    /**
     * 仅仅只把Head部份取回来，用于解析跳转的时候使用，关闭handleResponse即可
     *
     * @param request
     * @param httpSetting
     * @return
     */
    public QyHttpResult downloadHeader(QyHttpRequest request, QyHttpGlobalSetting httpSetting) {

        if (httpSetting == null) {
            throw new NullPointerException("task or site can not be null");
        }

        request.setMethod(QyHttpMethodConst.HEAD);//只请求头信息

        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = this.getHttpClient(httpSetting);
        ModelProxy proxy = this.proxyProvider != null ? this.proxyProvider.getProxy(httpSetting) : null;
        HttpClientRequestContext requestContext = this.httpUriRequestConverter.convert(request, httpSetting, proxy);
        QyHttpResult page = new QyHttpResult();

        QyHttpResult var9;
        try {

            httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());
            RedirectLocations locationList = requestContext.getHttpClientContext().getRedirectLocations();
//            httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());

//            List<URI> locationList = requestContext.getHttpClientContext().getRedirectLocations();


            if (locationList != null && locationList.size() > 0) {
                page.setLocationUrl(locationList.get(locationList.size() - 1).toString());
            }

            page.setStatusCode(httpResponse.getCode());

            // 返回码大于400的，都是错误 ，需要重试请求
            if (httpResponse.getCode() > 400) {
                log.warn("download page {} error,Status could not be {}", request.getUrl(), httpResponse.getCode());
                page.setDownloadSuccess(false);
                return page;
            }

            page.setUrl(request.getUrl());

            if (request.isReturnHeader())
                page.setHeaders(QyHttpEntityUtil.handleHeadersToMap(httpResponse.getHeaders()));

            this.log.info("downloading page success {}", request.getUrl());
            QyHttpResult var8 = page;
            return var8;
        } catch (IOException var13) {

            // 代理里面出现SocketTimeOut太多了，屏蔽掉异常说明
            if (var13 instanceof SocketTimeoutException) {
                log.warn("download page {} error, SocketTimeout", request.getUrl());
            } else {
                log.warn("download page {} error", request.getUrl(), var13);
            }
            var9 = page;
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }

            if (this.proxyProvider != null && proxy != null) {
                this.proxyProvider.returnProxy(proxy, page, httpSetting);
            }

        }

        return var9;
    }

    /**
     * 设置线程数量
     *
     * @param thread
     */
    public void setThread(int thread) {
        this.httpClientGenerator.setPoolSize(thread);
    }

}
