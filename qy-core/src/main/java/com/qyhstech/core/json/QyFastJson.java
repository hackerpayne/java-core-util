package com.qyhstech.core.json;

import com.alibaba.fastjson2.*;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.qyhstech.core.collection.QyList;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * FastJson常用类封装
 */
@Slf4j
public class QyFastJson {

    /**
     * @param obj
     * @return
     */
    public static Map beanToMap(Object obj) {
        return beanToMap(obj, true);
    }

    /**
     * Bean转换为Map，忽略里面的Null转为空字符串
     * 可以解决：
     * 1、fastjson生成json时Null属性不显示
     * 2、ES里面比较常用的缺失字段不进索引的问题
     *
     * @param obj
     * @param nullToEmpty 是否把Null处理为Map
     * @return
     */
    public static Map beanToMap(Object obj, Boolean nullToEmpty) {
        if (nullToEmpty) {
            //            return JSON.parseObject(JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty), Map.class);
            return JSON.parseObject(JSON.toJSONString(obj, JSONWriter.Feature.WriteMapNullValue, JSONWriter.Feature.WriteNullStringAsEmpty), Map.class);
        }
        return JSON.parseObject(JSON.toJSONString(obj, JSONWriter.Feature.WriteMapNullValue), Map.class);
    }

    /**
     * Batis查询结果里面的数据直接转换为实体里面去
     *
     * @param dataList
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> mapListToEntity(List<Map<String, Object>> dataList, Class<T> clazz) {
        String jsonStr = JSON.toJSONString(dataList);
        return JSON.parseArray(jsonStr, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    /**
     * Map转换为Entity
     *
     * @param mapData
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToEntity(Map mapData, Class<T> clazz) {
        String jsonStr = JSON.toJSONString(mapData);
        return JSON.parseObject(jsonStr, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    /**
     * 从JSON中取出某个一级Key对应的Value
     *
     * @param jsonStr
     * @param jsonKey
     * @return
     */
    public static String getJsonValue(String jsonStr, String jsonKey) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject.getString(jsonKey);
    }

    /**
     * @param jsonStr
     * @param jsonKey
     * @return
     */
    public static JSONObject getJsonObj(String jsonStr, String jsonKey) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject.getJSONObject(jsonKey);
    }

    /**
     * JSON转Map格式
     *
     * @param json
     * @return
     */
    public static Map<String, String> jsonToMap(String json) {
        return JSON.parseObject(json, new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * JSON转换为List格式的Map数据
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> jsonToListMap(String json) {
        return JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    /**
     * 转对象数组
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 转Json对象
     *
     * @param json
     * @return
     */
    public static JSONArray jsonToArray(String json) {
        return JSON.parseArray(json);
    }

    /**
     * Object转换为Json的String字符串
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 转换为指定的Object，未测试
     *
     * @param json
     * @return
     */
    public static Object jsonToObject(String json) {
        return JSON.parseObject(json);
    }

    /**
     * JSON转换为实体类
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToEntity(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz, JSONReader.Feature.SupportSmartMatch);// 智能识别
    }

    /**
     * 转换JSON
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 非注解深层次过滤Json里面的某些元素
     *
     * @param o           对象
     * @param excludeKeys 要过滤的字段名称
     * @return
     */
    public static String toJsonString(Object o, String... excludeKeys) {
        List<String> excludes = Arrays.asList(excludeKeys);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().addAll(excludes);    //重点！！！
        return JSON.toJSONString(o, filter);
    }

    /**
     * 直接取为String类型
     *
     * @param jsonInput
     * @param jsonPath
     * @return
     */
    public static String getByJsonPath(String jsonInput, String jsonPath) {
        Object obj = JSONPath.eval(jsonInput, jsonPath);
        return obj.toString();
    }

    /**
     * 直接通过Jsonpath转换为指定的类
     * https://alibaba.github.io/fastjson2/jsonpath_cn.html
     *
     * @param jsonInput Json字符串
     * @param jsonPath  JsonPath表达式
     * @param clazz     目标实体类型
     * @param <T>
     * @return
     */
    public static <T> T getByJsonPath(String jsonInput, String jsonPath, Class<T> clazz) {
        Object obj = JSONPath.eval(jsonInput, jsonPath);
        if (Objects.nonNull(obj)) {
            return JSON.parseObject(obj.toString(), clazz, JSONReader.Feature.SupportSmartMatch);
        }
        return null;
    }

    /**
     * 转换为指定的List列表
     *
     * @param jsonInput
     * @param jsonPath
     * @param <T>
     * @return
     */
    public static <T> List<T> getByJsonPathToList(String jsonInput, String jsonPath, Class<T> clazz) {
        Object obj = JSONPath.eval(jsonInput, jsonPath);
        if (Objects.nonNull(obj)) {
            return JSON.parseArray(obj.toString(), clazz, JSONReader.Feature.SupportSmartMatch);
        }
        return QyList.empty();
    }

    /**
     * 默认值为空的JSON取值
     *
     * @param jsonObject
     * @param jsonPath
     * @return
     */
    public static String getJsonPath(JSONObject jsonObject, String jsonPath) {
        return getJsonPath(jsonObject, jsonPath, "");
    }

    /**
     * 加一层不为空判断吧，省得出错
     *
     * @param jsonObject   对象
     * @param jsonPath     JsonPath
     * @param defaultValue 为空时返回默认值
     * @return
     */
    public static String getJsonPath(JSONObject jsonObject, String jsonPath, String defaultValue) {
        if (jsonObject == null) { // 如果为Null直接返回
            return defaultValue;
        }
        Object ret = jsonObject.getByPath(jsonPath);
        return ret != null ? ret.toString() : defaultValue;
    }

}
