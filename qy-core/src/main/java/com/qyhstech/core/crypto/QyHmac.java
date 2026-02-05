package com.qyhstech.core.crypto;

import cn.hutool.core.util.HexUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 使用HMac256加密
 * HMAC（Hash-based Message Authentication Code）：基于散列的消息认证码，使用一个密钥和一个消息作为输入，生成它们的消息摘要。
 */
@Slf4j
public class QyHmac {

    private static final Charset UTF_ENCODE = StandardCharsets.UTF_8;

    /**
     * 使用指定的密码对整个Map的内容生成消息摘要（散列值）
     *
     * @param key
     * @param map
     * @return
     */
    public static String encrypt(String key, Map<String, ?> map) throws Exception {
        StringBuilder s = new StringBuilder();
        for (Object values : map.values()) {
            if (values instanceof String[]) {
                for (String value : (String[]) values) {
                    s.append(value);
                }
            } else if (values instanceof List) {
                for (String value : (List<String>) values) {
                    s.append(value);
                }
            } else {
                s.append(values);
            }
        }
        return encrypt(key, s.toString());
    }

    /**
     * 生成HMAC加密结果
     * 使用指定的密码对内容生成消息摘要（散列值）
     *
     * @param key     密钥，服务端保存
     * @param content 内容，客户端提交
     * @return
     */
    public static String encrypt(String content, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = key.getBytes(UTF_ENCODE);
            byte[] dataBytes = content.getBytes(UTF_ENCODE);

            SecretKey secret = new SecretKeySpec(secretByte, "HmacSHA256");
            mac.init(secret);

            byte[] doFinal = mac.doFinal(dataBytes);
            return HexUtil.encodeHexStr(doFinal);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

}
