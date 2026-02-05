package com.qyhstech.core.collection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * 数据处理类
 */
public class QyArray extends ArrayUtil {

    /**
     * 将字符串列表转换为数组
     *
     * @param list
     * @return
     */
    public static String[] toArray(List<String> list) {
        if (CollUtil.isEmpty(list)) {
            return new String[0];
        }

        return list.toArray(new String[0]);
    }

    /**
     * 将 List<T> 转换为数组
     *
     * @param list  源 List
     * @param clazz 元素类型的 Class
     * @param <T>   元素类型
     * @return 转换后的数组
     */
    public static <T> T[] toArray(List<T> list, Class<T> clazz) {
        if (list == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz, list.size());
        return list.toArray(array);
    }

}
