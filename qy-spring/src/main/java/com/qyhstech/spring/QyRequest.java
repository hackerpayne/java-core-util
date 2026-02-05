package com.qyhstech.spring;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.collection.QyMap;
import com.qyhstech.core.encode.QyBase64;
import com.qyhstech.core.http.QyIp;
import com.qyhstech.core.io.QyInputStream;
import com.qyhstech.core.json.QyJackson;
import com.qyhstech.spring.request.NonRequestAttributes;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URLConnection;
import java.util.*;

@Slf4j
public class QyRequest extends org.springframework.web.util.WebUtils {

    // HEADER 认证头 value 的前缀
    public static final String AUTHORIZATION_BEARER = "Bearer ";

    // 默认的Token
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * 获取上下文
     *
     * @return
     */
    public static ServletRequestAttributes getRequestAttributes() {
        try {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 安全获取上下文，没有的时候会返回一个空的上下文供使用
     *
     * @return
     */
    public static RequestAttributes getRequestAttributesSafely() {
        RequestAttributes requestAttributes = null;
        try {
            requestAttributes = RequestContextHolder.currentRequestAttributes();
        } catch (IllegalStateException e) {
            log.error("getRequestAttributesSafely获取失败，可能非Web环境，返回默认的空值NonRequestAttributes");
            requestAttributes = new NonRequestAttributes();
        }
        return requestAttributes;
    }

    /**
     * 获取Request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        try {
            return getRequestAttributes().getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequestSafely() {
        try {
            RequestAttributes requestAttributesSafely = getRequestAttributesSafely();
            return requestAttributesSafely instanceof NonRequestAttributes ? null : ((ServletRequestAttributes) requestAttributesSafely).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取response
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        try {
            return getRequestAttributes().getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 安全获取Response
     *
     * @return
     */
    public static HttpServletResponse getResponseSafely() {
        try {
            RequestAttributes requestAttributesSafely = getRequestAttributesSafely();
            return requestAttributesSafely instanceof NonRequestAttributes ? null : ((ServletRequestAttributes) requestAttributesSafely).getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取session
     *
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 检测是否https
     *
     * @param url URL
     * @return 是否https
     */
    public static boolean isHttps(String url) {
        return url.toLowerCase().startsWith("https");
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (QyStr.isNotBlank(accept) && accept.contains("application/json")) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (QyStr.isNotBlank(xRequestedWith) && xRequestedWith.contains("XMLHttpRequest")) {
            return true;
        }

        String contentType = request.getHeader("Content-Type");
        if (QyStr.isNotBlank(contentType) && contentType.contains("application/json")) {
            return true;
        }

        String uri = request.getRequestURI();
        if (QyStr.containsAny(uri, ".json", ".xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        return QyStr.containsAny(ajax, "json", "xml");
    }

    /**
     * 获取客户端IP
     *
     * @return
     */
    public static String getClientIp() {
        HttpServletRequest request = getRequest();
        return QyIp.getClientIp(request);
    }

    /**
     * 获取请求的URL
     *
     * @param request
     * @return
     */
    public static String getRequestDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }

    /**
     * 获取Http header的参数
     *
     * @param name    要取值的参数名称
     * @param request 请求信息
     * @return
     */
    public static String getHeaderParam(String name, HttpServletRequest request) {
        if (request != null) {
            return request.getHeader(name);
        } else {
            return null;
        }
    }

    /**
     * 获取请求头信息到Map中
     *
     * @param request
     * @return
     */
    public static Map<String, String> getHttpHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        if (request != null) {
            Enumeration<String> enumeration = request.getHeaderNames();
            if (enumeration != null) {
                while (enumeration.hasMoreElements()) {
                    String key = enumeration.nextElement();
                    String value = request.getHeader(key);
                    map.put(key, value);
                }
            }
        }

        return map;
    }

    /**
     * 根据文件扩展名获得MimeType
     *
     * @param filePath 文件路径或文件名
     * @return MimeType
     */
    public static String getMimeType(String filePath) {
        return URLConnection.getFileNameMap().getContentTypeFor(filePath);
    }

    /**
     * 设置客户端缓存过期时间 的Header.
     *
     * @param response
     * @param expiresSeconds
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        // Http 1.0 header, set model fix expires date.
        response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
        // Http 1.1 header, set model time after now.
        response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的Header.
     *
     * @param response
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader(HttpHeaders.EXPIRES, 1L);
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        // Http 1.1 header
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header.
     *
     * @param response
     * @param lastModifiedDate
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
    }

    /**
     * 设置Etag Header.
     *
     * @param response
     * @param etag
     */
    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader(HttpHeaders.ETAG, etag);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     * <p>
     * 如果无修改, checkIfModify返回false ,设置304 not modify status.
     *
     * @param lastModified 内容的最后修改时间.
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
                                               long lastModified) {
        long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
        if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        return true;
    }

    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     * <p>
     * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
     *
     * @param etag 内容的ETag.
     */
    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader(HttpHeaders.ETAG, etag);
                return false;
            }
        }
        return true;
    }

    /**
     * 客户端对Http Basic验证的 Header进行编码.
     *
     * @param userName
     * @param password
     * @return
     */
    public static String encodeHttpBasic(String userName, String password) {
        String encode = userName + ":" + password;
        return "Basic " + QyBase64.encodeToStr(encode.getBytes());
        //return "Basic " + Base64.encodeBase64String(encode.getBytes());
    }

    /**
     * 获取完整路径
     *
     * @param request
     * @return
     */
    public static String getRequestFullUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    /**
     * @param request
     * @return
     */
    public static String getContextPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    /**
     * 获取根路径
     *
     * @return
     */
    public static String getRootUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    /**
     * 获取请求为字符串
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestBody(final HttpServletRequest request) throws IOException {
        return QyInputStream.inputStreamToStr(request.getInputStream(), CharsetUtil.UTF_8);
    }

    /**
     * 从流中读取数据
     *
     * @param request
     * @param encode  解析的字符串的编码
     * @return
     * @throws IOException
     */
    public static String getRequestBody(final HttpServletRequest request, String encode) throws IOException {
        return QyInputStream.inputStreamToStr(request.getInputStream(), encode);
    }

    /**
     * 从 request 从获取 body, 并转成 javabean, contentType 必须包含'application/json'
     *
     * @param request HttpServletRequest
     * @param clazz   返回结果类型
     * @param <T>     返回结果类型
     * @return
     * @throws IOException
     */
    public static <T> T getRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        String body = getRequestBody(request, "utf-8");
        return QyJackson.parseObject(body, clazz);
    }

    /**
     * 获取请求体HttpServletRequest内容到Map内
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Map getRequestBodyToMap(HttpServletRequest request) throws IOException {
        String body = getRequestBody(request, "utf-8");
        return QyJackson.parseObject(body, Map.class); // 读取String到Map里面
    }

    /**
     * 获取request里面的参数
     *
     * @param name
     * @return
     */
    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        return request.getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue) {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name) {
        return Convert.toBool(getRequest().getParameter(name));
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name, Boolean defaultValue) {
        return Convert.toBool(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取request里面的参数
     *
     * @param name
     * @return
     */
    public static String[] getParameterValues(String name) {
        HttpServletRequest request = getRequest();
        return request.getParameterValues(name);
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String[]> getParams(ServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }

    /**
     * 从request中获得参数，并返回可读的Map,自动转换Json和Form请求到Map里面
     * application/x-www-form-urlencode
     * application/json
     * application/json;charset=UTF-8
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) throws IOException {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        Map<String, String> params = QyMap.empty();

        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {  // json类型参数
            String body = QyRequest.getRequestBody(request);
            if (QyStr.isNotBlank(body)) {
                try {
                    params = QyJackson.parseMapStr(body);
                } catch (Exception ignored) {
                }
            }
        } else {
            // 普通表单形式
            for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
//                params.put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
                params.put(entry.getKey(), StrUtil.join(",", entry.getValue()));
            }
        }
        // 参数Map
        return params;
    }

    /**
     * 获取request里面的参数
     *
     * @param request
     * @param handlerMethod
     * @return
     */
    public static Map<String, Object> getRequestMethodParams(HttpServletRequest request, HandlerMethod handlerMethod) {
        Map<String, Object> paramMap = QyMap.empty();
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        List<Map<String, Object>> formParameters = new ArrayList<Map<String, Object>>();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        for (MethodParameter methodParameter : methodParameters) {
            String paramName = methodParameter.getParameterName();
            Class<?> paramType = methodParameter.getParameterType();
            Object paramValue = null;
            if (!pathVariables.containsKey(paramName) && paramType != HttpServletRequest.class && paramType != HttpSession.class && paramType != Model.class) {
                if (paramType.isArray()) {
                    List<String> paramList = new ArrayList<String>();
                    String[] params = request.getParameterValues(paramName);
                    if (params != null) {
                        paramList.addAll(Arrays.asList(params));
                    }
                    paramValue = paramList;
                } else {
                    paramValue = request.getParameter(paramName);
                    if ("password".equals(paramName) || "pwd".equals(paramName) || "passwd".equals(paramName) && paramValue != null && QyStr.isNotEmpty(paramValue.toString())) {
                        paramValue = QyStr.repeat("*", paramValue.toString().length());
                    }
                }

                Map<String, Object> formParam = QyMap.empty();
                formParam.put(paramName, paramValue);
                formParameters.add(formParam);
            }
        }
        paramMap.put("pathVariables", pathVariables);
        paramMap.put("formParameters", formParameters);
        return paramMap;
    }

    /**
     * 301永久跳转到指定URL，URL默认为相对路径
     *
     * @param url
     * @return
     */
    public static ModelAndView redirect301(String url) {
        RedirectView red = new RedirectView(url, true);
        red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return new ModelAndView(red);
    }

    /**
     * 302临时跳转
     *
     * @param url
     * @return
     */
    public static ModelAndView redirect302(String url) {
        RedirectView red = new RedirectView(url, true);
        red.setStatusCode(HttpStatus.FOUND);
        return new ModelAndView(red);
    }

    /**
     * 获取Token
     *
     * @return
     */
    public static String getAccessToken() {
        return resolveToken(getRequestSafely(), ACCESS_TOKEN);
    }

    /**
     * 获取Token
     *
     * @param tokenHeader
     * @return
     */
    public static String getAccessToken(String tokenHeader) {
        return resolveToken(getRequestSafely(), tokenHeader);
    }


    /**
     * 解析Token
     *
     * @param request    请求
     * @param headerName Token所在Header
     * @return
     */
    public static String resolveToken(HttpServletRequest request, String headerName) {
        return resolveToken(request, headerName, "");
    }

    /**
     * 从请求中，获得认证 Token
     *
     * @param request       请求
     * @param headerName    认证 Token 对应的 Header 名字
     * @param parameterName 认证 Token 对应的 Parameter 名字，如果不能通过Header传时，通过参数获取Token
     * @return 认证 Token
     */
    public static String resolveToken(HttpServletRequest request, String headerName, String parameterName) {
        // 1. 获得 Token。优先级：Header > Parameter
        String token = request.getHeader(headerName);
        if (StrUtil.isEmpty(token) && StrUtil.isNotEmpty(parameterName)) {
            token = request.getParameter(parameterName);
        }
        if (StrUtil.isBlank(token)) {
            return null;
        }
        // 2. 去除 Token 中带的 Bearer
        if (token.startsWith(AUTHORIZATION_BEARER)) {
            token = token.substring(AUTHORIZATION_BEARER.length());
        }
        return StrUtil.isNotEmpty(token) ? token.trim() : "";
    }

}
