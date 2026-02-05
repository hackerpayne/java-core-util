package com.qyhstech.core.domain.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 键值对
 */
@Data
@NoArgsConstructor
public class QyModelPair<T> implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    private String key;

    private T value;

    public QyModelPair(String key, T value) {
        this.key = key;
        this.value = value;
    }

}
