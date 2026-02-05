package com.qyhstech.core.util;

import com.qyhstech.core.domain.enums.QyEnum;
import org.junit.jupiter.api.Test;

public class EnumBinUtilTest {

    @Test
    public void test() {

        Integer check1 = 1 << 0; // (2)进制：0000 0001, (10)进制：1
        Integer check2 = 1 << 1; // (2)进制：0000 0010, (10)进制：2
        Integer check3 = 1 << 2; // (2)进制：0000 0100, (10)进制：4
        Integer check4 = 1 << 3; // (2)进制：0000 1000, (10)进制：8
        System.out.println(check1);
        System.out.println(check2);
        System.out.println(check3);
        System.out.println(check4);

        Integer total = QyEnum.add(check1, check2, check4);
        System.out.println("计算组合1：" + total);

        System.out.println("计算包含1：" + QyEnum.contains(total, check1));
        System.out.println("计算包含2：" + QyEnum.contains(total, check2));
        System.out.println("计算包含3：" + QyEnum.contains(total, check3));
        System.out.println("计算包含4：" + QyEnum.contains(total, check4));

        total = QyEnum.remove(total, check2);
        System.out.println("删除其中一个之后：" + total);

        System.out.println("计算包含1：" + QyEnum.contains(total, check1));
        System.out.println("计算包含2：" + QyEnum.contains(total, check2));
        System.out.println("计算包含3：" + QyEnum.contains(total, check3));
        System.out.println("计算包含4：" + QyEnum.contains(total, check4));
    }

}