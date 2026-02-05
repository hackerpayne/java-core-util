package com.qyhstech.core.httpclient.bean;

import lombok.Data;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.net.SocketAddress;

@Data
public class QySocketConfig {

    // Socket配置
    private Timeout soTimeout;
    private Boolean soReuseAddress;
    private TimeValue soLinger;
    private Boolean soKeepAlive;
    private Boolean tcpNoDelay;
    private Integer sndBufSize;
    private Integer rcvBufSize;
    private Integer backlogSize;
    private SocketAddress socksProxyAddress;

}
