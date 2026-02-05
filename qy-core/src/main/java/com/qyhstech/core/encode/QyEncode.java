package com.qyhstech.core.encode;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.domain.enums.QyEncodingTypeEnum;

/**
 * 编码处理辅助类
 */
public class QyEncode {

    /**
     * 二进制转为指定类型
     *
     * @param input
     * @param qyEncodingTypeEnum
     * @return
     */
    public static String encodeTo(byte[] input, QyEncodingTypeEnum qyEncodingTypeEnum) {
        String encryptedStr = null;
        switch (qyEncodingTypeEnum) {
            case HEX:
                encryptedStr = QyHex.encodeHexStr(input);
                break;
            case BASE64:
                encryptedStr = QyBase64.encodeToStr(input);
                break;
            case STRING:
                encryptedStr = StrUtil.str(input, QyCharset.CHARSET_UTF_8);
                break;
        }
        return encryptedStr;
    }

    /**
     * 从指定字符串解开到二进制
     *
     * @param input
     * @param qyEncodingTypeEnum
     * @return
     */
    public static byte[] decodeTo(String input, QyEncodingTypeEnum qyEncodingTypeEnum) {
        byte[] encrypted = null;
        switch (qyEncodingTypeEnum) {
            case HEX:
                encrypted = QyHex.decodeHex(input);
                break;
            case BASE64:
                encrypted = QyBase64.decode(input);
                break;
            case STRING:
                encrypted = input.getBytes(QyCharset.CHARSET_UTF_8);
                break;
        }
        return encrypted;
    }

    /**
     * @param string
     * @return
     */
    public static String str2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * @param unicode
     * @return
     */
    public static String unicode2Str(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

}
