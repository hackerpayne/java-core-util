package com.qyhstech.spring.configuration;

import com.qyhstech.core.validation.QyValid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 让Hibernate支持GET里面校验
 * 使用时：
 * 1、Controller上加注解@Validated
 * 2、然后就可以使用了
 */
@Configuration
@Slf4j
public class HibernateValidatorAutoConfiguration {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        // 设置validator模式为快速失败返回,默认是普通模式，会返回所有的验证不通过信息集合
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    @Bean
    public Validator validator() {
        log.info("<<<<<<<<<<<<<<< 【QyCore】启用 Hibernate FastFail 快速失败模式 >>>>>>>>>>>>>>>>>>");
        return QyValid.getHibernateFastValidator(true);
    }

}
