package com.qyhstech.core.crypto;

import cn.hutool.crypto.CryptoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * QyAes加密解密工具类的测试用例
 * 
 * @author QyHsTech
 */
@DisplayName("QyAes加密解密测试")
class QyAesTest {

    // 测试数据
    private static final String TEST_CONTENT = "this is a test";
    private static final String TEST_CONTENT_CHINESE = "这是一个中文测试内容";
    private static final String TEST_CONTENT_SPECIAL = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
    private static final String TEST_CONTENT_LONG = "This is a very long test content that contains multiple sentences and should test the encryption and decryption capabilities with longer text data to ensure the algorithm works correctly with various content lengths.";
    
    private static final String CUSTOM_KEY = "MyCustomKey12345";
    private static final String CUSTOM_VECTOR = "MyCustomVector16";
    private static final String INVALID_KEY_SHORT = "short";
    private static final String INVALID_KEY_LONG = "this_key_is_too_long_for_aes";

    @Test
    @DisplayName("测试使用默认密钥和向量的加密功能")
    void testEncryptWithDefaultKeyAndVector() {
        // 测试基本字符串加密
        String encrypted = QyAes.encrypt(TEST_CONTENT);
        assertNotNull(encrypted, "加密结果不应为null");
        assertNotEquals(TEST_CONTENT, encrypted, "加密后的内容应与原文不同");
        assertTrue(encrypted.length() > 0, "加密结果长度应大于0");
        
        // 测试中文内容加密
        String encryptedChinese = QyAes.encrypt(TEST_CONTENT_CHINESE);
        assertNotNull(encryptedChinese, "中文内容加密结果不应为null");
        assertNotEquals(TEST_CONTENT_CHINESE, encryptedChinese, "加密后的中文内容应与原文不同");
        
        // 测试特殊字符加密
        String encryptedSpecial = QyAes.encrypt(TEST_CONTENT_SPECIAL);
        assertNotNull(encryptedSpecial, "特殊字符加密结果不应为null");
        assertNotEquals(TEST_CONTENT_SPECIAL, encryptedSpecial, "加密后的特殊字符应与原文不同");
        
        // 测试长文本加密
        String encryptedLong = QyAes.encrypt(TEST_CONTENT_LONG);
        assertNotNull(encryptedLong, "长文本加密结果不应为null");
        assertNotEquals(TEST_CONTENT_LONG, encryptedLong, "加密后的长文本应与原文不同");
        
        System.out.println("默认加密结果: " + encrypted);
    }

    @Test
    @DisplayName("测试使用自定义密钥和默认向量的加密功能")
    void testEncryptWithCustomKey() {
        // 测试使用自定义密钥加密
        String encrypted = QyAes.encrypt(TEST_CONTENT, CUSTOM_KEY);
        assertNotNull(encrypted, "使用自定义密钥的加密结果不应为null");
        assertNotEquals(TEST_CONTENT, encrypted, "加密后的内容应与原文不同");
        
        // 测试不同密钥产生不同结果
        String encryptedDefault = QyAes.encrypt(TEST_CONTENT);
        assertNotEquals(encryptedDefault, encrypted, "不同密钥应产生不同的加密结果");
        
        System.out.println("自定义密钥加密结果: " + encrypted);
    }

    @Test
    @DisplayName("测试使用自定义密钥和向量的加密功能")
    void testEncryptWithCustomKeyAndVector() {
        // 测试使用自定义密钥和向量加密
        String encrypted = QyAes.encrypt(TEST_CONTENT, CUSTOM_KEY, CUSTOM_VECTOR);
        assertNotNull(encrypted, "使用自定义密钥和向量的加密结果不应为null");
        assertNotEquals(TEST_CONTENT, encrypted, "加密后的内容应与原文不同");
        
        // 测试不同向量产生不同结果
        String encryptedDefaultVector = QyAes.encrypt(TEST_CONTENT, CUSTOM_KEY);
        assertNotEquals(encryptedDefaultVector, encrypted, "不同向量应产生不同的加密结果");
        
        System.out.println("自定义密钥和向量加密结果: " + encrypted);
    }

    @Test
    @DisplayName("测试使用默认密钥和向量的解密功能")
    void testDecryptWithDefaultKeyAndVector() {
        // 先加密再解密，验证往返一致性
        String encrypted = QyAes.encrypt(TEST_CONTENT);
        String decrypted = QyAes.decrypt(encrypted);
        
        assertNotNull(decrypted, "解密结果不应为null");
        assertEquals(TEST_CONTENT, decrypted, "解密后应恢复原始内容");
        
        // 测试已知的加密内容解密
        String knownEncrypted = "1Eg1K3X75/lr3uw6uBoKIQ==";
        String knownDecrypted = QyAes.decrypt(knownEncrypted);
        assertNotNull(knownDecrypted, "已知加密内容的解密结果不应为null");
        
        System.out.println("解密结果: " + decrypted);
        System.out.println("已知内容解密结果: " + knownDecrypted);
    }

    @Test
    @DisplayName("测试使用自定义密钥和默认向量的解密功能")
    void testDecryptWithCustomKey() {
        // 先用自定义密钥加密，再用相同密钥解密
        String encrypted = QyAes.encrypt(TEST_CONTENT, CUSTOM_KEY);
        String decrypted = QyAes.decrypt(encrypted, CUSTOM_KEY);
        
        assertNotNull(decrypted, "使用自定义密钥的解密结果不应为null");
        assertEquals(TEST_CONTENT, decrypted, "解密后应恢复原始内容");
        
        System.out.println("自定义密钥解密结果: " + decrypted);
    }

    @Test
    @DisplayName("测试使用自定义密钥和向量的解密功能")
    void testDecryptWithCustomKeyAndVector() {
        // 先用自定义密钥和向量加密，再用相同参数解密
        String encrypted = QyAes.encrypt(TEST_CONTENT, CUSTOM_KEY, CUSTOM_VECTOR);
        String decrypted = QyAes.decrypt(encrypted, CUSTOM_KEY, CUSTOM_VECTOR);
        
        assertNotNull(decrypted, "使用自定义密钥和向量的解密结果不应为null");
        assertEquals(TEST_CONTENT, decrypted, "解密后应恢复原始内容");
        
        System.out.println("自定义密钥和向量解密结果: " + decrypted);
    }

    @Test
    @DisplayName("测试加密解密往返一致性")
    void testEncryptDecryptRoundTrip() {
        // 测试各种内容的往返一致性
        String[] testContents = {
            TEST_CONTENT,
            TEST_CONTENT_CHINESE,
            TEST_CONTENT_SPECIAL,
            TEST_CONTENT_LONG,
            "",  // 空字符串
            " ",  // 空格
            "a",  // 单字符
            "123456789012345678901234567890"  // 数字字符串
        };
        
        for (String content : testContents) {
            // 默认参数往返测试
            String encrypted1 = QyAes.encrypt(content);
            String decrypted1 = QyAes.decrypt(encrypted1);
            assertEquals(content, decrypted1, "默认参数往返测试失败: " + content);
            
            // 自定义密钥往返测试
            String encrypted2 = QyAes.encrypt(content, CUSTOM_KEY);
            String decrypted2 = QyAes.decrypt(encrypted2, CUSTOM_KEY);
            assertEquals(content, decrypted2, "自定义密钥往返测试失败: " + content);
            
            // 自定义密钥和向量往返测试
            String encrypted3 = QyAes.encrypt(content, CUSTOM_KEY, CUSTOM_VECTOR);
            String decrypted3 = QyAes.decrypt(encrypted3, CUSTOM_KEY, CUSTOM_VECTOR);
            assertEquals(content, decrypted3, "自定义密钥和向量往返测试失败: " + content);
        }
    }

    @Test
    @DisplayName("测试null值处理")
    void testNullHandling() {
        // 测试加密null值
        String encryptedNull = QyAes.encrypt(null);
        // 根据实际实现，这里可能返回null或抛出异常，需要根据具体实现调整
        
        // 测试解密null值
        String decryptedNull = QyAes.decrypt(null);
        // 根据实际实现调整断言
    }

    @Test
    @DisplayName("测试无效密钥处理")
    void testInvalidKeyHandling() {
        // 测试null密钥
        String encryptedWithNullKey = QyAes.encrypt(TEST_CONTENT, null);
        assertNull(encryptedWithNullKey, "使用null密钥应返回null");
        
        String decryptedWithNullKey = QyAes.decrypt("test", null);
        assertNull(decryptedWithNullKey, "使用null密钥解密应返回null");
        
        // 测试长度不正确的密钥
        String encryptedWithShortKey = QyAes.encrypt(TEST_CONTENT, INVALID_KEY_SHORT);
        assertNull(encryptedWithShortKey, "使用过短密钥应返回null");
        
        String encryptedWithLongKey = QyAes.encrypt(TEST_CONTENT, INVALID_KEY_LONG);
        assertNull(encryptedWithLongKey, "使用过长密钥应返回null");
        
        String decryptedWithShortKey = QyAes.decrypt("test", INVALID_KEY_SHORT);
        assertNull(decryptedWithShortKey, "使用过短密钥解密应返回null");
        
        String decryptedWithLongKey = QyAes.decrypt("test", INVALID_KEY_LONG);
        assertNull(decryptedWithLongKey, "使用过长密钥解密应返回null");
    }

    @Test
    @DisplayName("测试无效Base64内容解密")
    void testInvalidBase64Decryption() {
        // 测试无效的Base64内容解密
        assertThrows(CryptoException.class, () -> {
            QyAes.decrypt("invalid_base64_content");
        }, "解密无效Base64内容应抛出CryptoException");
        
        assertThrows(CryptoException.class, () -> {
            QyAes.decrypt("invalid_base64_content", CUSTOM_KEY);
        }, "使用自定义密钥解密无效Base64内容应抛出CryptoException");
        
        assertThrows(CryptoException.class, () -> {
            QyAes.decrypt("invalid_base64_content", CUSTOM_KEY, CUSTOM_VECTOR);
        }, "使用自定义密钥和向量解密无效Base64内容应抛出CryptoException");
    }

    @Test
    @DisplayName("测试密钥不匹配的解密")
    void testDecryptionWithWrongKey() {
        // 用一个密钥加密，用另一个密钥解密
        String encrypted = QyAes.encrypt(TEST_CONTENT, CUSTOM_KEY);
        
        // 用错误的密钥解密应该得到错误的结果或抛出异常
        assertThrows(CryptoException.class, () -> {
            QyAes.decrypt(encrypted, "WrongKey1234567");
        }, "使用错误密钥解密应抛出异常");
    }

    @Test
    @DisplayName("测试向量不匹配的解密")
    void testDecryptionWithWrongVector() {
        // 用一个向量加密，用另一个向量解密
        String encrypted = QyAes.encrypt(TEST_CONTENT, CUSTOM_KEY, CUSTOM_VECTOR);
        
        // 用错误的向量解密应该得到错误的结果或抛出异常
        assertThrows(CryptoException.class, () -> {
            QyAes.decrypt(encrypted, CUSTOM_KEY, "WrongVector12345");
        }, "使用错误向量解密应抛出异常");
    }

    @Test
    @DisplayName("测试加密结果的唯一性")
    void testEncryptionUniqueness() {
        // 相同内容多次加密应产生相同结果（因为使用固定IV）
        String encrypted1 = QyAes.encrypt(TEST_CONTENT);
        String encrypted2 = QyAes.encrypt(TEST_CONTENT);
        assertEquals(encrypted1, encrypted2, "相同内容使用相同参数加密应产生相同结果");
        
        // 不同内容应产生不同结果
        String encryptedDifferent = QyAes.encrypt(TEST_CONTENT + "_different");
        assertNotEquals(encrypted1, encryptedDifferent, "不同内容应产生不同的加密结果");
    }
}