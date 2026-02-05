package com.qyhstech.core.encode;

import org.junit.jupiter.api.Test;

public class EncodeUtilTest {

    @Test
    public void test() {
        String unicode = QyEncode.str2Unicode("中文");

        System.out.println(unicode);

        String decode = QyEncode.unicode2Str(unicode);

        System.out.println("还原结果：" + decode);
    }

}