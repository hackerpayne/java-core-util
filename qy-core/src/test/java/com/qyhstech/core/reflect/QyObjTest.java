package com.qyhstech.core.reflect;

import com.qyhstech.core.domain.ModelTest;
import org.junit.jupiter.api.Test;

class QyObjTest {

    @Test
    void getType() {
        ModelTest modelTest = new ModelTest();
        System.out.println(QyObj.getType(modelTest));
    }

    @Test
    void getInterfaceGenericType() {
    }

    @Test
    void isType() {
    }

    @Test
    void isTypeList() {
    }
}