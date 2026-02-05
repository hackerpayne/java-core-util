package com.qyhstech.core.http.net;

import com.qyhstech.core.http.QyAdsl;
import org.junit.jupiter.api.Test;

public class QyAdslTest {


    @Test
    public void testConn() throws Exception {
        QyAdsl.connAdsl("宽带", "hzhz**********", "******");
        Thread.sleep(1000);
        QyAdsl.cutAdsl("宽带");
        Thread.sleep(1000);
        //再连，分配一个新的IP
        QyAdsl.connAdsl("宽带", "hzhz**********", "******");
    }

}