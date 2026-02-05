package com.qyhstech.core.collection;

import cn.hutool.core.collection.CollUtil;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.crypto.QyMd5;
import com.qyhstech.core.function.QyBatchHandlerFunction;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合相关工具类，包括数组
 */
public class QyColl extends CollUtil {

    /**
     * 初始化ArrayList
     *
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> List<T> initArrayList(Class<T> requiredType) {
        return new ArrayList<T>();
    }

    /**
     * 删除列表中的指定元素
     *
     * @param list        需要处理的列表
     * @param removeItems 待删除的元素
     * @return
     */
    public static List<String> removeItems(List<String> list, String... removeItems) {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();

            for (String item : removeItems) {
                if (next.equals(item)) {
                    iterator.remove();
                }
            }
        }
        return list;
    }

    /**
     * 把List拆分成多个List子集,主要通过偏移量来实现的
     *
     * @param source 源List
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {

        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;  // (先计算出余数)
        int number = source.size() / n;  // 然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * List转Array
     *
     * @param list List
     * @return Array
     */
    public static String[] toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    /**
     * 按每max_number个一组分割
     *
     * @param max_number
     * @param data
     * @param type
     * @return
     */
    public static List<String> listSkip(Integer max_number, String data, int type) {
        List<String> sts = new ArrayList<String>();
        List<String> list = Arrays.asList(data.split(",")).stream().map(s -> String.valueOf(type == 0 ? QyMd5.md5(s.trim()).toUpperCase() : s.trim()))
                .collect(Collectors.toList());
        int limit = countStep(max_number, list.size());

        // 方法一：使用流遍历操作
        List<List<String>> mglist = new ArrayList<>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            mglist.add(list.stream().skip(i * max_number).limit(max_number).collect(Collectors.toList()));
        });
        for (List<String> mg : mglist) {
            sts.add(String.join(",", mg.stream().map(Object::toString).collect(Collectors.toList())));
        }
        return sts;
        // 方法二：获取分割后的集合
        //		List<List<Integer>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel()
        //				.map(a -> list.stream().skip(a * MAX_NUMBER).limit(MAX_NUMBER).parallel().collect(Collectors.toList()))
        //				.collect(Collectors.toList());
        //
        //		System.out.println(splitList);
    }

    /**
     * 计算切分次数
     */
    private static Integer countStep(Integer max_number, Integer size) {
        return (size + max_number - 1) / max_number;
    }


    /**
     * 取数组里面，随机位置的索引，需要传入的是数组的长度:arr.length
     * 亦可用于返回0-length之间值的方法以
     *
     * @param length
     * @return
     */
    public static int getRandomIndex(int length) {
        return (int) (Math.random() * length);
    }

    /**
     * 获取List随机结果一个
     *
     * @param listArray
     * @param <T>
     * @return
     */
    public static <T> T getRandomItem(List<T> listArray) {
        int index = (int) (Math.random() * listArray.size());
        return listArray.get(index);
    }

    /**
     * 为数组每一项添加开头和结尾字符串
     *
     * @param listArray
     * @param head
     * @param foot
     * @return
     */
    public static List<String> addStr(List<String> listArray, String head, String foot) {
        return addStr(listArray, head, foot, true);
    }

    /**
     * 在数组每一项的头尾增加字符串
     *
     * @param dataList    数组列表
     * @param head        添加的头部信息
     * @param foot        添加的属部信息
     * @param ignoreEmpty 是否忽略空元素
     * @return
     */
    public static List<String> addStr(List<String> dataList, String head, String foot, boolean ignoreEmpty) {
        for (int i = 0; i < dataList.size(); i++) {
            if (ignoreEmpty && QyStr.isEmpty(dataList.get(i))) {
                continue;
            }
            if (QyStr.isNotEmpty(head)) {
                dataList.set(i, head + dataList.get(i));
            }
            if (QyStr.isNotEmpty(foot)) {
                dataList.set(i, dataList.get(i) + foot);
            }
        }
        return dataList;
    }

    /**
     * 分页查询数据，并将结果添加到字段中
     * 使用默认的固定线程池
     *
     * @param items                 数据列表
     * @param fetchDataFromDatabase 查询语句
     * @param dataSetter            数据更新器
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<T> listProcessor(List<T> items, Function<T, R> fetchDataFromDatabase, BiConsumer<T, R> dataSetter) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<T> listResults;
        try {
            listResults = listProcessor(items, fetchDataFromDatabase, dataSetter, executorService);
        } finally {
            executorService.shutdown();
        }

        return listResults;
    }

    /**
     * 多查询多条件查询列表数据，使用默认线程池
     *
     * @param items
     * @param queriesAndSetters
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<T> listProcessor(List<T> items, List<QyBatchHandlerFunction<T, ?>> queriesAndSetters) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<T> listResults;
        try {
            listResults = listProcessor(items, queriesAndSetters, executorService);
        } finally {
            executorService.shutdown();
        }
        return listResults;
    }

    /**
     * 简化的列表处理器
     *
     * @param items
     * @param fetchDataFromDatabase
     * @param dataSetter
     * @param executor
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<T> listProcessor(List<T> items, Function<T, R> fetchDataFromDatabase, BiConsumer<T, R> dataSetter, Executor executor) {
        return listProcessor(items, List.of(new QyBatchHandlerFunction<T, R>(fetchDataFromDatabase, dataSetter)), executor);
    }

    /**
     * 多线程实现，子查询并附加到原来的字段中
     * QyColl.listProcessor(page, this::fetchDataFromDatabase, MallProductEntity::setMallStockList, commonTaskExecutor);
     * 流程是遍历每一条数据，执行：fetchDataFromDatabase，再将结果通过dataSetter还原设置到列表内，可以指定线程池
     * 场景：
     * 根据文章ID，查标签信息，再返回来添加到文章的标签字段中供使用
     *
     * @param items             要查询的数据列表
     * @param queriesAndSetters 数据查询列表
     * @param executor          指定线程池
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T, R> List<T> listProcessor(List<T> items, List<QyBatchHandlerFunction<T, ?>> queriesAndSetters, Executor executor) {

        // 为空就不要查了
        if (CollUtil.isEmpty(items)) {
            return QyList.empty();
        }

        // 创建一个用于存储CompletableFuture的列表
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        if (Objects.isNull(executor)) {
            executor = Executors.newFixedThreadPool(10);
        }

        //        for (T item : items) {
        //            // 对于每个项目，异步地从数据库查询数据
        //            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> fetchDataFromDatabase.apply(item), executor)
        //                    // 然后，当数据可用时，将其设置为项目的data字段
        //                    .thenAcceptAsync(data -> dataSetter.accept(item, data));
        //
        //            // 将CompletableFuture添加到列表中
        //            futures.add(future);
        //        }

        for (T item : items) {
            for (QyBatchHandlerFunction<T, ?> qyBatchHandlerFunction : queriesAndSetters) {
                CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> qyBatchHandlerFunction.dataHandler.apply(item), executor)
                        .thenAcceptAsync(data -> ((BiConsumer<T, Object>) qyBatchHandlerFunction.updateHandler).accept(item, data), executor);

                futures.add(future);
            }
        }

        // 等待所有的CompletableFuture完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return items;
    }

}