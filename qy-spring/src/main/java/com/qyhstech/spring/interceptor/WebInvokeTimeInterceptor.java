package com.qyhstech.spring.interceptor;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.qyhstech.core.json.QyJackson;
import com.qyhstech.core.spring.QySpringContext;
import com.qyhstech.spring.filter.QyRepeatedlyRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * web的调用时间统计拦截器
 * dev环境有效
 */
@Slf4j
public class WebInvokeTimeInterceptor implements HandlerInterceptor {

    private final String prodProfile = "prod";

    private final TransmittableThreadLocal<StopWatch> invokeTimeTL = new TransmittableThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!prodProfile.equals(QySpringContext.getActiveProfile())) {
            String url = request.getMethod() + " " + request.getRequestURI();

            // 打印请求参数
            if (isJsonRequest(request)) {
                String jsonParam = "";
                if (request instanceof QyRepeatedlyRequestWrapper) {
                    BufferedReader reader = request.getReader();
                    jsonParam = IoUtil.read(reader);
                }
                log.info("[API]开始请求 => URL[{}],参数类型[json],参数:[{}]", url, jsonParam);
            } else {
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (MapUtil.isNotEmpty(parameterMap)) {
                    String parameters = QyJackson.toJsonString(parameterMap);
                    log.info("[API]开始请求 => URL[{}],参数类型[param],参数:[{}]", url, parameters);
                } else {
                    log.info("[API]开始请求 => URL[{}],无参数", url);
                }
            }

            StopWatch stopWatch = new StopWatch();
            invokeTimeTL.set(stopWatch);
            stopWatch.start();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!prodProfile.equals(QySpringContext.getActiveProfile())) {
            StopWatch stopWatch = invokeTimeTL.get();
            stopWatch.stop();
            log.info("[API]结束请求 => URL[{}],耗时:[{}]毫秒", request.getMethod() + " " + request.getRequestURI(), stopWatch.getTotal(TimeUnit.MILLISECONDS));
            invokeTimeTL.remove();
        }
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return boolean
     */
    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (StrUtil.isNotEmpty(contentType)) {
            return StrUtil.startWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE);
        }
        return false;
    }

}
