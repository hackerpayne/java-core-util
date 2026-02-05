package com.qyhstech.spring.aop.advisor;

import com.qyhstech.spring.aop.BaseAdvisor;
import com.qyhstech.spring.aop.PackAdvisor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 统计业务耗时的Advisor
 */
@PackAdvisor
public class TimeTookAdvisor extends BaseAdvisor {

    public TimeTookAdvisor() {
        setAdvice(new MethodInterceptor() {
            public Object invoke(MethodInvocation invocation) throws Throwable {
                long start = System.currentTimeMillis();
                Object ret = invocation.proceed();
                System.err.printf("业务执行耗时: %dms%n", (System.currentTimeMillis() - start));
                return ret;
            }
        });
        setExpression("execution(* com.pack..*.*(..))");
    }

    @Override
    protected String getName() {
        return "timetoook";
    }
}