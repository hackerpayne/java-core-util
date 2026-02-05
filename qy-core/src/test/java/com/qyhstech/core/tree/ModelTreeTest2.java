package com.qyhstech.core.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelTreeTest2 {

    Integer id;

    Integer pid;

    /**
     * 排序字段
     */
    private Integer rank;

    List<ModelTreeTest2> items;
}
