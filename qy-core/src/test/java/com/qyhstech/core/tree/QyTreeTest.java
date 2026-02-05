package com.qyhstech.core.tree;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

class QyTreeTest {

    private List<ModelTreeTest2> initTree() {
        ModelTreeTest2 treeTest1 = ModelTreeTest2.builder().id(1).pid(5).rank(2).build();
        ModelTreeTest2 treeTest2 = ModelTreeTest2.builder().id(2).pid(4).rank(3).build();
        ModelTreeTest2 treeTest3 = ModelTreeTest2.builder().id(3).pid(4).rank(1).build();
        ModelTreeTest2 treeTest4 = ModelTreeTest2.builder().id(4).pid(0).rank(1).build();
        ModelTreeTest2 treeTest5 = ModelTreeTest2.builder().id(5).pid(0).rank(2).build();
        ModelTreeTest2 treeTest6 = ModelTreeTest2.builder().id(6).pid(0).rank(3).build();

        List<ModelTreeTest2> trees = List.of(treeTest1, treeTest2, treeTest3, treeTest4, treeTest5, treeTest6);
        return trees;
    }

    @Test
    void findTree() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> result = QyTree.findTree(trees, ModelTreeTest2::getItems, item -> item.getId() > 3);
        result.forEach(System.out::println);
    }

    @Test
    void flatTree() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> result = QyTree.flatTree(trees, ModelTreeTest2::getItems);
        result.forEach(System.out::println);
    }

    @Test
    void makeTree() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);
        System.out.println(tree);
    }

    @Test
    void forPreOrder() {

        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);

        //先序
        StringBuffer preStr = new StringBuffer();
        QyTree.forPreOrder(tree, x -> preStr.append(x.getId().toString()), ModelTreeTest2::getItems);
        System.out.println(preStr);
    }

    @Test
    void forLevelOrder() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);

        //层序
        StringBuffer levelStr = new StringBuffer();
        QyTree.forLevelOrder(tree, x -> levelStr.append(x.getId().toString()), ModelTreeTest2::getItems);
        System.out.println(levelStr);
    }

    @Test
    void forPostOrder() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);

        //后序
        StringBuffer postOrder = new StringBuffer();
        QyTree.forPostOrder(tree, x -> postOrder.append(x.getId().toString()), ModelTreeTest2::getItems);
        System.out.println(postOrder);

    }

    @Test
    void flat() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);

        List<ModelTreeTest2> flat = QyTree.flat(tree, ModelTreeTest2::getItems, x -> x.setItems(null));
        flat.forEach(System.out::println);
    }

    /**
     * 正序排列
     */
    @Test
    void sort() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);

        List<ModelTreeTest2> sortTree = QyTree.sort(tree, Comparator.comparing(ModelTreeTest2::getRank), ModelTreeTest2::getItems);
        sortTree.forEach(System.out::println);
    }

    @Test
    void sortDesc() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);

        List<ModelTreeTest2> sortTree = QyTree.sort(tree, (x, y) -> y.getRank().compareTo(x.getRank()), ModelTreeTest2::getItems);
        sortTree.forEach(System.out::println);
    }

    @Test
    void filter() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);
        List<ModelTreeTest2> filterMenus = QyTree.filter(tree, x -> x.getId() <= 4, ModelTreeTest2::getItems);
        filterMenus.forEach(System.out::println);
    }

    @Test
    void search() {
        List<ModelTreeTest2> trees = initTree();
        List<ModelTreeTest2> tree = QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setItems);
        List<ModelTreeTest2> searchRes = QyTree.search(tree, x -> Integer.valueOf(1).equals(x.getId()), ModelTreeTest2::getItems);
        searchRes.forEach(System.out::println);
    }
}