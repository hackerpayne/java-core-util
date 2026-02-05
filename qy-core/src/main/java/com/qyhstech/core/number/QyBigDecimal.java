package com.qyhstech.core.number;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * BigDecimal处理类
 */
public class QyBigDecimal {

    // 除法运算默认精度
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 保留2位长度显示
     *
     * @param num
     * @return
     */
    public static String toStr(BigDecimal num) {
        return toStr(num, 2, "");
    }

    /**
     * 四舍五入转为结果
     *
     * @param num   数字
     * @param scale 保留小数位数
     * @return
     */
    public static String toStr(BigDecimal num, Integer scale) {
        return toStr(num, scale, "");
    }

    /**
     * 保留长度显示
     *
     * @param num          要处理的数值
     * @param scale        长度
     * @param defaultValue 默认返回值
     * @return
     */
    public static String toStr(BigDecimal num, int scale, String defaultValue) {
        if (Objects.nonNull(num)) {
            return num.setScale(scale, RoundingMode.HALF_UP).toString();
        }
        return defaultValue;
    }

    /**
     * 四舍五入保留2位小数
     *
     * @param num
     * @return
     */
    public static String toStr(String num) {
        return toStr(num, 2);
    }

    /**
     * 直接从字符串转换过来
     *
     * @param num   字符串格式的数据
     * @param scale 小数点位数
     * @return
     */
    public static String toStr(String num, Integer scale) {
        return toStr(new BigDecimal(num), scale, "");
    }

    /**
     * 将Number类型转换为BigDecimal格式。
     *
     * @param number
     * @return
     */
    public static BigDecimal toBigDecimal(Number number) {
        if (number == null) {
            return BigDecimal.ZERO;
        }
        if (number instanceof BigDecimal num) {
            return num;
        }
        if (number instanceof Integer || number instanceof Long || number instanceof Short || number instanceof Byte) {
            return BigDecimal.valueOf(number.longValue());
        } else {
            // Float, Double, AtomicInteger 等
            return BigDecimal.valueOf(number.doubleValue());
        }
    }

    /**
     * 转换为BigDecimal，转不了，为Null，里面不能有带多行数字的存在，否则会出错。
     *
     * @param numStr
     * @return
     */
    public static BigDecimal toBigDecimalClean(String numStr) {
        if (StrUtil.isEmpty(numStr)) {
            return null;
        }

        // 先清理掉所有非数字,号和.的数据,再把,号清理掉
        numStr = numStr.replaceAll("([^0-9,.])", "").replace(",", "");

        // 匹配数据
        if (StrUtil.isNotBlank(numStr)) {
            numStr = ReUtil.get("(\\d[.0-9]*)", numStr, 1);
        }

        if (StrUtil.isEmpty(numStr)) {
            return null;
        }
        return NumberUtil.isNumber(numStr) ? new BigDecimal(numStr) : null;
    }

    /**
     * 将元转换为分，不要小数
     *
     * @param num
     * @return
     */
    public static String yuan2FenStr(BigDecimal num) {
        return yuan2FenStr(num, 0, "0");
    }

    /**
     * 元转换为分
     *
     * @param yuan
     * @return
     */
    public static Integer yuan2FenInt(BigDecimal yuan) {
        return yuan2FenInt(yuan, 0, 0);
    }

    /**
     * 分转元的Int格式
     *
     * @param yuan
     * @param scale
     * @param defaultValue
     * @return
     */
    public static Integer yuan2FenInt(BigDecimal yuan, int scale, int defaultValue) {
        BigDecimal result = yuan2Fen(yuan, scale, null);
        if (Objects.isNull(result)) {
            return defaultValue;
        }
        return result.intValue();
    }

    /**
     * 分转换为元的String格式
     *
     * @param yuan
     * @param scale
     * @param defaultValue
     * @return
     */
    public static String yuan2FenStr(BigDecimal yuan, int scale, String defaultValue) {
        BigDecimal result = yuan2Fen(yuan, scale, null);
        if (Objects.isNull(result)) {
            return defaultValue;
        }
        return result.toString();
    }

    /**
     * 将元转换为分，比如23.12元，转换为2312分
     *
     * @param yuan         金额
     * @param scale        保留小数
     * @param defaultValue 默认值
     * @return
     */
    public static BigDecimal yuan2Fen(BigDecimal yuan, int scale, BigDecimal defaultValue) {

        if (Objects.isNull(yuan)) {
            return defaultValue;
        }

        // 将元转换为分
        BigDecimal fen = yuan.movePointRight(2);
        // 设置小数位数为2位
        return fen.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * @param fenStr
     * @return
     */
    public static BigDecimal fen2Yuan(String fenStr) {
        return fen2Yuan(fenStr, 2, null);
    }

    /**
     * 将人民币分，转换为元的单位比如323分，转换为3.23元
     *
     * @param fenStr       分数字符串
     * @param scale        保留小数长度
     * @param defaultValue 默认值
     * @return
     */
    public static BigDecimal fen2Yuan(String fenStr, int scale, BigDecimal defaultValue) {
        if (StrUtil.isBlank(fenStr)) {
            return defaultValue;
        }

        // 将分字符串转换为BigDecimal
        BigDecimal fen = new BigDecimal(fenStr);
        // 将分转换为元
        return fen.divide(new BigDecimal(100), scale, RoundingMode.HALF_UP);
    }

    /**
     * 精确加法
     *
     * @param first  第一个数
     * @param second 第二个数
     * @return
     */
    public static double add(double first, double second) {
        BigDecimal b1 = BigDecimal.valueOf(first);
        BigDecimal b2 = BigDecimal.valueOf(second);
        return b1.add(b2).doubleValue();
    }

    /**
     * 连续相加的加法运算
     *
     * @param numbers 需要连续相加的数据
     * @return
     */
    public static BigDecimal add(BigDecimal... numbers) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal number : numbers) {
            if (Objects.isNull(number)) {
                continue;
            }
            sum = sum.add(number);
        }
        return sum;
    }

    /**
     * 精确加法
     *
     * @param first
     * @param second
     * @return
     */
    public static double add(String first, String second) {
        // 将字符串转换为BigDecimal类型
        BigDecimal b1 = new BigDecimal(first);
        BigDecimal b2 = new BigDecimal(second);
        // 返回两个BigDecimal类型的和
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确减法
     *
     * @param first
     * @param second
     * @return
     */
    public static double sub(double first, double second) {
        BigDecimal b1 = BigDecimal.valueOf(first);
        BigDecimal b2 = BigDecimal.valueOf(second);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确减法
     *
     * @param first
     * @param second
     * @return
     */
    public static double sub(String first, String second) {
        BigDecimal b1 = new BigDecimal(first);
        BigDecimal b2 = new BigDecimal(second);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确乘法
     *
     * @param first  第一个数
     * @param second 第二个数
     * @return
     */
    public static BigDecimal mul(BigDecimal first, BigDecimal second) {
        return Objects.nonNull(first) && Objects.nonNull(second) ? first.multiply(second) : BigDecimal.ZERO;
    }

    /**
     * 精确乘法
     *
     * @param first
     * @param second
     * @return
     */
    public static double mul(double first, double second) {
        BigDecimal b1 = BigDecimal.valueOf(first);
        BigDecimal b2 = BigDecimal.valueOf(second);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确乘法
     *
     * @param first
     * @param second
     * @return
     */
    public static double mul(String first, String second) {
        BigDecimal b1 = new BigDecimal(first);
        BigDecimal b2 = new BigDecimal(second);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 乘法占比。即第2个数，如果是0到1，乘法正好
     * 如果是>1，直接再除100
     *
     * @param first
     * @param rates 百分比
     * @return
     */
    public static BigDecimal mulRates(BigDecimal first, BigDecimal rates) {
        first = Objects.isNull(first) ? BigDecimal.ZERO : first;
        rates = Objects.isNull(rates) ? BigDecimal.ZERO : rates;

        // 如果大于1，需要除100
        if (isGreaterThanZero(rates) && !isSmallThanOne(rates)) {
            rates = div(rates, BigDecimal.valueOf(100));
        }
        return first.multiply(rates);
    }

    /**
     * 精确除法 使用默认精度
     *
     * @param first
     * @param second
     * @return
     * @throws IllegalAccessException
     */
    public static double div(double first, double second) throws IllegalAccessException {
        return div(first, second, DEF_DIV_SCALE);
    }

    /**
     * 精确除法 使用默认精度
     *
     * @param first
     * @param second
     * @return
     * @throws IllegalAccessException
     */
    public static double div(String first, String second) throws IllegalAccessException {
        return div(first, second, DEF_DIV_SCALE);
    }

    /**
     * 除法，四舍五入。为0直接返回0，需要自行处理为0的情况
     *
     * @param first
     * @param second
     * @return
     */
    public static BigDecimal div(BigDecimal first, BigDecimal second) {
        return div(first, second, 2);
    }

    /**
     * 除法，除不尽抛异常，必须设置精度。
     * 这里是保留2位，四舍5入
     *
     * @param first
     * @param second
     * @return
     */
    public static BigDecimal div(BigDecimal first, BigDecimal second, int scale) {
        return first.divide(second, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法,保留2位小数
     *
     * @param childNum
     * @param total
     * @return
     */
    public static BigDecimal divide(Integer childNum, Integer total) {
        BigDecimal b1 = new BigDecimal(childNum);
        BigDecimal b2 = new BigDecimal(total);
        return b1.divide(b2, 2, RoundingMode.HALF_UP);
    }

    /**
     * 精确除法
     *
     * @param scale 精度
     */
    public static double div(double first, double second, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(first);
        BigDecimal b2 = BigDecimal.valueOf(second);
        // return b1.divide(b2, scale).doubleValue();
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 精确除法
     *
     * @param scale 精度
     */
    public static double div(String first, String second, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(first);
        BigDecimal b2 = new BigDecimal(second);
        // return b1.divide(b2, scale).doubleValue();
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param scale 小数点后保留几位
     */
    public static double round(double v, int scale) throws IllegalAccessException {
        return div(v, 1, scale);
    }

    /**
     * 四舍五入
     *
     * @param scale 小数点后保留几位
     */
    public static double round(String v, int scale) throws IllegalAccessException {
        return div(v, "1", scale);
    }

    /**
     * 四舍五入留2位小数
     *
     * @param data
     * @return
     */
    public static BigDecimal round(BigDecimal data) {
        return data.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 判断2个Decimal是否一致，只能用CompareTo不能用equal
     * 20.00和20.0000，用equal会报错，compareTo不会
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean equal(BigDecimal first, BigDecimal second) {
        if (first == null || second == null) {
            return false;
        }
        return first.compareTo(second) == 0;
    }

    /**
     * 判断A是否大于B
     *
     * @param b1
     * @param b2
     * @return
     */
    public static boolean isGreater(BigDecimal b1, BigDecimal b2) {
        if (b1 == null || b2 == null) {
            return false;
        }
        return b1.compareTo(b2) > 0;
    }

    /**
     * 判断是否比0大
     *
     * @param big
     * @return
     */
    public static boolean isGreaterThanZero(BigDecimal big) {
        if (big == null) {
            return false;
        }
        return big.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 判断A是否大于B
     *
     * @param b1
     * @param b2
     * @return
     */
    public static boolean isSmall(BigDecimal b1, BigDecimal b2) {
        if (b1 == null || b2 == null) {
            return false;
        }
        return b1.compareTo(b2) < 0;
    }

    /**
     * 判断数据是否小于1
     *
     * @param bigDecimal
     * @return
     */
    public static boolean isSmallThanOne(BigDecimal bigDecimal) {
        if (Objects.isNull(bigDecimal)) {
            return true;
        }
        return bigDecimal.compareTo(BigDecimal.valueOf(1)) < 0;
    }

    /**
     * 不为空且不为0
     *
     * @param big
     * @return
     */
    public static boolean isNotZero(BigDecimal big) {
        return big != null && big.compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * 判断为空或者为0
     *
     * @param big
     * @return
     */
    public static boolean isNullOrZero(BigDecimal big) {
        return big == null || big.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 求最小值
     *
     * @param dataList
     * @param function
     * @param <T>
     * @return
     */
    public static <T> BigDecimal min(List<T> dataList, Function<T, BigDecimal> function) {
        return min(dataList, function, BigDecimal.ZERO);
    }

    /**
     * 取金额最小值
     *
     * @param dataList 数据列表
     * @param function 要取值的字段
     * @param <T>
     * @return
     */
    public static <T> BigDecimal min(List<T> dataList, Function<T, BigDecimal> function, BigDecimal defaultValue) {
        if (CollUtil.isEmpty(dataList)) {
            return defaultValue;
        }
        return dataList.stream()
                .map(function)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(null);
    }

    /**
     * 求最大值
     *
     * @param dataList
     * @param function
     * @param <T>
     * @return
     */
    public static <T> BigDecimal max(List<T> dataList, Function<T, BigDecimal> function) {
        return max(dataList, function, BigDecimal.ZERO);
    }

    /**
     * 取列表中金额最大值
     *
     * @param dataList 数据列表
     * @param function 要取值的字段
     * @param <T>
     * @return
     */
    public static <T> BigDecimal max(List<T> dataList, Function<T, BigDecimal> function, BigDecimal defaultValue) {
        if (CollUtil.isEmpty(dataList)) {
            return defaultValue;
        }
        return dataList.stream()
                .map(function)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(null);
    }

    /**
     * 求和
     *
     * @param dataList
     * @param function
     * @param <T>
     * @return
     */
    public static <T> BigDecimal sum(List<T> dataList, Function<T, BigDecimal> function) {
        return sum(dataList, function, BigDecimal.ZERO);
    }

    /**
     * 对List中指定项使用BigDecimal求和
     *
     * @param dataList 要处理的数据列表
     * @param function 提取的字段列表
     * @param <T>
     * @return
     */
    public static <T> BigDecimal sum(List<T> dataList, Function<T, BigDecimal> function, BigDecimal defaultValue) {
        if (CollUtil.isEmpty(dataList)) {
            return defaultValue;
        }
        return dataList.stream()
                .map(function)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
