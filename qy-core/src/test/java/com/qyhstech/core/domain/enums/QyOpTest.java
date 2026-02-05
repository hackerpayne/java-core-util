package com.qyhstech.core.domain.enums;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

class QyOpTest {

    @Test
    void apply() {
        boolean result = QyOp.GREATER.apply(BigDecimal.ONE, BigDecimal.ZERO);
        System.out.println(result);
    }

    @Test
    void fromStr() {
        boolean result = Objects.requireNonNull(QyOp.fromStr(">")).apply(BigDecimal.ONE, BigDecimal.ZERO);
        System.out.println(result);
    }
}