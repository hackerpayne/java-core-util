package com.qyhstech.core.settting;

import com.qyhstech.core.settting.anno.SettingKey;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SettingConverter {

    /**
     * 根据配置类获取所有配置项 key
     * @param clazz
     * @return
     */
    public static Set<String> getAllKeys(Class<?> clazz) {
        Set<String> keys = new HashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            SettingKey anno = field.getAnnotation(SettingKey.class);
            if (anno != null && !anno.value().isEmpty()) {
                keys.add(anno.value());
            } else {
                keys.add(field.getName());
            }
        }
        return keys;
    }

    /**
     * 对象 → BaseSetting 列表
     * @param config
     * @return
     * @param <T>
     */
    public static <T> List<BaseSetting> toBaseSettings(T config) {
        List<BaseSetting> settings = new ArrayList<>();
        if (config == null) {
            return settings;
        }

        Field[] fields = config.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(config);
                if (value != null) {
                    BaseSetting setting = new BaseSetting();
                    SettingKey keyAnno = field.getAnnotation(SettingKey.class);
                    String key = (keyAnno != null) ? keyAnno.value() : field.getName();
                    setting.setKey(key);
                    setting.setValue(String.valueOf(value));
                    settings.add(setting);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("读取字段失败: " + field.getName(), e);
            }
        }
        return settings;
    }

    /**
     * BaseSetting 列表 → 对象
     * 将数据库中的配置信息列表，转换为单个配置实体
     * @param settings 配置列表
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T toObject(List<BaseSetting> settings, Class<T> clazz) {
        if (settings == null || settings.isEmpty()) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("实例化失败: " + clazz.getName(), e);
            }
        }

        // 转为 Map 方便查找
        Map<String, String> map = settings.stream()
                .collect(Collectors.toMap(BaseSetting::getKey, BaseSetting::getValue));

        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                SettingKey keyAnno = field.getAnnotation(SettingKey.class);
                String key = (keyAnno != null) ? keyAnno.value() : field.getName();
                if (map.containsKey(key)) {
                    String value = map.get(key);
                    Object convertedValue = convertValue(field.getType(), value);
                    field.set(obj, convertedValue);
                }
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("转换为对象失败: " + clazz.getName(), e);
        }
    }

    /**
     * 类型转换辅助
     * @param targetType
     * @param value
     * @return
     */
    private static Object convertValue(Class<?> targetType, String value) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return Integer.valueOf(value);
        } else if (targetType == Long.class || targetType == long.class) {
            return Long.valueOf(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.valueOf(value);
        } else if (targetType == Double.class || targetType == double.class) {
            return Double.valueOf(value);
        }
        return value; // 其他类型可以扩展
    }
}
