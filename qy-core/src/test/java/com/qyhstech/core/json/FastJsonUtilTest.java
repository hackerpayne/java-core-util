package com.qyhstech.core.json;

import com.qyhstech.core.collection.QyMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class FastJsonUtilTest {

    @Test
    public void testBeanToMap() {

        Map<String, String> mapData = QyMap.empty();
        mapData.put("title", "title");
        mapData.put("body", null);

        Map map = QyFastJson.beanToMap(mapData, false);
        System.out.println(map);
    }

}
