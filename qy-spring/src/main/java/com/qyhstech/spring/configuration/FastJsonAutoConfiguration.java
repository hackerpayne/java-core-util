package com.qyhstech.spring.configuration;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.qyhstech.core.dates.QyDatePattern;
import com.qyhstech.core.encode.QyCharset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Collections;
import java.util.List;

/**
 * fastjson的配置
 */
//@Configuration
//@ConditionalOnClass(com.alibaba.fastjson.JSON.class)
//@ConditionalOnMissingBean(FastJsonHttpMessageConverter4.class)
//@ConditionalOnBean(FastJsonMakerConfiguration.Marker.class)
@ConditionalOnWebApplication
@EnableWebMvc
@Slf4j
public class FastJsonAutoConfiguration extends WebMvcConfigurationSupport {

    /**
     * 添加这个 Converter
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        FastJsonHttpMessageConverter httpMessageConverters = fastJsonHttpMessageConverter();
        converters.add(httpMessageConverters);
    }

    /**
     * @return
     */
//    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】使用FastJson做为Json解析库 >>>>>>>>>>>>>>>>>>");
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFastJsonConfig(fastjsonConfig());
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));

        converter.setDefaultCharset(QyCharset.CHARSET_UTF_8);
        //converters.add(0, converter);

        return converter;
    }

    /**
     * fastjson的配置
     */
    public FastJsonConfig fastjsonConfig() {

//        log.info("========使用FastJson做为Json解析库========");

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setReaderFeatures(JSONReader.Feature.FieldBased, JSONReader.Feature.SupportArrayToBean);
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteMapNullValue, JSONWriter.Feature.PrettyFormat);

        // 处理时间格式
        fastJsonConfig.setDateFormat(QyDatePattern.DATE_TIME_FORMAT);
        //fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

        fastJsonConfig.setCharset(QyCharset.CHARSET_UTF_8);

//
//        // Null处理为空
//        ValueFilter valueFilter = new ValueFilter() {
//            @Override
//            public Object process(Object o, String s, Object o1) {
//                if (null == o1) {
//                    o1 = "";
//                }
//                return o1;
//            }
//        };
//
//        // 设置为UTF8编码
//        fastJsonConfig.setCharset(Charset.forName("utf-8"));
//        fastJsonConfig.setWriterFeatures(valueFilter);
//
//        //解决Long转json精度丢失的问题
//        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
//        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
//        serializeConfig.put(Long.class, ToStringSerializer.instance);
//        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
//        fastJsonConfig.setSerializeConfig(serializeConfig);

        return fastJsonConfig;
    }

}
