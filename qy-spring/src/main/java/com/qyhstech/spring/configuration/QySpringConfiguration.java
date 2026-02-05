package com.qyhstech.spring.configuration;

import com.qyhstech.core.spring.QySpringContext;
import com.qyhstech.spring.advice.QyGlobalExceptionAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 自动使用Spring上下文管理器
 */
@AutoConfiguration
@Slf4j
public class QySpringConfiguration {

    @Bean
    public QySpringContext qySpringContext() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】初始化 QySpringContext 上下文管理工具 >>>>>>>>>>>>>>>>>>");
        return new QySpringContext();
    }

    /**
     * 全局异常处理器
     */
    @Bean
    public QyGlobalExceptionAdvice qyGlobalExceptionHandler() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】初始化 GlobalException 全局异常处理器  >>>>>>>>>>>>>>>>>>");
        return new QyGlobalExceptionAdvice();
    }

}
