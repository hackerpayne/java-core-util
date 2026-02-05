package com.qyhstech.core.util;

import com.qyhstech.core.number.QyNum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link QyNum} 单元测试类
 *
 * @author Looly
 */
public class NumberUtilTest {

    @Test
    public void test() {
        System.out.println(QyNum.formatDouble(116.46604901357878, 5));
    }


    @Test
    public void roundTest() {

//        //四舍
//        double round3 = NumberUtil.round(2.674, 2);
//        double round4 = NumberUtil.round("2.674", 2);
//        Assertions.assertEquals(round3, 2.67, 0);
//        Assertions.assertEquals(round4, 2.67, 0);
//
//        //五入
//        double round1 = NumberUtil.round(2.675, 2);
//        double round2 = NumberUtil.round("2.675", 2);
//        Assertions.assertEquals(round1, 2.68, 0);
//        Assertions.assertEquals(round2, 2.68, 0);
    }

    @Test
    public void roundStrTest() {
        String roundStr = QyNum.roundStr(2.647, 2);
        Assertions.assertEquals(roundStr, "2.65");
    }

    @Test
    public void decimalFormatTest() {
        long c = 299792458;//光速

//        String format = NumberUtil.decimalFormat(",###", c);
//        Assertions.assertEquals("299,792,458", format);
    }
}
