package com.qyhstech.core.io;

import cn.hutool.core.io.IoUtil;
import com.qyhstech.core.domain.constant.QyConsts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtilTest {

    public static void main(String[] args) throws IOException {

        ZipOutputStream out = QyZipUtil.getZipOutputStream(QyFile.file(QyConsts.CurrentDir, "multithreading.zip"));

        // 添加文件进来
        InputStream stream = QyFile.getInputStream(QyFile.file(QyConsts.CurrentDir, "pom.xml"));
        out.putNextEntry(new ZipEntry("pom.xml"));
        IoUtil.copy(stream, out);
        out.flush();

        // 添加字符串进来
        String input = "中文内容测试";
        InputStream content = new ByteArrayInputStream(input.getBytes());
        out.putNextEntry(new ZipEntry("haha.js"));
        IoUtil.copy(content, out);

        out.flush();

        // 关闭流
        out.closeEntry();
        out.close();
    }

}