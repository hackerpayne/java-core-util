package com.qyhstech.core.json;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * jackson获取工具
 */
public class QyJacksonFactory {

    /**
     * 获取普通的ObjectMapper对象，不会存对象的类信息
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return getObjectMapper(false);
    }

    /**
     * 带对象信息的Jackson获取
     *
     * @return
     */
    public static ObjectMapper getObjectMapperWithType() {
        return getObjectMapper(true);
    }

    /**
     * 获取一个ObjectMapper对象，处理了时间和Long型精度失效的问题
     *
     * @return
     */
    private static ObjectMapper getObjectMapper(boolean withJsonType) {

        // 1、解决查询缓存转换异常的问题
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getDefault());// 设置 java.util.Date, Calendar 序列化、反序列化的时区

        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS) // 对象的所有字段全部列入，不管是否null
                .setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)) // 设置 java.util.Date, Calendar 序列化、反序列化的格式
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 反序列化时，json 中包含 pojo 不存在属性时，是否抛异常
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) // 是否将空字符串序列化为null
//                .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true) // 是否将空数组序列化为null
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true) // 序列化：序列化BigDecimal时不使用科学计数法输出
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false) // 忽略空Bean转json的错误
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);// 禁止将 java.util.Date、Calendar 序列化为数字(时间戳)

        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        if (withJsonType) {
            //         此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
            //        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            //        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

//            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
//                    ObjectMapper.DefaultTyping.NON_FINAL,//类名序列化到json串中
//                    JsonTypeInfo.As.WRAPPER_ARRAY);

        } else {
            // 作为工具类，需要取消这个属性，当存到Redis或文件中时，需要手动使用上一行的代码，这样会把Class类型自动存下来
            objectMapper.deactivateDefaultTyping();
        }

        // 2、序列换成json时,将所有的long变成string,因为js中得数字类型不能包含所有的java long值
        SimpleModule simpleModule = new SimpleModule();

        // 解决Long丢失精度问题
        simpleModule.addSerializer(Long.class, new LongToStringSerializer());
        simpleModule.addSerializer(Long.TYPE, new LongToStringSerializer());
        simpleModule.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);
        simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        // 3、解决jackson2无法反序列化LocalDateTime的问题
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 序列化日期格式
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));

        // 反序列化日期格式
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));

        // 注册模块
        objectMapper.registerModule(javaTimeModule);

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 默认时区设置为上海
        objectMapper.setTimeZone(TimeZone.getDefault()); // 使用默认时区

        return objectMapper;
    }
}
