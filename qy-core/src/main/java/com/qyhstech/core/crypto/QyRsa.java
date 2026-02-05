package com.qyhstech.core.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用RSA做签名和验签，密钥长度默认1024
 * Java私钥为PKCS8格式，PHP使用的私钥为PKCS1格式，先将Java私钥转换为PKCS1格式，公钥不用转换，转换工具可以使用支付宝提供的签名工具
 * https://docs.open.alipay.com/291/105971/
 * https://www.cnblogs.com/nihaorz/p/10690643.html
 */
@Slf4j
public class QyRsa {

    private static final String CHARSET = "utf-8";

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 算法/模式/填充
     */
    public static final String CIPHER_TRANSFORMATION_RSA = "RSA/ECB/PKCS1Padding";

    /**
     * 获取公钥的Map的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的Map的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 签名算法 MD5withRSA
     */
    public static final String SIGNATURE_ALGORITHM_MD5 = "MD5withRSA";

    /**
     * 签名算法 SHA1withRSA
     */
    public static final String SIGNATURE_ALGORITHM_SHA1 = "SHA1withRSA";

    /**
     * 生成2048位的公私钥
     *
     * @return
     */
    public static KeyPair generateKeyPair() {
        return generateKeyPair(2048);
    }

    /**
     * 生成KeyPair密钥对，公钥和私钥生成
     *
     * @param keySize
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeyPair(Integer keySize) {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyPairGenerator.initialize(null != keySize ? keySize : 2048);
        return keyPairGenerator.generateKeyPair();
    }


    /**
     * 默认使用2048位加密
     *
     * @return
     */
    public static QyRsaKeyPair generateKeyPairToBase64() {
        return generateKeyPairToBase64(2048);
    }

    /**
     * 构建RSA密钥对
     *
     * @param keySize
     * @return
     */
    public static QyRsaKeyPair generateKeyPairToBase64(Integer keySize) {
        KeyPair keyPair = generateKeyPair(keySize);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encode(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encode(rsaPrivateKey.getEncoded());
        return new QyRsaKeyPair(publicKeyString, privateKeyString);
    }

    /**
     * 生成公钥和私钥到Map里面
     *
     * @param keySize
     * @return
     */
    public static HashMap<String, Object> generateKeyPairToMap(Integer keySize) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPair keyPair = generateKeyPair(keySize);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, privateKey);
        return map;
    }

    /**
     * 从Base64字符串中得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromBase64Str(String key) {
        try {
            byte[] keyBytes = Base64.decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * 从Base64字符串中得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromBase64Str(String key) {
        try {
            byte[] keyBytes = Base64.decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 得到密钥字符串（经过base64编码）
     *
     * @param key
     * @return
     */
    public static String getKeyBase64Str(Key key) {
        byte[] keyBytes = key.getEncoded();
        return Base64.encode(keyBytes);
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     */
    public static String getPrivateKeyFromMap(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encode(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return
     */
    public static String getPublicKeyFromMap(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encode(key.getEncoded());
    }

    /**
     * 私钥加密
     *
     * @param privateKeyText 私钥
     * @param text           待加密的信息
     * @return /
     * @throws Exception /
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPrivateKeyFromBase64Str(privateKeyText));
            byte[] result = doLongerCipherFinal(Cipher.ENCRYPT_MODE, cipher, text.getBytes());
            return Base64.encode(result);
        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }
    }

    /**
     * 公钥解密
     *
     * @param publicKeyText 公钥
     * @param text          待解密的信息
     * @return /
     */
    public static String decryptByPublicKey(String publicKeyText, String text) {

        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getPublicKeyFromBase64Str(publicKeyText));
            byte[] result = doLongerCipherFinal(Cipher.DECRYPT_MODE, cipher, Base64.decode(text));
            return StrUtil.str(result, CharsetUtil.UTF_8);
        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }

    }

    /**
     * 公钥加密
     *
     * @param publicKeyText 公钥
     * @param text          待加密的文本
     * @return /
     */
    public static String encryptByPublicKey(String publicKeyText, String text) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyFromBase64Str(publicKeyText));
            byte[] result = doLongerCipherFinal(Cipher.ENCRYPT_MODE, cipher, text.getBytes());
            return Base64.encode(result);
        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText 私钥
     * @param text           待解密的文本
     * @return /
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKeyFromBase64Str(privateKeyText));
            byte[] result = doLongerCipherFinal(Cipher.DECRYPT_MODE, cipher, Base64.decode(text));
            return StrUtil.str(result, CharsetUtil.UTF_8);
        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }
    }

    /**
     * 动态根据Cipher进行加密或者解密操作
     *
     * @param opMode 指定加密还是解密方式
     * @param cipher Cipher对象
     * @param source 要处理的数据
     * @return
     */
    private static byte[] doLongerCipherFinal(int opMode, Cipher cipher, byte[] source) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (opMode == Cipher.DECRYPT_MODE) {
                out.write(cipher.doFinal(source));
            } else {
                int offset = 0;
                int totalSize = source.length;
                while (totalSize - offset > 0) {
                    int size = Math.min(cipher.getOutputSize(0) - 11, totalSize - offset);
                    out.write(cipher.doFinal(source, offset, size));
                    offset += size;
                }
            }
            out.close();
            return out.toByteArray();
        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }
    }

    /**
     * 分段加解密
     *
     * @param cipher
     * @param data
     * @param maxBlockSize 最大加密或者解密长度
     * @return
     * @throws Exception
     */
    public static byte[] doLongerCipherFinalSeg(Cipher cipher, byte[] data, int maxBlockSize) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int offSet = 0;
            byte[] cache;
            int i = 0;
            int inputLen = data.length;

            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxBlockSize) {
                    cache = cipher.doFinal(data, offSet, maxBlockSize);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxBlockSize;
            }
            out.close();
            return out.toByteArray();

        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }

    }

    /**
     * 使用指定方法进行签名
     *
     * @param content
     * @param base64PrivateKey
     * @param algorithm
     * @return
     */
    public static String sign(String content, String base64PrivateKey, String algorithm) {
        return sign(content, getPrivateKeyFromBase64Str(base64PrivateKey), algorithm);
    }

    /**
     * 使用指定私钥和算法进行签名
     *
     * @param content
     * @param privateKey
     * @param algorithm
     * @return
     */
    public static String sign(String content, PrivateKey privateKey, String algorithm) {
        return sign(StrUtil.bytes(content, CHARSET), privateKey, algorithm);
    }

    /**
     * 使用指定算法进行签名
     *
     * @param contentBytes 要签名的内容
     * @param privateKey   私钥
     * @param algorithm    使用的签名算法
     * @return
     * @throws Exception
     */
    public static String sign(byte[] contentBytes, PrivateKey privateKey, String algorithm) {
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(privateKey);
            signature.update(contentBytes);
            return Base64.encode(signature.sign());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用指定算法验签
     *
     * @param content
     * @param publicKey
     * @param sign
     * @param algorithm
     * @return
     */
    public static boolean verify(String content, String publicKey, String sign, String algorithm) {
        return verify(StrUtil.bytes(content, CHARSET), getPublicKeyFromBase64Str(publicKey), sign, algorithm);
    }

    /**
     * 公钥验签
     *
     * @param content
     * @param publicKey
     * @param sign
     * @param algorithm
     * @return
     */
    public static boolean verify(String content, PublicKey publicKey, String sign, String algorithm) {
        return verify(StrUtil.bytes(content, CHARSET), publicKey, sign, algorithm);
    }

    /**
     * 公钥验签
     *
     * @param data      要加密的数据
     * @param publicKey 公钥
     * @param sign      签名
     * @param algorithm 使用的算法
     * @return
     */
    public static boolean verify(byte[] data, PublicKey publicKey, String sign, String algorithm) {
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(Base64.decode(sign));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用MD5WithRSA签名
     *
     * @param content
     * @param base64PrivateKey
     * @return
     */
    public static String signWithMd5(String content, String base64PrivateKey) {
        return sign(content, base64PrivateKey, SIGNATURE_ALGORITHM_MD5);
    }

    /**
     * 使用Base64加密后的数据做验签使用
     *
     * @param content         加密内容
     * @param sign            签名
     * @param base64PublicKey base64格式的公钥
     * @return
     */
    public static boolean verifyMd5Sign(String content, String sign, String base64PublicKey) {
        return verify(content, base64PublicKey, sign, SIGNATURE_ALGORITHM_MD5);
    }

    /**
     * SHA1签名
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static String signWithSha1(String content, String privateKey) {
        return sign(content, privateKey, SIGNATURE_ALGORITHM_SHA1);
    }

    /**
     * SHA1 验签
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean verifySha1Sign(String content, String sign, String publicKey) {
        return verify(content, publicKey, sign, SIGNATURE_ALGORITHM_SHA1);
    }


}
