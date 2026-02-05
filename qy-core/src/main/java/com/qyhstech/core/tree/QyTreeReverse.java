package com.qyhstech.core.tree;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 从树结构中提取父级结构节点
 */
public class QyTreeReverse<T> {

    /**
     * 构建 Map，key 是节点 id，value 是带有 parent 引用的节点对象。
     *
     * @param items    所有原始节点数据
     * @param onlyLeaf 是否只保留叶子节点到结果 Map 中
     * @return Map<id, 节点>，每个节点都有 parent 字段指向其父节点（递归）
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeParentNode> Map<Long, T> buildNodeMap(List<T> items, boolean onlyLeaf) {
        // 原始数据按id映射，方便查找
        Map<Long, T> idToItem = items.stream()
                .collect(Collectors.toMap(QyTreeParentNode::getId, item -> item));

        // 用来记录哪些节点被作为父节点引用了（非叶子）
        Set<Long> nonLeafIds = new HashSet<>();

        // 构建 parent 引用
        for (T item : items) {
            Long parentId = item.getParentId();
            if (parentId != null) {
                T parent = idToItem.get(parentId);
                if (parent != null) {
                    item.setParent(parent);
                    nonLeafIds.add(parentId);
                }
            }
        }

        Map<Long, T> result = new HashMap<>();
        for (T item : items) {
            boolean isLeaf = !nonLeafIds.contains(item.getId());
            if (!onlyLeaf || isLeaf) {
                result.put(item.getId(), item);
            }
        }

        return result;
    }

    /**
     * 构建从根节点到指定节点的名称路径（例如：祖先-父级-当前）
     *
     * @param itemMap   带有 parent 引用的节点 Map（由 buildNodeMap 构建）
     * @param targetId  要查找的目标节点 ID
     * @param field     要连接的节点的字段，比如把名称连接
     * @param separator 名称之间的连接符（如 "-"）
     * @return 完整路径字符串，如果未找到则返回 null
     */
    @SuppressWarnings("all")
    public static <T extends QyTreeParentNode> String buildNamePath(Map<Long, T> itemMap, Long targetId, Function<T, String> field, String separator) {
        T current = itemMap.get(targetId);
        if (current == null) {
            return null;
        }

        LinkedList<String> pathNames = new LinkedList<>();

        while (current != null) {
            pathNames.addFirst(field.apply(current));
            current = (T) current.getParent();
        }

        return String.join(separator, pathNames);
    }


}
