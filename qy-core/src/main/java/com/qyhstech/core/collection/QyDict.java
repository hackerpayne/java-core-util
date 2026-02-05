package com.qyhstech.core.collection;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.qyhstech.core.domain.base.QyModelNameValue;
import com.qyhstech.core.domain.base.QyOptionVo;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class QyDict {

    /**
     * 自带修改数值
     *
     * @param dataList
     * @param nameResolver
     * @param valueResolver
     * @param <T>
     * @return
     */
    public static <T> List<QyModelNameValue> buildDict(List<T> dataList, Function<T, String> nameResolver, Function<T, ?> valueResolver) {
        return buildDict(dataList, nameResolver, valueResolver, true);
    }

    /**
     * 生成KV字典，自动把Value转换为数字
     *
     * @param dataList
     * @param nameResolver
     * @param fixNumber    是否修复数字，如果遇到数字自动修复
     * @param <T>
     * @return
     */
    public static <T> List<QyModelNameValue> buildDict(List<T> dataList, Function<T, String> nameResolver, Function<T, ?> valueResolver, boolean fixNumber) {
        List<QyModelNameValue> listKv = QyList.empty();
        dataList.forEach(item -> {
            Object value = valueResolver.apply(item);
            if (fixNumber && Objects.nonNull(value) && value instanceof String && NumberUtil.isInteger(value.toString())) {
                listKv.add(new QyModelNameValue(nameResolver.apply(item), Convert.toInt(value)));
            } else {
                listKv.add(new QyModelNameValue(nameResolver.apply(item), value));
            }
        });
        return listKv;
    }

    /**
     * 实体列表转换为KV格式
     *
     * @param dataList      数据列表
     * @param nameResolver  Name解析器
     * @param valueResolver Value解析器
     * @param dataResolver  Data数据解析器
     * @param <T>
     * @return
     */
    public static <T> List<QyModelNameValue> buildDict(List<T> dataList, Function<T, String> nameResolver, Function<T, ?> valueResolver, Function<T, ?> dataResolver) {
        List<QyModelNameValue> listKv = QyList.empty();
        dataList.forEach(item -> listKv.add(new QyModelNameValue(nameResolver.apply(item), valueResolver.apply(item), dataResolver.apply(item))));
        return listKv;
    }

    /**
     * 生成选择下拉列表
     *
     * @param dataList     要生成的数据列表
     * @param idResolver   ID字段解析器
     * @param nameResolver Name名称字段解析器
     * @param <T>
     * @return
     */
    public static <T> List<QyOptionVo> buildOption(List<T> dataList, Function<T, ?> idResolver, Function<T, String> nameResolver) {
        return buildOption(dataList, idResolver, nameResolver, null);
    }

    /**
     * 生成选择下拉列表，带附加数据
     *
     * @param dataList     要生成的数据列表
     * @param idResolver   ID字段解析器
     * @param nameResolver Name名称字段解析器
     * @param dataResolver Value字段解析器
     * @param <T>
     * @return
     */
    public static <T> List<QyOptionVo> buildOption(List<T> dataList, Function<T, ?> idResolver, Function<T, String> nameResolver, Function<T, ?> dataResolver) {
        List<QyOptionVo> listKv = QyList.empty();
        if (Objects.nonNull(dataResolver)) {
            dataList.forEach(item -> listKv.add(new QyOptionVo(idResolver.apply(item), nameResolver.apply(item), dataResolver.apply(item))));
        } else {
            dataList.forEach(item -> listKv.add(new QyOptionVo(idResolver.apply(item), nameResolver.apply(item))));
        }
        return listKv;
    }


}
