package com.qyhstech.core.tree;

import cn.hutool.core.collection.CollUtil;
import com.qyhstech.core.collection.QyList;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * 参考资源
 * {@link <a href="https://mp.weixin.qq.com/s/LsgDnr43hVH0dAjF1WHPkw?poc_token=HJQBdGejV7dna0eSEPOud8YHfdqG1v0w0ZmMG5NJ"></a>}
 * {@link <a href="https://mp.weixin.qq.com/s?__biz=MzU1MzczMjg3MQ==&mid=2247484375&idx=1&sn=a7d5e44f5906d68f05ee9f4b720b7d9a&chksm=fbef1dbfcc9894a9f2f564fe6737e585b80138b0dfb88deea4ab089f4cab0f7b28006431c3ae&cur_album_id=3546038015452676098&scene=189#wechat_redirect"></a>}
 * {@link <a href="https://juejin.cn/post/7398047016183889935"></a>}
 * {@link <a href="https://mp.weixin.qq.com/s/SdyRQUk1aOdf5Yq7ArRM7A"></a>}
 */
public class QyTree {

    /**
     * 构建树结构
     * 使用方法：
     * List<MenuVo> tree= TreeUtil.makeTree(menuList, x->x.getPId()==-1L,(parent, child) -> parent.getId().equals(child.getPid()), MenuVo::setSubMenus);
     * 参考来源：
     * 超大数据量时，会有性能问题
     *
     * @param list           要处理的数据列表
     * @param rootCheck      判断为根节点的条件
     * @param parentCheck    判断为父节点的条件
     * @param setSubChildren 子节点的设置方法
     * @param <E>
     * @return
     */
    @Deprecated
    public static <E> List<E> makeTreeOld(List<E> list, Predicate<E> rootCheck, BiFunction<E, E, Boolean> parentCheck, BiConsumer<E, List<E>> setSubChildren) {
        return list.stream()
                .filter(rootCheck)
                .peek(x -> setSubChildren.accept(x, makeChildren(x, list, parentCheck, setSubChildren)))
                .collect(Collectors.toList());
    }

    /**
     * 使用Map合成树
     * 使用方法：QyTree.makeTree(trees, ModelTreeTest2::getPid, ModelTreeTest2::getId, item -> item.getPid() == 0, ModelTreeTest2::setChildren);
     *
     * @param menuList       需要合成树的List
     * @param pId            对象中的父ID字段,如:Menu:getPid
     * @param id             对象中的id字段 ,如：Menu:getId
     * @param rootCheck      判断E中为根节点的条件，如：x->x.getPId()==-1L , x->x.getParentId()==null,x->x.getParentMenuId()==0
     * @param setSubChildren E中设置下级数据方法，如：Menu::setSubMenus
     * @param <T>            ID字段类型
     * @param <E>            泛型实体对象
     * @return
     */
    public static <T, E> List<E> makeTree(List<E> menuList, Function<E, T> pId, Function<E, T> id, Predicate<E> rootCheck, BiConsumer<E, List<E>> setSubChildren) {
        //按原数组顺序构建父级数据Map，使用Optional考虑pId为null
        Map<Optional<T>, List<E>> parentMenuMap = menuList.stream().collect(Collectors.groupingBy(
                node -> Optional.ofNullable(pId.apply(node)),
                LinkedHashMap::new,
                Collectors.toList()
        ));
        List<E> result = new ArrayList<>();
        for (E node : menuList) {
            //添加到下级数据中
            setSubChildren.accept(node, parentMenuMap.get(Optional.ofNullable(id.apply(node))));
//            setSubChildren.accept(node, parentMenuMap.getOrDefault(id.apply(node), new ArrayList<>()));
            //如里是根节点，加入结构
            if (rootCheck.test(node)) {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 创建子节点
     *
     * @param parent         父节点
     * @param allData        所有数据列表
     * @param parentCheck    父节点判断条件
     * @param setSubChildren 设置子节点
     * @param <E>
     * @return
     */
    private static <E> List<E> makeChildren(E parent, List<E> allData, BiFunction<E, E, Boolean> parentCheck, BiConsumer<E, List<E>> setSubChildren) {
        return allData.stream()
                .filter(x -> parentCheck.apply(parent, x))
                .peek(x -> setSubChildren.accept(x, makeChildren(x, allData, parentCheck, setSubChildren)))
                .collect(Collectors.toList());
    }

    /**
     * 树中过滤
     *
     * @param tree        需要过滤的树
     * @param predicate   过滤条件
     * @param getChildren 获取下级数据方法，如：MenuVo::getSubMenus
     * @param <E>         泛型实体对象
     * @return List<E> 过滤后的树
     */
    public static <E> List<E> filter(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getChildren) {
        return tree.stream().filter(item -> {
            if (predicate.test(item)) {
                List<E> children = getChildren.apply(item);
                if (children != null && !children.isEmpty()) {
                    filter(children, predicate, getChildren);
                }
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    /**
     * 树中过滤并进行节点处理（此处是将节点的choose字段置为false，那么就在页面上可以展示但无法被勾选）
     *
     * @param tree        需要过滤的树
     * @param predicate   过滤条件
     * @param getChildren 获取下级数据方法，如：MenuVo::getSubMenus
     * @param setChoose   要被处理的字段，如：MenuVo::getChoose，可根据业务场景自行修改
     * @param <E>         泛型实体对象
     * @return List<E> 过滤后的树
     */
    public static <E> List<E> filterAndHandler(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getChildren, BiConsumer<E, Boolean> setChoose) {
        return tree.stream().filter(item -> {
            //如果命中条件，则可以被勾选。（可根据业务场景自行修改）
            if (predicate.test(item)) {
                setChoose.accept(item, true);
            } else {
                setChoose.accept(item, false);
            }
            List<E> children = getChildren.apply(item);
            if (children != null && !children.isEmpty()) {
                filterAndHandler(children, predicate, getChildren, setChoose);
            }
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * 树中搜索,返回搜索节点及其所有父节点
     *
     * @param tree           树结构
     * @param predicate      条件判断
     * @param getSubChildren 获取子级的方法
     * @param <E>
     * @return 返回搜索到的节点及其父级到根节点
     */
    public static <E> List<E> search(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getSubChildren) {
        Iterator<E> iterator = tree.iterator();
        while (iterator.hasNext()) {
            E item = iterator.next();
            List<E> childList = getSubChildren.apply(item);
            if (childList != null && !childList.isEmpty()) {
                search(childList, predicate, getSubChildren);
            }
            if (!predicate.test(item) && (childList == null || childList.isEmpty())) {
                iterator.remove();
            }
        }
        return tree;
    }

    /**
     * 遍历树结构，使用先序排列
     *
     * @param tree
     * @param consumer
     * @param getSubChildren
     * @param <E>
     */
    public static <E> void forPreOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> getSubChildren) {
        for (E l : tree) {
            consumer.accept(l);
            List<E> es = getSubChildren.apply(l);
            if (es != null && es.size() > 0) {
                forPreOrder(es, consumer, getSubChildren);
            }
        }
    }

    /**
     * 遍历树结构
     *
     * @param tree
     * @param consumer
     * @param getSubChildren
     * @param <E>
     */
    public static <E> void forLevelOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> getSubChildren) {
        Queue<E> queue = new LinkedList<>(tree);
        while (!queue.isEmpty()) {
            E item = queue.poll();
            consumer.accept(item);
            List<E> childList = getSubChildren.apply(item);
            if (childList != null && !childList.isEmpty()) {
                queue.addAll(childList);
            }
        }
    }

    /**
     * 层序遍历树结构
     *
     * @param tree
     * @param consumer
     * @param getSubChildren
     * @param <E>
     */
    public static <E> void forPostOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> getSubChildren) {
        for (E item : tree) {
            List<E> childList = getSubChildren.apply(item);
            if (childList != null && !childList.isEmpty()) {
                forPostOrder(childList, consumer, getSubChildren);
            }
            consumer.accept(item);
        }
    }

    /**
     * 树结构打平
     *
     * @param tree
     * @param getSubChildren
     * @param setSubChildren
     * @param <E>
     * @return
     */
    public static <E> List<E> flat(List<E> tree, Function<E, List<E>> getSubChildren, Consumer<E> setSubChildren) {
        List<E> res = new ArrayList<>();
        forPostOrder(tree, item -> {
            setSubChildren.accept(item);
            res.add(item);
        }, getSubChildren);
        return res;
    }

    /**
     * 树结构排序
     *
     * @param tree
     * @param comparator
     * @param getChildren
     * @param <E>
     * @return
     */
    public static <E> List<E> sort(List<E> tree, Comparator<? super E> comparator, Function<E, List<E>> getChildren) {
        for (E item : tree) {
            // 获取当前节点的子节点列表
            List<E> childList = getChildren.apply(item);

            // 如果子节点列表不为空，则递归调用 sort 方法对其进行排序
            if (childList != null && !childList.isEmpty()) {
                sort(childList, comparator, getChildren);
            }
        }
        tree.sort(comparator);
        return tree;
    }

    /**
     * 将List树结构，扁平化为普通列表
     *
     * @param dataList
     * @param childrenField
     * @param <T>
     * @return
     */
    public static <T> List<T> flatTree(List<T> dataList, Function<T, List<T>> childrenField) {
        Set<T> result = new HashSet<>();

        if (CollUtil.isEmpty(dataList)) {
            return QyList.empty();
        }

        // 将第一层节点加入队列
        Queue<T> queue = new LinkedList<>(dataList);

        // 当队列不为空时继续处理
        while (!queue.isEmpty()) {
            T current = queue.poll();
            result.add(current);

            // 如果有子节点，将子节点加入队列
            if (CollUtil.isNotEmpty(childrenField.apply(current))) {
                queue.addAll(childrenField.apply(current));
            }
        }

        return new ArrayList<>(result);
    }

    /**
     * 从树结构里面，查找满足条件的数据
     * 结果会自动使用Set去重
     *
     * @param dataList      树级列表
     * @param childrenField 子级所在的字段
     * @param predicate     匹配方法
     * @param <T>
     * @return
     */
    public static <T> List<T> findTree(List<T> dataList, Function<T, List<T>> childrenField, Predicate<T> predicate) {
        Queue<T> queue = new LinkedList<>(dataList);
        Set<T> matchList = new HashSet<>();
        while (!queue.isEmpty()) {
            T current = queue.poll();

            if (predicate.test(current)) {
                matchList.add(current);
            }

            if (CollUtil.isNotEmpty(childrenField.apply(current))) {
                queue.addAll(childrenField.apply(current));
            }
        }
        return new ArrayList<>(matchList);
    }

    /**
     * 从树结构中，查找第一个匹配项
     *
     * @param dataList
     * @param childrenField
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> T findFirstFromTree(List<T> dataList, Function<T, List<T>> childrenField, Predicate<T> predicate) {
        Queue<T> queue = new LinkedList<>(dataList);
        while (!queue.isEmpty()) {
            T current = queue.poll();

            if (predicate.test(current)) {
                return current;
            }

            if (CollUtil.isNotEmpty(childrenField.apply(current))) {
                queue.addAll(childrenField.apply(current));
            }
        }
        return null;
    }

}
