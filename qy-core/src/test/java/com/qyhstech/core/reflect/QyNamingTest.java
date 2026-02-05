package com.qyhstech.core.reflect;

import org.junit.jupiter.api.Test;

class QyNamingTest {

    @Test
    void getFirstUpperName() {
        System.out.println(QyNaming.camelToUnderline("myBestService"));
    }

    @Test
    void getFirstLowerName() {
    }

    @Test
    void camelToUnderlineNew() {
        System.out.println(QyNaming.toCamelCase("my_Best_Service"));
    }

    @Test
    void camelToUnderline() {
        System.out.println(QyNaming.toUnderlineCase("myBestService"));
        System.out.println(QyNaming.toUnderlineCase("my_Best_Service"));
    }

    @Test
    void underlineToCamelNew() {
        System.out.println(QyNaming.toPascalCase("myBestService"));
    }

    @Test
    void underlineToCamel() {
    }

    @Test
    void toSlug() {
        String title = "How to Build Site:   First Rules";
        System.out.println(QyNaming.toSlug(title));
        System.out.println(QyNaming.toKebabCase("how to do this ? !"));

        // 输出: how-to-build-site-first-rules
    }

    @Test
    void toTitle() {
        System.out.println(QyNaming.toTitle("how to do this ? !"));
    }
}