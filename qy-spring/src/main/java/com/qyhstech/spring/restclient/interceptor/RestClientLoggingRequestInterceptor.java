package com.qyhstech.spring.restclient.interceptor;

import com.qyhstech.spring.restclient.RepeatReadClientHttpRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 打印请求和响应信息
 */
@Slf4j
public class RestClientLoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // 打印请求信息
        logRequest(request, body);
        // 执行请求
        ClientHttpResponse response = execution.execute(request, body);
        // 创建可重复读取的响应包装器
        RepeatReadClientHttpRequestWrapper wrappedResponse = new RepeatReadClientHttpRequestWrapper(response);
        // 打印响应信息
        logResponse(wrappedResponse);
        return wrappedResponse;
    }

    /**
     * 记录请求信息
     *
     * @param request
     * @param body
     */
    private void logRequest(HttpRequest request, byte[] body) {
        log.debug("Request URI: {} {}", request.getMethod(), request.getURI());
        log.debug("Request Headers: {}", request.getHeaders());
        if (body.length == 0) {
            return;
        }

        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        if (contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            // multipart/form-data 处理
            String boundary = extractBoundary(contentType);
            if (boundary != null) {
                String bodyStr = new String(body, StandardCharsets.UTF_8);
                String[] parts = bodyStr.split("--" + boundary);
                StringBuilder safeBody = new StringBuilder();

                for (String part : parts) {
                    if (part.trim().isEmpty() || part.equals("--")) {
                        continue;
                    }
                    if (part.contains("filename=")) {
                        // 文件部分
                        safeBody.append("[File content hidden]").append("\n");
                    } else {
                        // 普通字段
                        safeBody.append(part).append("\n");
                    }
                }
                log.debug("Request Body (multipart, sanitized):\n{}", safeBody);
                return;
            }
        }

        // 其他情况正常打印
        log.debug("Request Body: {}", new String(body, StandardCharsets.UTF_8));
    }

    private String extractBoundary(String contentType) {
        int idx = contentType.indexOf("boundary=");
        if (idx != -1) {
            return contentType.substring(idx + 9).trim();
        }
        return null;
    }

    /**
     * 记录返回值信息
     *
     * @param response
     * @throws IOException
     */
    private void logResponse(RepeatReadClientHttpRequestWrapper response) throws IOException {
        log.debug("Response  Code: {} , Headers: {}", response.getStatusCode(), response.getHeaders());

        // 根据 Content-Type 判断是否需要打印 Body
        MediaType contentType = response.getHeaders().getContentType();
        if (contentType != null) {
            String type = contentType.toString().toLowerCase();
            boolean isTextLike = type.startsWith("text/")
                    || type.contains("json")
                    || type.contains("xml")
                    || type.contains("html");

            if (isTextLike) {
                String responseBody = response.getBodyAsString();
                if (!responseBody.isEmpty()) {
                    log.debug("Response Body: {}", responseBody);
                }
            } else {
                log.debug("Response Body: {}", contentType);
            }
        } else {
            log.debug("Response Body: [Content-Type未知，未打印]");
        }

    }
}
