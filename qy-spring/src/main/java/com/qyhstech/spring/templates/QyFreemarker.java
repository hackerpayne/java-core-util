package com.qyhstech.spring.templates;

import cn.hutool.core.io.IoUtil;
import com.qyhstech.core.spring.QySpringContext;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.Map;

/**
 * Freemarker工具类
 * <p>
 * usage：
 * String templateFile = "src/test/resources/template/ftl/foo.ftl";
 * String outFile = "src/test/resources/output/foo.html";
 * Map<String, Object> rootMap = new HashMap<String, Object>();
 * rootMap.put("id", 3306);
 * rootMap.put("name", "Sinamber");
 * <p>
 * Writer out = new FileWriter(new File(outFile));
 * FreemarkerUtil.flushData(templateFile, out, rootMap);
 * <p>
 * *************************************************************************
 * foo.ftl --> Hello ${name} , your Id is ${id}
 * foo.html will out put --> Hello Sinamber , your Id is 3306
 */
@UtilityClass
@Slf4j
public class QyFreemarker {

    private static Configuration CONFIGURATION;

    static {
        try {
            // 优先从Spring容器获取
            CONFIGURATION = QySpringContext.getBean(Configuration.class);
        } catch (Exception e) {
            // Spring容器不存在时初始化默认配置
            CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);
            CONFIGURATION.setDefaultEncoding("UTF-8");
//            CONFIGURATION.setDirectoryForTemplateLoading(new File("classpath:/templates/"));
        }
    }

    public void resetConfiguration(Configuration newConfig) {
        CONFIGURATION = newConfig; // 需移除 final 修饰符
    }

    public Configuration getConfig() {
        return CONFIGURATION;
    }

    /**
     * 加载指定类下面的模板
     *
     * @param resourceLoaderClass
     */
    public void loadTemplate(Class<?> resourceLoaderClass) {
        //这里比较重要，用来指定加载模板所在的路径
        CONFIGURATION.setTemplateLoader(new ClassTemplateLoader(resourceLoaderClass, "/templates"));
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    /**
     * 从目录中加载模板
     *
     * @param templateDir
     * @throws IOException
     */
    public void loadTemplate(File templateDir) throws IOException {
        //这里比较重要，用来指定加载模板所在的路径
        CONFIGURATION.setDirectoryForTemplateLoading(templateDir);
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    /**
     * 获取模板
     *
     * @param templateName
     * @return
     * @throws IOException
     */
    public Template getTemplate(String templateName) throws IOException {
        try {
            return CONFIGURATION.getTemplate(templateName);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 清除模板缓存
     */
    public void clearCache() {
        CONFIGURATION.clearTemplateCache();
    }

    /**
     * 根据模板渲染数据
     *
     * @param templateFile freemarker模板文件路径
     * @param rootMap      值栈对象
     */
    public String flushTemplateToStr(String templateFile, Map<String, Object> rootMap) {
        StringWriter stringWriter = flushTemplateTo(CONFIGURATION, templateFile, rootMap);
        return stringWriter.toString();
    }

    /**
     * 按模板和参数生成html字符串,再转换为flying-saucer识别的Document
     *
     * @param configurer   Freemarker配置
     * @param templateName 模板文件名
     * @param dataModel    变量列表
     * @return
     */
    public String flushTemplateToStr(FreeMarkerConfigurer configurer, String templateName, Map<String, Object> dataModel) {
        StringWriter stringWriter = flushTemplateTo(configurer, templateName, dataModel);
        return stringWriter.toString();
    }

    /**
     * 按模板和参数生成html字符串,再转换为flying-saucer识别的Document
     *
     * @param configurer   Freemarker配置
     * @param templateName 模板文件名
     * @param dataModel    变量列表
     * @return
     */
    public InputStream flushTemplateToStream(FreeMarkerConfigurer configurer, String templateName, Map<String, Object> dataModel) {
        StringWriter stringWriter = flushTemplateTo(configurer, templateName, dataModel);
        return new ByteArrayInputStream(stringWriter.toString().getBytes());
    }

    /**
     * 按模板和参数生成html字符串,再转换为flying-saucer识别的Document
     *
     * @param configurer   Freemarker配置
     * @param templateName 模板文件名
     * @param dataModel    变量列表
     * @return
     */
    public StringWriter flushTemplateTo(FreeMarkerConfigurer configurer, String templateName, Map<String, Object> dataModel) {
        return flushTemplateTo(configurer.getConfiguration(), templateName, dataModel);
    }

    /**
     * 使用指定配置刷模板
     *
     * @param cfg
     * @param templateName
     * @param dataModel
     * @return
     */
    public StringWriter flushTemplateTo(Configuration cfg, String templateName, Map<String, Object> dataModel) {
        StringWriter stringWriter = new StringWriter();
        flushTemplateTo(cfg, templateName, dataModel, stringWriter);
        return stringWriter;
    }

    /**
     * 使用默认配置刷模板变量
     *
     * @param templateName
     * @param dataModel
     * @param writer
     */
    public void flushTemplateTo(String templateName, Map<String, Object> dataModel, Writer writer) {
        flushTemplateTo(CONFIGURATION, templateName, dataModel, writer);
    }

    /**
     * 刷模板
     *
     * @param cfg          配置文件
     * @param templateName 模板名称
     * @param dataModel    数据
     * @param writer       写出目标
     */
    public void flushTemplateTo(Configuration cfg, String templateName, Map<String, Object> dataModel, Writer writer) {
        Template tp;
        try {
            tp = cfg.getTemplate(templateName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return;
        }

        try {
            tp.process(dataModel, writer);
            writer.flush();
        } catch (TemplateException e) {
            log.error("模板不存在或者路径错误", e);
        } catch (IOException e) {
            log.error("IO异常", e);
        }

    }

    /**
     * 根据字符串模板渲染数据
     *
     * @param content
     * @param rootMap
     * @throws IOException
     * @throws TemplateException
     */
    public String flushString(String content, Map<String, Object> rootMap) throws IOException, TemplateException {
        StringWriter out = new StringWriter();
        flushStringTo(content, out, rootMap);
        return out.toString();
    }

    /**
     *
     * @param configurer
     * @param content
     * @param rootMap
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String flushString(FreeMarkerConfigurer configurer, String content, Map<String, Object> rootMap) throws IOException, TemplateException {
        StringWriter out = new StringWriter();
        flushStringTo(configurer.getConfiguration(), content, out, rootMap);
        return out.toString();
    }

    /**
     * 根据字符串模板渲染数据
     *
     * @param inputStr 字符串模板内容
     * @param out      输出器
     * @param rootMap  数据填充列表
     * @throws IOException
     * @throws TemplateException
     */
    public void flushStringTo(String inputStr, Writer out, Map<String, Object> rootMap) throws IOException,
            TemplateException {
        flushStringTo(CONFIGURATION, inputStr, out, rootMap);
    }

    /**
     *
     * @param configurer
     * @param inputStr
     * @param out
     * @param rootMap
     * @throws IOException
     * @throws TemplateException
     */
    public void flushStringTo(FreeMarkerConfigurer configurer, String inputStr, Writer out, Map<String, Object> rootMap) throws IOException,
            TemplateException {
        flushStringTo(configurer.getConfiguration(), inputStr, out, rootMap);
    }

    /**
     *
     * @param cfg
     * @param inputStr
     * @param out
     * @param rootMap
     * @throws IOException
     * @throws TemplateException
     */
    public void flushStringTo(Configuration cfg, String inputStr, Writer out, Map<String, Object> rootMap) throws IOException, TemplateException {
        String templateName = "_innerTemplate";
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(templateName, inputStr);
        cfg.setTemplateLoader(stringLoader);
        Template temp = cfg.getTemplate(templateName, "utf-8");
        temp.process(rootMap, out);
        out.flush();
    }


    /**
     * 获取模板渲染后的内容
     * 需要在Map里面设置templateName模板名称
     *
     * @param content   模板内容
     * @param dataModel 数据模型
     */
    public String getContent(String content, Map<String, Object> dataModel) {
        if (dataModel.isEmpty()) {
            return content;
        }

        StringReader reader = new StringReader(content);
        StringWriter sw = new StringWriter();
        try {
            // 渲染模板
            String templateName = dataModel.get("templateName").toString();
            Template template = new Template(templateName, reader, null, "utf-8");
            template.process(dataModel, sw);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        content = sw.toString();

        IoUtil.close(reader);
        IoUtil.close(sw);

        return content;
    }

    /**
     * 用于生成静态页面
     *
     * @param templateName 模板名称
     * @param outPutPath   导出文件的根目录
     * @param htmlPageName 导出Html文件名称
     * @param dataMap      数据变量
     */
    public void flushHtml(String templateName, String outPutPath, String htmlPageName, Map<String, Object> dataMap) {
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是FreeMarker对于的版本号。
        Writer out = null;
        try {
            // 加载模版文件
            Template template = CONFIGURATION.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outPutPath + "/" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);

            //            InputStream inputStream = IOUtils.toInputStream(content);
            //            //输出文件
            //            FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/test/test2.html"));
            //            IOUtils.copy(inputStream, fileOutputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
