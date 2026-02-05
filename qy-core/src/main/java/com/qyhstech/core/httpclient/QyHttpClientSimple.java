package com.qyhstech.core.httpclient;

import cn.hutool.core.io.IoUtil;
import com.qyhstech.core.httpclient.tool.QyHttpEntityUtil;
import com.qyhstech.core.QyStr;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kyle on 16/6/15.
 */
@Slf4j
@Getter
@Setter
public class QyHttpClientSimple {

    private CloseableHttpClient httpClient = null;
    private String userAgent = "";
    private String location = "";
    private CookieStore cookieStore = null;

    /**
     * 构造函数
     */
    public QyHttpClientSimple() {
        cookieStore = new BasicCookieStore();
//        this.httpClient = HttpClients.createDefault();

        // 定义全局配置文件，可以在Get里面用get.setConfig(configuration);配置，或者直接用全局的
        RequestConfig globalConfig = RequestConfig.custom()
//                .setConnectTimeout(6000)
                .setResponseTimeout(6000, TimeUnit.MILLISECONDS)
//                .setSocketTimeout(6000)
//                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .build();


        // 设置重试规则
        HttpRequestRetryStrategy handler = DefaultHttpRequestRetryStrategy.INSTANCE;

//        StandardHttpRequestRetryHandler standardHandler = new StandardHttpRequestRetryHandler(5, true);
//        HttpRequestRetryStrategy handler = new HttpRequestRetryStrategy() {
//
//            @Override
//            public boolean retryRequest(IOException arg0, int retryTimes, HttpContext arg2) {
//                if (arg0 instanceof UnknownHostException || arg0 instanceof ConnectTimeoutException
//                        || !(arg0 instanceof SSLException) || arg0 instanceof NoHttpResponseException) {
//                    return true;
//                }
//                if (retryTimes > 5) {
//                    return false;
//                }
//                HttpClientContext clientContext = HttpClientContext.adapt(arg2);
//                HttpRequest request = clientContext.getRequest();
//                boolean idempotent = request == null;
//                // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
//                return idempotent;
//            }
//        };


//        HttpHost proxy = new HttpHost("127.0.0.1", 80);// 设置代理ip
//        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        this.httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setDefaultRequestConfig(globalConfig).setRetryStrategy(handler)
//                .setRetryHandler(handler) //设置重试规则
//                .setRoutePlanner(routePlanner) //设置代理IP
                .build();

    }


    /**
     * 获取Cookie列表
     *
     * @return
     */
    public List<Cookie> getCookieList() {
        return cookieStore.getCookies();
    }

    /**
     * 清空Cookie
     */
    public void clearCookie() {
        if (cookieStore != null) {
            cookieStore.clear();
        }
    }

    /**
     * 添加一个Cookie进来
     *
     * @param ck
     */
    public void addCookie(Cookie ck) {
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }

        if (ck != null) {
            cookieStore.addCookie(ck);
        }
    }

    /**
     * 获取HTML
     *
     * @param url
     * @return
     */
    public String getHtml(String url) {
        return getHtml(url, "", "", this.getUserAgent(), false);
    }

    /**
     * 使用指定的UA进行访问
     *
     * @param url
     * @param referer
     * @return
     */
    public String getHtml(String url, String referer) {
        return getHtml(url, "", referer, this.getUserAgent(), false);
    }

    /**
     * 获取HTML
     *
     * @param url
     * @param cookie
     * @return
     */
    public String getHtml(String url, String cookie, String referer, String userAgent, boolean boolKeepAlive) {
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        String html = "";

        try {
            if (QyStr.isNotEmpty(cookie)) {
                httpGet.setHeader("Set-Cookie", cookie);
            }

            if (QyStr.isNotEmpty(userAgent)) {
                httpGet.setHeader("User-Agent", userAgent);
            }

            if (QyStr.isNotEmpty(referer)) {
                httpGet.setHeader("Referer", referer);
            }

            if (boolKeepAlive) {
                httpGet.setHeader("Connection", "keep-alive");
            }

            response = this.httpClient.execute(httpGet);

            Header head = response.getFirstHeader("Location");
            if (head != null) {
                String location = head.getValue();
                this.setLocation(location);
            }

            html = QyHttpEntityUtil.handleResponseToStr(response);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IoUtil.close(response);
            return html;
        }
    }


    /**
     * 上传文件
     *
     * @param postUrl  上传URL
     * @param postFile 上传文件本地
     */
    public String upload(String postUrl, String postFile) {

        String html = "";

        CloseableHttpResponse response = null;

        try {
//            HttpPost httpPost = new HttpPost(postUrl);
//
//            FileBody bin = new FileBody(new File(postFile));
//            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
//
//            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();
//
//            httpPost.setEntity(reqEntity);
//
//            response = this.httpClient.execute(httpPost);
//
//            html = responseToStr(response);

        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
            return html;
        }
    }

    /**
     * 使用Cookie下载图片
     *
     * @param url
     * @param cookie
     * @return
     * @throws IOException
     */
    public BufferedImage getImage(String url, String cookie) {

        HttpGet httpGet = new HttpGet(url);

        if (QyStr.isNotEmpty(cookie)) {
            httpGet.setHeader("Set-Cookie", cookie);
        }

        CloseableHttpResponse response = null;
        try {
            response = this.httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();

            ByteArrayInputStream is = new ByteArrayInputStream(EntityUtils.toByteArray(entity));
            BufferedImage img = ImageIO.read(is);
            is.close();
            return img;

        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * post方式提交表单（模拟用户登录请求）
     *
     * @param url
     * @param postData
     * @return
     */
    public String postHtml(String url, HashMap<String, String> postData) {
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        for (Map.Entry<String, String> item : postData.entrySet()) {
            formparams.add(new BasicNameValuePair(item.getKey(), item.getValue()));
        }

        UrlEncodedFormEntity uefEntity = null;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams);
            return postHtml(url, uefEntity, "", this.getUserAgent(), "");

        } catch (Exception e) {
            log.error("", e);
            return "";
        }


    }

    /**
     * postHtml发送
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String postHtml(String url, List<NameValuePair> postData) {

        return postHtml(url, new UrlEncodedFormEntity(postData), "", this.getUserAgent(), "");
    }

    /**
     * @param url
     * @param postData
     * @return
     */
    public String postJson(String url, String postData) {
        return postJson(url, postData, "", this.getUserAgent(), "");
    }

    /**
     * 提交Json格式的数据
     *
     * @param url
     * @param postData
     * @return
     */
    public String postJson(String url, String postData, String cookie, String userAgent, String referer) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        String html = "";

        CloseableHttpResponse response = null;
        try {

            if (QyStr.isNotEmpty(cookie)) {
                httpPost.setHeader("Set-Cookie", cookie);
            }

            if (QyStr.isNotEmpty(userAgent)) {
                httpPost.setHeader("User-Agent", userAgent);
            }

            if (QyStr.isNotEmpty(referer)) {
                httpPost.setHeader("Referer", referer);
            }

            if (QyStr.isNotEmpty(postData)) {
                StringEntity se = new StringEntity(postData);
//                se.setContentType("text/json");
                httpPost.setEntity(se);
            }

            response = this.httpClient.execute(httpPost);
            html = QyHttpEntityUtil.responseToStr(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return html;
    }


    /**
     * post方式提交表单（模拟用户登录请求）
     *
     * @param url
     * @param postEntity
     * @param cookie
     * @param userAgent
     * @param referer
     * @return
     */
    public String postHtml(String url, HttpEntity postEntity, String cookie, String userAgent, String referer) {

        CloseableHttpResponse response = null;
        String html = "";

        // 创建HttpPost
        HttpPost httpPost = new HttpPost(url);

        try {
            if (postEntity != null) {
                httpPost.setEntity(postEntity);
            }

            if (QyStr.isNotEmpty(cookie)) {
                httpPost.setHeader("Set-Cookie", cookie);
            }

            if (QyStr.isNotEmpty(userAgent)) {
                httpPost.setHeader("User-Agent", userAgent);
            }

            if (QyStr.isNotEmpty(referer)) {
                httpPost.setHeader("Referer", referer);
            }

            response = this.httpClient.execute(httpPost);

            html = QyHttpEntityUtil.responseToStr(response);

        } catch (Exception e) {
            log.error("", e);
        } finally {
            IoUtil.close(response);
            return html;
        }

    }


    /**
     * 关闭HttpClient
     */
    public void close() {
        if (this.httpClient != null) {
            try {
                this.httpClient.close();
            } catch (IOException e) {
                log.error("关闭HttpClient出错", e);
            }
        }
    }

}
