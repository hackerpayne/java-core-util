package com.qyhstech.core.crypto;

import cn.hutool.crypto.digest.DigestUtil;
import com.qyhstech.core.encode.QyHex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QyDigest extends DigestUtil {

    /**
     * 生成SHA256指纹
     *
     * @param input
     * @return
     */
    public static String generateFingerprint(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // 可替换为 MD5/SHA-1
            byte[] hashBytes = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return QyHex.byteToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("算法不支持", e);
        }
    }

}
