package com.qyhstech.spring.aop.advisor;

import com.qyhstech.spring.aop.BaseAdvisor;
import com.qyhstech.spring.aop.PackAdvisor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@PackAdvisor
class LogAdvisor extends BaseAdvisor {
    public LogAdvisor() {
        setAdvice(new MethodInterceptor() {
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("记录日志...");
                Object ret = invocation.proceed();
                return ret;
            }
        });
        setExpression("execution(* com.pack..*.*(..))");
    }

    @Override
    protected String getName() {
        return "log";
    }
}