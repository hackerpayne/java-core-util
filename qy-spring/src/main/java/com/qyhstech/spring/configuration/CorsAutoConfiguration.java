package com.qyhstech.spring.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

/**
 * 全局跨域设置，使用时：
 * 使用示例：
 * cors:
 * enabled: true
 * methods: GET,POST
 * domains: www.s.com
 * paths:
 */
//@Configuration
@ConditionalOnWebApplication //必须是Web项目
@Slf4j
public class CorsAutoConfiguration {

    /**
     * 不用nginx来实现反向代理(前后端分离)支持第三方项目跨域引用
     * 简单跨域就是GET，HEAD和POST请求，但是POST请求的"Content-Type"只能是application/x-www-form-urlencoded, multipart/form-data 或 text/plain
     * 反之，就是非简单跨域，此跨域有一个预检机制，说直白点，就是会发两次请求，一次OPTIONS请求，一次真正的请求
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {

        log.info("<<<<<<<<<<<<<<< 【QyCore】初始化 Cors 自动配置跨域 >>>>>>>>>>>>>>>>>>");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = getCorsConfiguration();

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    /**
     * 配置跨域信息
     *
     * @return
     */
    private static CorsConfiguration getCorsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许cookies跨域
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*")); // 设置访问源地址
        config.setAllowedMethods(Collections.singletonList("*")); // 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        config.setAllowedHeaders(Collections.singletonList("*")); // 允许访问的头信息,*表示全部
        config.setExposedHeaders(Collections.singletonList("*")); //  配置header属性
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);

        //  配置header属性
//        config.setExposedHeaders(Arrays.asList("Authorization", "X-Total-Count", "Link", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));

        return config;
    }

}