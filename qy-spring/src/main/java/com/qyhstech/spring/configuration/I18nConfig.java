package com.qyhstech.spring.configuration;

import com.qyhstech.spring.i18n.I18nLocaleResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 国际化配置
 */
@AutoConfiguration(before = WebMvcAutoConfiguration.class)
@Slf4j
public class I18nConfig {

    @Bean
    public LocaleResolver localeResolver() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】初始化 i18n 多语言处理器  >>>>>>>>>>>>>>>>>>");
        return new I18nLocaleResolver();
    }

}
