package com.qyhstech.core.domain.dto;

import com.qyhstech.core.domain.base.QyBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.Proxy;

/**
 * 代理IP实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelProxy extends QyBaseEntity {

    private String host;

    private int port;

    private String username;

    private String password;


    /**
     * @param host
     * @param port
     */
    public ModelProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 获取代理的字符串表达形式
     *
     * @return
     */
    public String getProxyStr() {
        return this.host.trim() + ":" + this.port;
    }

    public Proxy buildProxy() {
        return new Proxy(Proxy.Type.HTTP, new java.net.InetSocketAddress(this.host, this.port));
    }

}