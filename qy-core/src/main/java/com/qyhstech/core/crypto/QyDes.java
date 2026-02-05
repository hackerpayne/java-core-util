package com.qyhstech.core.crypto;

import cn.hutool.core.util.HexUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class QyDes {

    public final static String DES = "DES";

    /**
     * 加密返回16进制值，一般也可以使用Base64
     *
     * @param content
     * @param key
     * @return
     */
    public static String encryptToHexString(String content, String key) {
        byte[] resultHex = encrypt(content, key);
        return HexUtil.encodeHexStr(resultHex);
    }

    /**
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String content, String key) {
        return encrypt(content.getBytes(), key.getBytes());
    }

    /**
     * DES 解密 字符串
     *
     * @param hexResult
     * @param key
     * @return
     */
    public static String decryptHex(String hexResult, String key) {
        byte[] hexBytes = HexUtil.decodeHex(hexResult.toCharArray());
        return decrypt(hexBytes, key);
    }

    /**
     * 加密
     *
     * @param content 待加密内容
     * @param key     加密的密钥
     * @return
     */
    public static byte[] encrypt(byte[] content, byte[] key) {
        try {
            SecureRandom random = new SecureRandom(); // 生成一个可信任的随机数源
            DESKeySpec desKey = new DESKeySpec(key); // 从原始密钥数据创建DESKeySpec对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);

            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);

            // 用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);

            return cipher.doFinal(content);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param key     解密的密钥
     * @return
     */
    public static String decrypt(byte[] content, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            byte[] result = cipher.doFinal(content);
            return new String(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
