package com.qyhstech.core.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QyTreeParentTest {

    @Test
    public void test() {

        List<ModelTreeParentTest> allItems = new ArrayList<>(); // 数据来源
        allItems.add(ModelTreeParentTest.builder().id(10L).name("test10").parentId(4L).build());
        allItems.add(ModelTreeParentTest.builder().id(1L).name("test1").parentId(0L).build());
        allItems.add(ModelTreeParentTest.builder().id(4L).name("test4").parentId(1L).build());
        allItems.add(ModelTreeParentTest.builder().id(5L).name("test5").parentId(1L).build());
        allItems.add(ModelTreeParentTest.builder().id(6L).name("test6").parentId(1L).build());
        allItems.add(ModelTreeParentTest.builder().id(7L).name("test7").parentId(5L).build());
        allItems.add(ModelTreeParentTest.builder().id(8L).name("test8").parentId(10L).build());
        Map<Long, ModelTreeParentTest> itemMap = QyTreeReverse.buildNodeMap(allItems, false);

        Long targetId = 8L;
        String path = QyTreeReverse.buildNamePath(itemMap, targetId, ModelTreeParentTest::getName, " - ");

        System.out.println("路径为：" + path);
        // 输出示例： 电子产品 - 手机 - 安卓 - 三星

    }


}
