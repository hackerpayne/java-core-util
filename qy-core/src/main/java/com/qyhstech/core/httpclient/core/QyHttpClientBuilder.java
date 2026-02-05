package com.qyhstech.core.httpclient.core;

import cn.hutool.core.lang.Pair;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.CredentialsProviderBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class QyHttpClientBuilder {

    /**
     * 自动信任X509证书
     *
     * @return
     */
    public static X509TrustManager createX509TrustManager() {

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
//                return new java.security.cert.X509Certificate[]{};
                return null;
            }
        };
        return x509TrustManager;
    }

    /**
     * 绕过所有主机验证
     *
     * @return
     */
    public static HostnameVerifier createHostNameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return hostnameVerifier;
    }

    /**
     * 创建默认的TLSConfig配置
     *
     * @return
     */
    public static TlsConfig buildTlsConfig() {
        return buildTlsConfig(Timeout.ofSeconds(30));
    }

    /**
     * 创建默认的TlsConfig，默认30秒超时时间
     *
     * @param handshakeTimeout
     * @return
     */
    public static TlsConfig buildTlsConfig(Timeout handshakeTimeout) {
        return TlsConfig.custom()
                .setHandshakeTimeout(handshakeTimeout)
                .setSupportedProtocols(TLS.V_1_0, TLS.V_1_1, TLS.V_1_2, TLS.V_1_3)
                .build();
    }

    /**
     * 创建SSL Context上下文
     *
     * @return
     */
    public static SSLContext buildSslContext() {
        return buildSslContext(false);
    }

    /**
     * SSL证书绕过配置：如果使用默认配置，就直接跳过，否则使用自定义的设置
     *
     * @param useDefault 如果是使用默认的，就直接跳过去
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext buildSslContext(boolean useDefault) {

        // 默认即使用系统默认的配置
        SSLContext sc = SSLContexts.createSystemDefault();

        if (!useDefault) {
            try {
                // 方法一
                //sc = SSLContexts.custom().loadTrustMaterial(null, new TrustAllStrategy()).build();

                // 方法二
                sc = SSLContext.getInstance("SSLv3");
                sc.init(null, new TrustManager[]{createX509TrustManager()}, null);
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
//        sc.init(null, new TrustManager[]{createX509TrustManager()}, new SecureRandom());
        }

        return sc;
    }

    /**
     * 创建原生的SSLFactory
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLSocketFactory buildSslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        return buildSslContext().getSocketFactory();
    }

    /**
     * 创建 Registry<ConnectionSocketFactory>
     *
     * @return
     */
    public static Registry<ConnectionSocketFactory> buildRegistrySslConnectionSocketFactory() {
        return buildRegistrySslConnectionSocketFactory(buildSslContext(false));
    }

    /**
     * 创建Registry<ConnectionSocketFactory>，等同于下面的SSLConnectionSocketFactory，
     * 用时：PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
     * socketFactoryRegistry,
     * PoolConcurrencyPolicy.STRICT,
     * PoolReusePolicy.LIFO,
     * TimeValue.ofMinutes(5), null, dnsResolver,
     * connFactory);
     *
     * @param sslContext
     * @return
     */
    public static Registry<ConnectionSocketFactory> buildRegistrySslConnectionSocketFactory(SSLContext sslContext) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSslConnectionSocketFactory(sslContext))
                //.register("https", new SSLConnectionSocketFactory(sslContext))
                .build();

        return registry;
    }

    /**
     * 自动创建可以处理HTTPS的连接工厂
     *
     * @return
     */
    public static SSLConnectionSocketFactory buildSslConnectionSocketFactory() {
        return buildSslConnectionSocketFactory(buildSslContext());
    }

    /**
     * 创建SSL认证的过滤绕过规则
     *
     * @return
     */
    public static SSLConnectionSocketFactory buildSslConnectionSocketFactory(SSLContext sslContext) {

        // 支持TLS 1.2
        // javax.net.ssl.SSLException: Received fatal alert: protocol_version 参考：https://www.cnblogs.com/sunny08/p/8038440.html
        if (sslContext == null) {
            sslContext = buildSslContext();
        }
        return new SSLConnectionSocketFactory(sslContext, new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, null, createHostNameVerifier()); // 优先绕过安全证书

        // return new SSLConnectionSocketFactory(buildSslContext()); // 优先绕过安全证书
        // return SSLConnectionSocketFactoryBuilder.create().setSslContext(sslContext).build();
        // return new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    /**
     * @param credentials
     * @return
     */
    public static CredentialsProvider buildCredentialsProvider(Map<String, Pair<String, String>> credentials) {
        CredentialsProviderBuilder credentialsProviderBuilder = CredentialsProviderBuilder.create();
        if (credentials != null) {
            for (Map.Entry<String, Pair<String, String>> entry : credentials.entrySet()) {
                String key = entry.getKey();
                Pair<String, String> pair = entry.getValue();
                String[] split = key.split(":");
                HttpHost httpHost = new HttpHost(split[0], split.length > 1 ? Integer.parseInt(split[1]) : 80);
                credentialsProviderBuilder.add(httpHost, pair.getKey(), pair.getValue().toCharArray());
            }
        }
        return credentialsProviderBuilder.build();
    }

    /**
     * HTTP 基本认证（Basic Authorization）是一种比较简单的认证实现
     * 使用时：CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
     *
     * @param url
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static BasicCredentialsProvider buildBasicCredentials(String url, Integer port, String username, String password) {
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(url, port), new UsernamePasswordCredentials(username, password.toCharArray()));
        return credsProvider;
    }

    /**
     * 创建默认的DnsResolver
     *
     * @return
     */
    public static DnsResolver buildDnsResolver() {
        return buildDnsResolver(null);
    }

    /**
     * 自行创建
     *
     * @param resolves
     * @return
     */
    public static DnsResolver buildDnsResolver(Map<String, InetAddress> resolves) {

        DnsResolver dnsResolver = new SystemDefaultDnsResolver();

        if (!resolves.isEmpty()) {
            dnsResolver = new SystemDefaultDnsResolver() {
                @Override
                public InetAddress[] resolve(final String host) throws UnknownHostException {
                    final InetAddress inetAddress = resolves.get(host);
                    if (inetAddress != null) {
                        return new InetAddress[]{inetAddress};
                    }
                    return super.resolve(host);
                }
            };
        }
        return dnsResolver;
    }

    /**
     * 设置连接和传输超时为一样的
     *
     * @param timeout 超时设置
     * @return
     */
    public static RequestConfig buildRequestConfig(Timeout timeout) {
        return buildRequestConfig(timeout, timeout);
    }

    /**
     * 创建 RequestConfig
     *
     * @param responseTimeout   传输超时
     * @param connectionTimeout
     * @return
     */
    public static RequestConfig buildRequestConfig(Timeout responseTimeout, Timeout connectionTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                //.setConnectTimeout(timeout)                   // 建立连接超时时间
                .setResponseTimeout(responseTimeout)                    // 传输超时
                .setConnectionRequestTimeout(connectionTimeout)         // 设置从连接池获取连接实例的超时
                .build();
        return requestConfig;
    }

    /**
     * 默认10秒，100个线程
     *
     * @return
     */
    public static HttpClientBuilder buildHttpClientBuilder() {
        return buildHttpClientBuilder(Timeout.ofSeconds(10), 100);
    }

    /**
     * 默认100个线程
     *
     * @param timeout
     * @return
     */
    public static HttpClientBuilder buildHttpClientBuilder(Timeout timeout) {
        return buildHttpClientBuilder(timeout, 100);
    }

    /**
     * 创建一个常用的配置器，用于生成HttpClients，使用时
     * HttpClientBuilder httpBuilder = buildHttpClientBuilder(Timeout.ofSeconds(5));
     * httpClient = httpBuilder.build();
     *
     * @param timeout
     * @return
     */
    public static HttpClientBuilder buildHttpClientBuilder(Timeout timeout, Integer poolSize) {

        // 1、创建连接配置
        RequestConfig requestConfig = buildRequestConfig(timeout);

        // 2、创建Cookie存储器，也可以在后面再设置
        //final BasicCookieStore defaultCookieStore = new BasicCookieStore();

        // 3、创建SSL连接配置 自定义 SSL 策略
        Registry<ConnectionSocketFactory> registry = buildRegistrySslConnectionSocketFactory();

        // 4.1、设置连接池
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(registry);
        connMgr.setMaxTotal(poolSize); // 设置连接池大小
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal()); // 设置每条路由的最大并发连接数

        // 4.2 设置连接池，等同于上面的代码，如果需要SSL请求访问的时候，需要按下面的设置处理
        //HttpClientConnectionManager connManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create()
        //        .setSSLSocketFactory(buildSslConnectionSocketFactory(buildSslContext()))
        //        .setDefaultConnectionConfig(buildConnectionConfig(timeout))
        //        .setMaxConnTotal(poolSize)
        //        .setMaxConnPerRoute(connMgr.getMaxTotal())
        //        .build();

        HttpClientBuilder builder = HttpClients.custom()
                //.setDefaultCookieStore(defaultCookieStore)
                .setConnectionManager(connMgr)
                //.setConnectionManager(connManagerBuilder) // 使用Builder创建的结果
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, TimeValue.ofSeconds(3)))  // 重试 3 次，间隔3s
                .setDefaultRequestConfig(requestConfig);
        builder.evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(5));
        return builder;
    }

    /**
     * 创建默认的PoolingHttpClientConnectionManager对象管理线程池
     *
     * @return
     */
    public static PoolingHttpClientConnectionManager buildConnectionManager(Timeout timeout) {

        PoolingHttpClientConnectionManagerBuilder connectionManager = PoolingHttpClientConnectionManagerBuilder.create();

        // 设置SSL连接 SSLConnectionSocketFactoryBuilder
        connectionManager.setSSLSocketFactory(buildSslConnectionSocketFactory());

        // 设置 SocketConfig
        connectionManager.setDefaultSocketConfig(buildSocketConfig(timeout));

        // 设置PoolConcurrencyPolicy
        connectionManager.setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT).setConnPoolPolicy(PoolReusePolicy.LIFO);

        // 设置连接超时 ConnectionConfig
        connectionManager.setDefaultConnectionConfig(buildConnectionConfig(timeout));

        return connectionManager.build();
    }

    /**
     * 创建SocketConfig
     *
     * @param timeout
     * @return
     */
    public static SocketConfig buildSocketConfig(Timeout timeout) {
        return SocketConfig.custom()
                .setSoTimeout(timeout)
                .build();
    }

    /**
     * 创建ConnectionConfig
     *
     * @param timeout
     * @return
     */
    public static ConnectionConfig buildConnectionConfig(Timeout timeout) {
        return ConnectionConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setTimeToLive(TimeValue.ofMinutes(10))
                .build();
    }

}
