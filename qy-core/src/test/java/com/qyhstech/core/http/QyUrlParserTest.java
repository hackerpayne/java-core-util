package com.qyhstech.core.http;

import org.junit.jupiter.api.Test;

/**
 * Created by Kyle on 16/8/29.
 */
public class QyUrlParserTest {

    private static QyUrlParser parser;

    public static void Init() {
        parser = new QyUrlParser("https://www.yirendai.com/LandingPage/mhd/mhd2/?utm_source=bd-pc-ss&utm_medium=SEM_borrower&utm_campaign=%C6%B7%C5%C6%B4%CA&utm_content=%D2%CB%C8%CB%B4%FB-%BA%CB%D0%C4&utm_term=%D2%CB%C8%CB%B4%FB&utm_cparameters=baiduPC03#downloading", "gb2312");
    }

    @Test
    public void getCleanUrl() throws Exception {

        String clean = parser.getCleanUrl();
        System.out.println(clean);
    }

    @Test
    public void getQueryStr() throws Exception {

    }

    @Test
    public void getFragment() throws Exception {

    }

    @Test
    public void parseQuery() throws Exception {
        QyUrlParser parser = new QyUrlParser("http://www.track.com/track_log_simple.html?source=101&aid=hahah#index");
        System.out.println(parser.getParam("sid", "aid", "source"));
    }

    @Test
    public void parseUrlModel() throws Exception {
        System.out.println("parseUrlModel");

        System.out.println(parser.parseUrlModel());
    }

}