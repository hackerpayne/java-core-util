package com.qyhstech.spring;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

class QySpelTest {

    @Test
    void getExpression() {
    }

    @Test
    void getExpressionValue() {
        final String[] argExpressions = new String[]{"[0].id", "[0].name"};
        Object[] args = new Object[]{"123", "456"};
        StringBuilder sb = new StringBuilder(64);
        Object[] argsForKey = QySpel.getExpressionValue(args, argExpressions);
        for (Object obj : argsForKey) {
            sb.append(obj.toString());
        }
        System.out.println(sb);
    }

    @Test
    void testGetExpressionValue() {
    }

    @Test
    void testGetExpressionValue1() {
    }

    @Test
    void getConditionValue() {
    }

    @Test
    void testGetConditionValue() {
    }

    @Data
    public static class OrderTest {
        private Long id;
        private Double money;
    }

    @Test
    void parse() {
        String elString = "#order.money";
        String elString2 = "#user";
        String elString3 = "#p0";

        TreeMap<String, Object> map = new TreeMap<>();
        OrderTest order = new OrderTest();
        order.setId(111L);
        order.setMoney(123D);
        map.put("order", order);
        map.put("user", "Hydra");

        String val = QySpel.parse(elString, map);
        String val2 = QySpel.parse(elString2, map);
        String val3 = QySpel.parse(elString3, map);

        System.out.println(val);
        System.out.println(val2);
        System.out.println(val3);
    }
}