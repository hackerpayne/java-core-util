package com.qyhstech.core.httpclient.proxy;

import com.qyhstech.core.domain.dto.ModelProxy;
import com.qyhstech.core.httpclient.bean.QyHttpGlobalSetting;
import com.qyhstech.core.httpclient.bean.QyHttpResult;

/**
 * 代理IP提供的接口
 */
public interface ProxyProvider {

    /**
     * 使用完成之后，退还IP代理
     *
     * @param proxy
     * @param page
     * @param httpSetting
     */
    void returnProxy(ModelProxy proxy, QyHttpResult page, QyHttpGlobalSetting httpSetting);

    /**
     * 获取一条代理
     *
     * @param httpSetting
     * @return
     */
    ModelProxy getProxy(QyHttpGlobalSetting httpSetting);

}
