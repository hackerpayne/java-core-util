package com.qyhstech.core.domain.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 带ID的实体类
 */
@Getter
@Setter
public abstract class QyBaseIdEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

}
