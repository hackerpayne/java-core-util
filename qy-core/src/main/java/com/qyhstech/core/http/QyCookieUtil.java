package com.qyhstech.core.http;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.qyhstech.core.QyStr;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Cookie处理和操作类
 */
public class QyCookieUtil {

    /**
     * 获得指定Cookie的值
     *
     * @param name 名称
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, String name) {
        return getCookie(request, null, name, false);
    }

    /**
     * 获得指定Cookie的值，并删除。
     *
     * @param name 名称
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        return getCookie(request, response, name, true);
    }

    /**
     * 获得指定Cookie的值
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     名字
     * @param isRemove 是否移除
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemove) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    try {
                        value = URLDecoder.decode(cookie.getValue(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (isRemove) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
        }
        return value;
    }


    /**
     * Map格式的Cookie转到CookieStore中存放
     * @param myCookies
     * @param cookieDomain
     * @return
     */
    public static BasicCookieStore setCookieToCookieStore(Map<String, String> myCookies, String cookieDomain) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie1 ;
        for (Map.Entry<String, String> cookieEntry : myCookies.entrySet()) {
            cookie1 = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie1.setDomain(QyUrl.removePort(QyUrl.getDomain(cookieDomain)));
            cookieStore.addCookie(cookie1);
        }
        return cookieStore;
    }

    /**
     * 从Response中读取Cookie
     *
     * @param response
     * @return
     */
    public static String getCookiesFromResponse(HttpResponse response) {
        Header[] headers = response.getHeaders("Set-Cookie");
        StringBuilder sb = new StringBuilder();
        for (Header header : headers) {
            sb.append(header.getValue()).append(";");
        }
        return sb.toString();
    }

    /**
     * 获取Cookie字符串格式
     *
     * @param cookieStore
     * @return
     */
    public static String getCookieStr(BasicCookieStore cookieStore) {
        String cookie = "";
        if (cookieStore != null) {
            List<org.apache.hc.client5.http.cookie.Cookie> listCookies = cookieStore.getCookies();
            StringBuilder sb = new StringBuilder();
            if (listCookies != null && listCookies.size() > 0) {
                for (org.apache.hc.client5.http.cookie.Cookie ck : listCookies) {
                    sb.append(ck.getName() + "=" + ck.getValue() + ";");
                }
                cookie = sb.toString();
            }
        }

        return cookie;
    }

    /**
     * 清除 某个指定的cookie
     *
     * @param response HttpServletResponse
     * @param key      cookie key
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    /**
     * 设置 Cookie（生成时间为1天）
     *
     * @param name  名称
     * @param value 值
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, name, value, 60 * 60 * 24);
    }

    /**
     * 设置 Cookie
     *
     * @param name  名称
     * @param value 值
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path) {
        setCookie(response, name, value, path, 60 * 60 * 24);
    }

    /**
     * 设置 Cookie
     *
     * @param name   名称
     * @param value  值
     * @param maxAge 生存时间（单位秒）
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        setCookie(response, name, value, "/", maxAge);
    }

    /**
     * 设置 Cookie
     *
     * @param name   名称
     * @param value  值
     * @param maxAge 生存时间（单位秒）
     * @param path   路径
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        if (QyStr.isNotEmpty(value)) {
            cookie.setValue(URLUtil.encode(value, StandardCharsets.UTF_8));
        } else {
            cookie.setValue(null);
        }
        response.addCookie(cookie);
    }

    /**
     * 解析Cookie头
     *
     * @param cookieHeader
     * @return
     */
    public static Map<String, Cookie> parseCookies(String cookieHeader) {
        Map<String, Cookie> result = new LinkedHashMap<String, Cookie>();
        if (cookieHeader != null) {
            String[] cookiesRaw = cookieHeader.split("; ");
            for (int i = 0; i < cookiesRaw.length; i++) {
                String[] parts = cookiesRaw[i].split("=", 2);
                String value = parts.length > 1 ? parts[1] : "";
                if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                //下面的代码没有无法正常运行，需要检查写法已经 变了
                result.put(parts[0], new Cookie(parts[0], value));
            }
        }
        return result;
    }

    /**
     * 打印一下Cookie到一个列表里面
     *
     * @param cookieStr
     */
    public static void printCookieStr(String cookieStr) {

        Map<String, Cookie> mapCookies = parseCookies(cookieStr);

        mapCookies.forEach((key, value) -> {
            System.out.println(StrUtil.format(".addCookie(\"{}\",\"{}\");", key, value.getValue()));
        });
    }

}
