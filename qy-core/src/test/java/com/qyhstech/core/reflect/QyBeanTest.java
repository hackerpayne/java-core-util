package com.qyhstech.core.reflect;

import com.qyhstech.core.domain.ModelTest;
import com.qyhstech.core.domain.dto.ModelRecordUpdateInfoDto;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QyBeanTest {

    @Test
    void copyIgnoreNull() {
    }

    @Test
    void testCopyIgnoreNull() {
    }

    @Test
    void copy() {
    }

    @Test
    void mapToList() {
    }

    @Test
    void mapToEntity() {
    }

    @Test
    void compareDifference() {

        ModelTest modelTest = new ModelTest();
        modelTest.setId(1);

        ModelTest update = new ModelTest();
        update.setName("update");

        Map<String, String> mapFields = new HashMap<>();
        mapFields.put("id", "主键");
        mapFields.put("name", "名称");

        List<ModelRecordUpdateInfoDto> updateInfoDto = QyBean.compareDifference(modelTest, update, mapFields);
        System.out.println(updateInfoDto);
    }
}