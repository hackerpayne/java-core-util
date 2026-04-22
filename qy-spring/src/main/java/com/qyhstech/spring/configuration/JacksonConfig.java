package com.qyhstech.spring.configuration;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.qyhstech.core.json.serializer.BigNumberSerializer;
import com.qyhstech.core.json.serializer.LongToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.SpringHandlerInstantiator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * jackson 配置
 */
@Slf4j
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer(ApplicationContext ctx) {
        return builder -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 全局配置序列化返回 JSON 处理
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(Long.class, BigNumberSerializer.INSTANCE);
            javaTimeModule.addSerializer(Long.TYPE, BigNumberSerializer.INSTANCE);
            javaTimeModule.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);
            javaTimeModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);

            // 序列化工具
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(formatter));

            // 反序列化工具
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));
            javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(formatter));
            builder.modules(javaTimeModule);

            builder.serializerByType(BigInteger.class, new LongToStringSerializer());
            builder.serializerByType(Long.class, new LongToStringSerializer());
            builder.timeZone(TimeZone.getDefault());

            // 尝试从Spring容器中读取转换实例
            builder.postConfigurer(mapper ->
                    mapper.setHandlerInstantiator(
                            new SpringHandlerInstantiator(ctx.getAutowireCapableBeanFactory())
                    ));
            log.info("<<<<<<<<<<<<<<< 【QyCore】初始化 Jackson  >>>>>>>>>>>>>>>>>>");
        };
    }

}
