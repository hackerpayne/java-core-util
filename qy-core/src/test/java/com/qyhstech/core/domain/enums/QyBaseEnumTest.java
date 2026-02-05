package com.qyhstech.core.domain.enums;

import com.qyhstech.core.json.QyJackson;
import org.junit.jupiter.api.Test;

class QyBaseEnumTest {


    @Test
    public void testGetOptionsEnum() {
        System.out.println(QyBaseEnum.getOptions(TestStatusEnum.class));
    }

    @Test
    public void testFromValueEnum() {
        TestStatusEnum testStatusEnum = QyBaseEnum.fromValue(TestStatusEnum.class, 1);
        System.out.println(testStatusEnum);
        System.out.println(QyJackson.toJsonString(testStatusEnum));
    }
}