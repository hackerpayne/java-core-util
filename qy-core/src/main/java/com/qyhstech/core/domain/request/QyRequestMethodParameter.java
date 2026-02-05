package com.qyhstech.core.domain.request;


import com.qyhstech.core.domain.base.QyBaseEntity;
import lombok.Data;

/**
 * 参数
 **/
@Data
public class QyRequestMethodParameter extends QyBaseEntity {
    /**
     * 参数名
     */
    private String name;

    /**
     * 添加的注解
     */
    private String annoation;

    /**
     * 数据类型
     */
    private String type;

}
