package com.qyhstech.core;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.domain.dto.ModelArticle;
import com.qyhstech.core.domain.enums.QyErrorCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class QyAssertTest {

    @Test
    void throwIfValidate() {
        ModelArticle article = new ModelArticle();
        article.setTitle("test");

        // 测试throwIfValidate方法
        article.setTitle("testtest");
        QyAssert.throwIfValidate(article, u -> StrUtil.isNotEmpty(u.getTitle()), () -> new IllegalArgumentException("参数异常"));
    }

    @Test
    void testThrowIfValidate() {
    }

    @Test
    void throwIfNotEmpty() {
        // 测试集合不为空时抛出异常
        QyAssert.throwIfNotEmpty(List.of("1"), "列表不能为空");
    }

    @Test
    void testThrowIfNotEmpty() {
        // 测试字符串不为空时抛出异常
        QyAssert.throwIfNotEmpty("abc", "列表不能为空");
    }

    @Test
    void testThrowIfNotEmpty1() {
    }

    @Test
    void testThrowIfNotEmpty2() {
    }

    @Test
    void testThrowIfNotEmpty3() {
    }

    @Test
    void testThrowIfNotEmpty4() {
    }

    @Test
    void testThrowIfNotEmpty5() {
    }

    @Test
    void testThrowIfNotEmpty6() {
    }

    @Test
    void throwIfEmpty() {
        // 测试字符串为空时抛出异常
        QyAssert.throwIfEmpty("", "列表为空");
    }

    @Test
    void testThrowIfEmpty() {
        // 测试集合为空时抛出异常
        QyAssert.throwIfEmpty(new ArrayList<>(), "列表为空");
    }

    @Test
    void testThrowIfEmpty1() {
        // 测试使用自定义异常提供器的空集合检查
        QyAssert.throwIfEmpty(new ArrayList<>(), () -> new IllegalArgumentException("参数异常，提示信息"));
    }

    @Test
    void testThrowIfEmpty2() {
    }

    @Test
    void testThrowIfEmpty3() {
    }

    @Test
    void testThrowIfEmpty4() {
    }

    @Test
    void testThrowIfEmpty5() {
    }

    @Test
    void testThrowIfEmpty6() {
    }

    @Test
    void throwIfTrue() {
        // 测试条件为真时抛出异常
        QyAssert.throwIfTrue(Boolean.TRUE, "不能为True");
    }

    @Test
    void testThrowIfTrue() {
    }

    @Test
    void testThrowIfTrue1() {
    }

    @Test
    void throwIfNull() {
        // 测试对象为null时抛出异常
        QyAssert.throwIfNull(null, "数据不能为null");
    }

    @Test
    void testThrowIfNull() {
    }

    @Test
    void testThrowIfNull1() {
    }

    @Test
    void throwIfEquals() {
    }

    @Test
    void testThrowIfEquals() {
    }

    @Test
    void throwIfNotEquals() {
    }

    @Test
    void testThrowIfNotEquals() {
    }

    @Test
    void testThrowIfValidate1() {
    }

    @Test
    void throwIfFalse() {
        QyAssert.throwIfFalse(Boolean.TRUE, "数据不能为False");
        QyAssert.throwIfFalse(Boolean.FALSE, "数据不能为False");
    }

    @Test
    void testThrowIfFalse() {
    }

    @Test
    void testThrowIfFalse1() {
    }

    @Test
    void testThrowIfFalse2() {
    }
}