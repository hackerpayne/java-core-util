package com.qyhstech.core.restful;

import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.enums.QyErrorCode;
import com.qyhstech.core.domain.response.QyResp;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RespPageTest {

    @Test
    public void testFailResult() throws Exception {
        //System.out.println("失败结果");
        System.out.println(QyResp.fail(QyErrorCode.SUCCESS.getCode(), "TestResutl"));
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
        //        System.out.println(RespPage.fail(ResultCode.SuccessCode, "TestResutl", listData));
    }

    @Test
    public void testSuccessResult() throws Exception {
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

        QyResp respPage = QyResp.successPage(100, 10, 1, 102, listData);

        System.out.println("成功结果");
        System.out.println(respPage);
        System.out.println(respPage.jsonString());
    }

    @Test
    public void testSuccessResultPage() throws Exception {
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

        //        Page page = new Page();
        //        page.setPageNumber(1);
        //        page.setPageSize(100);
        //        page.setTotalPage(1000);
        //        page.setTotalRow(1000000);
        //
        //        page.setResult(listData);
        //
        //        System.out.println("Page成功结果");
        //        System.out.println(RespPage.fail(page));
    }

}