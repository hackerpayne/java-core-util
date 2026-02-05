package com.qyhstech.spring.service;

import com.qyhstech.core.QyStr;

import java.util.Collection;
import java.util.Map;

/**
 * 公共的字典服务
 */
public interface DictService {

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    default String getDictLabel(String dictType, String dictValue) {
        return getDictLabel(dictType, dictValue, QyStr.COMMA);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    String getDictLabel(String dictType, String dictValue, String separator);

    /**
     * 获取字典下所有的字典值与标签
     * dictValue为key，dictLabel为值Value组成的Map
     *
     * @param dictType 字典类型
     * @return 字典结果Map
     */
    Map<String, String> getAllDictByDictType(String dictType);

    /**
     * 检查数据是否在字段中
     * checkDict("sys_gender","男")
     *
     * @param dictType  字典类型：枚举列表值
     * @param dictLabel 字典标签
     * @return
     */
    boolean checkDict(String dictType, String dictLabel);

    /**
     * 检查字典值是否都在字典中
     * checkDict("sys_gender","男")
     *
     * @param dictType  字典类型
     * @param dictLabel 字典值
     * @return
     */
    boolean checkDict(String dictType, Collection<String> dictLabel);

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    default String getDictValue(String dictType, String dictLabel) {
        return getDictValue(dictType, dictLabel, QyStr.COMMA);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    String getDictValue(String dictType, String dictLabel, String separator);

}
