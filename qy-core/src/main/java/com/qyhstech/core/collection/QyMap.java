package com.qyhstech.core.collection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.map.MapUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MapUtil扩展方法
 */
public class QyMap extends cn.hutool.core.map.MapUtil {

    /**
     * 创建一个空Map
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> empty() {
        return new HashMap<>();
    }

    /**
     * 创建一个空的线程安全队列
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConcurrentHashMap<K, V> emptyConcurrentMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * List列表按某一列做为key存入Map
     *
     * @param dataList 列表数据
     * @param function 提取某一列
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Map<R, T> buildMap(Collection<T> dataList, Function<T, R> function) {
        return buildMap(dataList, function, null);
    }

    /**
     * List列表按某一列做为key存入Map
     * 重复数据后条覆盖前一条
     *
     * @param dataList    数据列表
     * @param keyResolver 分组的Key所在字段，为空将不会过滤
     * @param filter      过滤条件
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Map<R, T> buildMap(Collection<T> dataList, Function<T, R> keyResolver, Predicate<T> filter) {
        if (CollUtil.isEmpty(dataList)) {
            return QyMap.empty();
        }
        Stream<T> stream = dataList.stream()
                .filter(item -> Objects.nonNull(keyResolver.apply(item)));

        if (null != filter) {
            stream = stream.filter(filter);
        }

        return stream.collect(Collectors.toMap(keyResolver, Function.identity(), (existing, newValue) -> existing));
    }

    /**
     * List列表转换为KV结构
     *
     * @param listData
     * @param keyResolver
     * @param valueResolver
     * @param <T>
     * @param <R>
     * @param <V>
     * @return
     */
    public static <T, R, V> Map<R, V> buildMap(List<T> listData, Function<T, R> keyResolver, Function<T, V> valueResolver) {
        return buildMap(listData, keyResolver, valueResolver, null);
    }

    /**
     * 数据转换为Map，如果Value为空，自动为空放入
     *
     * @param listData      列表数据
     * @param keyResolver   键名
     * @param valueResolver 键值
     * @param filter        过滤条件
     * @param <T>
     * @return
     */
    public static <T, R, V> Map<R, V> buildMap(List<T> listData, Function<T, R> keyResolver, Function<T, V> valueResolver, Predicate<T> filter) {
        if (CollUtil.isEmpty(listData)) {
            return new HashMap<>();
        }

        // 方案一，valueResolver解析出来为Null会抛异常
        //        return listData.stream().collect(Collectors.toMap(keyResolver, valueResolver));

        // 方案二，valueResolver解析出来为Null转换为空字符串，也不太好
        //        return listData.stream().collect(Collectors.toMap(keyResolver, t -> valueResolver.apply(t) == null ? "" : valueResolver.apply(t)));

        // 方案三，原样存入，不管数据是否是空的或者null
        Stream<T> stream = listData.stream()
                .filter(item -> Objects.nonNull(keyResolver.apply(item)));

        if (Objects.nonNull(filter)) {
            stream = stream.filter(filter);
        }

        return stream.collect(HashMap::new, (map, item) -> map.put(keyResolver.apply(item), valueResolver != null ? valueResolver.apply(item) : null), HashMap::putAll);
    }

    /**
     * 过滤条件
     *
     * @param dataList
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Map<R, List<T>> buildGroupMap(List<T> dataList, Function<T, R> function) {
        return buildGroupMapFilter(dataList, function, null);
    }

    /**
     * 按某一列分组
     *
     * @param dataList   数据列表
     * @param keyResolve 分组Key，为空将不会过滤
     * @param <T>
     * @return
     */
    public static <T, R> Map<R, List<T>> buildGroupMapFilter(Collection<T> dataList, Function<T, R> keyResolve, Predicate<T> filter) {
        if (CollUtil.isEmpty(dataList)) {
            return new HashMap<>();
        }
        return dataList.stream()
                .filter(item -> Objects.nonNull(keyResolve.apply(item)))
                .filter(filter == null ? x -> true : filter)
                .collect(Collectors.groupingBy(keyResolve, HashMap::new, Collectors.toList()));
    }

    /**
     * 将List转换为Map<K,List<V>>格式的Map
     * key是某一列。value是某一列的List求和。
     *
     * @param collection 数据列表
     * @param key        键名取值方法
     * @param value      键值取值方法
     * @param <E>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <E, K, V> Map<K, List<V>> buildGroupMap(Collection<E> collection, Function<E, K> key, Function<E, V> value) {
        if (CollUtil.isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .filter(item -> Objects.nonNull(key.apply(item)))
                .collect(Collectors.groupingBy(key, Collectors.mapping(value, Collectors.toList())))
                ;
    }

    /**
     * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map<br>
     * <B>{@code Collection<E>  --->  Map<T,Map<U,List<E>>> } </B>
     *
     * @param collection 需要分类的集合
     * @param key1       第一个分类的规则
     * @param key2       第二个分类的规则
     * @param <E>        集合元素类型
     * @param <K>        第一个map中的key类型
     * @param <U>        第二个map中的key类型
     * @return 分类后的map
     */
    public static <E, K, U> Map<K, Map<U, List<E>>> buildGroupDoubleMap(Collection<E> collection, Function<E, K> key1, Function<E, U> key2) {
        if (CollUtil.isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection
                .stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(key1, LinkedHashMap::new, Collectors.groupingBy(key2, LinkedHashMap::new, Collectors.toList())));
    }

    /**
     * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map<br>
     * <B>{@code Collection<E>  --->  Map<T,Map<U,E>> } </B>
     *
     * @param collection 需要分类的集合
     * @param key1       第一个分类的规则
     * @param key2       第二个分类的规则
     * @param <T>        第一个map中的key类型
     * @param <U>        第二个map中的key类型
     * @param <E>        collection中的泛型
     * @return 分类后的map
     */
    public static <E, T, U> Map<T, Map<U, E>> buildGroupInnerMap(Collection<E> collection, Function<E, T> key1, Function<E, U> key2) {
        if (CollUtil.isEmpty(collection) || key1 == null || key2 == null) {
            return new HashMap<>();
        }
        return collection
                .stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(key1, LinkedHashMap::new, Collectors.toMap(key2, Function.identity(), (l, r) -> l)));
    }

    /**
     * 初始化一个列表Map
     *
     * @return
     */
    public static List<Map<String, Object>> initMapList() {
        return new ArrayList<Map<String, Object>>();
    }

    /**
     * Map排序生成签名列表字符
     *
     * @param hashParameters
     * @return
     */
    public static String getSignStr(HashMap<String, Object> hashParameters) {
        return getSignStr(hashParameters, List.of("sign"));
    }

    /**
     * Map排序生成签名列表字符
     * Popup弹出指定的数据，Map排序
     *
     * @param params 参数Map列表
     * @param popKey 弹出Key，这里面的key不会参与签名
     * @return
     */
    public static String getSignStr(Map<String, Object> params, List<String> popKey) {

        if (isEmpty(params)) {
            return null;
        }

        Map<String, Object> newHash = params.entrySet().stream()
                .filter(item -> !popKey.contains(item.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s, b) -> s));

        // 对Map进行ASCII字典排序，生成String格式，工具类由HuTool-Core提供
        return join(sort(newHash), "&", "=", false);
    }

    /**
     * 根据Map的键Key，通过条件自动过滤数据
     *
     * @param map       要过滤的Map数据列表
     * @param predicate 条件过滤器
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T> Map<String, T> filterMap(Map<String, T> map, Predicate<Map.Entry> predicate) {
        return Optional.ofNullable(map).map(
                (v) -> v.entrySet().stream()
                        .filter(predicate)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        ).orElse(new HashMap<>());
    }

    /**
     * 合并两个相同key类型的map
     *
     * @param map1  第一个需要合并的 map
     * @param map2  第二个需要合并的 map
     * @param merge 合并的lambda，将key  value1 value2合并成最终的类型,注意value可能为空的情况
     * @param <K>   map中的key类型
     * @param <X>   第一个 map的value类型
     * @param <Y>   第二个 map的value类型
     * @param <V>   最终map的value类型
     * @return 合并后的map
     */
    public static <K, X, Y, V> Map<K, V> mergeMap(Map<K, X> map1, Map<K, Y> map2, BiFunction<X, Y, V> merge) {
        if (MapUtil.isEmpty(map1) && MapUtil.isEmpty(map2)) {
            return new HashMap<>();
        } else if (MapUtil.isEmpty(map1)) {
            map1 = new HashMap<>();
        } else if (MapUtil.isEmpty(map2)) {
            map2 = new HashMap<>();
        }
        Set<K> key = new HashSet<>();
        key.addAll(map1.keySet());
        key.addAll(map2.keySet());
        Map<K, V> map = new HashMap<>();
        for (K t : key) {
            X x = map1.get(t);
            Y y = map2.get(t);
            V z = merge.apply(x, y);
            if (z != null) {
                map.put(t, z);
            }
        }
        return map;
    }
}
