package com.qyhstech.core.upload;

import com.qyhstech.core.domain.enums.FileNamePatternEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class QyUploadFileDto {

    /**
     * 文件名的命名规则
     */
    FileNamePatternEnum namePattern;

    /**
     * 源文件名
     */
    private String oldName;

    /**
     * 现文件名
     */
    private String name;

    /**
     * 本地保存路径，目录格式。相对路径
     */
    private String relativePath;

    /**
     * 完整文件路径
     */
    private String fullPath;

    /**
     * 文件类型，后缀
     */
    private String ext;

    /**
     * 访问地址，完整URL形式
     */
    private String sourceUrl;

    /**
     * 根据当前网站生成的当前可用的URL，自己的地址
     */
    private String url;

    /**
     * 文件流的Md5值
     */
    private String md5;

    /**
     * 文件大小：KB
     */
    private long size;

    /**
     * 记录下文件的状态
     */
    private String status;

    /**
     * 保存额外的数据
     */
    private Map<String, Object> extra;


    /**
     * 获取附加的字段信息
     * @param key
     * @return
     */
    public Object getExtra(String key) {
        return getExtra(key, null);
    }

    /**
     * 获取额外字段
     *
     * @param key
     * @return
     */
    public Object getExtra(String key, Object defaultValue) {
        if (extra == null) {
            return null;
        }
        return extra.getOrDefault(key, defaultValue);
    }

    /**
     * 放置额外字段
     *
     * @param key
     * @param value
     * @return
     */
    public QyUploadFileDto putExtra(String key, Object value) {
        if (extra == null) {
            extra = new HashMap<String, Object>();
        }
        extra.put(key, value);
        return this;
    }

}