package com.qyhstech.core.domain.param;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QyParamSort {

    /**
     * 属性
     */
    private String name;
    /**
     * 方式
     */
    private String order;

    public String getName() {
        return StrUtil.toUnderlineCase(name);
    }
}
