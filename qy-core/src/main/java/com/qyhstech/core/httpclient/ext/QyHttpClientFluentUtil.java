package com.qyhstech.core.httpclient.ext;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.NameValuePair;

import java.io.IOException;
import java.util.List;

public class QyHttpClientFluentUtil {

    /**
     * @param url
     * @return
     */
    public static String getByFluent(String url) {
        String result = null;
        try {
            Response response = Request.get(url).execute();
            result = response.returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 使用Post提交数据
     *
     * @param url
     * @param postParas
     * @return
     */
    public static String post(String url, List<NameValuePair> postParas) {
        String result = null;
        Request request = Request.post(url);

        //// POST 请求参数手动添加
        //request.bodyForm(
        //        new BasicNameValuePair("username", "wdbyte.com"),
        //        new BasicNameValuePair("password", "secret"));

        request.bodyForm(postParas);

        try {
            result = request.execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
