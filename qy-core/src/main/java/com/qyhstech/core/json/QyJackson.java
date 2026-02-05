package com.qyhstech.core.json;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Json 工具类，jackson的一层封装
 * 核心代码来源：https://juejin.cn/post/7195117148140732453
 */
@UtilityClass
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class QyJackson {

    //    private static final ObjectMapper OBJECT_MAPPER = QySpringContext.getBean(ObjectMapper.class);
    private static final ObjectMapper OBJECT_MAPPER = QyJacksonFactory.getObjectMapper();

    /**
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 读取到节点里面，可以继续操作
     *
     * @param json
     * @return
     */
    public JsonNode readTree(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为JSON字符串
     *
     * @param obj
     * @return
     */
    public String toJsonString(Object obj) {
        if (null == obj) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("序列化对象【" + obj + "】时出错", e);
        }
    }

    /**
     *
     * @param obj
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> String toJsonString(Object obj, TypeReference<T> typeReference) {
        if (null == obj) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writerFor(typeReference)
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("序列化对象【" + obj + "】时出错", e);
        }
    }

    /**
     * 序列化时过滤指定的字段不要序列化
     *
     * @param obj       要序列化的对象
     * @param filterStr 过滤的字段，将不会添加到结果中
     * @return
     */
    public String toJsonString(Object obj, String[] filterStr) {
        try {
            // 创建一个只序列化sex和weight的过滤器
            //serializeAllExcept 表示序列化全部，除了指定字段
            //filterOutAllExcept 表示过滤掉全部，除了指定的字段
            // SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("sex", "weight");
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(filterStr);
            // 将上面的过滤器和ID为myFilter的注解进行关联
            FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", filter);
            return OBJECT_MAPPER.writer(filters).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("序列化对象【" + obj + "】时出错", e);
        }
    }

    /**
     * 将列表List转换为Json字符串，保留List格式。尤其在列表时非常有用，不然jackson会掉列表信息
     *
     * @param list
     * @param elementClass
     * @param <T>
     * @return
     */
    public static <T> String toJsonListString(List<T> list, Class<T> elementClass) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, elementClass);
            return OBJECT_MAPPER.writerFor(javaType).writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException("序列化对象【" + list + "】时出错", e);
        }
    }

    /**
     * 对象转Json，可以指定Mapper
     *
     * @param obj
     * @param mapper
     * @return
     */
    public static String toJsonString(Object obj, ObjectMapper mapper) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 动态过滤属性的方法
     *
     * @param clazz   需要设置规则的class
     * @param include 转换时包含哪些字段
     * @param filter  转换时过滤那些字段
     */
    public static void filter(Class<?> clazz, String include, String filter) {
        if (clazz == null) {
            return;
        }
        if (StrUtil.isNotEmpty(include)) {
            OBJECT_MAPPER.setFilterProvider(new SimpleFilterProvider().addFilter("dynamic_include", SimpleBeanPropertyFilter.filterOutAllExcept(include.split(","))));
        } else if (StrUtil.isNotEmpty(filter)) {
            OBJECT_MAPPER.setFilterProvider(new SimpleFilterProvider().addFilter("dynamic_filter", SimpleBeanPropertyFilter.serializeAllExcept(filter.split(","))));
        }
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     *
     * @param obj 对象
     * @return 美化的Json格式字符串
     */
    public <T> String toJsonStringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }


    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        Assert.notNull(bytes, "bytes not null");
        try {
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException(String.format("jackson parse object error, json: %s, class: %s", new String(bytes, StandardCharsets.UTF_8), clazz), e);
        }
    }

    public static <T> T parseObject(InputStream stream, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(stream, clazz);
        } catch (IOException e) {
            throw new RuntimeException(String.format("jackson parse object error, json: %s, class: %s", stream, clazz), e);
        }
    }

    /**
     * 解析为指定的类
     *
     * @param inputStr
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T parseObject(String inputStr, Class<T> clazz) {
        if (StrUtil.isBlank(inputStr)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(inputStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON【" + inputStr + "】转对象时出错", e);
        }
    }

    /**
     * 将Json转换为指定类
     *
     * @param obj   要转换的对象，比如Map
     * @param clazz 要处理的类名
     * @param <T>
     * @return
     */
    public <T> T convertObject(Object obj, Class<T> clazz) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.convertValue(obj, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON【" + obj + "】转对象时出错", e);
        }
    }

    /**
     * @param inputStr
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T parseObject(String inputStr, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(inputStr, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析对象
     *
     * @param json
     * @param clazz
     * @param mapper
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz, ObjectMapper mapper) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(String.format("jackson parse object error, json: %s, class: %s", json, clazz), e);
        }
    }

    /**
     * 直接转成List
     *
     * @param json
     * @param elementClass
     * @param <T>
     * @return
     */
    public static <T> List<T> parseList(String json, Class<T> elementClass) {

        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, elementClass);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param str   要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public <T> T string2Obj(String str, Class<T> clazz) {
        if (StrUtil.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : OBJECT_MAPPER.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 转换为List列表
     *
     * @param json
     * @param tTypeReference
     * @param <T>
     * @return
     */
    public static <T> List<T> readList(String json, TypeReference<List<T>> tTypeReference) {

        if (StrUtil.isBlank(json) || !(json.startsWith("[") && json.endsWith("]"))) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, tTypeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为Map
     *
     * @param json
     * @param tTypeReference
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> readMap(String json, TypeReference<Map<String, T>> tTypeReference) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, tTypeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 例：
     * JsonUtil.string2Obj(userListJson, new TypeReference<List<User>>() {});
     *
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StrUtil.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(str, typeReference);
        } catch (IOException e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 根据指定的类型来转换字符串到对象
     *
     * @param str
     * @param type
     * @param <T>
     * @return
     */
    public <T> T string2Obj(String str, Type type) {
        if (StrUtil.isEmpty(str) || type == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(str, OBJECT_MAPPER.getTypeFactory().constructType(type));
        } catch (IOException e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 转换为List结果：
     * List<User> userListBean2 = JsonUtil.string2Obj(userListJson, List.class, User.class);
     *
     * @param str
     * @param collectionClazz
     * @param elementClazzes
     * @param <T>
     * @return
     */
    public <T> T string2Obj(String str, Class<?> collectionClazz, Class<?>... elementClazzes) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClazz, elementClazzes);
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 将Json数组转换为List集合
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StrUtil.isBlank(text)) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Input流转换为List
     *
     * @param steam
     * @param type
     * @param <V>
     * @return
     */
    public static <V> List<V> parseArray(InputStream steam, Class<V> type) {
        try {
            CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, type);
            return OBJECT_MAPPER.readValue(steam, collectionType);
        } catch (IOException e) {
            throw new RuntimeException(String.format("jackson from error, json: %s, type: %s", steam, type), e);
        }
    }

    /**
     * @param jsonArrayStr
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> toList(String jsonArrayStr, Class<T> clazz) throws Exception {
        List<Map<String, Object>> list = (List<Map<String, Object>>) OBJECT_MAPPER.readValue(jsonArrayStr, new TypeReference<List<T>>() {
        });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(mapToBean(map, clazz));
        }
        return result;
    }

    /**
     * 读取某个字段的值
     *
     * @param jsonStr
     * @param jsonKey
     * @return
     */
    public static String getJsonValue(String jsonStr, String jsonKey) {
        Map<String, Object> jsonMap = parseMap(jsonStr);
        return MapUtil.getStr(jsonMap, jsonKey);
    }

    /**
     * 读取json某个字段的值
     *
     * @param jsonStr
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T> T getJsonValue(String jsonStr, Function<Map<String, Object>, T> consumer) {
        Map<String, Object> jsonMap = parseMap(jsonStr);
        return consumer.apply(jsonMap);
    }

    /**
     * json转Map
     *
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public Map<String, Object> parseMap(String jsonStr) {
        try {
            if (jsonStr != null && !jsonStr.isEmpty()) {
                return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
                });
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    /**
     * 节点转Map
     *
     * @param jsonStr
     * @return
     */
    public Map<String, Object> parseMap(JsonNode jsonStr) {
        try {
            if (jsonStr != null) {
                return OBJECT_MAPPER.convertValue(jsonStr, new TypeReference<Map<String, Object>>() {
                });
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    /**
     * 转换为MapString
     *
     * @param jsonStr
     * @return
     */
    public Map<String, String> parseMapStr(String jsonStr) {
        try {
            if (StrUtil.isNotEmpty(jsonStr)) {
                return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<String, String>>() {
                });
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    /**
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> Map<String, T> toMapBean(String jsonStr, Class<T> clazz) {

        try {
            Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<String, T>>() {
            });
            Map<String, T> result = new HashMap<String, T>();
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), mapToBean(entry.getValue(), clazz));
            }
            return result;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return null;
        }

    }

    /**
     * Map转换为Bean
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T mapToBean(Map map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    /**
     * Bean转Map格式
     *
     * @param obj
     * @param <T>
     * @return
     */
    public <T> Map<String, Object> beanToMap(T obj) {
        return OBJECT_MAPPER.convertValue(obj, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * 判断是否是有效的Json格式
     *
     * @param json
     * @return
     */
    public static boolean validate(String json) {
        try {
            JsonParser parser = OBJECT_MAPPER.getFactory().createParser(json);
            while (parser.nextToken() != null) {
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 将JSON格式的字符串转换为Dict对象
     *
     * @param text JSON格式的字符串
     * @return 转换后的Dict对象，如果字符串为空或者不是JSON格式则返回null
     * @throws RuntimeException 如果转换过程中发生IO异常，则抛出运行时异常
     */
    public static Dict parseDict(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructType(Dict.class));
        } catch (MismatchedInputException e) {
            // 类型不匹配说明不是json
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON格式的字符串转换为Dict对象的列表
     *
     * @param text JSON格式的字符串
     * @return 转换后的Dict对象的列表，如果字符串为空则返回null
     * @throws RuntimeException 如果转换过程中发生IO异常，则抛出运行时异常
     */
    public static List<Dict> parseDictMap(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, Dict.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 拷贝对象
     *
     * @param t
     * @param clazz
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copy(S t, Class<T> clazz) {
        return parseObject(toJsonString(t), clazz);
    }

    /**
     * 拷贝对象
     *
     * @param t
     * @param typeReference
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copy(S t, TypeReference<T> typeReference) {
        return parseObject(toJsonString(t), typeReference);
    }

    /**
     * 拷贝对象
     *
     * @param t
     * @param clazz
     * @param ignoreProperties
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copy(S t, Class<T> clazz, String... ignoreProperties) {
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                List<BeanPropertyWriter> result = new ArrayList<>();
                List<String> ignoreFileNames = Arrays.asList(ignoreProperties);
                for (BeanPropertyWriter writer : beanProperties) {
                    if (!ignoreFileNames.contains(writer.getName())) {
                        result.add(writer);
                    }
                }
                return result;
            }
        });

        ObjectMapper mapper = OBJECT_MAPPER.copy();
        mapper.registerModule(module);
        return parseObject(toJsonString(t, mapper), clazz, mapper);
    }

    /**
     * 把List转换为另一种实体类List
     * QyJackson.copyList(fromPage.getResult(), resultClz)
     *
     * @param list
     * @param clazz
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> list, Class<T> clazz) {
        return parseArray(toJsonString(list), clazz);
    }

    /**
     * 拷贝集合，循环单个对象转换
     *
     * @param list
     * @param clazz
     * @param convertConsumer
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> list, Class<T> clazz, BiConsumer<S, T> convertConsumer) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(s -> {
            T t = parseObject(toJsonString(s), clazz);
            convertConsumer.accept(s, t);
            return t;
        }).collect(Collectors.toList());
    }

}
