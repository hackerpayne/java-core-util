package com.qyhstech.core.reflect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.dto.ModelRecordUpdateInfoDto;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 阿里代码规范推荐：避免用Apache Beanutils进行属性的copy，Apache BeanUtils性能较差，可以使用其他方案比如Spring BeanUtils, Cglib BeanCopier
 * <p>
 * 评测：
 * https://github.com/arey/java-object-mapper-benchmark
 * https://github.com/yangtu222/BeanUtils#performance
 * <p>
 * 参考：
 * https://github.com/yangtu222/BeanUtils
 * <p>
 * 常用的BeanCopy类
 * 1、org.springframework.beans.BeanUtils.copyProperties
 * 2、net.sf.ezmorph.bean.BeanMorpher
 * 3、org.apache.commons.beanutils.BeanUtil.copyProperties 反射机制
 * 4、org.apache.commons.beanutils.PropertyUtils.copyProperties 反射机制
 * 5、org.springframework.beans.BeanUtils.copyProperties 反射机制
 * 6、org.springframework.cglib.beans.BeanCopier.create 动态代理，效率高
 * 7、org.dozer.DozerBeanMapper.map XML配置映射，性能最低下
 * <p>
 * Cglib copy 问题
 * <p>
 * 不支持链式 bean，mybatis-plus 生成的 Model 中默认添加了 @Accessors(chain = true) 注解默认为链式。
 * 不支持 原始类型和封装类型 copy int <-> Integer。
 * 类型转换不够智能，设置 useConverter 为 true 和重写 Converter，类型相同也会走转换的逻辑。
 * <p>
 * 简单的复制拷贝，这种情况下，BeanCopyer>PropertyUtils>Dozer
 */
public class QyBean {

    private static final Map<String, BeanCopier> BEAN_COPIERS = new HashMap<String, BeanCopier>();

    /**
     * 复制到目标实体，null不复制
     *
     * @param src
     * @param dst
     * @param <T>
     */
    public static <T> void copyIgnoreNull(T src, T dst) {
        copyIgnoreNull(src, dst, List.of("id", "createTime", "updateTime", "createBy", "updateBy"));
    }

    /**
     * 复制到目标实体，null不复制
     *
     * @param src
     * @param dst
     * @param <T>
     * @return
     */
    public static <T> void copyIgnoreNull(T src, T dst, List<String> ignoreFields) {
        CopyOptions options = CopyOptions.create()
                .setIgnoreProperties(ignoreFields.toArray(new String[0])) // 过滤的字段
                .ignoreNullValue() // 源数据为空不更新
                .ignoreError();  // 忽略错误提示
        BeanUtil.copyProperties(src, dst, options);
    }

    /**
     * 使用BeanCopier复制对象
     *
     * @param srcObj
     * @param destObj
     */
    public static void copy(Object srcObj, Object destObj) {
        String key = genKey(srcObj.getClass(), destObj.getClass());
        BeanCopier copier = null;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(srcObj, destObj, null);
    }

    /**
     * 生成缓存的Key
     *
     * @param srcClazz
     * @param destClazz
     * @return
     */
    private static String genKey(Class<?> srcClazz, Class<?> destClazz) {
        return srcClazz.getName() + destClazz.getName();
    }

    /**
     * MyBatisPlus的Map列表转对象
     *
     * @param mapList
     * @param elementType
     * @param <T>
     * @return
     */
    public static <T> List<T> mapToList(List<Map<String, Object>> mapList, Class<T> elementType) {
        List<T> listResult = QyList.empty();
        if (CollectionUtils.isEmpty(mapList)) {
            return null;
        }
        mapList.forEach(mapItem -> {
            listResult.add(BeanUtil.mapToBean(mapItem, elementType, true));
        });
        return listResult;
    }

    /**
     * Map转到一个对象里面
     *
     * @param mapList
     * @param elementType
     * @param <T>
     * @return
     */
    public static <T> T mapToEntity(List<Map<String, Object>> mapList, Class<T> elementType) {

        if (CollectionUtils.isEmpty(mapList)) {
            return null;
        }

        T result = BeanUtil.mapToBean(mapList.get(0), elementType, true);
        return result;
    }

    /**
     * 判断2个实体类的变化
     *
     * @param source    原始数据
     * @param update    更新数据
     * @param fieldsMap 需要对比的字段，key为字段名，value为字段中文名
     * @param <T>
     * @return
     */
    public static <T> List<ModelRecordUpdateInfoDto> compareDifference(T source, T update, Map<String, String> fieldsMap) {

        if (null == source || null == update) {
            return new ArrayList<>();
        }

        // 遍历结构，生成变化信息
        List<ModelRecordUpdateInfoDto> list = QyList.empty();
        try {
            //        Field[] declaredFields = source.getClass().getDeclaredFields();
            Field[] declaredFields = ReflectUtil.getFields(source.getClass());
            for (Field declaredField : declaredFields) {
                String fieldName = declaredField.getName();
                // 如果字段不在指定的映射中，跳过
                if (!fieldsMap.containsKey(fieldName)) {
                    continue;
                }

                declaredField.setAccessible(true);
                Object oldValue = declaredField.get(source);
                Object newValue = declaredField.get(update);
                if (!Objects.equals(oldValue, newValue)) {
                    list.add(new ModelRecordUpdateInfoDto(fieldName, fieldsMap.get(fieldName), oldValue != null ? String.valueOf(oldValue) : null, newValue != null ? String.valueOf(newValue) : null));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("比较字段时发生错误", e);
        }
        return list;
    }

}
