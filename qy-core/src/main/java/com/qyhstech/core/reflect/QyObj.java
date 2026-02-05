package com.qyhstech.core.reflect;

import org.springframework.core.GenericTypeResolver;

import java.util.List;
import java.util.Objects;

public class QyObj {

    /**
     * 获取数据的类型
     *
     * @param obj
     * @return
     */
    public static String getType(Object obj) {
        return Objects.nonNull(obj) ? obj.getClass().getSimpleName() : null;
    }

    /**
     * 解析当前类所使用的泛型Class
     *
     * @param obj
     * @param interfaceType
     * @param <T>
     * @return
     */
    public static <T> Class<T> getInterfaceGenericType(Object obj, Class<?> interfaceType) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (interfaceType == null) {
            throw new IllegalArgumentException("Interface type cannot be null");
        }

        @SuppressWarnings("unchecked")
        Class<T> genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(obj.getClass(), interfaceType);
        if (genericType == null) {
            throw new IllegalArgumentException(
                    String.format("Cannot resolve generic type for interface %s on class %s",
                            interfaceType.getSimpleName(),
                            obj.getClass().getSimpleName())
            );
        }

        return genericType;
    }

    /**
     * 判断指定的Object是否是指定的泛型类型
     *
     * @param data
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> boolean isType(Object data, Class<T> clz) {
        if (Objects.isNull(data)) {
            return false;
        }
        return clz.isInstance(data);
    }


    /**
     * 判断指定的Object是否是指定的泛型类型
     *
     * @param data
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> boolean isTypeList(Object data, Class<T> clz) {
        if (!(data instanceof List<?>)) {
            return false;
        }

        List<?> list = (List<?>) data;
        if (list.isEmpty()) {
            return false; // 或者根据业务需求返回 true
        }

        Object firstElement = list.getFirst();
        return clz.isInstance(firstElement);
    }

}
