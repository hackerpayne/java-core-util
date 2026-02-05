package com.qyhstech.core.domain.response;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class QyPageTree {

    private List<Tree<Long>> data;

    private Integer pageNo;

    private Integer pageSize;

    private Integer totalCount;

    private Integer pageCount;

    public QyPageTree() {

    }

    public QyPageTree(IPage<?> page) {
        this.pageNo = (int) page.getCurrent();
        this.pageCount = (int) page.getPages();
        this.pageSize = (int) page.getSize();
        this.totalCount = (int) page.getTotal();
    }
    public QyPageTree(IPage<?> page, List<Tree<Long>> tree) {
        this.pageNo = (int) page.getCurrent();
        this.pageCount = (int) page.getPages();
        this.pageSize = (int) page.getSize();
        this.totalCount = (int) page.getTotal();
        this.data = tree;
    }
}
