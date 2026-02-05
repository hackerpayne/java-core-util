package com.qyhstech.core.crypto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RSA密钥对对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QyRsaKeyPair {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

}
