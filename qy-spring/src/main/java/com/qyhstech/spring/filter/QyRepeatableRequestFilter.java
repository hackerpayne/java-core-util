package com.qyhstech.spring.filter;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * Repeatable 过滤器
 * 现在可重复读取流的请求对象构造好了，但是需要在拦截器中获取，就需要将包装后的请求对象放在拦截器中。由于filter在interceptor之前执行，因此可以通过filter进行实现。
 */
@WebFilter(filterName = "bodyReaderFilter", urlPatterns = "/*")
public class QyRepeatableRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest && StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            requestWrapper = new QyRepeatedlyRequestWrapper((HttpServletRequest) request, response);
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
