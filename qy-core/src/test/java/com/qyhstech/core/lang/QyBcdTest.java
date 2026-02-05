package com.qyhstech.core.lang;


import com.qyhstech.core.encode.QyBcd;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QyBcdTest {

    @Test
    public void bcdTest() {
        String strForTest = "123456ABCDEF";

        //转BCD
        byte[] bcd = QyBcd.strToBcd(strForTest);
        String str = QyBcd.bcdToStr(bcd);
        //解码BCD
        Assertions.assertEquals(strForTest, str);
    }
}
