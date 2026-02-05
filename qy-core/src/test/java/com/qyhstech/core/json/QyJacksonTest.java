package com.qyhstech.core.json;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qyhstech.core.collection.QyMap;
import com.qyhstech.core.domain.response.QyResp;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class QyJacksonTest {

    public static QyResp resp = QyResp.fail("faild");
    ObjectMapper objectMapper = QyJacksonFactory.getObjectMapper();

    @Test
    void getObjectMapper() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(resp));
    }

    @Test
    public void toJsonString() {
        //        resp.setCode(0);
        System.out.println(QyJackson.toJsonString(resp));
    }

    @Test
    public void toJsonStringFilter() {
        Map<String, Object> testMaps = QyMap.empty();
        testMaps.put("ok", 0L);
        testMaps.put("ok2", "te");
        System.out.println(QyJackson.toJsonString(testMaps, new String[]{"ok"}));
    }

    @Test
    void toJsonStringPretty() {
    }

    @Test
    void parseObject() {

        String json = "{\"ok\":0,\"ok2\":\"te\"}";
        JsonNode jsonNode = QyJackson.readTree(json);
        Iterator<JsonNode> voicesList = jsonNode.path("voices").elements();
        if (CollUtil.isEmpty(voicesList)) {
            while (voicesList.hasNext()) {
                JsonNode currentNode = voicesList.next();
                System.out.println(currentNode.asText());
            }
        }
    }

    @Test
    void testParseObject() {
    }

    @Test
    void string2Obj() {
    }

    @Test
    void testString2Obj() {
    }

    @Test
    void testString2Obj1() {
    }

    @Test
    void parseArray() {
    }

    @Test
    void toList() {
    }

    @Test
    void parseMap() {
    }

    @Test
    void parseMapStr() {
    }

    @Test
    void toMapBean() {
    }

    @Test
    void mapToBean() {
    }

    @Test
    void validate() {
    }


    /**
     * 多态Json测试
     */
    @Test
    void testInheritance() throws JsonProcessingException {
        List<Animal> animals = new ArrayList<>();
        Animal dog = new Dog("旺财", "金毛");
        animals.add(dog);
        animals.add(new Cat("小花", "黑白"));
        animals.add(new Fish("尼莫", "小丑鱼"));

        String simpleJson = QyJackson.toJsonString(dog);
        System.out.println("单个序列化结果：");
        System.out.println(simpleJson);

        Animal animalsResultSimple = QyJackson.parseObject(simpleJson, Animal.class);
        System.out.println("\n单个反序列化结果：");
        System.out.println(animalsResultSimple);

        // 序列化
        String json = QyJackson.toJsonListString(animals, Animal.class);
        System.out.println("多个序列化结果：");
        System.out.println(json);

        // 反序列化测试
        List<Animal> animalsResult = QyJackson.parseList(json, Animal.class);
        System.out.println("\n多个反序列化结果：");
        System.out.println(animalsResult);

        // 验证结果
        System.out.println("\n验证结果：");
        System.out.println("第一个动物类型: " + animalsResult.get(0).getClass().getSimpleName());
        System.out.println("第一个动物名称: " + animalsResult.get(0).getName());
        if (animalsResult.get(0) instanceof Dog) {
            System.out.println("第一个动物品种: " + ((Dog) animalsResult.get(0)).getBreed());
        }
    }


}