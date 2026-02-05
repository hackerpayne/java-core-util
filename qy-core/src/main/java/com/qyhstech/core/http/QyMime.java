package com.qyhstech.core.http;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class QyMime {

    // MIME -> 扩展名映射
    public static final Map<String, String> MIME_EXTENSION_MAP = new HashMap<>() {{
        put("image/jpeg", ".jpg");
        put("image/png", ".png");
        put("image/gif", ".gif");
        put("image/webp", ".webp");
        put("image/bmp", ".bmp");
        put("application/pdf", ".pdf");
        put("text/plain", ".txt");
        put("application/zip", ".zip");
    }};

    /**
     * 根据ContentType中的Mime得到文件类型扩展名
     *
     * @param contentType
     * @return
     */
    public static String getExtFromContentType(String contentType) {
        return getExtFromContentType(contentType, null);
    }

    /**
     * 根据ContentType中的Mime得到文件类型扩展名
     *
     * @param contentType
     * @param defaultValue
     * @return
     */
    public static String getExtFromContentType(String contentType, String defaultValue) {
        if (StrUtil.isEmpty(contentType)) {
            return "";
        }
        return MIME_EXTENSION_MAP.getOrDefault(contentType.toLowerCase(), defaultValue);
    }

}
