package com.qyhstech.core.algorithm;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.dto.ModelAttributeList;
import com.qyhstech.core.json.QyJackson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@Slf4j
class QyDescartesTest {

    @Test
    public void testDecartes() {
        List<String> colorList = Arrays.asList("红色", "黑色", "金色");
        List<String> sizeList = Arrays.asList("32G", "64G");
        List<String> placeList = Arrays.asList("国产", "进口");
        List<String> descartesList = QyDescartes.descartesStr(colorList, sizeList, placeList);
        log.info("生成结果数量为：{}", descartesList.size());
        descartesList.forEach(System.out::println);
    }

    @Test
    public void testDecartes2() {
        List<ModelAttributeList> attributes = QyList.empty();
        attributes.add(new ModelAttributeList("颜色", Arrays.asList("红色", "黑色", "金色")));
        attributes.add(new ModelAttributeList("", Arrays.asList("32G", "64G")));
        attributes.add(new ModelAttributeList("", Arrays.asList("国产", "进口")));
        attributes.add(new ModelAttributeList("", Arrays.asList("红色", "黑色", "金色")));
        //        var descartesList = QyDescartes.cartesianProduct(attributes);
        //        log.info("生成结果数量为：{}", descartesList.size());
        //        descartesList.forEach(System.out::println);
    }

    @Test
    public void testDecartes3() {
        List<ModelAttributeList> attributes = QyList.empty();
        attributes.add(new ModelAttributeList("颜色", Arrays.asList("红色", "黑色", "金色")));
        attributes.add(new ModelAttributeList("大小", Arrays.asList("32G", "64G")));
        attributes.add(new ModelAttributeList("标准", Arrays.asList("国产", "进口")));
        attributes.add(new ModelAttributeList("版本", Arrays.asList("标准", "专业", "旗舰")));
        var descartesList = QyDescartes.descartesAttr(attributes);
        log.info("生成结果数量为：{}", descartesList.size());
        descartesList.forEach(System.out::println);
        System.out.println(QyJackson.toJsonString(descartesList));
    }

    @Test
    public void test4() {
        List<List<String>> dimValue = QyList.empty();
//        dimValue.add(Splitter.on("|").splitToList("电影|电视剧"));
        dimValue.add(StrUtil.split("电影|电视剧", "|"));
        dimValue.add(StrUtil.split("剧情|爱情|喜剧", "|"));
        dimValue.add(StrUtil.split("大陆|美国", "|"));
        dimValue.add(StrUtil.split("经典|冷门佳片|魔幻|黑帮|女性", "|"));

        // 递归实现笛卡尔积
        List<List<String>> recursiveResult = QyDescartes.descartesList(dimValue);
        System.out.println("递归实现笛卡尔乘积: 共 " + recursiveResult.size() + " 个结果");
        for (List<String> list : recursiveResult) {
            for (String string : list) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void test5() {

        List<List<String>> dimValue = QyList.empty();
        dimValue.add(List.of("红色", "黑色", "金色"));
        dimValue.add(List.of("32G", "64G"));
        dimValue.add(List.of("国产", "进口"));

        List<List<String>> recursiveResult = QyDescartes.descartesList(dimValue);
        System.out.println("递归实现笛卡尔乘积: 共 " + recursiveResult.size() + " 个结果");
        for (List<String> list : recursiveResult) {
            for (String string : list) {
                System.out.print(string + " ");
            }
            System.out.println();
        }

    }

}