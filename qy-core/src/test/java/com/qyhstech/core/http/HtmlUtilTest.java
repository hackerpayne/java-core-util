package com.qyhstech.core.http;

import org.junit.jupiter.api.Test;

public class HtmlUtilTest {

    @Test
    public void testMetaFresh() {
        String html = "<meta http-equiv=\"refresh\" content=\"0; url='http://bbs.moonseo.cn/'\" />";
        System.out.println(QyHtmlUtil.getMetaRefresh(html));
    }
}