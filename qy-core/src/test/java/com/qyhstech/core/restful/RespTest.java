package com.qyhstech.core.restful;

import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.enums.QyErrorCode;
import com.qyhstech.core.domain.response.QyResp;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RespTest {

    @Test
    public void testFailResult() throws Exception {
        System.out.println("失败结果");
        System.out.println(QyResp.fail(QyErrorCode.SERVER_ERROR, "TestResutl"));
    }

    @Test
    public void testFailResult1() throws Exception {
        List<ModelTest> listData = QyList.empty();
        ModelTest modelTest = new ModelTest();
        modelTest.setTitle("title");
        modelTest.setValue("hahah");
        modelTest.setContent("content");
        listData.add(modelTest);

        modelTest = new ModelTest();
        modelTest.setTitle("title2");
        modelTest.setValue("hahah2");
        modelTest.setContent("content2");
        listData.add(modelTest);

        System.out.println("失败结果2");
        //        System.out.println(Resp.fail(ResultCode.SuccessCode, "TestResutl", listData));
    }

    @Test
    public void testSuccessResult() throws Exception {
        ModelTest modelTest = new ModelTest();
        modelTest.setTitle("标题");
        modelTest.setValue("值");
        modelTest.setContent("内容");
        QyResp resp = QyResp.success(modelTest);

        System.out.println("成功结果");
        System.out.println(resp);
        System.out.println(resp.jsonString());
    }

    @Test
    public void testFailResult2() throws Exception {

        System.out.println("失败结果3");
        QyResp resp = QyResp.success("只有失败信息");
        System.out.println(resp);
        System.out.println(resp.jsonString());
    }

    @Test
    public void testJsonSuccessResult1() throws Exception {
        System.out.println("成功结果3");
        QyResp resp = QyResp.success("只有成功信息");
        System.out.println(resp);
        System.out.println(resp.jsonString());
    }

}