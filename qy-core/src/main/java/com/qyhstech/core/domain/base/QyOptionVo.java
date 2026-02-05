package com.qyhstech.core.domain.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 名值对(一般用于下拉选)
 * 预留一个拓展字段obj,按需使用
 */
@Data
@NoArgsConstructor
public class QyOptionVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Object id;

    private String name;

    private Object data;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<?> children;

    /**
     * 添加KV键值对
     *
     * @param id
     * @param name
     */
    public QyOptionVo(Object id, String name) {
        this(id, name, null, null);
    }

    /**
     * 添加子节点
     *
     * @param id
     * @param name
     * @param data
     */
    public QyOptionVo(Object id, String name, Object data) {
        this(id, name, data, null);
    }

    /**
     * 添加子节点
     *
     * @param id
     * @param name
     * @param data
     * @param children
     */
    public QyOptionVo(Object id, String name, Object data, List<?> children) {
        super();
        this.id = id;
        this.name = name;
        this.data = data;
        this.children = children;
    }
}
