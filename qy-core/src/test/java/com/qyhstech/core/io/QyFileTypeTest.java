package com.qyhstech.core.io;

import com.qyhstech.core.collection.QyList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class QyFileTypeTest {

    public List<String> init() {
        List<String> fileList = QyList.empty();
        fileList.add("/Users/kyle/临时用用/BLKPAY_20231010.xlsx");
        fileList.add("/Users/kyle/临时用用/AX600固件/MIWIFIRepairTool.x86.zip");
        fileList.add("/Users/kyle/临时用用/test.xls");
        fileList.add("/Users/kyle/Downloads/淘宝运营策略.png");
        fileList.add("/Users/kyle/Downloads/淘宝运营策略_副本.jpeg");
        fileList.add("/Users/kyle/Downloads/淘宝运营策略_副本.jpg");
        fileList.add("/Users/kyle/Downloads/test.json");
        fileList.add("/Users/kyle/Downloads/test.bat");
        return fileList;
    }

    @Test
    public void testcheckFileType() {
        List<String> testFiles = init();
        testFiles.forEach(filePath -> {
            System.out.println(filePath);
            System.out.println(QyFileType.checkFileType(filePath));
        });
    }

    @Test
    public void testGetFileType() {
        List<String> testFiles = init();
        testFiles.forEach(filePath -> {
            System.out.println(filePath);
            System.out.println(QyFileType.getFileType(filePath));
        });
    }

}