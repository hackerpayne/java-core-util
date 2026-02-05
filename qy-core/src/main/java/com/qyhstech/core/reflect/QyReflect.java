package com.qyhstech.core.reflect;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 */
@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class QyReflect extends ReflectUtil {

    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    /**
     * 使用最简的方法获取字段类型的值。
     * Hutool的过于复杂了
     *
     * @param object
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     */
    private Object getFieldSimple(Object object, String fieldName) throws IllegalAccessException {
        if (object == null) {
            return null;
        }
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field.get(object);
            }
        }
        return null;
    }

    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     *
     * @param obj
     * @param propertyName
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        for (String name : StrUtil.split(propertyName, ".")) {
            String getterMethodName = GETTER_PREFIX + StrUtil.upperFirst(name);
            object = invoke(object, getterMethodName);
        }
        return (E) object;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     *
     * @param obj
     * @param propertyName
     * @param value
     * @param <E>
     */
    public static <E> void invokeSetter(Object obj, String propertyName, E value) {
        Object object = obj;
        String[] names = StrUtil.splitToArray(propertyName, ".");
        for (int i = 0; i < names.length; i++) {
            if (i < names.length - 1) {
                String getterMethodName = GETTER_PREFIX + StrUtil.upperFirst(names[i]);
                object = invoke(object, getterMethodName);
            } else {
                String setterMethodName = SETTER_PREFIX + StrUtil.upperFirst(names[i]);
                Method method = getMethodByName(object.getClass(), setterMethodName);
                invoke(object, method, value);
            }
        }
    }

    /**
     * 通过反射将对象中的string类型字段做trim操作
     *
     * @param obj
     */
    public static <T> T trimStringFields(T obj) {
        if (obj == null) {
            return obj;
        }
        Field[] fields = getFields(obj.getClass());
        for (Field field : fields) {
            if (String.class == field.getType()) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(obj);
                    if (value != null) {
                        field.set(obj, value.trim());
                    }
                } catch (IllegalAccessException e) {
                    log.error("trimStringFields ERROR!field={},err={}", field, e.getMessage());
                }
            }
        }

        return obj;
    }

    /**
     * 删除敏感字段，比如password
     *
     * @param obj             要删除的对象
     * @param removeFieldList 要删除的
     * @param <T>
     * @return
     */
    public static <T> T removeField(T obj, List<String> removeFieldList) {
        if (obj == null) {
            return obj;
        }
        Field[] fields = getFields(obj.getClass());
//        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (removeFieldList.contains(field.getName())) {
                try {
                    field.setAccessible(true);
                    field.set(obj, null);
                } catch (IllegalAccessException e) {
                    log.error("removeField ERROR!field={},err={}", field, e.getMessage());
                }
            }
        }

        return obj;
    }

}
