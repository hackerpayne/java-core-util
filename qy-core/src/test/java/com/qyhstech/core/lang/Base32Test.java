package com.qyhstech.core.lang;

import com.qyhstech.core.encode.QyBase32;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Base32Test {

    @Test
    public void encodeAndDecodeTest() {
        String a = "伦家是一个非常长的字符串";
        String encode = QyBase32.encode(a);
        Assertions.assertEquals("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI", encode);

        String decodeStr = QyBase32.decodeStr(encode);
        Assertions.assertEquals(a, decodeStr);
    }

    @Test
    public void Test() {
        String result = QyBase32.encode("hahahahaha");
        System.out.println("结果1为：" + result);

        result = QyBase32.decodeStr(result);
        System.out.println("解密结果为：" + result);
    }

}
