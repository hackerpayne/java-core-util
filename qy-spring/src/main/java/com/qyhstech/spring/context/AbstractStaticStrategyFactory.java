package com.qyhstech.spring.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用策略上下文管理器基类
 *
 * @param <T> 策略接口类型
 * @param <E> 策略类型枚举
 */
@Slf4j
public abstract class AbstractStaticStrategyFactory<T, E extends Enum<E> & StrategyType> {

    private static final Map<Class<?>, AbstractStaticStrategyFactory<?, ?>> FACTORY_MAP = new ConcurrentHashMap<>();

    protected Map<String, T> strategyMap = new ConcurrentHashMap<>();
    protected final Class<T> strategyClass;
    protected final Class<E> enumClass;
    protected final String factoryName;

    protected AbstractStaticStrategyFactory(Class<T> strategyClass, Class<E> enumClass, String factoryName) {
        this.strategyClass = strategyClass;
        this.enumClass = enumClass;
        this.factoryName = factoryName;
        // 注册到全局工厂映射
        FACTORY_MAP.put(this.getClass(), this);
    }

    /**
     * 初始化策略映射（由Spring容器调用）
     */
    public void initStrategies(ApplicationContext applicationContext) {
        Map<String, T> beans = applicationContext.getBeansOfType(strategyClass);
        strategyMap.putAll(beans);
        log.info("{} initialized with {} strategies", factoryName, strategyMap.size());
        validateStrategies();
    }

    /**
     * 获取策略实例
     */
    protected T getStrategy(E strategyType) {
        T strategy = strategyMap.get(strategyType.getCode());
        if (strategy == null) {
            throw new StrategyNotFoundException("Strategy not found: " + strategyType.getCode());
        }
        return strategy;
    }

    /**
     * 获取策略实例（静态方法供外部调用）
     */
    @SuppressWarnings("unchecked")
    public static <T, E extends Enum<E> & StrategyType> T getStrategy(Class<? extends AbstractStaticStrategyFactory<T, E>> factoryClass, E strategyType) {
        AbstractStaticStrategyFactory<T, E> factory = (AbstractStaticStrategyFactory<T, E>) FACTORY_MAP.get(factoryClass);
        if (factory == null) {
            throw new StrategyNotFoundException("Factory not initialized: " + factoryClass.getName());
        }
        return factory.getStrategy(strategyType);
    }

    /**
     * 验证策略完整性
     */
    protected void validateStrategies() {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E enumConstant : enumConstants) {
            if (!strategyMap.containsKey(enumConstant.getCode())) {
                log.warn("Missing strategy implementation for type: {}", enumConstant.getCode());
            }
        }
    }
}