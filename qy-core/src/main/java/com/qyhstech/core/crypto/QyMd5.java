package com.qyhstech.core.crypto;

import cn.hutool.crypto.SecureUtil;
import com.qyhstech.core.collection.QyMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class QyMd5 {

    /**
     * 使用Md5
     *
     * @param data
     * @return
     */
    public static String md5(byte[] data) {
        return SecureUtil.md5().digestHex(data).toUpperCase();
    }

    /**
     * MD5加密字符串
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        return SecureUtil.md5(str).toUpperCase();
    }

    /**
     * 获取16位的MD5
     *
     * @param input
     * @return
     */
    public static String md516(String input) {
        return md5(input).substring(8, 24);
    }

    /**
     * 检查Map怎么样组合，才能生成所需要的Sign，用于破解别人的Sign签名时使用
     *
     * @param mapData
     * @param checkMd5
     * @return
     */
    public static Map<String, String> mapMd5SignCheck(Map<String, String> mapData, String checkMd5) {

        Map<String, String> mapResult = QyMap.empty();

        String md5;

        // 先一条一条MD5的试一下吧
        for (Map.Entry<String, String> item : mapData.entrySet()) {
            md5 = md5(item.getValue());
            if (md5.toUpperCase().equals(checkMd5.toUpperCase())) {
                mapResult.clear();
                mapResult.put(item.getKey(), item.getValue());
                break;
            }
        }

        // 再排个序试一下
        return mapResult;
    }

}
