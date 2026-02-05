package com.qyhstech.core.lang;


import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.encode.QyBase64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Base64UtilTest {

    @Test
    public void encodeAndDecodeTest() {
        String a = "伦家是一个非常长的字符串66";
        String encode = QyBase64.encodeToStr(a);
        Assertions.assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5LiyNjY=", encode);

        String decodeStr = QyBase64.decodeToStr(encode);
        Assertions.assertEquals(a, decodeStr);
    }

    @Test
    public void urlSafeEncodeAndDecodeTest() {
        String a = "伦家需要安全感55";
        String encode = StrUtil.utf8Str(QyBase64.encodeToStr(a));
        Assertions.assertEquals("5Lym5a626ZyA6KaB5a6J5YWo5oSfNTU", encode);

        String decodeStr = QyBase64.decodeToStr(encode);
        Assertions.assertEquals(a, decodeStr);
    }
}
