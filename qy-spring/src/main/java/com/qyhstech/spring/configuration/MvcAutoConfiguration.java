package com.qyhstech.spring.configuration;

import com.qyhstech.core.json.QyJacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * MVC 通用配置
 */
//@Configuration
//@ConditionalOnProperty(name = "mvc.enabled")// 必须开启nvc.set才会使用此配置
@Slf4j
public class MvcAutoConfiguration extends WebMvcConfigurationSupport {

    /**
     * 自定义JSON输入输出格式
     *
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(QyJacksonFactory.getObjectMapper());
        log.info("<<<<<<<<<<<<<<< 【QyCore】MvcAutoConfiguration 自定义JSON输入输出格式 >>>>>>>>>>>>>>>>>>");
        return jsonConverter;
    }

    /**
     * 扩展输出
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customJackson2HttpMessageConverter());
    }

}
