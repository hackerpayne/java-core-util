package com.qyhstech.core.domain.enums;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数学计算
 */
public enum QyOp {

    /**
     * 大于
     */
    GREATER(">") {
        @Override
        public boolean apply(BigDecimal x, BigDecimal y) {
            return x.compareTo(y) > 0;
        }
    },

    /**
     * 大于等于
     */
    GREATER_OR_EQUAL(">=") {
        @Override
        public boolean apply(BigDecimal x, BigDecimal y) {
            return x.compareTo(y) >= 0;
        }
    },

    /**
     * 小于
     */
    LESS("<") {
        @Override
        public boolean apply(BigDecimal x, BigDecimal y) {
            return x.compareTo(y) < 0;
        }
    },

    /**
     * 小于等于
     */
    LESS_OR_EQUAL("<=") {
        @Override
        public boolean apply(BigDecimal x, BigDecimal y) {
            return x.compareTo(y) <= 0;
        }
    },

    /**
     * 等于
     */
    EQUAL("=") {
        @Override
        public boolean apply(BigDecimal x, BigDecimal y) {
            return Objects.equals(x, y);
        }
    },

    /**
     * 不等于
     */
    NOT_EQUAL("!=") {
        @Override
        public boolean apply(BigDecimal x, BigDecimal y) {
            return !Objects.equals(x, y);
        }
    };

    /**
     * 运算符
     */
    private final String symbol;

    QyOp(String symbol) {
        this.symbol = symbol;
    }

    /**
     * 计算方式
     *
     * @param x
     * @param y
     * @return
     */
    public abstract boolean apply(BigDecimal x, BigDecimal y);

    private static final Map<String, QyOp> SYMBOL_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(op -> op.symbol, op -> op));

    /**
     * 根据操作符字符串获取对应的枚举实例
     *
     * @param symbol
     * @return
     */
    public static QyOp fromStr(String symbol) {
        return SYMBOL_MAP.get(symbol);
    }

//    /**
//     * 根据操作符字符串获取对应的枚举实例
//     * @param symbol
//     * @return
//     */
//    public static QyOp fromString(String symbol) {
//        for (QyOp op : values()) {
//            if (op.symbol.equals(symbol)) {
//                return op;
//            }
//        }
//        return null; // 或者抛出异常
//    }

}
