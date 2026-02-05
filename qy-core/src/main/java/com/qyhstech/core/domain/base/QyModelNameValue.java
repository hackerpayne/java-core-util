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
public class QyModelNameValue implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private Object value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<?> children;

    /**
     * 添加KV键值对
     *
     * @param name
     * @param value
     */
    public QyModelNameValue(String name, Object value) {
        this(name, value, null, null);
    }

    /**
     * 添加额外的数据
     *
     * @param name
     * @param value
     * @param data
     */
    public QyModelNameValue(String name, Object value, Object data) {
        this(name, value, data, null);
    }

    /**
     * 添加子节点
     *
     * @param name
     * @param value
     * @param data
     * @param children
     */
    public QyModelNameValue(String name, Object value, Object data, List<?> children) {
        super();
        this.name = name;
        this.value = value;
        this.data = data;
        this.children = children;
    }
}
