package com.qyhstech.spring.aop;

import lombok.Data;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

@Data
public abstract class BaseAdvisor implements PointcutAdvisor, BeanFactoryAware {
    protected BeanFactory beanFactory;
    private String expression;
    private Advice advice;

    @Override
    public final Advice getAdvice() {
        return this.advice;
    }

    @Override
    public final Pointcut getPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(this.expression);
        pointcut.setBeanFactory(this.beanFactory);
        return pointcut;
    }

    protected abstract String getName();
}