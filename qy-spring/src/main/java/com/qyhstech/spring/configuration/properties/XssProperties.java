package com.qyhstech.spring.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * xss过滤 配置属性
 */
@Data
@ConfigurationProperties(prefix = "xss")
public class XssProperties {

    /**
     * Xss开关
     */
    private Boolean enabled = false;

    /**
     * 排除路径
     */
    private List<String> excludeUrls = new ArrayList<>();

}
