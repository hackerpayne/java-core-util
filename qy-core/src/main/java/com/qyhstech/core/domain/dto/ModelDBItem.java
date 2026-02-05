package com.qyhstech.core.domain.dto;

import com.qyhstech.core.domain.base.QyBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 插入数据库的实体类整理
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ModelDBItem extends QyBaseEntity {

    private String key;

    private Object value;

    private boolean isUpdate = false;

    public ModelDBItem(String key, Object value) {
        this.key = key;
        this.value = value;
    }

}
