package com.qyhstech.core.domain.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 一对多数据分配参数，
 * 将一条数据分配给多个目标列表
 */
@Data
public class QyParamAssign {

    @NotNull(message = "数据ID不能为空")
    @JsonProperty("id")
    private Long dataId;

    @NotNull(message = "分配列表不能为空")
    @NotEmpty(message = "分配列表不能为空")
    @JsonProperty("assignList")
    private List<Long> assignList;
}
