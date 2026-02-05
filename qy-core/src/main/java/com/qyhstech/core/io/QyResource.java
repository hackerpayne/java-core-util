package com.qyhstech.core.io;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.domain.constant.QyConsts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Spring资源文件读取类
 */
@Slf4j
public class QyResource {

    private final static String staticSuffix = ".css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.htm,.html,.crx,.xpi,.exe,.ipa,.apk,.woff2,.ico,.swf,.ttf,.otf,.svg,.woff";
    /**
     * 静态文件后缀
     */
    @Getter
    private final static String[] staticFiles = StrUtil.splitToArray(staticSuffix, ",");

    /**
     * 动态映射URL后缀
     */
    private final static String urlSuffix = ".html";
    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    /**
     * 判断访问URI是否是静态文件请求
     *
     * @throws Exception
     */
    public static boolean isStaticFile(String uri) {
        return StrUtil.endWithAny(uri, staticFiles) && !StrUtil.endWithAny(uri, new String[]{urlSuffix})
                && !StrUtil.endWithAny(uri, new String[]{".jsp"}) && !StrUtil.endWithAny(uri, new String[]{".java"});
    }

    /**
     * 从路径下解析多个资源，仿MybatisPlus
     * 参考：@spring-boot-starter/mybatis-plus-spring-boot-autoconfigure/src/main/java/com/baomidou/mybatisplus/autoconfigure/MybatisPlusProperties.java
     *
     * @param searchLocations 搜索路径
     * @return
     */
    public static Resource[] resolveLocations(String[] searchLocations) {
        return Stream.of(Optional.ofNullable(searchLocations).orElse(new String[0]))
                .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
    }

    /**
     * 获取表达式下面的所有资源列表
     * 读取时使用：res.getContentAsString(StandardCharsets.UTF_8)
     *
     * @param location 路径表达式
     * @return
     */
    public static Resource[] getResources(String location) {
        //  Location表达式可以为：classpath*:/mapper/**/*.xml
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    /**
     * 获取文件完整路径的方法
     *
     * @param path 文件名称
     * @return URL 文件完整路径
     */
    public static <T> URL findAsResource(Class<T> cls, String path) {
        URL url = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            url = contextClassLoader.getResource(path);
        }
        if (url != null) {
            return url;
        }
        url = cls.getClassLoader().getResource(path);
        if (url != null) {
            return url;
        }
        url = ClassLoader.getSystemClassLoader().getResource(path);
        return url;
    }

    /**
     * 把资源文件读取到字符串里
     *
     * @param relativePath
     * @return
     */
    public static String getResourceStr(String relativePath) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(relativePath);
            return new String(classPathResource.getInputStream().readAllBytes());
        } catch (Exception ex) {
            log.error("getResourceStr发生异常", ex);
            return null;
        }

    }

    /**
     * 从相对路径中读取资源文件，兼容Jar包里面的资源文件读取。
     * 但是只能读取InputStream里面。如果要读取对象里面，使用：
     * InputStream inputStream = QyResource.getFileStream(MODEL_PATH);
     * ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
     * NaiveBayesModel bean = (NaiveBayesModel) objectInputStream.readObject();
     *
     * @param relativePath
     * @return
     */
    public static InputStream getFileStream(String relativePath) {

        try {
            File destPath = QyFile.file(relativePath);
            if (destPath.exists()) {
                return new FileInputStream(destPath);
            }

            destPath = QyFile.file(QyConsts.CurrentDir, relativePath);
            if (destPath.exists()) {
                return new FileInputStream(destPath);
            }

            destPath = QyFile.file(QyConsts.CurrentDir, "config", relativePath);
            if (destPath.exists()) {
                return new FileInputStream(destPath);
            }
            ClassPathResource classPathResource = new ClassPathResource(relativePath);
            return classPathResource.getInputStream();

        } catch (Exception ex) {
            log.error("getFileStream发生异常{}", ex.toString());
            return null;
        }
    }

}
