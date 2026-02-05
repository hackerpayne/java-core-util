package com.qyhstech.core.util;


import cn.hutool.core.lang.Console;
import com.qyhstech.core.encode.QyCharset;
import com.qyhstech.core.encode.QyHex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * HexUtil单元测试
 *
 * @author Looly
 */
public class HexUtilTest {

    @Test
    public void hexStrTest() {
        String str = "我是一个字符串";

        String hex = QyHex.encodeHexStr(str, QyCharset.CHARSET_UTF_8);
        Console.log(hex);

        String decodedStr = QyHex.decodeHexStr(hex);

        Assertions.assertEquals(str, decodedStr);
    }
}
