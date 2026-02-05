package com.qyhstech.core.domain.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 一对一数据分配实体类
 */
@Data
public class QyParamAssignOne {

    @NotNull(message = "数据ID不能为空")
    @JsonProperty("id")
    private Long dataId;

    @NotNull(message = "分配目标ID不能为空")
    @JsonProperty("assignId")
    private Long assignId;
}
