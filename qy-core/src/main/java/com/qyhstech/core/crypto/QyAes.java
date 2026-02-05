package com.qyhstech.core.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import com.qyhstech.core.encode.QyBase64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES CBC加密工具类
 * <p>
 * 提供基于AES-128-CBC模式的加密和解密功能。
 * 使用PKCS5Padding填充方式，支持自定义密钥和初始化向量。
 * </p>
 * 
 * <p>
 * 主要特性：
 * <ul>
 *   <li>支持AES-128-CBC加密模式</li>
 *   <li>使用Base64编码处理加密结果</li>
 *   <li>提供默认密钥和向量的便捷方法</li>
 *   <li>支持自定义密钥和初始化向量</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 注意事项：
 * <ul>
 *   <li>密钥长度必须为16位（AES-128）</li>
 *   <li>初始化向量长度必须为16位</li>
 *   <li>加密结果使用Base64编码</li>
 * </ul>
 * </p>
 * 
 * @author QyHsTech
 * @version 1.0
 * @since 1.0
 */
public class QyAes {

    /**
     * 默认加密密钥
     * <p>
     * 用于AES-128-CBC加密模式，密钥长度为16位。
     * 可以由26个字母和数字组成。
     * </p>
     */
    private static String KEY = "!QA2Z@w1sxO*(-8L";

    /**
     * 默认初始化向量
     * <p>
     * 用于CBC模式的初始化向量，长度为16位。
     * 可增加加密算法的强度。
     * </p>
     */
    private static String VECTOR = "!WFNZFU_{H%M(S|a";

    /**
     * AES算法标识符
     */
    public final static String AES = "AES";

    /**
     * 使用默认密钥和向量进行AES加密
     * <p>
     * 使用预设的默认密钥和初始化向量对指定内容进行AES-128-CBC加密。
     * 加密结果使用Base64编码返回。
     * </p>
     * 
     * @param content 待加密的明文内容
     * @return 加密后的Base64编码字符串，如果输入为null则返回null
     * @throws CryptoException 当加密过程中发生错误时抛出
     * @see #encrypt(String, String, String)
     */
    public static String encrypt(String content) {
        return encrypt(content, KEY, VECTOR);
    }

    /**
     * 使用指定密钥和默认向量进行AES加密
     * <p>
     * 使用指定的密钥和预设的默认初始化向量对指定内容进行AES-128-CBC加密。
     * 加密结果使用Base64编码返回。
     * </p>
     * 
     * @param content 待加密的明文内容
     * @param key 加密密钥，长度必须为16位
     * @return 加密后的Base64编码字符串，如果输入为null或密钥长度不为16位则返回null
     * @throws CryptoException 当加密过程中发生错误时抛出
     * @see #encrypt(String, String, String)
     */
    public static String encrypt(String content, String key) {
        return encrypt(content, key, VECTOR);
    }

    /**
     * 使用指定密钥和向量进行AES加密
     * <p>
     * 使用指定的密钥和初始化向量对指定内容进行AES-128-CBC加密。
     * 采用PKCS5Padding填充方式，加密结果使用Base64编码返回。
     * </p>
     * 
     * <p>
     * 加密流程：
     * <ol>
     *   <li>验证密钥有效性（非null且长度为16位）</li>
     *   <li>创建SecretKeySpec和IvParameterSpec</li>
     *   <li>初始化Cipher为加密模式</li>
     *   <li>执行加密操作</li>
     *   <li>对加密结果进行Base64编码</li>
     * </ol>
     * </p>
     * 
     * @param content 待加密的明文内容
     * @param key 加密密钥，长度必须为16位（AES-128）
     * @param vector 初始化向量，长度必须为16位，用于增强加密强度
     * @return 加密后的Base64编码字符串，如果密钥为null或长度不为16位则返回null
     * @throws CryptoException 当加密过程中发生错误时抛出，包装了底层的加密异常
     */
    public static String encrypt(String content, String key, String vector) {

        if (key == null) {
            return null;
        }
        if (key.length() != 16) {
            return null;
        }

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CharsetUtil.CHARSET_UTF_8), AES);
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes(CharsetUtil.CHARSET_UTF_8));

            return Base64.encode(encrypted);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 使用默认密钥和向量进行AES解密
     * <p>
     * 使用预设的默认密钥和初始化向量对Base64编码的加密内容进行AES-128-CBC解密。
     * 返回解密后的明文字符串。
     * </p>
     * 
     * @param content 待解密的Base64编码加密内容
     * @return 解密后的明文字符串，如果输入为null则返回null
     * @throws CryptoException 当解密过程中发生错误时抛出
     * @see #decrypt(String, String, String)
     */
    public static String decrypt(String content) {
        return decrypt(content, KEY, VECTOR);
    }

    /**
     * 使用指定密钥和默认向量进行AES解密
     * <p>
     * 使用指定的密钥和预设的默认初始化向量对Base64编码的加密内容进行AES-128-CBC解密。
     * 返回解密后的明文字符串。
     * </p>
     * 
     * @param content 待解密的Base64编码加密内容
     * @param key 解密密钥，长度必须为16位
     * @return 解密后的明文字符串，如果输入为null或密钥长度不为16位则返回null
     * @throws CryptoException 当解密过程中发生错误时抛出
     * @see #decrypt(String, String, String)
     */
    public static String decrypt(String content, String key) {
        return decrypt(content, key, VECTOR);
    }

    /**
     * 使用指定密钥和向量进行AES解密
     * <p>
     * 使用指定的密钥和初始化向量对Base64编码的加密内容进行AES-128-CBC解密。
     * 采用PKCS5Padding填充方式，返回解密后的明文字符串。
     * </p>
     * 
     * <p>
     * 解密流程：
     * <ol>
     *   <li>验证密钥有效性（非null且长度为16位）</li>
     *   <li>对输入内容进行Base64解码</li>
     *   <li>创建SecretKeySpec和IvParameterSpec</li>
     *   <li>初始化Cipher为解密模式</li>
     *   <li>执行解密操作</li>
     *   <li>将解密结果转换为UTF-8字符串</li>
     * </ol>
     * </p>
     * 
     * @param content 待解密的Base64编码加密内容
     * @param key 解密密钥，长度必须为16位（AES-128）
     * @param vector 初始化向量，长度必须为16位，必须与加密时使用的向量相同
     * @return 解密后的明文字符串，如果密钥为null或长度不为16位则返回null
     * @throws CryptoException 当解密过程中发生错误时抛出，包装了底层的解密异常
     */
    public static String decrypt(String content, String key, String vector) {
        if (key == null) {
            return null;
        }
        if (key.length() != 16) {
            return null;
        }

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), AES);
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = QyBase64.decode(content);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            return StrUtil.str(original, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            throw new CryptoException(e);
        }

    }
}
