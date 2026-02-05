package com.qyhstech.core.util;


import cn.hutool.core.util.ReUtil;
import com.qyhstech.core.http.QyNet;
import com.qyhstech.core.regex.QyRegexPatterns;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

/**
 * NetUtil单元测试
 *
 * @author Looly
 */
public class NetUtilTest {

    @Test
    public void getLocalhostTest() {
        InetAddress localhost = QyNet.getLocalhost();
        Assertions.assertNotNull(localhost);
    }

    @Test
    public void getLocalMacAddressTest() {
        String macAddress = QyNet.getLocalMacAddress();
        Assertions.assertNotNull(macAddress);

        //验证MAC地址正确
        boolean match = ReUtil.isMatch(QyRegexPatterns.MAC_ADDRESS, macAddress);
        Assertions.assertTrue(match);
    }
}
