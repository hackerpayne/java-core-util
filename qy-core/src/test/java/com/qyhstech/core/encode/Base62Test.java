package com.qyhstech.core.encode;

import org.junit.jupiter.api.Test;

public class Base62Test {


    @Test
    public void test() {

        String encode = QyBase62.encodeBase62("12345");
        System.out.println("加密结果为：" + encode);

        String decode = QyBase62.decodeBase62Str(encode);
        System.out.println("解密结果为：" + decode);
    }

}
