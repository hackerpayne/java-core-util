package com.qyhstech.core.http;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.qyhstech.core.QyStr;
import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 统一资源定位符相关工具类
 */
@Slf4j
public class QyUrl {

    // 主机名匹配的正则
    public static final Pattern patternHost = Pattern.compile("[\\w-]+\\.(com\\.cn|net\\.cn|gov\\.cn|org\\.cn|edu\\.au|gov\\.au|com|net|org|cc|biz|info|cn|co|cc|me|tel|mobi|biz|info|name|tv|hk|uk|la|fm|jp|公司|中国|网络|university)\\b()*");

    private static final Pattern patternForProtocal = Pattern.compile("[\\w]+://");

    /**
     * 使用UTF-8编码URL
     *
     * @param input
     * @return
     */
    public static String encodeUtf8(String input) {
        return urlEncode(input, "utf-8");
    }

    /**
     * 使用GBK加密URL
     *
     * @param input
     * @return
     */
    public static String encodeGbk(String input) {
        return urlEncode(input, "gb2312");
    }

    /**
     * 内容编码
     *
     * @param str 内容
     * @return 编码后的内容
     */
    public static String urlEncode(String str) {
        return urlEncode(str, StandardCharsets.UTF_8);
    }

    /**
     * @param url
     * @param charset
     * @return
     */
    public static String urlEncode(String url, String charset) {
        return urlEncode(url, Charset.forName(charset));
    }

    /**
     * @param url
     * @param charset
     * @return
     */
    public static String urlEncode(String url, Charset charset) {
        return URLEncoder.encode(url, QyStr.notNull(charset) ? charset : StandardCharsets.UTF_8);
    }

    /**
     * 内容解码
     *
     * @param str 内容
     * @return 解码后的内容
     */
    public static String urlDecode(String str) {
        return urlDecode(str, StandardCharsets.UTF_8);
    }

    /**
     * 解码
     *
     * @param url
     * @param charset
     * @return
     */
    public static String urlDecode(String url, Charset charset) {
        return URLDecoder.decode(url, QyStr.notNull(charset) ? charset : StandardCharsets.UTF_8);
    }

    /**
     * URL解码，使用指定编码
     *
     * @param url
     * @param charset
     * @return
     */
    public static String urlDecode(String url, String charset) {
        return urlDecode(url, Charset.forName(charset));
    }


    /**
     * 根据绝对地址和相对地址，获取完整的路径
     *
     * @param absolutePath 基准路径，绝对
     * @param relativePath 相对路径
     * @return 绝对URL
     */
    public static String getAbsUrl(String absolutePath, String relativePath) {
        try {
            URL parseUrl;
            if (QyStr.isNotEmpty(absolutePath)) {
                URL absoluteUrl = new URL(absolutePath);
                parseUrl = new URL(absoluteUrl, relativePath);
            } else {
                parseUrl = new URL(relativePath);
            }
            return parseUrl.toString();
        } catch (MalformedURLException e) {
            log.error("getAbsUrl发生异常", e);
            return relativePath;
        }
    }

    /**
     * canonicalizeUrl
     * <br>
     * Borrowed from Jsoup.
     *
     * @param url   url
     * @param refer refer
     * @return canonicalizeUrl
     */
    public static String canonicalizeUrl(String url, String refer) {
        URL base;
        try {
            try {
                base = new URL(refer);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its own, so try that
                URL abs = new URL(refer);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
            if (url.startsWith("?")) {
                url = base.getPath() + url;
            }
            URL abs = new URL(base, url);
            return abs.toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    /**
     * 检查URL是否是标准的URL格式
     *
     * @param urlString
     * @return
     */
    public static boolean isUrl(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException mue) {
            return false;
        }
    }

    /**
     * 格式化URL链接
     *
     * @param url 需要格式化的URL
     * @return 格式化后的URL，如果提供了null或者空串，返回null
     */
    public static String formatUrl(String url) {
        if (QyStr.isBlank(url)) {
            return null;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        return "http://" + url;
    }

    /**
     * @param url url
     * @return new url
     * @deprecated
     */
    public static String encodeIllegalCharacterInUrl(String url) {
        return url.replace(" ", "%20");
    }

    /**
     * 删除URL中的非法字符
     *
     * @param url
     * @return
     */
    public static String fixIllegalCharacterInUrl(String url) {
        return url.replace(" ", "%20").replaceAll("#+", "#");
    }

    /**
     * @param url
     * @return
     */
    public static String getHostOld(String url) {
        String host = url;
        int i = StrUtil.ordinalIndexOf(url, "/", 3);
        if (i > 0) {
            host = StrUtil.sub(url, 0, i);
        }
        return host;
    }

    /**
     * 获取网站的主域名
     *
     * @param url
     * @return
     */
    public static String getHost(String url) {
        url = url.toLowerCase();
        String domain = "";
        Matcher matcher = patternHost.matcher(url);
        if (matcher.find()) {
            domain = matcher.group();
        }
        if (domain == null || domain.trim().isEmpty()) {
            return null;
        } else {
            return domain;
        }
    }

    public static String removeProtocol(String url) {
        return patternForProtocal.matcher(url).replaceAll("");
    }

    /**
     * 获取URL中的Domain部份
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        String domain = removeProtocol(url);
        int i = StrUtil.ordinalIndexOf(domain, "/", 1);
        if (i > 0) {
            domain = StrUtil.sub(domain, 0, i);
        }
        return removePort(domain);
    }

    /**
     * 删除URL中的端口部份
     *
     * @param domain
     * @return
     */
    public static String removePort(String domain) {
        int portIndex = domain.indexOf(":");
        if (portIndex != -1) {
            return domain.substring(0, portIndex);
        } else {
            return domain;
        }
    }

    /**
     * 从URL中抽取文件名的部份
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        return FileNameUtil.getName(URLUtil.getPath(url));
    }

    /**
     * 获取文件名，不带扩展名的部份
     *
     * @param url
     * @return
     */
    public static String getFileNameWithoutExtension(String url) {
        return FileNameUtil.mainName(URLUtil.getPath(url));
    }

    /**
     * 获取URL的扩展名部份
     *
     * @param url
     * @return
     */
    public static String getFileExt(String url) {
        return FileNameUtil.extName(URLUtil.getPath(url));
    }

    /**
     * 如果是多段URL地址，可以合并为一个完整的路径
     * 特别注意：如果后面的URL是 / 开头，会自动解析为根路径。
     *
     * @param parts
     * @return
     */
    public static String join(String... parts) {
        if (parts == null || parts.length == 0) {
            return "";
        }

        URI uri = URI.create(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (StrUtil.isNotEmpty(part)) {
                uri = uri.resolve(part);
            }
        }
        return uri.toString();
    }
}
