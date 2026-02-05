package com.qyhstech.spring.templates;

import cn.hutool.core.io.FileUtil;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QyFreemarkerTest {

    @Test
    void loadTemplate() throws IOException {
        QyFreemarker.loadTemplate(QyFreemarker.class);
        var template = QyFreemarker.getTemplate("index.html");
        System.out.println(template);
    }

    @Test
    void getTemplate() {
    }

    @Test
    void clearCache() {
    }

    @Test
    void flushTemplate() throws IOException {
        String templateFile = "/Users/kyle/CodeProject/YiHengWine/ScYiHeng-Java/ruoyi-admin/src/main/resources/templates/out.ftl";
        String outFile = "/Users/kyle/CodeProject/YiHengWine/ScYiHeng-Java/ruoyi-admin/src/main/resources/templates/out2.html";

        QyFreemarker.loadTemplate(FileUtil.file(templateFile).getParentFile());

        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("invoice", "Sinamber");
        rootMap.put("itemList", List.of("a", "b"));

        String result = QyFreemarker.flushTemplateToStr("out.ftl", rootMap);
        System.out.println(result);
    }

    @Test
    void testFlushTemplate() throws TemplateException, IOException {
        String StringTemplate = "${v1}>${f1}";
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("v1", 2);
        rootMap.put("f1", 3);
        rootMap.put("f3", 3);
        String result = QyFreemarker.flushString(StringTemplate, rootMap);
        System.out.println(result);
    }

    @Test
    void testFlushTemplate1() throws IOException, TemplateException {

        String templateFile = "/Users/kyle/CodeProject/YiHengWine/ScYiHeng-Java/ruoyi-admin/src/main/resources/templates/out.ftl";
        String outFile = "/Users/kyle/CodeProject/YiHengWine/ScYiHeng-Java/ruoyi-admin/src/main/resources/templates/out2.html";

        QyFreemarker.loadTemplate(FileUtil.file(templateFile).getParentFile());

        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("invoice", "Sinamber");
        rootMap.put("itemList", List.of("a", "b"));

        Writer out = new FileWriter(new File(outFile));
        QyFreemarker.flushTemplateTo("out.ftl", rootMap, out);
    }

    @Test
    void flushStringTo() throws TemplateException, IOException {
        String StringTemplate = "${v1}>${f1}";
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("v1", 2);
        rootMap.put("f1", 3);
        rootMap.put("f3", 3);
        String result = QyFreemarker.flushString(StringTemplate, rootMap);
        System.out.println(result);
    }

    @Test
    void testFlushStringTo() throws TemplateException, IOException {
        String StringTemplate = "${v1}>${f1}";
        StringWriter out = new StringWriter();
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("v1", 2);
        rootMap.put("f1", 3);
        rootMap.put("f3", 3);
        QyFreemarker.flushStringTo(StringTemplate, out, rootMap);
        System.out.println(out);
    }

    @Test
    void getContent() {
    }

    @Test
    void flushHtml() {
    }

    @Test
    void testLoadTemplate() {
    }

    @Test
    void testGetTemplate() {
    }

    @Test
    void testClearCache() {
    }

    @Test
    void testFlushTemplate2() {
    }

    @Test
    void testFlushTemplate3() {
    }

    @Test
    void testFlushTemplate4() {
    }

    @Test
    void testFlushStringTo1() {
    }

    @Test
    void testFlushStringTo2() {
    }

    @Test
    void testGetContent() {
    }

    @Test
    void testFlushHtml() {
    }
}