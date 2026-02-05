package com.qyhstech.spring.aop;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.json.QyFastJson;
import com.qyhstech.spring.QyClass;
import com.qyhstech.spring.QyRequest;
import com.qyhstech.core.spring.QySpringContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.MethodParameter;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 切面专用工具类
 */
public class QyAop {

    /**
     * 根据切面信息，生成连接结果字符串
     *
     * @param joinPoint
     * @param concatStr
     * @return
     */
    public static String getJoinPointStr(JoinPoint joinPoint, String concatStr) {
        StringBuilder sb = new StringBuilder();
        // 获取类名、方法名，组成参数列表
        String className = joinPoint.getTarget().getClass().getSimpleName();
        sb.append(className).append(concatStr);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        sb.append(methodName).append(concatStr);

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        String argString = Arrays.stream(args)
                .map(Object::toString)
                .collect(Collectors.joining(concatStr));
        return sb.append(argString).toString();
    }

    /**
     * 获取请求的参数，这个是会过滤掉multipartFile的
     *
     * @param joinPoint         操作日志
     * @param excludeParamNames 需要过滤的字段
     */
    public static String getRequestParam(JoinPoint joinPoint, String[] excludeParamNames) {
        Map<String, String[]> map = QyRequest.getRequest().getParameterMap();

        String param = "";
        if (MapUtil.isNotEmpty(map)) {
            String params = QyFastJson.toJsonString(map, excludeParamNames);
            param = StrUtil.sub(params, 0, 2000);
        } else {
            Object args = joinPoint.getArgs();
            if (Objects.nonNull(args)) {
                String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
                param = StrUtil.sub(params, 0, 2000);
            }
        }
        return param;
    }

    /**
     * 参数拼装
     *
     * @param paramsArray
     * @param excludeParamNames
     * @return
     */
    public static String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (ArrayUtil.isNotEmpty(paramsArray)) {
            for (Object o : paramsArray) {
                if (Objects.nonNull(o) && !isFilterObject(o)) {
                    try {
                        Object jsonObj = QyFastJson.toJsonString(o, excludeParamNames);
                        params.append(jsonObj.toString()).append(" ");
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public static boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }

    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

//    /**
    //     * 参考用法
    //     * @param point
    //     * @return
    //     */
    //    private boolean handleAuth(ProceedingJoinPoint point) {
    //        MethodSignature ms = point.getSignature() instanceof MethodSignature ? (MethodSignature) point.getSignature() : null;
    //        Method method = ms.getMethod();
    //        // 读取权限注解，优先方法上，没有则读取类
    //        PreAuth preAuth = ClassUtil.getAnnotation(method, PreAuth.class);
    //        // 判断表达式
    //        String condition = preAuth.value();
    //        if (StrUtil.isNotBlank(condition)) {
    //            Expression expression = EXPRESSION_PARSER.parseExpression(condition);
    //            // 方法参数值
    //            Object[] args = point.getArgs();
    //            StandardEvaluationContext context = getEvaluationContext(method, args,clz);
    //            //获取解析计算的结果
    //            return expression.getValue(context, Boolean.class);
    //        }
    //        return false;
    //    }

    /**
     * 获取方法上的参数
     *
     * @param method 方法
     * @param args   变量
     * @return {SimpleEvaluationContext}
     */
    private StandardEvaluationContext getEvaluationContext(Method method, Object[] args, Class<?> clz) {
        // 初始化SpEL表达式上下文，并设置 AuthFun
        StandardEvaluationContext context = new StandardEvaluationContext(clz);
        // 设置表达式支持spring bean
        context.setBeanResolver(new BeanFactoryResolver(QySpringContext.getApplicationContext()));
        for (int i = 0; i < args.length; i++) {
            // 读取方法参数
            MethodParameter methodParam = QyClass.getMethodParameter(method, i);
            // 设置方法参数名和值为SpEL变量
            context.setVariable(methodParam.getParameterName(), args[i]);
        }
        return context;
    }
}
