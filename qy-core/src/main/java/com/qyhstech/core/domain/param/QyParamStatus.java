package com.qyhstech.core.domain.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 状态值更新公共参数列表
 */
@Data
public class QyParamStatus implements Serializable {

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull(message = "状态值不能为空")
    private Integer status;
}
