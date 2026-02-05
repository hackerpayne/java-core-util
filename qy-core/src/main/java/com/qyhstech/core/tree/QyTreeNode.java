package com.qyhstech.core.tree;

import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * 树结构泛型接口
 * implements QyTreeNode
 */
public interface QyTreeNode<T> {

    /**
     * 获取父ID的方式
     *
     * @return
     */
    Long getParentId();

    /**
     * 获取ID
     *
     * @return
     */
    Long getId();

    /**
     * 获取名称标记
     *
     * @return
     */
    default String getName() {
        return StrUtil.EMPTY;
    }

    /**
     * 获取排序规则
     *
     * @return
     */
    default Integer getSortOrder() {
        return 0;
    }

    /**
     * 获取附加数据
     *
     * @return
     */
    default Object getExtra() {
        return null;
    }

    /**
     * 设置子菜单项
     *
     * @param list
     */
    void setChildren(List<T> list);

    /**
     * 获取所有子级列表
     *
     * @return
     */
    List<T> getChildren();

}
