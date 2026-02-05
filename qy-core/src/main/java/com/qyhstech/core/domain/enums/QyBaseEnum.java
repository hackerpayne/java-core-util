package com.qyhstech.core.domain.enums;

import com.qyhstech.core.domain.base.QyModelNameValue;

import java.util.Arrays;
import java.util.List;

/**
 * 参考：https://blog.csdn.net/u012102536/article/details/123711166
 *
 * @param <T>
 */
public interface QyBaseEnum<T> {

    T getValue();       // 数据库存的值

    String getName();   // 显示用的名字

    /**
     * 根据值获取枚举实例
     *
     * @param value
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    default <E extends Enum<E> & QyBaseEnum<T>> E fromValue(T value) {
        Class<E> enumClass = (Class<E>) this.getClass();
        for (E e : enumClass.getEnumConstants()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 静态方法：根据值获取枚举实例
     *
     * @param enumClass
     * @param value
     * @param <E>
     * @param <T>
     * @return
     */
    static <E extends Enum<E> & QyBaseEnum<T>, T> E fromValue(Class<E> enumClass, T value) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 获取所有选项
     *
     * @param enumClass
     * @param <E>
     * @param <T>
     * @return
     */
    static <E extends Enum<E> & QyBaseEnum<T>, T> List<QyModelNameValue> getOptions(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(e -> new QyModelNameValue(e.getName(), e.getValue()))
                .toList();
    }
}
