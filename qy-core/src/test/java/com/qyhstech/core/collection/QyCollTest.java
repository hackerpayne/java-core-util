package com.qyhstech.core.collection;

import com.qyhstech.core.domain.ModelTest;
import org.junit.jupiter.api.Test;

import java.util.List;

class QyCollTest {

    @Test
    void initArrayList() {
    }

    @Test
    void removeItems() {
    }

    @Test
    void averageAssign() {
    }

    @Test
    void toArray() {
    }

    @Test
    void listSkip() {
    }

    @Test
    void getRandomIndex() {
    }

    @Test
    void getRandomItem() {
    }

    @Test
    void addStr() {
    }

    @Test
    void testAddStr() {
    }

    private String processItem(ModelTest item) {
        return "OK";
    }

    @Test
    void listProcessor() {
        List<ModelTest> modelList = QyList.empty();
        modelList.add(new ModelTest(1, ""));
        modelList.add(new ModelTest(2, ""));
        modelList.add(new ModelTest(3, ""));
        QyColl.listProcessor(modelList, this::processItem, ModelTest::setName);
        System.out.println(modelList);
    }

    @Test
    void testListProcessor() {
    }

    @Test
    void testListProcessor1() {
    }

    @Test
    void testListProcessor2() {
    }
}