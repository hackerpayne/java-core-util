package com.qyhstech.spring.configuration;

import com.qyhstech.spring.configuration.properties.XssProperties;
import com.qyhstech.spring.filter.QyRepeatableRequestFilter;
import com.qyhstech.spring.filter.QyXssFilter;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 配置拦截器
 */
@AutoConfiguration
@EnableConfigurationProperties(XssProperties.class)
@Slf4j
public class FilterConfig {

    @Bean
    @ConditionalOnProperty(value = "xss.enabled", havingValue = "true")
    public FilterRegistrationBean<QyXssFilter> xssFilterRegistration() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】初始化 XssFilter 过滤器  >>>>>>>>>>>>>>>>>>");
        FilterRegistrationBean<QyXssFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new QyXssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

    /**
     * BodyReaderFilter替换默认的HttpRequest对象，以实现流的重复使用
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<QyRepeatableRequestFilter> repeatableRequestFilter() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】初始化 RepeatableFilter 过滤器  >>>>>>>>>>>>>>>>>>");
        FilterRegistrationBean<QyRepeatableRequestFilter> registration = new FilterRegistrationBean<QyRepeatableRequestFilter>();
        registration.setFilter(new QyRepeatableRequestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }

}
