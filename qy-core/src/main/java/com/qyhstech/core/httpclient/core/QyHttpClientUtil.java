package com.qyhstech.core.httpclient.core;

import com.qyhstech.core.encode.QyCharset;
import com.qyhstech.core.httpclient.tool.QyHttpEntityUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.TimeValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 常规用法的HttpClient
 * 请求和结果直接返回
 * https://hc.apache.org/httpcomponents-client-5.2.x/migration-guide/migration-to-classic.html
 */
@Data
@Slf4j
public class QyHttpClientUtil {

    private static final int MAX_TIMEOUT = 400;

    private final HttpClientBuilder httpBuilder = HttpClientBuilder.create();

    //定义头信息
    private Map<String, String> headerMap;

    //定义CookieStore对象
    private BasicCookieStore cookieStore;

    public QyHttpClientUtil() {
        // 一键创建
        //httpBuilder = QyHttpClientBuilder.buildHttpClientBuilder(Timeout.ofSeconds(MAX_TIMEOUT));

        //设置连接基本配置
        //注册访问协议相关的Socket工厂
        Registry<ConnectionSocketFactory> registry = QyHttpClientBuilder.buildRegistrySslConnectionSocketFactory();

        //设置连接池管理器
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(registry);
        manager.setMaxTotal(100);
        manager.setDefaultMaxPerRoute(manager.getMaxTotal() / 2);
        //manager.setValidateAfterInactivity(TimeValue.ofMinutes(5));
        manager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(30, TimeUnit.SECONDS).setTcpNoDelay(true).build());

        // 为HttpClientBuilder设置连接池
        httpBuilder.setConnectionManager(manager);

        // 设置定期清理连接池中过期的连接
        httpBuilder.evictExpiredConnections();
        httpBuilder.evictIdleConnections(TimeValue.ofMinutes(3));

        //设置请求基础配置
        //创建默认CookieStore
        cookieStore = new BasicCookieStore();

        //设置Http请求基本参数
        RequestConfig requestConfig = RequestConfig.custom()
                .setRedirectsEnabled(true) // 设置启用重定向
                .setMaxRedirects(30) // 设置最大重定向次数
                .setConnectionRequestTimeout(2, TimeUnit.MINUTES) // 设置请求超时时间
                .setResponseTimeout(2, TimeUnit.MINUTES)// 设置响应超时时间
                .build();

        //为HttpClientBuilder设置连接配置
        httpBuilder.setDefaultRequestConfig(requestConfig);

        //为HttpClientBuilder设置头信息
        //httpBuilder.setDefaultHeaders(buildHeader());

        httpBuilder.setDefaultCookieStore(cookieStore);

        //httpBuilder.setDefaultCredentialsProvider(basicCredentialsProvider);
    }

    public static void getUseCookie(String url) throws Exception {

        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // 创建一个本地的 Cookie 存储
            final CookieStore cookieStore = new BasicCookieStore();

            // 手动设置Cookie
            // BasicClientCookie clientCookie = new BasicClientCookie("name", "www.wdbyte.com");
            // clientCookie.setDomain("http://httpbin.org/cookies");
            // 过期时间
            // clientCookie.setExpiryDate(new Date());
            // 添加到本地 Cookie
            // cookieStore.addCookie(clientCookie);

            // 创建本地 HTTP 请求上下文 HttpClientContext
            final HttpClientContext localContext = HttpClientContext.create();
            // 绑定 cookieStore 到 localContext
            localContext.setCookieStore(cookieStore);

            final HttpGet httpget = new HttpGet(url);
            //log.info("执行请求 " + httpget.getMethod() + " " + httpget.getUri());

            // 获取 Coolie 信息
            try (final CloseableHttpResponse response = httpclient.execute(httpget, localContext)) {
                // log.info("----------------------------------------");
                // log.info(response.getCode() + " " + response.getReasonPhrase());
                final List<Cookie> cookies = cookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    log.info("Local cookie: " + cookies.get(i));
                }
                EntityUtils.consume(response.getEntity());
            }
        }
    }

    public static String get(HttpGet httpGet) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        return get(httpGet, null);
    }

    /**
     * 根据配置好的HttpGet返回数据
     * 获取状态码：
     * log.info(response.getVersion()); // HTTP/1.1
     * log.info(response.getCode()); // 200
     * log.info(response.getReasonPhrase()); // OK
     *
     * @param httpGet
     * @param requestConfig 如果带配置的，使用配置进行请求
     * @return
     */
    public static String get(HttpGet httpGet, RequestConfig requestConfig) {
        String resultContent;
        CloseableHttpClient httpclient;
        if (requestConfig != null) {
            httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        } else {
            httpclient = HttpClients.createDefault();
        }
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            // 获取响应信息
            resultContent = EntityUtils.toString(entity);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return resultContent;
    }

    /**
     * 最简单的获取HTML
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        return get(httpGet);
    }

    /**
     * 带参数请求
     * List<NameValuePair> nvps = new ArrayList<>();
     * nvps.add(new BasicNameValuePair("username", "wdbyte.com"));
     * nvps.add(new BasicNameValuePair("password", "secret"));
     *
     * @param url
     * @param nvps
     * @return
     */
    public static String get(String url, List<NameValuePair> nvps) {
        HttpGet httpGet = new HttpGet(url);
        // 增加到请求 URL 中
        try {
            URI uri = new URIBuilder(new URI(url))
                    .addParameters(nvps)
                    .build();
            httpGet.setUri(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return get(httpGet);
    }

    /**
     * 请求并返回字符串，默认使用UTF-8编码
     *
     * @param httpPost
     * @return
     */
    public static String post(HttpPost httpPost) {
        String result = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                // 获取响应信息
                result = EntityUtils.toString(entity, QyCharset.UTF_8);
                // 确保流被完全消费
                EntityUtils.consume(entity);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 提交指定的参数，返回字符串
     *
     * @param url
     * @param postParams
     * @return
     */
    public static String post(String url, List<NameValuePair> postParams) {

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(postParams));
        return post(httpPost);
    }

    /**
     * Post Json格式数据
     *
     * @param url
     * @param jsonBody
     * @return
     */
    public static String postJson(String url, String jsonBody) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        return post(httpPost);
    }

    /**
     * 提交Json格式的文件到远程
     *
     * @param url
     * @param jsonFilePath
     * @return
     * @throws FileNotFoundException
     */
    public static String postJsonFile(String url, String jsonFilePath) throws FileNotFoundException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(QyHttpEntityUtil.buildJsonFileEntity(new File(jsonFilePath)));
        return post(httpPost);

    }

    /**
     * 提交Json表单数据
     *
     * @param url
     * @param formParams
     * @throws Exception
     */
    public static String postForm(String url, List<NameValuePair> formParams) throws Exception {
        final BasicCookieStore cookieStore = new BasicCookieStore();

        HttpEntity httpEntity = null;
        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build()) {

            // 本应该使用 POST 请求发送表单参数，但是在 httpbin.org 中没有对应的接口用于测试，所以这里换成了 GET 请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(formParams));

            try (final CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
                httpEntity = response2.getEntity();

                //log.info("Login form get: " + response2.getCode() + " " + response2.getReasonPhrase());
                //log.info("当前响应信息 " + EntityUtils.toString(entity));
                //log.info("Post 登录 Cookie:");

                final List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    log.info("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        log.info("- " + cookies.get(i));
                    }
                }
            }
        }
        return EntityUtils.toString(httpEntity);
    }


}
