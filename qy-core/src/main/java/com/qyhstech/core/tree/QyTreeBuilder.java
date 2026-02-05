package com.qyhstech.core.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.base.QyModelNameValue;
import com.qyhstech.core.domain.base.QyOptionVo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 生成树结构
 */
public class QyTreeBuilder<T> {

    /**
     * 直接将所有数据生成树结构
     *
     * @param all
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<T> buildTree(List<T> all) {
        return buildTree(0L, all, t -> true);
    }

    /**
     * 直接用0生成所有树结构，要求根节点为0才可以
     *
     * @param all
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<T> buildTree(List<T> all, Predicate<? super T> predicate) {
        return buildTree(0L, all, predicate);
    }

    /**
     * 为listDatas的所有Children，赋值
     * 遍历listDatas，将all下面的数据，重新填入每个listDatas项的children中
     *
     * @param listDatas 数据列表，Children为空，
     * @param all       所有数据列表，全量值
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<T> buildTreeWithFill(List<T> listDatas, List<T> all) {
        // 如果列表为空，以0为根进行树生成
        if (CollUtil.isEmpty(listDatas)) {
            return buildTree(all, t -> true);
        }
        listDatas.forEach(item -> {
            if (item != null) {
                item.setChildren(buildTree(item, all, t -> true));
            }
        });
        return listDatas;
    }

    /**
     * 生成树结构到指定父节点下
     *
     * @param root
     * @param all
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<T> buildTree(T root, List<T> all) {
        return buildTree(root, all, t -> true);
    }

    /**
     * 递归获取有菜单的子菜单
     * 用法：
     * List<T> listMenus = sysMenuService.findAll();
     * SysMenuEntity topFirst = sysMenuService.findById(1L);
     * List<T> childrenList = QyTree.getChildren(topFirst, listMenus);
     *
     * @param root      父级数据ID
     * @param all       所有数据列表
     * @param predicate Predicate过滤器
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<T> buildTree(T root, List<T> all, Predicate<? super T> predicate) {
        return all.stream().filter(dataItem -> dataItem.getParentId().equals(root != null ? root.getId() : 0)).filter(predicate).peek(dataItem -> {
            //找到子菜单
            dataItem.setChildren(buildTree(dataItem, all, predicate));
        }).sorted(Comparator.comparingInt(menu -> (menu.getSortOrder() == null ? 0 : menu.getSortOrder()))).toList();
    }

    /**
     * 生成树结构
     *
     * @param rootId 父节点ID
     * @param all    所有数据列表
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<T> buildTree(Long rootId, List<T> all) {
        return buildTree(rootId, all, t -> true);
    }

    /**
     * 递归获取有菜单的子菜单
     *
     * @param rootId    父级菜单ID
     * @param all       所有数据列表
     * @param predicate 过滤器
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<T> buildTree(Long rootId, List<T> all, Predicate<? super T> predicate) {
        return all.stream().filter(dataItem -> dataItem.getParentId().equals(rootId != null ? rootId : 0)).filter(predicate).peek(dataItem -> {
            //找到子菜单
            dataItem.setChildren(buildTree(dataItem, all, predicate));
        }).sorted(Comparator.comparingInt(menu -> (menu.getSortOrder() == null ? 0 : menu.getSortOrder()))).toList();
    }

    /**
     * 使用TreeUtil生成
     *
     * @param dataList
     * @param parentId
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<Tree<Long>> buildTreeNew(List<T> dataList, Long parentId) {
        return buildTreeNew(dataList, parentId, null);
    }

    /**
     * 转换为HuTool的Tree
     *
     * @param dataList
     * @param parentId
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<Tree<Long>> buildTreeNew(List<T> dataList, Long parentId, Predicate<? super T> predicate) {

        List<T> collect = Objects.isNull(predicate) ? dataList : dataList.stream().filter(predicate).toList();

        // 3.转树，Tree<>里面泛型为id的类型
        List<Tree<Long>> trees = TreeUtil.build(collect, parentId, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSortOrder());
            tree.setName(treeNode.getName());
            tree.putExtra("extra", treeNode.getExtra());
        });
        return trees;
    }


    /**
     * 把树结构转换为KV格式
     * 遍历这一项的所有Children子项，并转换到ModelNameValue中
     *
     * @param treeItem      某个项
     * @param nameResolver  Name解析器
     * @param valueResolver Value解析器
     * @param dataResolver  Data解析器
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    private static <T extends QyTreeNode> QyModelNameValue buildKvTree(T treeItem, Function<T, String> nameResolver, Function<T, ?> valueResolver, Function<T, ?> dataResolver) {
        if (treeItem == null) {
            return null;
        }

        QyModelNameValue modelNameValue = new QyModelNameValue(nameResolver.apply(treeItem), valueResolver.apply(treeItem));
        if (Objects.nonNull(dataResolver)) {
            modelNameValue.setData(dataResolver.apply(treeItem));
        }

        if (CollUtil.isNotEmpty(treeItem.getChildren())) {
            List<QyModelNameValue> childVos = new ArrayList<>();

            for (Object child : treeItem.getChildren()) {
                QyModelNameValue childVo = buildKvTree((T) child, nameResolver, valueResolver, dataResolver);
                childVos.add(childVo);
            }

            modelNameValue.setChildren(childVos);
        }

        return modelNameValue;
    }

    /**
     * 把树结构数据，简化为KV键值树，去掉无用信息，只保留name、value、data、children
     * var brandList = QyTree.buildTree(listData);
     * var options = QyDict.buildDictTree(brandList, MallBrandEntity::getName, MallBrandEntity::getId, null);
     *
     * @param dataList      要处理的树列表List，可以经过QyTree.buildTree(listData); 先处理
     * @param nameResolver  KV的Key值
     * @param valueResolver KV的Value值
     * @param dataResolver  KV的Data解析值
     * @param <T>           返回KV的树结构
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<QyModelNameValue> buildKvTree(List<T> dataList, Function<T, String> nameResolver, Function<T, ?> valueResolver, Function<T, ?> dataResolver) {
        List<QyModelNameValue> resultList = QyList.empty();
        if (CollUtil.isNotEmpty(dataList)) {
            for (T dataItem : dataList) {
                resultList.add(buildKvTree(dataItem, nameResolver, valueResolver, dataResolver));
            }
        }
        return resultList;
    }

    /**
     * 把树结构转换为选项结构
     *
     * @param treeItem
     * @param nameResolver
     * @param idResolver
     * @param dataResolver
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    private static <T extends QyTreeNode> QyOptionVo buildOptionTree(T treeItem, Function<T, ?> idResolver, Function<T, String> nameResolver) {
        if (treeItem == null) {
            return null;
        }

        QyOptionVo optionValue = new QyOptionVo(idResolver.apply(treeItem), nameResolver.apply(treeItem));
        if (CollUtil.isNotEmpty(treeItem.getChildren())) {
            List<QyOptionVo> childVos = new ArrayList<>();

            for (Object child : treeItem.getChildren()) {
                QyOptionVo childVo = buildOptionTree((T) child, idResolver, nameResolver);
                childVos.add(childVo);
            }

            optionValue.setChildren(childVos);
        }

        return optionValue;
    }

    /**
     * 把树结构，简化为选项式树结构，去掉无用的信息，只保留id、name、children
     * var brandList = QyTree.buildTree(listData);
     * var options = QyDict.buildOptionTree(brandList, MallBrandEntity::getId, MallBrandEntity::getName);
     *
     * @param dataList     要处理的树形结构
     * @param idResolver   ID解析器
     * @param nameResolver Name解析器
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<QyOptionVo> buildOptionTree(List<T> dataList, Function<T, ?> idResolver, Function<T, String> nameResolver) {
        boolean hasChildren = dataList.stream().anyMatch(item -> CollUtil.isNotEmpty(item.getChildren()));
        // 如果有子元素代表已经是树结构，不需要再处理了
        if (hasChildren) {
            return buildOptionTreeInner(dataList, idResolver, nameResolver);
        }
        var treeDataList = QyTreeBuilder.buildTree(dataList);
        return buildOptionTreeInner(treeDataList, idResolver, nameResolver);
    }

    /**
     * 内部方法，只能分开，这样才能直接把结果转换成树形结构
     * 把树结构，简化为选项式树结构，去掉无用的信息，只保留id、name、children
     * var brandList = QyTree.buildTree(listData);
     * var options = QyDict.buildOptionTree(brandList, MallBrandEntity::getId, MallBrandEntity::getName);
     *
     * @param dataList     要处理的树形结构
     * @param idResolver   ID解析器
     * @param nameResolver Name解析器
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeNode> List<QyOptionVo> buildOptionTreeInner(List<T> dataList, Function<T, ?> idResolver, Function<T, String> nameResolver) {
        List<QyOptionVo> resultList = QyList.empty();
        if (CollUtil.isNotEmpty(dataList)) {
            for (T dataItem : dataList) {
                resultList.add(buildOptionTree(dataItem, idResolver, nameResolver));
            }
        }
        return resultList;
    }


}
