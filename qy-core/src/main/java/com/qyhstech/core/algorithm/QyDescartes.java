package com.qyhstech.core.algorithm;

import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.base.QyModelPair;
import com.qyhstech.core.domain.dto.ModelAttributeDetail;
import com.qyhstech.core.domain.dto.ModelAttributeList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 循环和递归两种方式实现未知维度集合的笛卡尔积
 */
public class QyDescartes {

    /**
     * 计算列表的笛卡尔积
     *
     * @param lists
     * @return
     */
    public static List<List<String>> descartesList(List<List<String>> lists) {
        List<List<String>> result = new ArrayList<>();
        descartesList(lists, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     *
     * @param lists
     * @param index
     * @param currentCombination
     * @param result
     */
    private static void descartesList(List<List<String>> lists, int index, List<String> currentCombination, List<List<String>> result) {
        if (index == lists.size()) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        List<String> currentList = lists.get(index);
        for (String item : currentList) {
            currentCombination.add(item);
            descartesList(lists, index + 1, currentCombination, result);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    /**
     * 利用字符串生成笛卡尔积的结果
     * List<String> colorList = Arrays.asList("红色", "黑色", "金色");
     * List<String> sizeList = Arrays.asList("32G", "64G");
     * List<String> placeList = Arrays.asList("国产", "进口");
     * List<String> descartesList = descartes(colorList, sizeList, placeList);
     * descartesList.forEach(System.out::println);
     *
     * @param lists
     * @return
     */
    public static List<String> descartesStr(List<String>... lists) {
        return descartesStr(",", lists);
    }

    /**
     * 笛卡尔积计算商品组合
     *
     * @param contactStr
     * @param lists
     * @return
     */
    public static List<String> descartesStr(String contactStr, List<String>... lists) {
        List<String> tempList = QyList.empty();
        for (List<String> list : lists) {
            if (tempList.isEmpty()) {
                tempList = list;
            } else {
                //java8新特性，stream流
                tempList = tempList.stream().flatMap(item -> list.stream().map(item2 -> item + contactStr + item2)).collect(Collectors.toList());
            }
        }
        return tempList;
    }

    /**
     * 生成笛卡尔积的组合，用于商品表SKU生成
     *
     * @param attrList
     * @return
     */
    public static List<ModelAttributeDetail> descartesAttr(List<ModelAttributeList> attrList) {
        return descartesAttr(attrList, ",");
    }

    /**
     * 生成笛卡尔积的组合，用于商品表SKU生成
     *
     * @param attrList 要处理的列表数据
     * @param joinStr  连接字符串
     * @return
     */
    public static List<ModelAttributeDetail> descartesAttr(List<ModelAttributeList> attrList, String joinStr) {
        List<List<QyModelPair<String>>> combinations = new ArrayList<>();
        generateCombinationsHelper(attrList, 0, new ArrayList<>(), combinations);

        List<ModelAttributeDetail> result = QyList.empty();
        combinations.forEach(item -> {
            ModelAttributeDetail modelAttributeDetail = new ModelAttributeDetail();
            //            modelAttributeDetail.setAttrKeys(item.stream().map(QyModelPair::getKey).collect(Collectors.toList()));
            modelAttributeDetail.setAttrValues(item.stream().map(QyModelPair::getValue).collect(Collectors.toList()));
            modelAttributeDetail.setAttrValueStr(item.stream().map(QyModelPair::getValue).collect(Collectors.joining(joinStr)));
            modelAttributeDetail.setAttrList(item);
            result.add(modelAttributeDetail);
        });
        return result;
    }

    /**
     * @param attrList           所有数据列表
     * @param index              当前位置
     * @param currentCombination 当前组合结果
     * @param combinations       所有组合结果
     */
    private static void generateCombinationsHelper(List<ModelAttributeList> attrList, int index, List<QyModelPair<String>> currentCombination, List<List<QyModelPair<String>>> combinations) {
        if (index == attrList.size()) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        ModelAttributeList currentAttribute = attrList.get(index);
        String attrKey = currentAttribute.getAttrKey();
        List<String> attrValues = currentAttribute.getAttrValue();

        for (String attrValue : attrValues) {
            currentCombination.add(new QyModelPair<String>(attrKey, attrValue));
            generateCombinationsHelper(attrList, index + 1, currentCombination, combinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}