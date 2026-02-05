package com.qyhstech.spring.context;

import com.qyhstech.core.domain.enums.QyYesNoEnum;
import com.qyhstech.spring.service.DictService;
import org.springframework.stereotype.Component;

/**
 * 会员注册策略上下文
 */
//@Component
//public class RegisterStrategyContext extends AbstractStrategyContext<DictService, QyYesNoEnum> {
//
//    public RegisterStrategyContext() {
//        super(DictService.class, "RegisterStrategyContext");
//    }
//
//    @Override
//    public <R, P> R execute(QyYesNoEnum strategyType, P params) {
//        DictService strategy = getStrategy(strategyType);
//        return (R) strategy.register((RegisterRequest) params);
//    }
//
//    // 业务特定方法
//    public RegisterResult register(RegisterTypeEnum registerType, RegisterRequest request) {
//        return execute(registerType, request);
//    }
//
//    @Override
//    protected void validateStrategies() {
//        // 验证所有注册类型都有对应实现
//        for (RegisterTypeEnum type : RegisterTypeEnum.values()) {
//            if (!isSupported(type)) {
//                log.warn("Missing strategy implementation for register type: {}", type.getCode());
//            }
//        }
//    }
//}