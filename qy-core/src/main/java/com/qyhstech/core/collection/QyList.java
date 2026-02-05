package com.qyhstech.core.collection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.qyhstech.core.QyStr;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class QyList extends ListUtil {

    /**
     * 创建一个空列表
     *
     * @param <E>
     * @return
     */
    public static <E> ArrayList<E> empty() {
        return new ArrayList<>();
    }

    /**
     * 获取List的第一个元素，如果为空则返回null
     *
     * @param list 要取值的列表数据
     * @param <T>
     * @return
     */
    public static <T> T getFirstOrNull(List<T> list) {
        return getFirstOr(list, null);
    }

    /**
     * 列表中是否某一项包含字符串
     *
     * @param list  列表
     * @param value 字符串
     * @return
     */
    public static boolean containsStr(List<String> list, String value) {
        return CollUtil.contains(list, item -> item.contains(value));
    }

    /**
     * 字符串是否包含列表中的某一项
     *
     * @param list  列表
     * @param value 字符串
     * @return
     */
    public static boolean containsList(List<String> list, String value) {
        return CollUtil.contains(list, value::contains);
    }

    /**
     * 取第一个值，取不出使用默认值
     *
     * @param list
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getFirstOr(List<T> list, T defaultValue) {
        return Optional.ofNullable(list)
                .filter(l -> !l.isEmpty())
                .map(List::getFirst)
                .orElse(defaultValue);
    }

    /**
     * 将列表按指定的字段，进行拼接
     *
     * @param collection 需要转化的集合
     * @param function   拼接方法
     * @return 拼接后的list
     */
    public static <E> String join(Collection<E> collection, Function<E, String> function) {
        return join(collection, function, QyStr.COMMA);
    }

    /**
     * 某一列或者多列进行join连接
     *
     * @param dataList 数所列表
     * @param function 提取函数
     * @param joinStr  连接字符串
     * @param <T>
     * @return
     */
    public static <T> String join(Collection<T> dataList, Function<T, String> function, CharSequence joinStr) {
        if (CollUtil.isEmpty(dataList)) {
            return QyStr.EMPTY;
        }

        return dataList.stream()
                .map(function)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(joinStr));
    }

    /**
     * 将collection过滤
     *
     * @param collection 需要转化的集合
     * @param function   过滤方法
     * @return 过滤后的list
     */
    public static <E> List<E> filter(Collection<E> collection, Predicate<E> function) {
        if (CollUtil.isEmpty(collection)) {
            return CollUtil.newArrayList();
        }
        // 注意此处不要使用 .toList() 新语法 因为返回的是不可变List 会导致序列化问题
        return collection.stream()
                .filter(function)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    /**
     * 找到流中满足条件的第一个元素
     *
     * @param collection 需要查询的集合
     * @param function   过滤方法
     * @return 找到符合条件的第一个元素，没有则返回null
     */
    public static <E> E findFirst(Collection<E> collection, Predicate<E> function) {
        if (CollUtil.isEmpty(collection)) {
            return null;
        }
        return collection.stream().filter(function).findFirst().orElse(null);
    }

    /**
     * 找到流中任意一个满足条件的元素
     *
     * @param collection 需要查询的集合
     * @param function   过滤方法
     * @return 找到符合条件的任意一个元素，没有则返回null
     */
    public static <E> Optional<E> findAny(Collection<E> collection, Predicate<E> function) {
        if (CollUtil.isEmpty(collection)) {
            return Optional.empty();
        }
        return collection.stream().filter(function).findAny();
    }

    /**
     * 将collection排序
     *
     * @param collection 需要转化的集合
     * @param comparing  排序方法
     * @return 排序后的list
     */
    public static <E> List<E> sort(Collection<E> collection, Comparator<E> comparing) {
        if (CollUtil.isEmpty(collection)) {
            return CollUtil.newArrayList();
        }
        // 注意此处不要使用 .toList() 新语法 因为返回的是不可变List 会导致序列化问题
        return collection.stream()
                .filter(Objects::nonNull)
                .sorted(comparing)
                .collect(Collectors.toList());
    }

    /**
     * 转换某一列的数据到List
     *
     * @param dataList
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> buildList(List<T> dataList, Function<T, R> function) {
        return buildList(dataList, function, null);
    }

    /**
     * 某一列转换为字段列表
     *
     * @param dataList 数据列表
     * @param function 指定的某一列
     * @param filter   过滤条件
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> buildList(List<T> dataList, Function<T, R> function, Predicate<T> filter) {
        if (CollUtil.isEmpty(dataList)) {
            return QyList.empty();
        }
        return dataList.stream()
                .filter(item -> Objects.nonNull(function.apply(item)))
                .filter(filter == null ? x -> true : filter)
                .map(function)
                .collect(Collectors.toList()); // 不要直接用toList，是不可修改的List
    }

    /**
     * 默认就去重转换某一列
     *
     * @param collection
     * @param function
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> List<T> buildListDistinct(Collection<E> collection, Function<E, T> function) {
        return buildListDistinct(collection, function, true);
    }

    /**
     * 将collection转化为List集合，但是两者的泛型不同<br>
     * <B>{@code Collection<E>  ------>  List<T> } </B>
     *
     * @param collection 需要转化的集合
     * @param function   collection中的泛型转化为list泛型的lambda表达式
     * @param distinct   是否去重
     * @param <E>        collection中的泛型
     * @param <T>        List中的泛型
     * @return 转化后的list
     */
    public static <E, T> List<T> buildListDistinct(Collection<E> collection, Function<E, T> function, boolean distinct) {
        if (CollUtil.isEmpty(collection)) {
            return CollUtil.newArrayList();
        }

        Stream<T> stream = collection.stream()
                .map(function)
                .filter(Objects::nonNull);

        if (distinct) {
            stream = stream.distinct();
        }

        return stream.collect(Collectors.toList());
    }

    /**
     * 去重方法，使用时：
     * codeGenResultList = codeGenResultList.stream().filter(QyListUtil.distinctByKey((p) -> (p.getResultPath()))).collect(Collectors.toList());
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        //putIfAbsent方法添加键值对，如果map集合中没有该key对应的值，则直接添加，并返回null，如果已经存在对应的值，则依旧为原来的值。
        //如果返回null表示添加数据成功(不重复)，不重复(null==null :TRUE)
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 递归把数据转换为平级
     *
     * @param listEntity
     * @param function
     * @param <T>
     * @return
     */
    public static <T> List<T> flatList(List<T> listEntity, Function<T, List<T>> function) {
        List<T> allData = QyList.empty();
        List<T> childrenList = QyList.empty();
        List<T> childList = QyList.empty();
        if (listEntity != null) {
            allData.addAll(listEntity);// 先添加自己
            for (T entity : listEntity) {
                childList = function.apply(entity);
                if (childList != null) {
                    allData.addAll(childList);
                }
                // 递归调用以添加子实体的AmqpDistrictDto列表
                childrenList = flatList(childList, function);
                if (CollUtil.isNotEmpty(childrenList)) {
                    allData.addAll(childrenList);
                }
            }
        }
        return allData;
    }

    /**
     * List去重
     *
     * @param list
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> List<T> distinctByKey(List<T> list, Function<? super T, ?> keyExtractor) {
        return list.stream().filter(distinctByKey(keyExtractor)).collect(Collectors.toList());
    }

    /**
     * 判断是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断是否不为空
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 创建可变的List
     *
     * @param values
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> List<T> ofNew(T... values) {
        return CollUtil.newArrayList(values);
    }

    /**
     * 冗余一个方法，方便进行替换使用
     *
     * @param values
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> List<T> newArrayList(T... values) {
        return CollUtil.newArrayList(values);
    }

}
