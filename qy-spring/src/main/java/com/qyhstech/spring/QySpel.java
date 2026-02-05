package com.qyhstech.spring;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring EL表达式解析工具类
 */
@SuppressWarnings("unused")
public class QySpel {

    private static final Map<String, Expression> EXPRESSION_CACHE = new ConcurrentHashMap<>(64);

    /**
     * 根据表达式来生成参数列表
     *
     * @param elString Key的值
     * @param map      原方法的参数封装后的结果，用于生成表达式
     * @return
     */
    public static String parse(String elString, TreeMap<String, Object> map) {
        elString = String.format("#{%s}", elString);
        //创建表达式解析器
        ExpressionParser parser = new SpelExpressionParser();
        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        map.entrySet().forEach(entry ->
                context.setVariable(entry.getKey(), entry.getValue())
        );

        //解析表达式
        Expression expression = parser.parseExpression(elString, new TemplateParserContext());
        //使用Expression.getValue()获取表达式的值，这里传入了Evaluation上下文
        String value = expression.getValue(context, String.class);
        return value;
    }

    /**
     * 获取Expression对象
     *
     * @param expressionString Spring EL 表达式字符串 例如 #{param.id}
     * @return Expression
     */
    @Nullable
    public static Expression getExpression(@Nullable String expressionString) {

        if (StrUtil.isBlank(expressionString)) {
            return null;
        }

        if (EXPRESSION_CACHE.containsKey(expressionString)) {
            return EXPRESSION_CACHE.get(expressionString);
        }

        Expression expression = new SpelExpressionParser().parseExpression(expressionString);
        EXPRESSION_CACHE.put(expressionString, expression);
        return expression;
    }

    /**
     * 根据Spring EL表达式字符串从根对象中求值
     *
     * @param root             根对象
     * @param expressionString Spring EL表达式
     * @param clazz            值得类型
     * @param <T>              泛型
     * @return 值
     */
    @Nullable
    public static <T> T getExpressionValue(@Nullable Object root, @Nullable String expressionString, @NonNull Class<? extends T> clazz) {
        if (root == null) {
            return null;
        }
        Expression expression = getExpression(expressionString);
        if (expression == null) {
            return null;
        }

        return expression.getValue(root, clazz);
    }

    /**
     *
     * @param root
     * @param expressionString
     * @return
     * @param <T>
     */
    @Nullable
    public static <T> T getExpressionValue(@Nullable Object root, @Nullable String expressionString) {
        if (root == null) {
            return null;
        }
        Expression expression = getExpression(expressionString);
        if (expression == null) {
            return null;
        }

        //noinspection unchecked
        return (T) expression.getValue(root);
    }

    /**
     * 求值
     *
     * @param root              根对象
     * @param expressionStrings Spring EL表达式
     * @param <T>               泛型 这里的泛型要慎用,大多数情况下要使用Object接收避免出现转换异常
     * @return 结果集
     */
    public static <T> T[] getExpressionValue(@Nullable Object root, @Nullable String... expressionStrings) {
        if (root == null) {
            return null;
        }

        if (ArrayUtil.isEmpty(expressionStrings)) {
            return null;
        }

        Assert.notNull(expressionStrings, "Expressions cannot be null!");
        //noinspection ConstantConditions
        Object[] values = new Object[expressionStrings.length];
        for (int i = 0; i < expressionStrings.length; i++) {

            //noinspection unchecked
            values[i] = (T) getExpressionValue(root, expressionStrings[i]);
        }
        //noinspection unchecked
        return (T[]) values;
    }

    /**
     * 表达式条件求值
     * 如果为值为null则返回false,
     * 如果为布尔类型直接返回,
     * 如果为数字类型则判断是否大于0
     *
     * @param root             根对象
     * @param expressionString Spring EL表达式
     * @return 值
     */
    @Nullable
    public static boolean getConditionValue(@Nullable Object root, @Nullable String expressionString) {
        Object value = getExpressionValue(root, expressionString);
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue() > 0;
        }

        return true;
    }

    /**
     * 表达式条件求值
     *
     * @param root              根对象
     * @param expressionStrings Spring EL表达式数组
     * @return 值
     */
    @Nullable
    public static boolean getConditionValue(@Nullable Object root, @Nullable String... expressionStrings) {

        if (root == null) {
            return false;
        }

        if (ArrayUtil.isEmpty(expressionStrings)) {
            return false;
        }

        Assert.notNull(expressionStrings, "Expressions cannot be null!");
        //noinspection ConstantConditions
        for (String expressionString : expressionStrings) {
            if (!getConditionValue(root, expressionString)) {
                return false;
            }
        }

        return true;
    }
}
