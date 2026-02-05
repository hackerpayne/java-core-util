package com.qyhstech.core.httpclient.core;

import cn.hutool.core.lang.Pair;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.domain.dto.ModelProxy;
import com.qyhstech.core.httpclient.bean.QyHttpGlobalSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.ExecChainHandler;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.hc.core5.http.io.HttpMessageParserFactory;
import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * HttpClient的创建工具
 */
@Slf4j
public class QyHttpClientGenerator {

    private HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

    // 默认创建 ManagedHttpClientConnectionFactory 需要的工具类
    private HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory();
    private HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
    private CharCodingConfig charCodingConfig = CharCodingConfig.DEFAULT;
    private Http1Config h1Config = Http1Config.DEFAULT;

    // SSL连接器配置
    private SSLContext sslcontext = SSLContexts.createSystemDefault();
    private DnsResolver dnsResolver = new SystemDefaultDnsResolver();
    private TlsConfig tlsConfig = null;

    // Cookie保存器配置
    private CookieStore cookieStore = null;

    // Socket连接配置
    private ConnectionConfig connectionConfig;

    private SocketConfig qySocketConfig = null;

    // 代理配置
    private HttpHost proxy = null;

    /**
     * 设置连接池大小
     */
    private Integer maxTotal = 100;

    /**
     *
     */
    private Integer maxPerRoute = 10;

    /**
     * 默认的配置文件
     */
    private RequestConfig defaultRequestConfig = RequestConfig.DEFAULT;

    /**
     * 创建自定义配置
     *
     * @return
     */
    public static QyHttpClientGenerator custom() {
        return new QyHttpClientGenerator();
    }


    /**
     * 使用HttpClientBuilder创建的Client，可以做多种自定义操作，包括添加代理和Cookie等
     *
     * @return
     */
    public static CloseableHttpClient buildHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        //httpClientBuilder.setConnectionManager(buildPoolingConnectionManager());

//        httpClientBuilder.setDefaultCookieStore();// 指定Cookie进行请求

//        httpClientBuilder.setUserAgent("");// 设置UA
//        httpClientBuilder.addInterceptorFirst(new GzipInterceptor()); // 允许Gzip压缩
//        httpClientBuilder.setKeepAliveStrategy(new MyConnectionKeepAliveStrategy(site.getTimeOut()));

        //解决post/redirect/post 302跳转问题
        //httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());

        // 设置Socket过期时间
//        SocketConfig socketConfig = HttpClientCreatorUtil.buildSocketConfig(site.getTimeOut());
//        httpClientBuilder.setDefaultSocketConfig(socketConfig);
//        connectionManager.setDefaultSocketConfig(socketConfig);
//
        // 建立Retry的机制，也可以使用上面的 buildRetryHandler
//        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));

        return httpClientBuilder.build();
    }

    /**
     * 设置线程池最大线程数量
     *
     * @param poolSize
     * @return
     */
    public QyHttpClientGenerator setPoolSize(Integer poolSize) {
        this.maxPerRoute = poolSize;
        return this;
    }

    /**
     * 添加Cookie到CookieStore
     * 使用时：httpClientBuilder.setDefaultCookieStore(cookieStore);
     *
     * @param listCookies
     * @return
     */
    public CookieStore buildCookie(List<Cookie> listCookies) {
        CookieStore cookieStore = new BasicCookieStore();

        listCookies.forEach(item -> {
            BasicClientCookie cookie = new BasicClientCookie(item.getName(), item.getValue());
            if (QyStr.isNotEmpty(item.getDomain())) {
                cookie.setDomain(item.getDomain());
            }
            cookieStore.addCookie(cookie);
        });

        return cookieStore;
    }


    /**
     * SSL证书绕过配置：如果使用默认配置，就直接跳过，否则使用自定义的设置
     *
     * @param useDefault 如果是使用默认的，就直接跳过去
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public QyHttpClientGenerator buildSslContext(boolean useDefault) throws NoSuchAlgorithmException, KeyManagementException {
        this.sslcontext = QyHttpClientBuilder.buildSslContext(useDefault);
        return this;
    }

    /**
     * 账号密码校验
     *
     * @param credentials
     * @return
     */
    public QyHttpClientGenerator authentications(Map<String, Pair<String, String>> credentials) {
        httpClientBuilder.setDefaultCredentialsProvider(QyHttpClientBuilder.buildCredentialsProvider(credentials));
        return this;
    }

    /**
     * 创建默认的PoolingHttpClientConnectionManager对象
     *
     * @return
     */

    /**
     * @param responseParserFactory
     * @return
     */
    public QyHttpClientGenerator responseParserFactory(HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
        this.responseParserFactory = responseParserFactory;
        return this;
    }

    /**
     * @param requestWriterFactory
     * @return
     */
    public QyHttpClientGenerator requestWriterFactory(HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory) {
        this.requestWriterFactory = requestWriterFactory;
        return this;
    }

    /**
     * @param charCodingConfig
     * @return
     */
    public QyHttpClientGenerator requestWriterFactory(CharCodingConfig charCodingConfig) {
        this.charCodingConfig = charCodingConfig;
        return this;
    }

    /**
     * @param sslcontext
     * @return
     */
    public QyHttpClientGenerator sslcontext(SSLContext sslcontext) {
        this.sslcontext = sslcontext;
        return this;
    }

    /**
     * @param resolves
     * @return
     */
    public QyHttpClientGenerator dnsResolver(Map<String, InetAddress> resolves) {
        dnsResolver = QyHttpClientBuilder.buildDnsResolver(resolves);
        return this;
    }

    /**
     * @param requestConfig
     * @return
     */
    public QyHttpClientGenerator requestConfig(RequestConfig requestConfig) {
        this.defaultRequestConfig = requestConfig;
        return this;
    }

    /**
     * @param cookieStore
     * @return
     */
    public QyHttpClientGenerator cookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    /**
     * @param connectionConfig
     * @return
     */
    public QyHttpClientGenerator connectionConfig(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
        return this;
    }

    /**
     * @param qySocketConfig
     * @return
     */
    public QyHttpClientGenerator socketConfig(SocketConfig qySocketConfig) {
        this.qySocketConfig = qySocketConfig;
        return this;
    }

    /**
     * 配置TLS
     *
     * @param config
     * @return
     */
    public QyHttpClientGenerator tlsConfig(TlsConfig config) {
        this.tlsConfig = config;
        return this;
    }

    /**
     * @param interceptor
     * @return
     */
    public QyHttpClientGenerator addRequestInterceptorFirst(final HttpRequestInterceptor interceptor) {
        httpClientBuilder.addRequestInterceptorFirst(interceptor);
        return this;
    }

    /**
     * @param interceptor
     * @return
     */
    public QyHttpClientGenerator addRequestInterceptorLast(final HttpRequestInterceptor interceptor) {
        httpClientBuilder.addRequestInterceptorLast(interceptor);
        return this;
    }

    /**
     * @param existing
     * @param name
     * @param interceptor
     * @return
     */
    public final QyHttpClientGenerator addExecInterceptorAfter(final String existing, final String name, final ExecChainHandler interceptor) {
        httpClientBuilder.addExecInterceptorAfter(existing, name, interceptor);
        return this;
    }

    /**
     * @param existing
     * @param name
     * @param interceptor
     * @return
     */
    public final QyHttpClientGenerator addExecInterceptorBefore(final String existing, final String name, final ExecChainHandler interceptor) {
        httpClientBuilder.addExecInterceptorBefore(existing, name, interceptor);
        return this;
    }

    /**
     * 创建可循环使用的连接池
     *
     * @return
     */
    public PoolingHttpClientConnectionManager buildPoolingHttpClientConnectionManager() {

        ManagedHttpClientConnectionFactory connFactory = new ManagedHttpClientConnectionFactory(this.h1Config, charCodingConfig, requestWriterFactory, responseParserFactory);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();

        //Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", HttpClientSslUtil.buildSSLConnectionSocketFactory()).build();
        //PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(reg);

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry,
                PoolConcurrencyPolicy.STRICT,
                PoolReusePolicy.LIFO,
                TimeValue.ofMinutes(5), null, dnsResolver,
                connFactory);

        // 配置Socket连接信息
        connManager.setDefaultSocketConfig(this.qySocketConfig);
        //connManager.setDefaultSocketConfig(buildSocketConfig(2000));

        // 配置Connection连接信息
        connManager.setDefaultConnectionConfig(this.connectionConfig);

        // Use TLS v1.3 only
        connManager.setDefaultTlsConfig(QyHttpClientBuilder.buildTlsConfig());

        //  1、MaxtTotal是整个池子的大小；
        //  2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
        //  MaxtTotal=400 DefaultMaxPerRoute=200
        //  而我只连接到http://sishuok.com时，到这个主机的并发最多只有200；而不是400；
        //  而我连接到http://sishuok.com 和 http://qq.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。
        connManager.setMaxTotal(maxTotal); // 设置整个连接池最大连接数 根据自己的场景决定 该值就是连接不够用的时候等待超时时间，一定要设置，而且不能太大

        connManager.setDefaultMaxPerRoute(maxPerRoute); // 每台主机分配的连接数字，是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。

        return connManager;
    }

    /**
     * 生成Cookie，根据Site里面配置的公用Cookie
     *
     * @param httpClientBuilder
     * @param site
     */
    private void generateCookie(HttpClientBuilder httpClientBuilder, QyHttpGlobalSetting site) {
        if (site.isDisableCookieManagement()) {
            httpClientBuilder.disableCookieManagement();
            return;
        }
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(site.getDomain());
            cookieStore.addCookie(cookie);
        }
        for (Map.Entry<String, Map<String, String>> domainEntry : site.getAllCookies().entrySet()) {
            for (Map.Entry<String, String> cookieEntry : domainEntry.getValue().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie.setDomain(domainEntry.getKey());
                cookieStore.addCookie(cookie);
            }
        }
        httpClientBuilder.setDefaultCookieStore(cookieStore);
    }

    /**
     * 配置使用代理
     * 使用时：httpClientBuilder.setRoutePlanner(routePlanner);
     *
     * @param proxyModel
     * @return
     */
    public QyHttpClientGenerator buildProxy(ModelProxy proxyModel) {

        // 添加普通的代理配置
        final HttpHost proxy = new HttpHost(proxyModel.getHost(), proxyModel.getPort());
        final DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        httpClientBuilder.setRoutePlanner(routePlanner);

        // 如果代理需要账号和密码登陆的话，使用如下：
        if (QyStr.isNotEmpty(proxyModel.getUsername()) && QyStr.isNotEmpty(proxyModel.getPassword())) {

            AuthScope authScope = new AuthScope(proxyModel.getHost(), proxyModel.getPort());
            UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(proxyModel.getUsername(), proxyModel.getPassword().toCharArray());

            final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(authScope, usernamePasswordCredentials);
            httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
            //httpClientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
        }


        //return routePlanner;

        // 旧方法已经弃用了
        //RequestConfig.Builder configBuilder;
        //
        ////使用代理
        //if (null != proxyModel && QyStrUtil.isNotBlank(proxyModel.getHost())) {
        //    HttpHost proxy = new HttpHost(proxyModel.getHost(), proxyModel.getPort());
        //    configBuilder = RequestConfig.custom().setProxy(proxy);
        //} else {
        //    //没有代理，使用默认值
        //    configBuilder = RequestConfig.custom();
        //}

        //return configBuilder.build();

        return this;
    }

    /**
     * 简单获取连接配置
     *
     * @return
     */
    public CloseableHttpClient buildSimpleClient() {
        return HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接
    }

    /**
     * 从链接池获取一个HttpClient实例，不做别的处理，仅仅是使用简单的连接池
     *
     * @return
     */
    public CloseableHttpClient buildSimplePoolingClient() {
        PoolingHttpClientConnectionManager connManager = buildPoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();
        return httpClient;
    }

    /**
     * 创建HttpClient的步骤：
     * 1、
     *
     * @return
     */
    public CloseableHttpClient build() {

        PoolingHttpClientConnectionManager connManager = buildPoolingHttpClientConnectionManager();
        httpClientBuilder.setConnectionManager(connManager).evictExpiredConnections().evictIdleConnections(TimeValue.ofSeconds(5));

        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }

        if (proxy != null) {
            httpClientBuilder.setProxy(proxy);
        }

        if (defaultRequestConfig != null) {
            httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
        }

        CloseableHttpClient build = httpClientBuilder.build();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                build.close();
            } catch (Exception e) {
            }
        }));
        return build;
    }

    /**
     * 自定义 HttpClient 增加 SSL TrustAllStrategy
     *
     * @return
     */
    public CloseableHttpClient buildOrigin() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        // 1、创建连接配置
        final RequestConfig defaultRequestConfig = QyHttpClientBuilder.buildRequestConfig(Timeout.ofSeconds(20));

        // 2、创建Cookie存储器
        final BasicCookieStore defaultCookieStore = new BasicCookieStore();

        // 3、创建SSL连接配置
        final SSLContext sslcontext = QyHttpClientBuilder.buildSslContext();

        final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create().setSslContext(sslcontext).build();

        // 4、创建连接池配置
        final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory).build();

        // 5、创建最终的连接对象
        return HttpClients.custom()
                .setDefaultCookieStore(defaultCookieStore)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(cm)
                .evictExpiredConnections()
                .build();
    }

    /**
     * 为每个站点设置独立的Cookie存储
     *
     * @param site
     * @return
     */
    public CloseableHttpClient build(QyHttpGlobalSetting site) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder.setConnectionManager(buildPoolingHttpClientConnectionManager());
        httpClientBuilder.setUserAgent(QyStr.isNotEmpty(site.getUserAgent()) ? site.getUserAgent() : "");

        if (site.isUseGzip()) {
//            httpClientBuilder.addInterceptorFirst(new GzipInterceptor());
//            ExecChainHandler gzipInterceptor = (ExecChainHandler) new GzipInterceptor();
//            httpClientBuilder.addExecInterceptorLast("gzip", gzipInterceptor);
        }

        //httpClientBuilder.setKeepAliveStrategy(new MyConnectionKeepAliveStrategy(site.getTimeOut()));

        //解决post/redirect/post 302跳转问题
        //httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());

        // 设置Socket过期时间
        //SocketConfig socketConfig = HttpClientCreatorUtil.buildSocketConfig(site.getTimeOut());
//        httpClientBuilder.setDefaultSocketConfig(socketConfig);
//        connectionManager.setDefaultSocketConfig(socketConfig);

        // 建立Retry的机制，也可以使用上面的 buildRetryHandler
//        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));
        httpClientBuilder.setRetryStrategy(new DefaultHttpRequestRetryStrategy(site.getRetryTimes(), TimeValue.ofSeconds(site.getSleepTime())));

        // 生成Cookie
        generateCookie(httpClientBuilder, site);

        return httpClientBuilder.build();
    }


}
