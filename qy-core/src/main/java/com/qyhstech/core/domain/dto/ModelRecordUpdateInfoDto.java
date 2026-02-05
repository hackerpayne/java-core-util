package com.qyhstech.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字段变化类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelRecordUpdateInfoDto {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段描述
     */
    private String fieldDesc;

    /**
     * 之前的旧值
     */
    private String updateBefore;

    /**
     * 现在的新值
     */
    private String after;

}
