package com.qyhstech.core.json;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 简化操作，避免每次生成各种TypeReference
 */
public class QyTypeRefs {

    /**
     * 生成List TypeReference
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> TypeReference<List<T>> listOf(Class<T> clazz) {
        return new TypeReference<List<T>>() {
        };
    }

    /**
     * 转换为Bytes
     *
     * @return
     */
    public static TypeReference<byte[]> ofBytes() {
        return new TypeReference<byte[]>() {
        };
    }

    /**
     * 生成Set TypeReference
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> TypeReference<Set<T>> setOf(Class<T> clazz) {
        return new TypeReference<Set<T>>() {
        };
    }

    /**
     * 返回Map
     *
     * @return
     */
    public static TypeReference<Map<String, Object>> ofMap() {
        return new TypeReference<Map<String, Object>>() {
        };
    }

    /**
     * 生成Map TypeReference
     *
     * @param k
     * @param v
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> TypeReference<Map<K, V>> mapOf(Class<K> k, Class<V> v) {
        return new TypeReference<Map<K, V>>() {
        };
    }
}
