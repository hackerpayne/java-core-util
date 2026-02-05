package com.qyhstech.core.util;

import com.qyhstech.core.domain.constant.QyConsts;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    public void testPath() {
        System.out.println("<<<<<<<<<<< CLassPath >>>>>>>>>>>>>");
        System.out.println(QyConsts.ClassPath);

        System.out.println("<<<<<<<<<<< CurrentDir >>>>>>>>>>>>>");
        System.out.println(QyConsts.CurrentDir);

        System.out.println(this.getClass().getResource("/"));
    }

}
