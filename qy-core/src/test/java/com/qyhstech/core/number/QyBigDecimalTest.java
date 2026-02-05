package com.qyhstech.core.number;

import com.qyhstech.core.collection.QyList;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class QyBigDecimalTest {




    @Test
    void toStr() {
    }

    @Test
    void testToStr() {
    }

    @Test
    void toMoneyStr() {
    }

    @Test
    void yuan2Fen() {
        BigDecimal bigDecimal = new BigDecimal("23.01");
        String fenStr = QyBigDecimal.yuan2FenStr(bigDecimal);
        System.out.println(fenStr);
    }

    @Test
    void yuan2FenInt() {
        BigDecimal bigDecimal = new BigDecimal("23.01");
        Integer fenStr = QyBigDecimal.yuan2FenInt(bigDecimal);
        System.out.println(fenStr);
    }

    @Test
    void testYuan2Fen() {
        BigDecimal bigDecimal = new BigDecimal("23.01232323");
        String fenStr = QyBigDecimal.yuan2FenStr(bigDecimal, 2, "");
        System.out.println(fenStr);
    }

    @Test
    void fen2Yuan() {
        String fenStr = "232.23";
        BigDecimal bigDecimal = QyBigDecimal.fen2Yuan(fenStr);
        System.out.println(bigDecimal);
    }

    @Test
    void testFen2Yuan() {
        String fenStr = "232.23";
        BigDecimal bigDecimal = QyBigDecimal.fen2Yuan(fenStr, 4, BigDecimal.ZERO);
        System.out.println(bigDecimal);
    }

    @Test
    void add() {
    }

    @Test
    void testAdd() {
    }

    @Test
    void testAdd1() {
    }

    @Test
    void sub() {
    }

    @Test
    void testSub() {
    }

    @Test
    void mul() {
    }

    @Test
    void testMul() {
    }

    @Test
    void mulRates() {
    }

    @Test
    void div() {
    }

    @Test
    void testDiv() {
    }

    @Test
    void testDiv1() {
    }

    @Test
    void testDiv2() {
    }

    @Test
    void divide() {
    }

    @Test
    void testDiv3() {
    }

    @Test
    void testDiv4() {
    }

    @Test
    void round() {
    }

    @Test
    void testRound() {
    }

    @Test
    void testRound1() {
    }

    @Test
    void equalTo() {
    }

    @Test
    void testToStr1() {
    }

    @Test
    void testToStr2() {
    }

    @Test
    void equal() {
    }

    @Test
    void isGreaterThanZero() {
    }

    @Test
    void isSmallThanOne() {
    }

    @Test
    void toBigDecimalClean() {
    }

    @Test
    void mainTest() {
        List<String> listStr = QyList.empty();
        listStr.add("s2323.200元");
        listStr.add("200元");
        listStr.add("200");
        listStr.add("1");
        listStr.add("1元");
        listStr.add("1.00");
        listStr.add("11.0sdsdfdsadf");
        listStr.add("1.012132元");
        listStr.add("2323,232,2323.00元");
        listStr.add("2323,2323.00元");
        listStr.add("223,323,232,2323.00元");

        listStr.forEach(amount -> System.out.println(QyBigDecimal.toBigDecimalClean(amount)));
    }
}