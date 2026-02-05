package com.qyhstech.core.domain.dto;


import com.qyhstech.core.domain.base.QyBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 返回HTML结果实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ModelHtml extends QyBaseEntity {

    private String html = "";
    private String error = "";
    private String redirectUrl;

}