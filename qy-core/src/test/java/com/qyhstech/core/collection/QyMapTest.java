package com.qyhstech.core.collection;

import com.qyhstech.core.domain.dto.ModelArticle;
import com.qyhstech.core.domain.dto.ModelProxy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QyMapTest {

    private List<ModelArticle> init() {
        List<ModelArticle> lists = QyList.empty();
        lists.add(ModelArticle.builder().artid(1).title("111").body("test").build());
        lists.add(ModelArticle.builder().artid(2).title("222").build());
        lists.add(ModelArticle.builder().artid(3).title("333").build());
        lists.add(ModelArticle.builder().artid(4).title("444").build());
        lists.add(ModelArticle.builder().artid(5).title("111").build());
        return lists;
    }

    @Test
    public void testBuildMap() {
        List<ModelArticle> lists = init();

        Map<String, ModelArticle> map = QyMap.buildMap(lists, ModelArticle::getTitle);
        System.out.println(map);
    }

    @Test
    public void testBuildMap2() {
        List<ModelArticle> lists = init();

        Map<String, Integer> map = QyMap.buildMap(lists, ModelArticle::getTitle, ModelArticle::getArtid);
        System.out.println(map);
    }

    @Test
    void toGroupMap() {
        List<ModelArticle> lists = init();
        Map<String, List<ModelArticle>> groupMap = QyMap.buildGroupMapFilter(lists, ModelArticle::getTitle, item -> !item.getTitle().startsWith("111"));
        System.out.println(groupMap);
    }

    @Test
    void buildMap() {
    }

    @Test
    void testBuildMap1() {
    }

    @Test
    void testBuildMap3() {
    }

    @Test
    void testBuildMap4() {
    }

    @Test
    void buildGroupMap() {
        List<ModelArticle> lists = init();
        Map<String, List<ModelArticle>> groupMap = QyMap.buildGroupMap(lists, ModelArticle::getTitle);
        System.out.println(groupMap);
    }

    @Test
    void buildGroupMapFilter() {
    }

    @Test
    void testBuildGroupMap() {

        ModelProxy proxy = new ModelProxy();
        proxy.setHost("localhost");
        proxy.setUsername("user1");

        ModelProxy proxy2 = new ModelProxy();
        proxy2.setHost("localhost");
        proxy2.setUsername("user2");

        ModelProxy proxy3 = new ModelProxy();
        proxy3.setHost("host3");
        proxy3.setUsername(null);

        System.out.println(QyMap.buildGroupMap(List.of(proxy, proxy2, proxy3), ModelProxy::getHost, ModelProxy::getUsername));
    }

    @Test
    void buildGroupDoubleMap() {
    }

    @Test
    void buildGroupInnerMap() {
    }

    @Test
    void initMapList() {
    }

    @Test
    void getSignStr() {
    }

    @Test
    void testGetSignStr() {
    }

    @Test
    void filterMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("test", "testValue");
        map.put("test2", "testValue2");

        Map<String, Object> result = QyMap.filterMap(map, entry -> entry.getKey().equals("test"));
        System.out.println(result);
    }
}