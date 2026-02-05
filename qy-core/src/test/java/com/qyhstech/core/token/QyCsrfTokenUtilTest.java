package com.qyhstech.core.token;

import com.qyhstech.core.token.QyCsrfTokenUtil;

class QyCsrfTokenUtilTest {


    public void test() {
        for (int i = 0; i < 9; i++) {
            System.out.println(QyCsrfTokenUtil.getInstance().makeToken(""));
        }
    }

}