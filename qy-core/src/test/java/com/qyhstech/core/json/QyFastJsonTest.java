package com.qyhstech.core.json;

import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.collection.QyMap;
import com.qyhstech.core.domain.base.QyModelNameValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class QyFastJsonTest {

    @Test
    void beanToMap() {
    }

    @Test
    void testBeanToMap() {
    }

    @Test
    void getJsonValue() {
    }

    @Test
    void getJsonObj() {
    }

    @Test
    void jsonToMap() {
    }

    @Test
    void jsonToListMap() {
    }

    @Test
    void jsonToArray() {
    }

    @Test
    void testJsonToArray() {
    }

    @Test
    void objectToJson() {
    }

    @Test
    void jsonToObject() {
    }

    @Test
    void testJsonToObject() {
    }

    @Test
    void jsonToEntity() {
    }

    @Test
    void toJsonString() {
    }

    @Test
    void toJsonString2() {
        Map<String, String> testMaps = QyMap.empty();
        testMaps.put("ok", "test");
        testMaps.put("ok2", "te");
        System.out.println(QyFastJson.toJsonString(testMaps, "ok"));
        System.out.println(QyFastJson.toJsonString(testMaps));
    }

    @Test
    void getByJsonPath() {
    }

    @Test
    void getJsonPath() {
    }

    @Test
    void testGetJsonPath() {
    }

    @Test
    void mapListToEntity() {
        List<Map<String, Object>> listMaps = QyList.empty();
        Map<String, Object> mapItem = QyMap.empty();
        mapItem.put("name", "testName");
        mapItem.put("value", "testValue");
        listMaps.add(mapItem);

        Map<String, Object> mapItem2 = QyMap.empty();
        mapItem2.put("name", "testName2");
        mapItem2.put("value", "testValue2");
        mapItem2.put("data", "testValue2");
        listMaps.add(mapItem2);

        Map<String, Object> mapItem3 = QyMap.empty();
        mapItem3.put("name", "testName3");
        mapItem3.put("value", "testValue3");
        mapItem3.put("data", "testValue3");
        mapItem3.put("test", "testValue3");
        listMaps.add(mapItem3);

        List<QyModelNameValue> listEntitys = QyFastJson.mapListToEntity(listMaps, QyModelNameValue.class);
        System.out.println(listEntitys);
    }
}