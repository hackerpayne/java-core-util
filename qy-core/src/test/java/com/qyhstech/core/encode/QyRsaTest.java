package com.qyhstech.core.encode;

import com.qyhstech.core.crypto.QyRsa;
import com.qyhstech.core.crypto.QyRsaKeyPair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;

@Slf4j
class QyRsaTest {

    static String sourceText = "qyhstech";

    private static String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwEjBtNHyog+z8kaPWGRG7g0ub5g8pn4nfqtBxR0WRB4m187XfwV2NyfNUjoAtWibat3IvSnwKl/fs9WzLGCBER+BAO6yzH/2NbuP+XwkHNFki6RRtzYZ1vgx8xIXl4H5qmbGVz6qzNWDI/H05wxxpWzN34YJFq/buKQ4f4hHtwP1TYfNebD0k9+YOLJ4/ggjBvafAM0EDfz6oLqF8AXWgW1X2W6pvOpzNerVLYCxClDAa6+ThywUP1qGOyJ30qo/5pQp0FiNZc6hfsyvfLJAglRWNXhcPX3wDj5nKQXx0AUM/GMeYQd7nGYMMCxpNmuIh0KDqvelW/rO9TLSomSOWQIDAQAB";

    private static String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDASMG00fKiD7PyRo9YZEbuDS5vmDymfid+q0HFHRZEHibXztd/BXY3J81SOgC1aJtq3ci9KfAqX9+z1bMsYIERH4EA7rLMf/Y1u4/5fCQc0WSLpFG3NhnW+DHzEheXgfmqZsZXPqrM1YMj8fTnDHGlbM3fhgkWr9u4pDh/iEe3A/VNh815sPST35g4snj+CCMG9p8AzQQN/PqguoXwBdaBbVfZbqm86nM16tUtgLEKUMBrr5OHLBQ/WoY7InfSqj/mlCnQWI1lzqF+zK98skCCVFY1eFw9ffAOPmcpBfHQBQz8Yx5hB3ucZgwwLGk2a4iHQoOq96Vb+s71MtKiZI5ZAgMBAAECggEARseBZkoSAk29c2U9xs/CvEXl3fYv/Sla6Gmp4CEA3TosGixtbrhX952TGr13HU/QRdo11kvlt/NqqQv01FUBrYfnOKyk7Fn+C3FrYe6sbF83n59w5PvpLxRVbqAuRvr1KCCEygCrZpMBnu2ltJcEDlyqu6jfF5bMTn9EvM0xeNA8xz+i6aO0s3yJhv0CwcbKxUqd2Iz/0MnFpDFnwXlIb50oAI8nqQSdq+31dJa5vABTbxUZ820kui0Q3TTJfUgU3h2InG6BTlvniEzrPuIXQpJctNk6ym7D1f3Lc/1Fl5sJovZ68dT0vJB7pI1dhyQtlYCHb0r5i7QJg+7/kFiVcQKBgQDkZEeTGeArXZe+EAZIPcPYV0CQmSWWpYH8MWtoxM1SBiYWPDr/PzHbeOnijETGR/m4qbKuP5ALM76gij2+8WwSMS/tjQjKaPT1l5YS1P2KGTpRdeUS8oQCESZq1WU9g06L5UGbEZ+jrSdBelX+eH3NK9+/lFhKdBuPVUbjv6Lz8wKBgQDXhxuzSxLPKcm69xEyVZ9gwhN99yNphsVg2XAC3QWPd0V6Q1mG1V82ZpW2B8jx51/oZTwWuazXxWyp28pKlGWBIwHw2wcdpWVYffBoqMC+rkYLSyoFS7lM+lE7V5FSUmToQja3Y8gLXVc9Q8VUv7F9ylz7skBZWWbluLSWThqjgwKBgAa3KB+cKCo+Q0L0Z6riHgsTbbOSvTczwKGwtk7LUpox0hbfSIDO0+F3KbFenHsUDLSwUK+s8MhTiixgRDaEFMBoL9eVSQfilClQfropBxdzbHKOu+CVvunspSuJAMQRQ1L7t7UejIocgkX7meG0m0ZYHukcLIhr54+MW1xA+0KfAoGAYJgQ9h4jY+1amAy7DFALihXxHndwy463T1ykMuRT5aSS4uTkbuWaMotvdZGvoZsNBN66O4UjzeCRft8enqY7h9DkwcmoOsUx0qM+5+kZ2VvJ0K3zO0Rg2/skBPCOFNMQnnduhmqV8WH4A2EoQ39WUQYs/jE5XdIPp81MQkDm93kCgYEAm3xn72emIEBsq/tHmgGlwJ+QpMy3421WdT5SqZWld+d3kpflAxbHJ8SFsdNCLMTJFCUedEEkik4AqY1lpT2eLNHyHg+vhqfYFvm7cvG5ro0pbkbkA2nAn+xW3Aja5Kfl0nIxZ5urs8V5pFiAMmAZG+ECO/bzbIbZQovcA3RYecY=";


    /**
     * 公钥加密私钥解密
     */
    public void test2(QyRsaKeyPair keyPair) throws Exception {
        System.out.println("***************** 公钥加密私钥解密开始 *****************");
        String text1 = QyRsa.encryptByPublicKey(keyPair.getPublicKey(), sourceText);
        String text2 = QyRsa.decryptByPrivateKey(keyPair.getPrivateKey(), text1);
        System.out.println("加密前：" + sourceText);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (sourceText.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 公钥加密私钥解密结束 *****************");
    }

    /**
     * 私钥加密公钥解密
     *
     * @throws Exception /
     */
    private void test3(QyRsaKeyPair keyPair) throws Exception {
        System.out.println("***************** 私钥加密公钥解密开始 *****************");
        String text1 = QyRsa.encryptByPrivateKey(keyPair.getPrivateKey(), sourceText);
        String text2 = QyRsa.decryptByPublicKey(keyPair.getPublicKey(), text1);
        System.out.println("加密前：" + sourceText);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (sourceText.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 私钥加密公钥解密结束 *****************");
    }


    @Test
    void generateKeyPair() {
        System.out.println(QyRsa.generateKeyPair());
    }

    @Test
    void generateKeyPair2() {
        System.out.println(QyRsa.generateKeyPair(1024));
    }

    @Test
    void generateKeyPairToBase64() {
        QyRsaKeyPair keyPair = QyRsa.generateKeyPairToBase64();
        System.out.println("公钥：" + keyPair.getPublicKey());
        System.out.println("私钥：" + keyPair.getPrivateKey());
    }

    @Test
    void testGenerateKeyPairToBase641() {
        QyRsaKeyPair keyPair = QyRsa.generateKeyPairToBase64(1024);
        System.out.println("公钥2：" + keyPair.getPublicKey());
        System.out.println("私钥2：" + keyPair.getPrivateKey());
    }

    @Test
    void generateKeyPairToMap() {
        System.out.println(QyRsa.generateKeyPairToMap(2048));
    }

    @Test
    void getPublicKeyFromBase64Str() {
        PublicKey publicKey = QyRsa.getPublicKeyFromBase64Str(publicKeyStr);
        System.out.println(publicKey);
    }

    @Test
    void getPrivateKeyFromBase64Str() {
        var privateKey = QyRsa.getPrivateKeyFromBase64Str(privateKeyStr);
        System.out.println(privateKey);
    }

    @Test
    void getKeyBase64Str() {
        PublicKey publicKey = QyRsa.getPublicKeyFromBase64Str(publicKeyStr);
        System.out.println("公钥");
        System.out.println(QyRsa.getKeyBase64Str(publicKey));

        var privateKey = QyRsa.getPrivateKeyFromBase64Str(privateKeyStr);
        System.out.println("私钥：");
        System.out.println(QyRsa.getKeyBase64Str(privateKey));
    }

    @Test
    void getPrivateKeyFromMap() {
    }

    @Test
    void getPublicKeyFromMap() {
    }


    @Test
    void encryptByPrivateKey() {
        System.out.println("私钥加密：");
        System.out.println(QyRsa.encryptByPrivateKey(privateKeyStr, "qyhstech"));
    }

    @Test
    void decryptByPublicKey() {
        String encrypt = QyRsa.encryptByPrivateKey(privateKeyStr, "qyhstech");
        System.out.println("私钥加密结果：");
        System.out.println(encrypt);

        System.out.println("公钥解密结果：");
        System.out.println(QyRsa.decryptByPublicKey(publicKeyStr, encrypt));
    }

    @Test
    void encryptByPublicKey() {
        System.out.println("公钥加密");
        System.out.println(QyRsa.encryptByPublicKey(publicKeyStr, "qyhstech"));
    }

    @Test
    void decryptByPrivateKey() {
        String encrypt = QyRsa.encryptByPublicKey(publicKeyStr, "qyhstech");
        System.out.println("公钥加密的结果：");
        System.out.println(encrypt);

        System.out.println("私钥解密的结果：");
        System.out.println(QyRsa.decryptByPrivateKey(privateKeyStr, encrypt));
    }

    @Test
    void doLongerCipherFinalSeg() {
    }

    @Test
    void sign() {

    }

    @Test
    void testSign() {
    }

    @Test
    void testSign1() {
    }

    @Test
    void verify() {
    }

    @Test
    void testVerify() {
    }

    @Test
    void testVerify1() {
    }

    @Test
    void signWithMd5() {
        System.out.println("签名");
        System.out.println(QyRsa.signWithMd5("qyhstech", privateKeyStr));
    }

    @Test
    void verifyMd5Sign() {
        System.out.println("签名");
        String signStr = QyRsa.signWithMd5("qyhstech", privateKeyStr);
        System.out.println(signStr);

        System.out.println("验签");
        System.out.println(QyRsa.verifyMd5Sign("qyhstech", signStr, publicKeyStr));
    }

    @Test
    void signWithSha1() {
        System.out.println("签名");
        System.out.println(QyRsa.signWithSha1("qyhstech", privateKeyStr));
    }

    @Test
    void verifySha1Sign() {
        System.out.println("签名");
        String signStr = QyRsa.signWithSha1("qyhstech", privateKeyStr);
        System.out.println(signStr);

        System.out.println("验签");
        System.out.println(QyRsa.verifySha1Sign("qyhstech", signStr, publicKeyStr));
    }
}