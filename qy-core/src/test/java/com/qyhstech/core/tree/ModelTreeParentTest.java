package com.qyhstech.core.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelTreeParentTest implements QyTreeParentNode<ModelTreeParentTest> {
    private Long id;
    private Long parentId;
    private String name;
    private String title;

    private ModelTreeParentTest parent;

    @Override
    public Integer getSortOrder() {
        return 1;
    }

    @Override
    public Object getExtra() {
        return null;
    }
}