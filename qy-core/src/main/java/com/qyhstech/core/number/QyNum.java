package com.qyhstech.core.number;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 数字工具类
 */
public class QyNum extends NumberUtil {

    private QyNum() {
    }

    /**
     * 不为空，且大于0为有效数字
     *
     * @param num
     * @return
     */
    public static boolean isValidNum(Integer num) {
        return num != null && num > 0;
    }

    public static boolean isValidNum(Long num) {
        return num != null && num > 0;
    }

    /**
     * 保留小数位，采用四舍五入
     *
     * @param number 被保留小数的数字
     * @param digit  保留的小数位数
     * @return 保留小数后的字符串
     */
    public static String roundStr(double number, int digit) {
        return String.format("%." + digit + 'f', number);
    }

    /**
     * A除B，保留2位小数
     *
     * @param a
     * @param b
     * @return
     */
    public static String getRate(double a, double b) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (b != 0) {
            return df.format(a / b);
        }
        return null;
    }

    /**
     * 获取百分比结果
     *
     * @param a
     * @param b
     * @return
     */
    public static String getPercent(double a, double b) {
        NumberFormat nt = NumberFormat.getPercentInstance();//获取格式化对象
        nt.setMinimumFractionDigits(2);// 设置百分数精确度2即保留两位小数
        return nt.format(a / b); // 最后格式化并输出
    }

    /**
     * Double转换为指定的精度
     * 比如：formatDouble(116.46604901357878,5) 结果会只保留5位小数
     *
     * @param data
     * @param length
     * @return
     */
    public static double formatDouble(double data, Integer length) {
        NumberFormat ddf1 = NumberFormat.getNumberInstance();
        ddf1.setMaximumFractionDigits(length);
        String convResult = ddf1.format(data);
        return parseDouble(convResult);
    }

    /**
     * @param o1
     * @param o2
     * @return
     */
    public static int compareLong(long o1, long o2) {
        if (o1 < o2) {
            return -1;
        } else if (o1 == o2) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 获取small在total中的占比
     *
     * @param small
     * @param total
     * @return
     */
    public String getPercent(Integer small, Integer total) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format((float) small / (float) total * 100);
    }

    /**
     * 转换为3位一逗号形式的数字表示
     * 232,232.00
     *
     * @param number
     * @return
     */
    public static String formatNumber(String number) {
        String strArr[] = number.split("\\.");
        //将整数部分分离出来
        StringBuffer sb = new StringBuffer(strArr[0]);
        //小于等于三位，不需要该操作，返回原数字
        if (sb.length() <= 3) {
            return number;
        }
        //大于三位
        int last = sb.length();
        //从后往前，每三位前插入一个逗号
        for (int i = last - 3; i > 0; i -= 3) {
            sb.insert(i, ",");
        }
        StringBuffer doubleStr = new StringBuffer(".");
        try {
            //如果有小数部分，那就小数点加上小数部分
            doubleStr.append(strArr[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            //进入这里表示没有小数部分，那就将doubleStr置为空字符
            doubleStr.deleteCharAt(doubleStr.length() - 1);
        }
        //整数和doubleStr的结果拼接
        sb.append(doubleStr);
        return sb.toString();
    }

    /**
     * 对列表中的指定字段进行求和
     *
     * @param dataList   数据列表
     * @param function   提取字段
     * @param resultType 返回的结果类型
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R extends Number> R sum(List<T> dataList, Function<T, R> function, Class<R> resultType) {
        if (dataList == null || dataList.isEmpty()) {
            return QyNum.getZeroValue(resultType);
        }

        // 对基本数值类型做特殊处理
        if (Integer.class.equals(resultType)) {
            Integer result = dataList.stream()
                    .map(function)
                    .filter(Objects::nonNull)
                    .map(Number::intValue)
                    .reduce(0, Integer::sum);
            return resultType.cast(result);
        } else if (Long.class.equals(resultType)) {
            Long result = dataList.stream()
                    .map(function)
                    .filter(Objects::nonNull)
                    .map(Number::longValue)
                    .reduce(0L, Long::sum);
            return resultType.cast(result);
        } else if (Double.class.equals(resultType)) {
            Double result = dataList.stream()
                    .map(function)
                    .filter(Objects::nonNull)
                    .map(Number::doubleValue)
                    .reduce(0.0, Double::sum);
            return resultType.cast(result);
        } else if (Float.class.equals(resultType)) {
            Float result = dataList.stream()
                    .map(function)
                    .filter(Objects::nonNull)
                    .map(Number::floatValue)
                    .reduce(0.0f, Float::sum);
            return resultType.cast(result);
        } else if (BigDecimal.class.equals(resultType)) {
            BigDecimal result = dataList.stream()
                    .map(function)
                    .filter(Objects::nonNull)
                    .map(n -> n instanceof BigDecimal ? (BigDecimal) n : new BigDecimal(n.toString()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return resultType.cast(result);
        } else if (BigInteger.class.equals(resultType)) {
            BigInteger result = dataList.stream()
                    .map(function)
                    .filter(Objects::nonNull)
                    .map(n -> n instanceof BigInteger ? (BigInteger) n : new BigInteger(n.toString()))
                    .reduce(BigInteger.ZERO, BigInteger::add);
            return resultType.cast(result);
        }

        // 默认情况下，处理为BigDecimal
        BigDecimal result = dataList.stream()
                .map(function)
                .filter(Objects::nonNull)
                .map(n -> n instanceof BigDecimal ? (BigDecimal) n : new BigDecimal(n.toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return resultType.cast(result);
    }

    /**
     * 返回指定类型的0的数据格式
     *
     * @param resultType
     * @param <R>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <R extends Number> R getZeroValue(Class<R> resultType) {
        if (Integer.class.equals(resultType)) {
            return (R) Integer.valueOf(0);
        } else if (Long.class.equals(resultType)) {
            return (R) Long.valueOf(0L);
        } else if (Double.class.equals(resultType)) {
            return (R) Double.valueOf(0.0);
        } else if (Float.class.equals(resultType)) {
            return (R) Float.valueOf(0.0f);
        } else if (BigDecimal.class.equals(resultType)) {
            return (R) BigDecimal.ZERO;
        } else if (BigInteger.class.equals(resultType)) {
            return (R) BigInteger.ZERO;
        }
        return (R) Integer.valueOf(0); // 默认返回Integer的0
    }
}
