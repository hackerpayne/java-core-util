package com.qyhstech.core.validation;

import com.qyhstech.core.collection.QyMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class QyValidTest {

    @Test
    public void jwtTest() {

//        JwtUserInfo userInfo = new JwtUserInfo();
        //        userInfo.setUserId("sfadf");
        //        userInfo.setSubject("subject");
        //
        //        ValidResult validResult = QyValid.validateBean(userInfo);
        //        if (validResult.hasErrors()) {
        //            String errors = validResult.getErrors();
        //            System.out.println(errors);
        //        }
    }

    @Test
    void getHibernateFastValidator() {
    }

    @Test
    void validateParams() {
    }

    @Test
    void testValidateParams() {
    }

    @Test
    void validateBean() {
    }

    @Test
    void validateProperty() {
    }

    @Test
    void validate() {
    }

    @Test
    void getMessage() {
        Map<String, Object> data = QyMap.empty();
        QyValid.validate(data);
    }

    @Test
    void testGetMessage() {
    }
}