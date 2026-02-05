package com.qyhstech.core.domain.dto;

import com.qyhstech.core.collection.QyMap;
import com.qyhstech.core.domain.base.QyBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * URL实体类,解析URL的链接,请求字符串,参数等
 * Created by Kyle on 16/8/29.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ModelUrl extends QyBaseEntity {

    private String url;
    private String cleanUrl;
    private String queryStr;
    private String fragment;
    private Map<String, String> queryMap = QyMap.empty();

    /**
     * 构造函数
     *
     * @param url URL地址
     */
    public ModelUrl(String url) {
        this.url = url;
    }

}
