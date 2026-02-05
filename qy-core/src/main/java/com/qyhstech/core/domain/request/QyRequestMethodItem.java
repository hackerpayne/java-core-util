package com.qyhstech.core.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 */
@Getter
@Setter
public class QyRequestMethodItem implements Comparable<QyRequestMethodItem> {

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestMethod;

    /**
     * 请求类型，主要是判断是否是文件上传用
     */
    private boolean requestHasFile;

    /**
     * 获取SpringSecurity注解PreAuthorize的内容
     */
    private String preAuthorize;

    /**
     * 参数明细
     */
    private List<QyRequestMethodParameter> requestParameters;

//    /**
//     * 控制器
//     */
//    private String controller;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 类名
     */
    private String className;

//    /**
//     * 完整的请求方法
//     */
//    private String methodFull;

    /**
     * 描述内容
     */
    private String desc;

    /**
     * 返回值类型，为空一般是Form，如果是Json会显示Json值
     */
    private String responseType;

    /**
     * 返回数据类型：json/form/string 3种
     */
    private String responseFormat;

    @Override
    public int compareTo(QyRequestMethodItem o) {
        return this.getRequestUrl().compareTo(o.getRequestUrl());
    }
}
