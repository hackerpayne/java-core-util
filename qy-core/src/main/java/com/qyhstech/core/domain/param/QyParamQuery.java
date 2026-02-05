package com.qyhstech.core.domain.param;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.List;

/**
 * 自动查询条件
 */
@Data
public class QyParamQuery {

    /**
     * 属性
     */
    private String name;
    /**
     * 值
     */
    private List<Object> value;

    /**
     * 方式 like 模糊搜索, eq（下拉多选）
     */
    private String expression;

    public String getName() {
        return StrUtil.toUnderlineCase(name);
    }

}
