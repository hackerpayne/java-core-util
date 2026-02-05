package com.qyhstech.spring.filter;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.QyStr;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 驼峰转换过滤器，post表单里面的 fo_to转foTo
 */
@Slf4j
public class CamelCaseConvertFilter extends OncePerRequestFilter {

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final Map<String, String[]> formattedParams = new ConcurrentHashMap<>();

        log.info("<================== CamelCaseConvertFilter ==================>");

        for (String param : request.getParameterMap().keySet()) {
            String formattedParam = QyStr.toCamelCase(param);
            formattedParams.put(formattedParam, request.getParameterValues(param));
        }

        filterChain.doFilter(new HttpServletRequestWrapper(request) {
            @Override
            public String getParameter(String name) {
                return formattedParams.containsKey(name) ? formattedParams.get(name)[0] : null;
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return Collections.enumeration(formattedParams.keySet());
            }

            @Override
            public String[] getParameterValues(String name) {
                return formattedParams.get(name);
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return formattedParams;
            }
        }, response);
    }
}
