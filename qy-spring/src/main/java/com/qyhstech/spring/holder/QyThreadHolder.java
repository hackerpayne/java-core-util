package com.qyhstech.spring.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.qyhstech.core.collection.QyMap;

import java.util.Map;

/**
 * 通过HashMap来动态存储数据到线程里面供使用
 */
public class QyThreadHolder {

    private static final TransmittableThreadLocal<Map<String, Object>> CONTEXT =
            new TransmittableThreadLocal<>() {
                @Override
                protected Map<String, Object> initialValue() {
                    return QyMap.empty();
                }
            };

    /**
     * 放入数据
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return CONTEXT.get().get(key);
    }

    /**
     * 删除数据
     *
     * @param key
     */
    public static void remove(String key) {
        CONTEXT.get().remove(key);
    }

    /**
     * 清空共享器
     */
    public static void clear() {
        CONTEXT.remove();
    }

}
