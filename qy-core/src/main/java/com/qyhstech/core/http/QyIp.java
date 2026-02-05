package com.qyhstech.core.http;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.collection.QyArray;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

/**
 * IP查询公共类
 */
public class QyIp {
    /**
     * 检测给定字符串是否为未知，多用于检测HTTP请求相关<br>
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     */
    public static boolean isUnknown(String checkString) {
        return StrUtil.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
    }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    public static String getMultistageReverseProxyIp(String ip) {
        // 多级反向代理检测
        if (StrUtil.isNotBlank(ip) && ip.indexOf(",") > 0) {
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取客户端IP<br>
     * 默认检测的Header：<br>
     * 1、X-Forwarded-For<br>
     * 2、X-Real-IP<br>
     * 3、ModelProxy-Client-IP<br>
     * 4、WL-ModelProxy-Client-IP<br>
     * otherHeaderNames参数用于自定义检测的Header
     *
     * @param request          请求对象
     * @param otherHeaderNames 其他自定义头文件
     * @return IP地址l
     */
    public static String getClientIp(HttpServletRequest request, String... otherHeaderNames) {
        if (Objects.isNull(request)) {
            return StrUtil.EMPTY;
        }

        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "ModelProxy-Client-IP", "WL-ModelProxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        if (QyArray.isNotEmpty(otherHeaderNames)) {
            headers = QyArray.addAll(headers, otherHeaderNames);
        }

        String ip;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (!isUnknown(ip)) {
                return getMultistageReverseProxyIp(ip);
            }
        }

        ip = request.getRemoteAddr();
        return getMultistageReverseProxyIp(ip);
    }

}
