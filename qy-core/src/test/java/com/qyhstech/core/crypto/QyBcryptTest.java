package com.qyhstech.core.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QyBcryptTest {

    @Test
    void encrypt() {

        String password1 = QyBcrypt.encrypt("admin");
        String password2 = QyBcrypt.encrypt("admin");
        System.out.println(password1);
        System.out.println(password2);
    }

    @Test
    void check() {
        String password1 = QyBcrypt.encrypt("admin");
        String password2 = QyBcrypt.encrypt("admin");

        System.out.println(QyBcrypt.check("admin", password1));
        System.out.println(QyBcrypt.check("admin", password2));
        System.out.println(QyBcrypt.check("admin1", password2));
    }


}