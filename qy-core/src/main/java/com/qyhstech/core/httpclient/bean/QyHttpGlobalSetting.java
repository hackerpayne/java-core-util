package com.qyhstech.core.httpclient.bean;

import com.qyhstech.core.domain.constant.QyHttpStatusCodeConst;

import java.util.*;

/**
 * Http请求全局设置，公共的配置
 */
public class QyHttpGlobalSetting {

    /**
     * 默认返回码列表
     */
    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();

    static {
        DEFAULT_STATUS_CODE_SET.add(QyHttpStatusCodeConst.CODE_200);
    }

    /**
     * 设置请求的域名
     */
    private String domain;
    /**
     * 设置请求公用的UA头
     */
    private String userAgent;
    /**
     * 默认Cookie列表
     */
    private Map<String, String> defaultCookies = new LinkedHashMap<String, String>();
    /**
     * Cookie列表
     */
    private Map<String, Map<String, String>> cookies = new HashMap<String, Map<String, String>>();
    /**
     * 默认编码
     */
    private String charset;
    /**
     * 每次请求休息时间
     */
    private int sleepTime = 5000;
    /**
     * 重试时间间隔
     */
    private int retryTimes = 0;
    /**
     * 重试次数
     */
    private int cycleRetryTimes = 0;
    /**
     * 重试时间间隔
     */
    private int retrySleepTime = 1000;
    /**
     * 默认超时时间
     */
    private int timeOut = 5000;
    private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;
    /**
     * 设置Header表头列表
     */
    private Map<String, String> headers = new HashMap<String, String>();
    /**
     * 是否设置GZip
     */
    private boolean useGzip = true;
    /**
     * 是否关闭Cookie
     */
    private boolean disableCookieManagement = false;

    /**
     * new a Site
     *
     * @return new site
     */
    public static QyHttpGlobalSetting me() {
        return new QyHttpGlobalSetting();
    }

    /**
     * Add a cookie with entity {@link #getDomain()}
     *
     * @param name  name
     * @param value value
     * @return this
     */
    public QyHttpGlobalSetting addCookie(String name, String value) {
        defaultCookies.put(name, value);
        return this;
    }

    /**
     * Add a cookie with specific entity.
     *
     * @param domain entity
     * @param name   name
     * @param value  value
     * @return this
     */
    public QyHttpGlobalSetting addCookie(String domain, String name, String value) {
        if (!cookies.containsKey(domain)) {
            cookies.put(domain, new HashMap<String, String>());
        }
        cookies.get(domain).put(name, value);
        return this;
    }

    /**
     * get cookies
     *
     * @return get cookies
     */
    public Map<String, String> getCookies() {
        return defaultCookies;
    }

    /**
     * get cookies of all domains
     *
     * @return get cookies
     */
    public Map<String, Map<String, String>> getAllCookies() {
        return cookies;
    }

    /**
     * get user agent
     *
     * @return user agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * set user agent
     *
     * @param userAgent userAgent
     * @return this
     */
    public QyHttpGlobalSetting setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * get entity
     *
     * @return get entity
     */
    public String getDomain() {
        return domain;
    }

    /**
     * set the entity of site.
     *
     * @param domain entity
     * @return this
     */
    public QyHttpGlobalSetting setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * get charset set manually
     *
     * @return charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Set charset of page manually.<br>
     * When charset is not set or set to null, it can be auto detected by Http header.
     *
     * @param charset charset
     * @return this
     */
    public QyHttpGlobalSetting setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public int getTimeOut() {
        return timeOut;
    }

    /**
     * set timeout for downloader in ms
     *
     * @param timeOut timeOut
     * @return this
     */
    public QyHttpGlobalSetting setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    /**
     * get acceptStatCode
     *
     * @return acceptStatCode
     */
    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }

    /**
     * Set acceptStatCode.<br>
     * When status code of http response is in acceptStatCodes, it will be processed.<br>
     * {200} by default.<br>
     * It is not necessarily to be set.<br>
     *
     * @param acceptStatCode acceptStatCode
     * @return this
     */
    public QyHttpGlobalSetting setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
        return this;
    }

    /**
     * Get the interval between the processing of two pages.<br>
     * Time unit is micro seconds.<br>
     *
     * @return the interval between the processing of two pages,
     */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * Set the interval between the processing of two pages.<br>
     * Time unit is micro seconds.<br>
     *
     * @param sleepTime sleepTime
     * @return this
     */
    public QyHttpGlobalSetting setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    /**
     * Get retry times immediately when download fail, 0 by default.<br>
     *
     * @return retry times when download fail
     */
    public int getRetryTimes() {
        return retryTimes;
    }

    /**
     * Set retry times when download fail, 0 by default.<br>
     *
     * @param retryTimes retryTimes
     * @return this
     */
    public QyHttpGlobalSetting setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Put an Http header for downloader. <br>
     * Use {@link #addCookie(String, String)} for cookie and {@link #setUserAgent(String)} for user-agent. <br>
     *
     * @param key   key of http header, there are some keys constant in {@link QyHttpHeaderConst}
     * @param value value of header
     * @return this
     */
    public QyHttpGlobalSetting addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * When cycleRetryTimes is more than 0, it will add back to scheduler and try download again. <br>
     *
     * @return retry times when download fail
     */
    public int getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    /**
     * Set cycleRetryTimes times when download fail, 0 by default. <br>
     *
     * @param cycleRetryTimes cycleRetryTimes
     * @return this
     */
    public QyHttpGlobalSetting setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
        return this;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    /**
     * Whether use gzip. <br>
     * Default is true, you can set it to false to disable gzip.
     *
     * @param useGzip useGzip
     * @return this
     */
    public QyHttpGlobalSetting setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    /**
     * Set retry sleep times when download fail, 1000 by default. <br>
     *
     * @param retrySleepTime retrySleepTime
     * @return this
     */
    public QyHttpGlobalSetting setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
        return this;
    }

    public boolean isDisableCookieManagement() {
        return disableCookieManagement;
    }

    /**
     * Downloader is supposed to store response cookie.
     * Disable it to ignore all cookie fields and stay clean.
     * Warning: Set cookie will still NOT work if disableCookieManagement is true.
     *
     * @param disableCookieManagement disableCookieManagement
     * @return this
     */
    public QyHttpGlobalSetting setDisableCookieManagement(boolean disableCookieManagement) {
        this.disableCookieManagement = disableCookieManagement;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QyHttpGlobalSetting site = (QyHttpGlobalSetting) o;

        if (cycleRetryTimes != site.cycleRetryTimes) return false;
        if (retryTimes != site.retryTimes) return false;
        if (sleepTime != site.sleepTime) return false;
        if (timeOut != site.timeOut) return false;
        if (acceptStatCode != null ? !acceptStatCode.equals(site.acceptStatCode) : site.acceptStatCode != null)
            return false;
        if (charset != null ? !charset.equals(site.charset) : site.charset != null) return false;
        if (defaultCookies != null ? !defaultCookies.equals(site.defaultCookies) : site.defaultCookies != null)
            return false;
        if (domain != null ? !domain.equals(site.domain) : site.domain != null) return false;
        if (headers != null ? !headers.equals(site.headers) : site.headers != null) return false;
        if (userAgent != null ? !userAgent.equals(site.userAgent) : site.userAgent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = domain != null ? domain.hashCode() : 0;
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        result = 31 * result + (defaultCookies != null ? defaultCookies.hashCode() : 0);
        result = 31 * result + (charset != null ? charset.hashCode() : 0);
        result = 31 * result + sleepTime;
        result = 31 * result + retryTimes;
        result = 31 * result + cycleRetryTimes;
        result = 31 * result + timeOut;
        result = 31 * result + (acceptStatCode != null ? acceptStatCode.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QyHttpGlobalSetting{" +
                "entity='" + domain + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", cookies=" + defaultCookies +
                ", charset='" + charset + '\'' +
                ", sleepTime=" + sleepTime +
                ", retryTimes=" + retryTimes +
                ", cycleRetryTimes=" + cycleRetryTimes +
                ", timeOut=" + timeOut +
                ", acceptStatCode=" + acceptStatCode +
                ", headers=" + headers +
                '}';
    }

}
