package com.qyhstech.spring.aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring容器在启动过程中会通过上面的处理器，判断当前创建的Bean实例是否有相关的切入点与之匹配，如果有则会对其创建对应的代理。
 */
public class PackDynamicAutoProxyCreator extends AbstractAutoProxyCreator {
    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        BeanFactory beanFactory = getBeanFactory();
        if (beanFactory instanceof ConfigurableListableBeanFactory bf) {

            // 获取所有带注解的类
            String[] advisorNames = bf.getBeanNamesForAnnotation(PackAdvisor.class);
            List<Advisor> advisors = new ArrayList<>();
            for (String name : advisorNames) {
                advisors.add(getBeanFactory().getBean(name, Advisor.class));
            }
            return advisors.toArray();
        }
        return new Object[]{};
    }
}