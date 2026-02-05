package com.qyhstech.core.http;

import com.qyhstech.core.httpclient.QyHttpClientSimple;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by kyle on 17/4/12.
 */
@Slf4j
public class HttpHelperTest {

    private QyHttpClientSimple http;

    public static void testSimple() throws IOException {

        System.out.println("multithreading http");

        QyHttpClientSimple http = new QyHttpClientSimple();

        String html = http.getHtml("http://www.baidu.com", "", "", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36", false);

        //String cookie = http.getCookieStore();
        //System.out.println("当前Cookie1为：" + cookie);
//        System.out.println(html);

        html = http.getHtml("http://baike.baidu.com", "", "", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36", false);
        //cookie = http.getCookieStr();
        //System.out.println("当前Cookie2为：" + cookie);
//        System.out.println(html);


//
//        //创建一个客户端
//        CloseableHttpClient client = HttpClients.createDefault();
//
//        //创建一个get方法
//        HttpGet get = new HttpGet("http://www.baidu.com");
//
//        //执行请求
//        HttpResponse res = client.execute(get);
//
//        //获取协议版本・・・「HTTP/1.1」
//        System.out.println(res.getProtocolVersion());
//        //获取返回状态码・・・「200」
//        System.out.println(res.getStatusLine().getStatusCode());
//        //获取原因短语・・・「OK」
//        System.out.println(res.getStatusLine().getReasonPhrase());
//        //获取完整的StatusLine・・・「HTTP/1.1 200 OK」
//        System.out.println(res.getStatusLine().toString());
//
//        //获取返回头部信息
//        Header[] headers = res.getAllHeaders();
//        for (Header header : headers) {
//            System.out.println(header.getName() + ": " + header.getValue());
//        }
//
//        //获取返回内容
//        if (res.getEntity() != null) {
//            System.out.println(EntityUtils.toString(res.getEntity()));
//        }
//
//        //关闭流
//        EntityUtils.consume(res.getEntity());
//
//        //关闭连接
//        client.close();
    }


    @Test
    public void testGetImage() throws Exception {
        http = new QyHttpClientSimple();
        BufferedImage img = http.getImage("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png", "");

//        ImageIO.write(img, "png", new File("result.png"));

        //显示输入框，输入用户验证码
        //String input = new CaptchaFrame(img).getUserInput();
        //log.info("验证码为：" + input);
    }

}