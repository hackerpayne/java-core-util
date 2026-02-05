package com.qyhstech.core.reflect;

import org.junit.jupiter.api.Test;

public class NamingUtilTest {


    @Test
    public void test() {
        System.out.println(QyNaming.camelToUnderline("a.userName"));
        System.out.println(QyNaming.underlineToCamel("a_user_Name"));
    }

    @Test
    public void getFirstUpperName() {
    }

    @Test
    public void getFirstLowerName() {
    }

    @Test
    public void camelToUnderlineNew() {
    }

    @Test
    public void camelToUnderline() {
    }

    @Test
    public void underlineToCamelNew() {
        System.out.println(QyNaming.camelToUnderlineNew("a.userName"));
    }

    @Test
    public void underlineToCamel() {
        System.out.println(QyNaming.underlineToCamelNew("a_user_Name_or_Invalid"));
    }
}