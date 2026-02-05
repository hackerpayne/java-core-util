package com.qyhstech.core.domain.dto;

import com.qyhstech.core.domain.base.QyBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 带Key、Value和附加Boolen的Tuple
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ModelTuple extends QyBaseEntity {

    private String key;
    private String value;
    private boolean judge;

    public ModelTuple() {

    }

    public ModelTuple(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ModelTuple(String key, String value, Boolean judge) {
        this.key = key;
        this.value = value;
        this.judge = judge;
    }

}
