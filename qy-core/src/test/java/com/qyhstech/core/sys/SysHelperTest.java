package com.qyhstech.core.sys;

import com.qyhstech.core.domain.constant.QyConsts;
import org.junit.jupiter.api.Test;

/**
 * Created by Kyle on 16/10/12.
 */
public class SysHelperTest {

    @Test
     void testJvmBitVersion() throws Exception {

        String ver = QyConsts.JvmVersion;
        System.out.println(ver);

    }

}