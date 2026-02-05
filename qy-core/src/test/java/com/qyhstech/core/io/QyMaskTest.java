package com.qyhstech.core.io;

import org.junit.jupiter.api.Test;

class QyMaskTest {

    @Test
    void commonMask() {
        String text = "12316874345";
        QyMaskRule rule = new QyMaskRule();
        rule.setType(1);// 0隐藏，1显示
        rule.setScope(3);// 规则：开头:0 、中间:1 、末尾: -1 、全部: 2 、区间：3
        rule.setCount(4); // 位数
        rule.setStart(1); // 开始
        rule.setEnd(6); // 结束
        String s = QyMask.commonMask(text, rule); // 显示前6位
        System.out.println(s);

        rule = new QyMaskRule();
        rule.setType(1);// 0隐藏，1显示
        rule.setScope(0);// 规则：开头:0 、中间:1 、末尾: -1 、全部: 2 、区间：3
        rule.setCount(4); // 位数
        s = QyMask.commonMask(text, rule); // 显示开头4位
        System.out.println(s);

        rule = new QyMaskRule();
        rule.setType(0);// 0隐藏，1显示
        rule.setScope(-1);// 规则：开头:0 、中间:1 、末尾: -1 、全部: 2 、区间：3
        rule.setCount(4); // 位数
        s = QyMask.commonMask(text, rule); // 隐藏后4位
        System.out.println(s);

        rule = new QyMaskRule();
        rule.setType(0);// 0隐藏，1显示
        rule.setScope(3);// 规则：开头:0 、中间:1 、末尾: -1 、全部: 2 、区间：3
        rule.setStart(4);
        rule.setEnd(7);
        s = QyMask.commonMask(text, rule); // 隐藏中间4位
        System.out.println(s);

    }
}