package com.qyhstech.core.httpclient.bean;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 抓取请求类
 */
@Getter
@Setter
@ToString
public class QyHttpRequest implements Serializable {

    /**
     * 保存重试信息的字符串标记
     */
    public static final String CYCLE_TRIED_TIMES = "_cycle_tried_times";

    /**
     * 需要请求的URL
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * HttpBody原始数据
     */
    private QyHttpRequestBody requestBody;

    /**
     * 保存额外的数据
     */
    private Map<String, Object> extras;

    /**
     * 添加的Cookie列表
     */
    private Map<String, String> cookies = new HashMap<String, String>();

    /**
     * 添加的头部信息列表
     */
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * 如果为True将不会解析内容为String格式
     */
    private boolean binaryContent = false;

    /**
     * 单独的字符串编码
     */
    private String charset;
    /**
     * 是否返回Header信息，默认是不需要的
     */
    private boolean returnHeader = false;

    public QyHttpRequest() {
    }

    public QyHttpRequest(String url) {
        this.url = url;
    }

    /**
     * 获取额外字段
     *
     * @param key
     * @return
     */
    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    /**
     * 放置额外字段
     *
     * @param key
     * @param value
     * @return
     */
    public QyHttpRequest putExtra(String key, Object value) {
        if (extras == null) {
            extras = new HashMap<String, Object>();
        }
        extras.put(key, value);
        return this;
    }

    public QyHttpRequest addCookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }

    public QyHttpRequest addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }


}
