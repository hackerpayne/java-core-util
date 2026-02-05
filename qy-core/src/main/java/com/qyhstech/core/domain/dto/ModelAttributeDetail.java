package com.qyhstech.core.domain.dto;

import com.qyhstech.core.domain.base.QyModelPair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 属性列表明细
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelAttributeDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 最后生成的SKU结果列表，如金色,32G,进口,专业
     */
    private List<String> attrValues;

    /**
     * 最后生成结果的字符串形式，如"金色,32G,进口,专业"
     */
    private String attrValueStr;

    /**
     * 最终生成的SKU属性加值的列表，如{"key":"颜色","value":"红色"},{"key":"大小","value":"32G"}
     */
    private List<QyModelPair<String>> attrList;

}
