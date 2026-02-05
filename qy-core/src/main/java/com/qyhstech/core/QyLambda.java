package com.qyhstech.core;

import com.qyhstech.core.function.QyFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Optional增强
 */
public class QyLambda {

    /**
     * 解析对象是否有Null存在，缺点是根据异常来处理的，性能会有损耗
     * 使用时：
     * 1、复杂用法
     * Optional<Integer> optionalActive = resolve(()->properties.getJedis().getPool().getMaxActive());
     * if (optionalActive.isPresent()) {
     * jedisPoolConfig.setMaxTotal(optionalActive.get());// 最大连接数, 默认8个，控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
     * }
     * 2、简单用法
     * resolve(() -> obj.getNested().getInner().getFoo());
     * .ifPresent(System.out::println);
     * 来源：https://stackoverflow.com/questions/10391406/java-avoid-checking-for-null-in-nested-classes-deep-null-checking
     * <p>
     * 原生方案：
     * 判断：product.getLatestVersion().getProductData().getTradeItem().getInformationProviderOfTradeItem().getGln(); 能否使用时：
     * Optional.ofNullable(product).map(
     * Product::getLatestVersion
     * ).map(
     * ProductVersion::getProductData
     * ).map(
     * ProductData::getTradeItem
     * ).map(
     * TradeItemType::getInformationProviderOfTradeItem
     * ).map(
     * PartyInRoleType::getGln
     * ).orElse(null);
     *
     * @param resolver
     * @param <T>
     * @return
     */
    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    /**
     * 实现自己的Lambda写法解析字段名，得到类似MybatisPlus的效果
     * 使用时：QyLambda.getFieldName(ModelPage::getCurPage);
     *
     * @param fn
     * @param <T>
     * @return
     */
    public static <T> String getFieldName(QyFunction<T, ?> fn) {
        try {
            // 通过反射调用writeReplace获取SerializedLambda
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(fn);

            // 从方法名推导字段名（去掉 get/is 前缀并首字母小写）
            String methodName = lambda.getImplMethodName();
            if (methodName.startsWith("get")) {
                methodName = methodName.substring(3);
            } else if (methodName.startsWith("is")) {
                methodName = methodName.substring(2);
            }
            return Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        } catch (Exception e) {
            throw new RuntimeException("无法解析字段名", e);
        }
    }

    /**
     * 根据Lambda表达式，获取对象的字段值
     *
     * @param obj
     * @param fn
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R getFieldValue(T obj, QyFunction<T, R> fn) {
        if (obj == null) {
            throw new IllegalArgumentException("对象不能为空");
        }
        try {
            String fieldName = getFieldName(fn);
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (R) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("无法获取字段值", e);
        }
    }

}
