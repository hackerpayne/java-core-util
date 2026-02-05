package com.qyhstech.core.token;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.collection.QyList;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Jwt生成Token的基本操作类
 */
@Slf4j
public class QyToken {

    /**
     * 得到Token
     *
     * @param request
     * @return
     */
    public static String resolveToken(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        String authorization = request.getHeader("Authorization");
        if (StrUtil.isBlank(authorization)) {
            authorization = request.getHeader("authorization");
        }
        if (StrUtil.isBlank(authorization)) {
            authorization = request.getHeader("token");
        }
        //尝试用request里面获取权限信息
        if (StrUtil.isBlank(authorization)) {
            authorization = request.getParameter("authorization");
        }
        if (StrUtil.isBlank(authorization)) {
            authorization = request.getParameter("token");
        }
        if (StrUtil.isNotBlank(authorization)) {
            authorization = StrUtil.trim(authorization.replace("Bearer", ""));
        }
        return authorization;
    }

    /**
     * 打印所有Header里面的信息
     *
     * @param request
     */
    private void printHeaders(HttpServletRequest request) {
        Enumeration<String> e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            String headerName = e.nextElement();//透明称
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                log.info(headerName + ":" + headerValues.nextElement());
            }
        }
    }

    /**
     * 获取token，从多个地方获取，可以加上自己的自定义头信息
     *
     * @param request
     * @param headerStr
     * @return
     */
    public String getToken(HttpServletRequest request, String... headerStr) {

        //        printHeaders(request);

        Set<String> listHeaders = new HashSet<String>(Arrays.asList(headerStr));
        if (CollUtil.isEmpty(listHeaders)) {
            listHeaders.addAll(Arrays.asList("Authorization", "Token", "token"));
        }

        final Optional<String> accessToken = listHeaders.stream()
                .filter(item -> StrUtil.isNotEmpty(request.getHeader(item)))
                .map(request::getHeader)
                .findFirst();

        if (accessToken.isPresent()) { // 取到了就直接返回了
            return accessToken.get();
        }

        // 还是获取不到再从Cookie中拿
        Cookie[] cookies = request.getCookies();
        final List<String> tokenInCookie = QyList.empty();
        for (Cookie cookie : cookies) {
            listHeaders.forEach(header -> {
                if (cookie.getName().equals(header)) {
                    tokenInCookie.add(cookie.getValue());
                }
            });
        }
        return CollUtil.isEmpty(tokenInCookie) ? null : tokenInCookie.get(0);
    }

}
