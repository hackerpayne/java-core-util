package com.qyhstech.core.reflect;

import com.qyhstech.core.domain.ModelTest;
import org.junit.jupiter.api.Test;

import java.util.List;

class QyReflectTest {

    @Test
    void invokeGetter() {
    }

    @Test
    void invokeSetter() {
    }

    @Test
    void trimStringFields() {

        ModelTest modelTest = new ModelTest();
        modelTest.setId(1);
        modelTest.setName("name  ");

        modelTest = QyReflect.trimStringFields(modelTest);
        System.out.println(modelTest);
    }

    @Test
    void removeField() {
        ModelTest modelTest = new ModelTest();
        modelTest.setId(1);
        modelTest.setName("name  ");

        modelTest = QyReflect.removeField(modelTest, List.of("name"));
        System.out.println(modelTest);
    }
}