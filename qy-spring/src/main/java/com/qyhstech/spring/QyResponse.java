package com.qyhstech.spring;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSON;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.domain.constant.QyContentTypeConst;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 对返回数据进行处理
 */
public class QyResponse {

    /**
     * 写Html
     *
     * @param response
     * @param html
     */
    public static void renderStr(HttpServletResponse response, String html) {
        renderStr(response, html, QyContentTypeConst.TEXT_HTML);
    }

    /**
     * 客户端返回JSON字符串
     *
     * @param response HttpServletResponse
     * @param result   结果对象
     */
    public static void renderJson(HttpServletResponse response, Object result) {
        renderStr(response, JSON.toJSONString(result), QyContentTypeConst.JSON_UTF8);
    }

    /**
     * 添加跨域专用头
     *
     * @param resp
     */
    public static void addCorsHeader(HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Cache-Control", "no-cache");
    }

    /**
     * 客户端返回字符串
     *
     * @param response
     * @param outputStr   需要输出的字符串
     * @param contentType 为空的时候输出UTF8格式的json
     */
    public static void renderStr(HttpServletResponse response, String outputStr, String contentType) {

        response.reset();// 会导致跨域失败，需要下面设置手动跨域

        // CORS setting
        response.setHeader("OpenAccess-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        //            response.setHeader("Content-type", "application/json;charset=UTF-8");

        if (QyStr.isEmpty(contentType)) {
            contentType = QyContentTypeConst.JSON;
        }
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK); // 返回200正常即可

        //输出内容
        //        response.getWriter().print(outputStr);
        //        response.getWriter().flush();
        //        response.getWriter().close();

        //可以自动释放资源的输出
        //        try (PrintWriter out = response.getWriter()) {
        //            out.append(outputStr);
        //        }

        try {
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(outputStr.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 设置响应结果,输出Excel的头信息
     *
     * @param response    响应结果对象
     * @param rawFileName 文件名
     */
    public static void renderExcel(HttpServletResponse response, String rawFileName) {
        //        response.reset();// 会导致跨域失败
        //        response.setContentType("application/msexcel");
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName + ".xlsx");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
    }

    /**
     * 输出Bytes字节下载
     *
     * @param response
     * @param data     需要下载的文件
     * @param fileName
     */
    public static void renderBytes(HttpServletResponse response, byte[] data, String fileName) {
        try {

            // 中文文件名支持
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");

            //            response.reset(); // 会导致跨域失败
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
            //            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");

            IoUtil.write(response.getOutputStream(), false, data);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void renderAttachment(HttpServletResponse response, String realFileName) {
        String percentEncodedFileName = percentEncode(realFileName);
        String contentDispositionValue = "attachment; filename=%s;filename*=utf-8''%s".formatted(percentEncodedFileName, percentEncodedFileName);
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue);
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8);
        return encode.replaceAll("\\+", "%20");
    }
}
