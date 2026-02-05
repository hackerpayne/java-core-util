package com.qyhstech.core.function;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 函数设置器
 * 将查询的结果，执行，并将结果放到dataSetter指定的字段中去
 *
 * @param <T>
 * @param <R>
 */
public class QyBatchHandlerFunction<T, R> {

    /**
     * 查询函数，查询T，返回R
     */
    public final Function<T, R> dataHandler;

    /**
     * 消费函数，消费T，返回R
     */
    public final BiConsumer<T, R> updateHandler;

    /**
     * 构造函数
     * @param dataHandler
     * @param updateHandler
     */
    public QyBatchHandlerFunction(Function<T, R> dataHandler, BiConsumer<T, R> updateHandler) {
        this.dataHandler = dataHandler;
        this.updateHandler = updateHandler;
    }
}