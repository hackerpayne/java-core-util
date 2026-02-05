package com.qyhstech.core.httpclient.bean;

import com.qyhstech.core.domain.constant.QyContentTypeConst;
import lombok.Data;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 要POST数据时，进行管理，以支持多种复杂的操作
 */
@Data
public class QyHttpRequestBody implements Serializable {

    private static final long serialVersionUID = -1;

    /**
     * POST使用的Body内容
     */
    private byte[] body;

    /**
     * ContentType
     */
    private String contentType;

    /**
     * 使用的编码
     */
    private String encoding;


    public QyHttpRequestBody() {
    }

    /**
     * @param body
     * @param contentType
     * @param encoding
     */
    public QyHttpRequestBody(byte[] body, String contentType, String encoding) {
        this.body = body;
        this.contentType = contentType;
        this.encoding = encoding;
    }

    /**
     * POST Json格式数据
     *
     * @param json
     * @param encoding
     * @return
     */
    public static QyHttpRequestBody json(String json, String encoding) {
        try {
            return new QyHttpRequestBody(json.getBytes(encoding), QyContentTypeConst.JSON, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }

    /**
     * Post XML格式数据
     *
     * @param xml
     * @param encoding
     * @return
     */
    public static QyHttpRequestBody xml(String xml, String encoding) {
        try {
            return new QyHttpRequestBody(xml.getBytes(encoding), QyContentTypeConst.TEXT_XML, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }

    /**
     * @param body
     * @param contentType
     * @param encoding
     * @return
     */
    public static QyHttpRequestBody custom(byte[] body, String contentType, String encoding) {
        return new QyHttpRequestBody(body, contentType, encoding);
    }

    /**
     * @param params
     * @param encoding
     * @return
     */
    public static QyHttpRequestBody form(Map<String, Object> params, String encoding) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }
        try {
            return new QyHttpRequestBody(URLEncodedUtils.format(nameValuePairs, Charset.forName(encoding)).getBytes(encoding), QyContentTypeConst.FORM, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }


}
