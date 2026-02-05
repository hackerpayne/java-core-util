package com.qyhstech.core.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QyDesTest {

    @Test
    void encryptToHexString() {
        System.out.println(QyDes.encryptToHexString("haha", "qyhstech"));
    }

    @Test
    void encrypt() {
    }

    @Test
    void decryptHex() {
        System.out.println("加密结果");
        String encrypt = QyDes.encryptToHexString("haha", "qyhstech");
        System.out.println(encrypt);

        System.out.println("解密结果");
        System.out.println(QyDes.decryptHex(encrypt, "qyhstech"));
    }

    @Test
    void testEncrypt() {
    }

    @Test
    void decrypt() {
    }
}