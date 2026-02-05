package com.qyhstech.core.util;

import cn.hutool.core.util.ClassUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * {@link ClassUtil} 单元测试
 *
 * @author Looly
 */
public class ClassUtilTest {

    @Test
    public void getClassNameTest() {

        String className = ClassUtil.getClassName(ClassUtil.class, false);
        Assertions.assertEquals("com.xiaoleilu.hutool.token.ClassUtil", className);

        String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
        Assertions.assertEquals("ClassUtil", simpleClassName);
    }

    @Test
    public void getPublicMethod() {
        Method superPublicMethod = ClassUtil.getPublicMethod(TestSubClass.class, "publicMethod");
        Assertions.assertNotNull(superPublicMethod);
        Method superPrivateMethod = ClassUtil.getPublicMethod(TestSubClass.class, "privateMethod");
        Assertions.assertNull(superPrivateMethod);

        Method publicMethod = ClassUtil.getPublicMethod(TestSubClass.class, "publicSubMethod");
        Assertions.assertNotNull(publicMethod);
        Method privateMethod = ClassUtil.getPublicMethod(TestSubClass.class, "privateSubMethod");
        Assertions.assertNull(privateMethod);
    }

    @Test
    public void getDeclaredMethod() throws Exception {
        Method noMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "noMethod");
        Assertions.assertNull(noMethod);

        Method privateMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "privateMethod");
        Assertions.assertNotNull(privateMethod);
        Method publicMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "publicMethod");
        Assertions.assertNotNull(publicMethod);

        Method publicSubMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "publicSubMethod");
        Assertions.assertNotNull(publicSubMethod);
        Method privateSubMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "privateSubMethod");
        Assertions.assertNotNull(privateSubMethod);

    }

    @Test
    public void getDeclaredField() {
        Field noField = ClassUtil.getDeclaredField(TestSubClass.class, "noField");
        Assertions.assertNull(noField);

        // 能够获取到父类字段
        Field privateField = ClassUtil.getDeclaredField(TestSubClass.class, "privateField");
        Assertions.assertNotNull(privateField);

        // 获取不到父类字段
        Field field = ClassUtil.getDeclaredField(TestSubClass.class, "field");
        Assertions.assertNull(field);

        Field subField = ClassUtil.getDeclaredField(TestSubClass.class, "subField");
        Assertions.assertNotNull(subField);
    }

    @SuppressWarnings("unused")
    class TestClass {
        protected String field;
        private String privateField;

        private void privateMethod() {
        }

        public void publicMethod() {
        }
    }

    @SuppressWarnings("unused")
    class TestSubClass extends TestClass {
        private String subField;

        private void privateSubMethod() {
        }

        public void publicSubMethod() {
        }

    }
}
