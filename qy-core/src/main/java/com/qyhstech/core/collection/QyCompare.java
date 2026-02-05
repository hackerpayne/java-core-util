package com.qyhstech.core.collection;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author HuBiao
 * @date 2023/11/02 14:08
 * <p>
 * 对象对比工具类
 * 对比两个集合 计算出需要新增/编辑/删除的集合
 */
public class QyCompare {

    /**
     * 对比结果对象
     *
     * @param <T>
     */
    @Getter
    @Setter
    public static class CompareResult<T> {
        private List<T> addList;
        private List<T> updateList;
        private List<T> deleteList;

        public CompareResult() {
            this.addList = new ArrayList<>();
            this.updateList = new ArrayList<>();
            this.deleteList = new ArrayList<>();
        }
    }

    /**
     * 对比两个集合 (计算出需要新增/编辑/删除的集合)
     * 该工具类有可能影响原集合的数据, 如果需要请自行深拷贝
     *
     * @param newItems      新集合
     * @param oldItems      旧集合
     * @param pkFun         提取主键的方法
     * @param comparer      比较器
     * @param matchConsumer 匹配后的处理方法
     * @param <T>           对比对象类型
     * @param <PK>          主键类型
     * @return
     */
    public static <T, PK> CompareResult<T> compare(List<T> newItems,
                                                   List<T> oldItems,
                                                   Function<T, PK> pkFun,
                                                   BiFunction<T, T, Boolean> comparer,
                                                   BiConsumer<T, T> matchConsumer) {
        CompareResult result = new CompareResult<T>();

        // 如果新集合为空 表示所有的旧集合均需要删除
        if (CollUtil.isEmpty(newItems)) {
            result.setDeleteList(oldItems);
            return result;
        }
        // 如果就集合为空 表示所有的新集合均需要新增
        if (CollUtil.isEmpty(oldItems)) {
            result.setAddList(newItems);
            return result;
        }

        ListIterator<T> newIterator = newItems.listIterator();
        while (newIterator.hasNext()) {
            T newItem = newIterator.next();

            Optional<T> optional = oldItems.stream().filter(oldItem -> pkFun.apply(oldItem).equals(pkFun.apply(newItem))).findFirst();
            if (!optional.isPresent()) {
                result.getAddList().add(newItem);
                continue;
            }

            T oldItem = optional.get();
            if (!comparer.apply(newItem, oldItem)) {
                matchConsumer.accept(newItem, oldItem);
                result.getUpdateList().add(newItem);
            }
            oldItems.remove(oldItem);
        }

        result.getDeleteList().addAll(oldItems);
        return result;
    }

    /**
     * 对比两个集合 (计算出需要新增/编辑/删除的集合)
     *
     * @param newItems 新集合
     * @param oldItems 旧集合
     * @param pkFun    提取主键的方法
     * @param comparer 比较器
     * @param <T>      对比对象类型
     * @param <PK>     主键类型
     * @return 对比结果
     */
    public static <T, PK> CompareResult<T> compare(List<T> newItems,
                                                   List<T> oldItems,
                                                   Function<T, PK> pkFun,
                                                   BiFunction<T, T, Boolean> comparer) {
        return compare(newItems, oldItems, pkFun, comparer, (newItem, oldItem) -> {
        });
    }


    /**
     * 对比两个集合 (计算出需要新增/编辑/删除的集合)
     *
     * @param newItems      新集合
     * @param oldItems      旧集合
     * @param pkFun         提取主键的方法
     * @param valFun        提取值的方法
     * @param matchConsumer 匹配后的处理方法
     * @param <T>           对比对象类型
     * @param <PK>          主键类型
     * @param <VAL>         值类型
     * @return 对比结果
     */
    public static <T, PK, VAL> CompareResult<T> compare(List<T> newItems,
                                                        List<T> oldItems,
                                                        Function<T, PK> pkFun,
                                                        Function<T, VAL> valFun,
                                                        BiConsumer<T, T> matchConsumer) {
        return compare(newItems, oldItems, pkFun,
                (newItem, oldItem) -> valFun.apply(newItem).equals(valFun.apply(oldItem)),
                matchConsumer
        );
    }

    /**
     * 对比两个集合 (计算出需要新增/编辑/删除的集合)
     *
     * @param newItems 新集合
     * @param oldItems 旧集合
     * @param pkFun    提取主键的方法
     * @param valFun   提取值的方法
     * @param <T>      对比对象类型
     * @param <PK>     主键类型
     * @param <VAL>    值类型
     * @return
     */
    public static <T, PK, VAL> CompareResult<T> compare(List<T> newItems,
                                                        List<T> oldItems,
                                                        Function<T, PK> pkFun,
                                                        Function<T, VAL> valFun) {
        return compare(newItems, oldItems, pkFun, valFun, (newItem, oldItem) -> {
        });
    }

    /**
     * 对比两个集合是否一致
     *
     * @param newItems 新集合
     * @param oldItems 旧集合
     * @param pkFun    提取主键的方法
     * @param valFun   提取值的方法
     * @param <T>      对比对象类型
     * @param <PK>     主键类型
     * @param <VAL>    值类型
     * @return true:一致 false:不一致
     */
    public static <T, PK, VAL> Boolean compareSame(List<T> newItems,
                                                   List<T> oldItems,
                                                   Function<T, PK> pkFun,
                                                   Function<T, VAL> valFun) {
        if (CollUtil.isEmpty(newItems) && CollUtil.isEmpty(oldItems)) {
            return Boolean.TRUE;
        }
        if (CollUtil.isEmpty(newItems) || CollUtil.isEmpty(oldItems)) {
            return Boolean.FALSE;
        }
        if (newItems.size() != oldItems.size()) {
            return Boolean.FALSE;
        }

        ListIterator<T> newIterator = newItems.listIterator();
        while (newIterator.hasNext()) {
            T newItem = newIterator.next();

            Optional<T> optional = oldItems.stream().filter(oldItem -> pkFun.apply(oldItem).equals(pkFun.apply(newItem))).findFirst();
            if (!optional.isPresent()) {
                return Boolean.FALSE;
            }

            VAL oldVal = valFun.apply(optional.get());
            VAL newVal = valFun.apply(newItem);
            if (oldVal instanceof BigDecimal) {
                if (((BigDecimal) oldVal).compareTo((BigDecimal) newVal) != 0) {
                    return Boolean.FALSE;
                }
            } else {
                if (!oldVal.equals(newVal)) {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 对比两个集合是否一致
     *
     * @param newItems 新集合
     * @param oldItems 旧集合
     * @param valFun   提取值的方法
     * @param <T>      对比对象类型
     * @param <VAL>    值类型
     * @return true:一致 false:不一致
     */
    public static <T, VAL> Boolean compareSame(List<T> newItems,
                                               List<T> oldItems,
                                               Function<T, VAL> valFun) {
        if (CollUtil.isEmpty(newItems) && CollUtil.isEmpty(oldItems)) {
            return Boolean.TRUE;
        }
        if (CollUtil.isEmpty(newItems) || CollUtil.isEmpty(oldItems)) {
            return Boolean.FALSE;
        }
        if (newItems.size() != oldItems.size()) {
            return Boolean.FALSE;
        }

        for (int i = 0; i < newItems.size(); i++) {
            if (!valFun.apply(newItems.get(i)).equals(valFun.apply(oldItems.get(i)))) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

}
