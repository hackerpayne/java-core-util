package com.qyhstech.core.httpclient.context;

import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.protocol.HttpClientContext;

/**
 * 上下文管理
 */
@Getter
@Setter
public class HttpClientRequestContext {

    private HttpUriRequest httpUriRequest;

    private HttpClientContext httpClientContext;

}
