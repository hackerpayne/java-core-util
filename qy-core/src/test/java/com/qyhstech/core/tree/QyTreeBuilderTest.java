package com.qyhstech.core.tree;

import com.qyhstech.core.json.QyJackson;
import com.qyhstech.core.domain.base.QyModelNameValue;
import com.qyhstech.core.domain.base.QyOptionVo;
import org.junit.jupiter.api.Test;

import java.util.List;

class QyTreeBuilderTest {

    public List<ModelTreeTest> initData() {
        ModelTreeTest parent1 = ModelTreeTest.builder().id(1L).parentId(0L).name("测试父级").title("测试父级").build();
        ModelTreeTest child1 = ModelTreeTest.builder().id(2L).parentId(1L).name("测试2").title("测试2").build();
        ModelTreeTest child2 = ModelTreeTest.builder().id(3L).parentId(1L).name("测试3").title("测试3").build();
        ModelTreeTest child3 = ModelTreeTest.builder().id(4L).parentId(0L).name("测试4").title("测试4").build();
        ModelTreeTest child4 = ModelTreeTest.builder().id(5L).parentId(3L).name("测试5").title("测试5").build();
        ModelTreeTest child6 = ModelTreeTest.builder().id(6L).parentId(4L).name("测试6").title("测试6").build();

        List<ModelTreeTest> listDatas = List.of(parent1, child2, child1, child3, child4, child6);
        return listDatas;
    }

    @Test
    void buildKvTree() {


    }

    @Test
    void testBuildKvTree() {
        List<ModelTreeTest> treeLists = QyTreeBuilder.buildTree(initData());
        List<QyModelNameValue> options = QyTreeBuilder.buildKvTree(treeLists, ModelTreeTest::getName, ModelTreeTest::getId, null);
        System.out.println(QyJackson.toJsonStringPretty(options));
    }

    @Test
    void buildOptionTree() {
    }

    @Test
    void testBuildOptionTree() {

        List<ModelTreeTest> treeLists = QyTreeBuilder.buildTree(initData());
        List<QyOptionVo> options = QyTreeBuilder.buildOptionTree(treeLists, ModelTreeTest::getId, ModelTreeTest::getName);
        System.out.println(QyJackson.toJsonStringPretty(options));

    }

    @Test
    void buildTree() {
        List<ModelTreeTest> listTree = QyTreeBuilder.buildTree(initData());
        System.out.println(QyJackson.toJsonStringPretty(listTree));
    }

    @Test
    void testBuildTree() {
    }

    @Test
    void buildTreeWithFill() {
    }

    @Test
    void testBuildTree1() {
    }

    @Test
    void testBuildTree2() {
    }

    @Test
    void testBuildTree3() {
    }

    @Test
    void testBuildTree4() {
    }

    @Test
    void buildTreeNew() {
    }

    @Test
    void testBuildTreeNew() {
    }
}