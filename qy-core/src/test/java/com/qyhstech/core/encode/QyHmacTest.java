package com.qyhstech.core.encode;

import com.qyhstech.core.crypto.QyHmac;
import org.junit.jupiter.api.Test;

class QyHmacTest {

    @Test
    void encrypt() {
        System.out.println(QyHmac.encrypt("qyhstech", "123456"));
    }


}