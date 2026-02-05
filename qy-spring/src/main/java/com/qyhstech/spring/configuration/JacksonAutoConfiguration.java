package com.qyhstech.spring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.json.QyJacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * 还是替换掉用 {@link JacksonConfig} 简单一点
 * spring.mvc.converters.preferred-json-mapper: jackson
 */
//@Configuration
@Deprecated
@Slf4j
public class JacksonAutoConfiguration {

    /**
     * jackson2 json序列化 null字段输出为空串
     *
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】加载 ObjectMapper Jackson自动配置器  >>>>>>>>>>>>>>>>>>");
        return QyJacksonFactory.getObjectMapper();
    }

    /**
     * 注入Jackson，以支持日期转换和中文
     * 注意：如果想要单个bean的某个日期字段显示年月日时分秒的话，只需要在对应日期的get方法上添加@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")即可。
     *
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】加载 MappingJackson2HttpMessageConverter 使用Jackson配置  >>>>>>>>>>>>>>>>>>");

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(QyJacksonFactory.getObjectMapper());

        //设置中文编码格式
        List<MediaType> list = QyList.of(MediaType.APPLICATION_JSON);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);

        return mappingJackson2HttpMessageConverter;
    }

}
