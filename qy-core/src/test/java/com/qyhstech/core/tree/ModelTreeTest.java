package com.qyhstech.core.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelTreeTest implements QyTreeNode<ModelTreeTest> {
    private Long id;
    private Long parentId;
    private String name;
    private String title;
    private List<ModelTreeTest> children;

    @Override
    public Integer getSortOrder() {
        return 0;
    }

    @Override
    public Object getExtra() {
        return null;
    }
}