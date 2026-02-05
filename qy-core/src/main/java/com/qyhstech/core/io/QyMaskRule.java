package com.qyhstech.core.io;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 自定义脱敏规则
 */
@Data
public class QyMaskRule {

    /**
     * 字段英文名称
     */
    @NotEmpty
    private String name;

    /**
     * 0：隐藏，1：显示
     */
    @NotNull
    private Integer type;

    /**
     * 规则：开头:0 、中间:1 、末尾: -1 、全部: 2 、区间：3
     */
    @NotNull
    private Integer scope;

    /**
     * 位数
     */
    private Integer count;

    /**
     * 开始位数
     */
    private Integer start;

    /**
     * 结束位数
     */
    private Integer end;
}
