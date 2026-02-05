package com.qyhstech.core.domain.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.json.deserializer.IntListJsonDeserializer;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 支持单个和多个ID数组的参数
 * 同时支持以下几种格式：
 * { "id": 123 }              // 数字
 * { "id": "123" }            // 字符串
 * { "id": [123, 456] }       // 数字数组
 * { "id": ["123", "456"] }   // 字符串数组
 */
@Data
public class QyParamInt implements Serializable {

    @NotEmpty(message = "要处理的数据ID不能为空")
    @JsonDeserialize(using = IntListJsonDeserializer.class) // 同时支持单个和多个ID参数
    @JsonProperty("id")
    private List<Long> idList;

    /**
     * 获取第一个数据或者返回Null
     *
     * @return
     */
    public Long getFirst() {
        return QyList.getFirstOrNull(idList);
    }
}
