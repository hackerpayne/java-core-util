package com.qyhstech.core.httpclient.tool;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.QyAssert;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.collection.QyMap;
import com.qyhstech.core.httpclient.bean.QyHttpRequest;
import com.qyhstech.core.httpclient.bean.QyHttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 结果HttpClient返回的结果HttpEntity的一些常用工具类
 */
@Slf4j
public class QyHttpEntityUtil {

    /**
     * 构造 Multipart 文件上传请求
     * 首先是构造 HttpEntity 的方法, 这个方法中设置请求为 1个文件 + 多个随表单参数
     * // 构造一个文件参数, 其它参数留空
     * NameValuePair fileNvp = new BasicNameValuePair("sendfile", filePath);
     * HttpEntity entity = httpEntityBuild(fileNvp, null);
     * HttpPost httpPost = new HttpPost(api);
     * httpPost.setEntity(entity);
     * <p>
     * try (CloseableHttpClient client = getClient(...)) {
     * Client5Resp<T> resp = client.execute(httpPost, response->{
     * ...
     * <p>
     * });
     * <p>
     * 注意: 在使用 HttpMultipartMode 时对 HttpEntity 设置 Header 要谨慎, 因为 HttpClient 会对 Content-Type增加 Boundary 后缀, 而这个是服务端判断文件边界的重要参数. 如果设置自定义 Header, 需要检查 boundary 是否正确生成. 如果没有的话需要自定义 Content-Type 将 boundary 加进去, 并且通过 EntityBuilder.setBoundary() 将自定义的 boundary 值传给 HttpEntity.
     *
     * @param fileNvp   文件 配置信息
     * @param postDatas 别的附加参数信息
     * @return
     */
    public static HttpEntity buildFileEntity(NameValuePair fileNvp, List<NameValuePair> postDatas) {

        File file = new File(fileNvp.getValue());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.STRICT);

        if (postDatas != null && postDatas.size() > 0) {
            for (NameValuePair nvp : postDatas) {
                builder.addTextBody(nvp.getName(), nvp.getValue(), ContentType.DEFAULT_BINARY);
            }
        }
        builder.addBinaryBody(fileNvp.getName(), file, ContentType.DEFAULT_BINARY, fileNvp.getValue());
        return builder.build();

    }

    /**
     * @param postData
     * @param useJson
     * @return
     */
    public static HttpEntity buildStringEntity(String postData, Boolean useJson) {
        // StringEntity处理任意格式字符串请求参数
        //HttpEntity stringEntity = new StringEntity(postData, Charset.forName("utf-8"));
        HttpEntity stringEntity;
//        stringEntity.setContentEncoding("UTF-8");

        if (useJson) {
            stringEntity = HttpEntities.create(postData, ContentType.APPLICATION_JSON);
//            stringEntity.setContentType("application/json");
        } else {
//            stringEntity.setContentType(contentType);
            stringEntity = HttpEntities.create(postData, CharsetUtil.CHARSET_UTF_8);

        }

        return stringEntity;
    }

    /**
     * Map 转 UrlEncodedFormEntity
     *
     * @param postData
     * @return
     */
    public static HttpEntity buildUrlEncodeEntity(HashMap<String, String> postData) {
        return buildUrlEncodeEntity(postData, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * Map格式的请求转换为UrlEncodedFormEntity形式，默认编码为：APPLICATION_FORM_URLENCODED，使用时：
     * httpPost.setEntity(postEntity);
     *
     * @param postData
     * @return
     */
    public static UrlEncodedFormEntity buildUrlEncodeEntity(HashMap<String, String> postData, Charset charset) {
        // 创建参数队列
        List<NameValuePair> formparams = QyList.empty();

        for (Map.Entry<String, String> item : postData.entrySet()) {
            formparams.add(new BasicNameValuePair(item.getKey(), item.getValue()));
        }

        return new UrlEncodedFormEntity(formparams, charset);
    }

    /**
     * 文件转换为json上传文件格式的Entity准备上传
     *
     * @param jsonFilePath Json文件的完整路径
     * @return
     * @throws FileNotFoundException
     */
    public static HttpEntity buildJsonFileEntity(File jsonFilePath) throws FileNotFoundException {
        final InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(jsonFilePath), -1, ContentType.APPLICATION_JSON);

        // 也可以使用 FileEntity 的形式
        // FileEntity reqEntity = new FileEntity(new File(params), ContentType.APPLICATION_JSON);

        //HttpPost httpPost = new HttpPost(url);
        //httpPost.setEntity(reqEntity);
        return reqEntity;
    }

    /**
     * 上传Form形式的表单
     *
     * @param formParams
     * @return
     */
    public static HttpEntity buildFormEntity(List<NameValuePair> formParams) {

        // 表单参数手动添加
        //List<NameValuePair> postParams = new ArrayList<>();
        //postParams.add(new BasicNameValuePair("username", "wdbyte.com"));
        //postParams.add(new BasicNameValuePair("password", "secret"));

        return new UrlEncodedFormEntity(formParams);
    }

    /**
     * 转换为Str字符串
     *
     * @param httpEntity
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static String handleEntityToStr(HttpEntity httpEntity) throws IOException, ParseException {
        // 获取响应信息
        String result = EntityUtils.toString(httpEntity);
        // 确保流被完全消费
        EntityUtils.consume(httpEntity);
        return result;
    }


    /**
     * 将Entity中的内容保存到文件里面，适用于下载文件内容
     *
     * @param httpEntity
     * @param saveFile
     * @throws IOException
     */
    public static void handleEntityToFile(HttpEntity httpEntity, File saveFile) throws IOException {

        QyAssert.notNull(httpEntity, "HttpEntity must not be null");
        InputStream is = httpEntity.getContent();

        FileOutputStream fos = new FileOutputStream(saveFile);
        byte[] buffer = new byte[4096];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.close();
        is.close();

    }


    /**
     * 打印Header信息
     *
     * @param response
     */
    public static void printAllHeader(HttpResponse response) {
        Header[] headers = response.getHeaders();
        for (Header header : headers) {
            log.info(header.getName() + " " + header.getValue());
        }
    }

    /**
     * Header转换为Map格式
     *
     * @param headers
     * @return
     */
    public static Map<String, List<String>> handleHeadersToMap(Header[] headers) {
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        for (Header header : headers) {
            List<String> list = results.get(header.getName());
            if (list == null) {
                list = new ArrayList<String>();
                results.put(header.getName(), list);
            }
            list.add(header.getValue());
        }
        return results;
    }

    /**
     * 直接转成字符串
     *
     * @param response
     * @return
     */
    public static String handleResponseToStr(CloseableHttpResponse response) throws IOException, ParseException {
        return handleResponseToStr(response, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * Response读取到String里面，不推荐，这样会复制出来处理，推荐使用Handler解决
     * res = EntityUtils.toString(response.getEntity(),"UTF-8");
     * EntityUtils.consume(response1.getEntity());
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String handleResponseToStr(CloseableHttpResponse response, Charset charset) throws IOException, ParseException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity, charset == null ? CharsetUtil.CHARSET_UTF_8 : charset);
        }
        return null;
    }

    /**
     * @param request
     * @param charset
     * @param httpResponse
     * @return
     * @throws IOException
     */
    public static QyHttpResult handleResponse(QyHttpRequest request, String charset, CloseableHttpResponse httpResponse) throws IOException {
        byte[] bytes = IoUtil.readBytes(httpResponse.getEntity().getContent());
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType();
        QyHttpResult page = new QyHttpResult();
        page.setBytes(bytes);
        if (!request.isBinaryContent()) {
            if (null == charset) {
                charset = getHtmlCharset(contentType, bytes);
            }

            page.setCharset(charset);
            page.setRawText(new String(bytes, charset));
        }

        page.setUrl(request.getUrl());
        page.setStatusCode(httpResponse.getCode());
        page.setDownloadSuccess(true);

        if (QyMap.isNotEmpty(request.getHeaders())) {
            page.setHeaders(QyHttpEntityUtil.handleHeadersToMap(httpResponse.getHeaders()));
        }

        return page;
    }

    /**
     * 直接处理返回的信息到QyHttpResult里面
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    public static QyHttpResult handleResponse(CloseableHttpResponse httpResponse) throws IOException {

        QyHttpResult httpResult = new QyHttpResult();

        byte[] bytes = IoUtil.readBytes(httpResponse.getEntity().getContent());
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType();

        httpResult.setBytes(bytes);

        String charset = getHtmlCharset(contentType, bytes);
        httpResult.setCharset(charset);

        httpResult.setRawText(new String(bytes, charset));
        httpResult.setStatusCode(httpResponse.getCode());
        httpResult.setDownloadSuccess(true);
        httpResult.setHeaders(QyHttpEntityUtil.handleHeadersToMap(httpResponse.getHeaders()));

        return httpResult;
    }

    /**
     * 结果保存至文件内
     *
     * @param httpResponse
     * @param saveFile
     * @throws IOException
     */
    public static void handleResponseToFile(CloseableHttpResponse httpResponse, String saveFile) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            InputStream is = entity.getContent();

            File file = new File(saveFile);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();
        }

    }

    /**
     * 检测页面编码
     *
     * @param contentType
     * @param contentBytes
     * @return
     * @throws IOException
     */
    private static String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
        String charset = QyCharsetUtil.detectCharset(contentType, contentBytes);
        if (charset == null) {
            charset = Charset.defaultCharset().name();
            log.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
        }

        return charset;
    }

    /**
     * 解压
     *
     * @param entity
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String handleGZipContent(HttpEntity entity, String encoding) throws IOException {
        String responseContent = "";
        GZIPInputStream gis = new GZIPInputStream(entity.getContent());
        int count = 0;
        byte data[] = new byte[1024];
        while ((count = gis.read(data, 0, 1024)) != -1) {
            String str = new String(data, 0, count, encoding);
            responseContent += str;
        }
        return responseContent;
    }

    /**
     * 压缩
     *
     * @param sendData
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream gZipContent(String sendData) throws IOException {
        if (StrUtil.isBlank(sendData)) {
            return null;
        }

        ByteArrayOutputStream originalContent = new ByteArrayOutputStream();
        originalContent.write(sendData.getBytes("UTF-8"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
        originalContent.writeTo(gzipOut);
        gzipOut.close();
        return baos;
    }

    /**
     * 关闭Entity实体
     *
     * @param entity
     * @throws IOException
     */
    public static void close(HttpEntity entity) throws IOException {
        if (entity == null) {
            return;
        }
        if (entity.isStreaming()) {
            final InputStream instream = entity.getContent();
            if (instream != null) {
                instream.close();
            }
        }
    }

    /**
     * Response中提取返回结果
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String responseToStr(CloseableHttpResponse response) throws IOException {
        String html = "";

        try {
//            System.out.println(response.getStatusLine());

//            HttpEntity entity = response.getEntity();

            // do something useful with the response body
            // and ensure it is fully consumed
            //EntityUtils.consume(entity);

            html = EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            response.close();
        }
        return html;
    }


    /**
     * 创建一个二进制的Response读取器
     *
     * @return
     */
//    public static ResponseHandler<byte[]> buildBytesHandler() {
//        ResponseHandler<byte[]> handler = response -> {
//            HttpEntity entity = (HttpEntity) response.getEntity();
//            if (entity != null) {
//                return EntityUtils.toByteArray(entity);
//            } else {
//                return null;
//            }
//        };
//        return handler;
//    }


}
