package com.qyhstech.core.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class MapUtilTest {

    @Test
    public void testMapToString() {

        Map<String, Object> mapPara = QyMap.empty();
        mapPara.put("sid", 14);//固定值
        mapPara.put("offset", 0);//偏移量
        mapPara.put("pageSize", "asdfasdfsd");//返回数量

//        String value = QyParam.mapToQueryString(mapPara);
//        log.info(value);
    }
}
