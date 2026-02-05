package com.qyhstech.core.collection;

import com.qyhstech.core.domain.dto.ModelArticle;
import com.qyhstech.core.number.QyNum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class QyListTest {


    private static List<ModelArticle> articles = new ArrayList<>();

    @BeforeAll
    public static void articles() {
        articles.clear();
        articles.add(ModelArticle.builder().artid(1).title("111").body("test").build());
        articles.add(ModelArticle.builder().artid(2).title("222").build());
        articles.add(ModelArticle.builder().artid(3).title("333").build());
        articles.add(ModelArticle.builder().artid(4).title("444").build());
        articles.add(ModelArticle.builder().artid(5).title("111").build());
    }

    @Test
    void buildList() {
        List<Integer> list = QyList.buildList(articles, ModelArticle::getArtid);
        System.out.println(list);
    }

    @Test
    void buildList2() {
        List<Integer> list = QyList.buildList(articles, ModelArticle::getArtid, item -> item.getArtid() > 3);
        System.out.println(list);
    }

    @Test
    void distinctByKey() {
    }

    @Test
    void flatList() {
    }

    @Test
    void testDistinctByKey() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void isNotEmpty() {
    }

    @Test
    void sum() {
    }

    @Test
    void testSum() {
        List<ModelArticle> list = articles;
        Integer totalSum = QyNum.sum(list, ModelArticle::getArtid, Integer.class);
        System.out.println(totalSum);
    }

    @Test
    void join() {
    }

    @Test
    void testJoin() {
    }

    @Test
    void filter() {
    }

    @Test
    void sort() {
    }

    @Test
    void testBuildList() {
    }

    @Test
    void testBuildList1() {
    }

    @Test
    void buildListDistinct() {
    }

    @Test
    void testBuildListDistinct() {
    }

    @Test
    void testDistinctByKey1() {
    }

    @Test
    void testFlatList() {
    }

    @Test
    void testDistinctByKey2() {
    }

    @Test
    void testIsEmpty() {
    }

    @Test
    void testIsNotEmpty() {
    }

    @Test
    void getFirstOrNull() {
        var list = articles;
        System.out.println(QyList.getFirstOrNull(list));
    }

    @Test
    void getFirstOr() {
        List<ModelArticle> list = new ArrayList<>();
        ModelArticle defaultArticle = new ModelArticle();
        defaultArticle.setArtid(1);
        System.out.println(QyList.getFirstOr(list, defaultArticle));
    }

    @Test
    void testJoin1() {
        String join = QyList.join(articles, ModelArticle::getTitle, ",");
        System.out.println(join);
    }

    @Test
    void testJoin2() {
    }

    @Test
    void testFilter() {
    }

    @Test
    void findFirst() {
    }

    @Test
    void findAny() {
    }

    @Test
    void testSort() {
    }

    @Test
    void testBuildList2() {
    }

    @Test
    void testBuildList3() {
    }

    @Test
    void testBuildListDistinct1() {
    }

    @Test
    void testBuildListDistinct2() {
    }

    @Test
    void testDistinctByKey3() {
    }

    @Test
    void testFlatList1() {
    }

    @Test
    void testDistinctByKey4() {
    }

    @Test
    void testIsEmpty1() {
    }

    @Test
    void testIsNotEmpty1() {
    }

    @Test
    void containsStr() {
        System.out.println(QyList.containsStr(List.of("abc,", "bcd", "def"), "de"));
        System.out.println(QyList.containsStr(List.of("abc,", "bcd", "def"), "da"));
    }

    @Test
    void containsList() {
        System.out.println(QyList.containsList(List.of("abc,", "bcd", "def"), "def"));
        System.out.println(QyList.containsList(List.of("abc,", "bcd", "defg"), "def"));
    }
}