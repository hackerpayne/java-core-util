package com.qyhstech.core.domain.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 电商平台：属性列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelAttributeList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 属性键名，比如内存
     */
    private String attrKey;

    /**
     * 属性值，比如8G、16G等等。统一存进来
     */
    private List<String> attrValue;

    /**
     * 获取逗号分割的字符结果
     *
     * @return
     */
    public String getValueStr() {
        if (CollUtil.isNotEmpty(attrValue)) {
            return String.join(",", attrValue);
        }
        return StrUtil.EMPTY;
    }

}
