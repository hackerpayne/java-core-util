package com.qyhstech.core.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QyParamTest {

    @Test
    void getUrlPara() {
        System.out.println(QyParam.getUrlPara(null));
        System.out.println(QyParam.getUrlPara("a=b&c=test&d=哈哈"));
        System.out.println(QyParam.getUrlPara("http://www.baidu.com/?a=b&c=test&d=哈哈"));
    }

    @Test
    void testGetUrlPara() {
        System.out.println(QyParam.getUrlPara("http://www.baidu.com/?a=b&c=test&d=哈哈","d"));
    }

    @Test
    void getUrlQueryString() {
    }

    @Test
    void urlWithForm() {
    }

    @Test
    void testUrlWithForm() {
    }

    @Test
    void sortParm() {
    }

    @Test
    void mapToQueryString() {
    }

    @Test
    void decodeParams() {
    }

    @Test
    void mapToList() {
    }

    @Test
    void mapToSortedQueryString() {
    }

    @Test
    void paramToQueryString() {
    }

    @Test
    void convertRequestParamsToMap() {
    }

    @Test
    void getParametersWith() {
    }

    @Test
    void encodeParameterWithPrefix() {
    }

    @Test
    void commonHttpRequestParamConvert() {
    }

    @Test
    void buildMultiValueMap() {
    }

    @Test
    void testBuildMultiValueMap() {
    }
}