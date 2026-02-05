package com.qyhstech.core.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QyDigestTest {

    @Test
    void generateFingerprint() {
        System.out.println(QyDigest.generateFingerprint("asfasdfasdfsdfasdfasfasdfasdfsdfasdfasfasdfa".repeat(20)));
    }
}