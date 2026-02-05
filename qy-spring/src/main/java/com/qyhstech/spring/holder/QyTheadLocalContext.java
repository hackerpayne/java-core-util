package com.qyhstech.spring.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.qyhstech.core.QyStr;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于TransmittableThreadLocal实现线程池上下文传递
 * 参考：https://www.cnblogs.com/JohnsonLiu/p/15733680.html
 * 使用时，必须
 * 1、子线程必须使用TtlRunnable\TtlCallable修饰或者
 * 2、线程池使用TtlExecutors修饰，比如
 * private static final Executor executor = TtlExecutors.getTtlExecutor(new ThreadPoolExecutor(1, 1, 1000, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1000)));
 */
@Slf4j
public class QyTheadLocalContext {

    private QyTheadLocalContext() {
    }

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal() {
        /**
         * 如果使用的是TtlExecutors装饰的线程池或者TtlRunnable、TtlCallable装饰的任务
         * 重写copy方法且重新赋值给新的LinkedHashMap，不然会导致父子线程都是持有同一个引用，只要有修改取值都会变化。引用值线程不安全
         * parentValue是父线程执行子任务那个时刻的快照值，后续父线程再次set值也不会影响子线程get，因为已经不是同一个引用
         * @param parentValue
         * @return
         */
        @Override
        public Object copy(Object parentValue) {
            if (parentValue instanceof Map) {
                log.info("QyTheadLocalContext copy");
                return new LinkedHashMap<String, Object>((Map) parentValue);
            }
            return null;
        }

        /**
         * 如果使用普通线程池执行异步任务，重写childValue即可实现子线程获取的是父线程执行任务那个时刻的快照值，重新赋值给新的LinkedHashMap，父线程修改不会影响子线程（非共享）
         * 但是如果使用的是TtlExecutors装饰的线程池或者TtlRunnable、TtlCallable装饰的任务，此时就会变成引用共享，必须得重写copy方法才能实现非共享
         * @param parentValue
         * @return
         */
        @Override
        protected Object childValue(Object parentValue) {
            if (parentValue instanceof Map) {
                log.info("QyTheadLocalContext childValue");
                return new LinkedHashMap<String, Object>((Map) parentValue);
            }
            return null;
        }

        /**
         * 初始化,每次get时都会进行初始化
         * @return
         */
        @Override
        protected Object initialValue() {
            log.info("QyTheadLocalContext initialValue");
            return new LinkedHashMap<String, Object>();
        }
    };

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    @SuppressWarnings("all")
    public static <T> T get(String key) {
        Map<String, Object> map = getLocalMap();
        return (T) map.get(key);
    }

    /**
     * 转换为指定的类型，并可以设置默认值
     *
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, T defaultValue) {
        Map<String, Object> map = getLocalMap();
        return (T) map.get(key) == null ? defaultValue : (T) map.get(key);
    }

    /**
     * 获取值
     *
     * @return
     */
    private static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? QyStr.EMPTY : value);
    }

    /**
     * 设置整个LocalMap
     *
     * @param threadLocalMap
     */
    public static void set(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    /**
     * 获取指定前缀的所有配置
     *
     * @param prefix
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T> Map<String, T> fetchVarsByPrefix(String prefix) {
        Map<String, T> vars = new HashMap<>();
        if (prefix == null) {
            return vars;
        }
        Map<String, Object> map = getLocalMap();
        Set<Map.Entry<String, Object>> set = map.entrySet();

        for (Map.Entry entry : set) {
            Object key = entry.getKey();
            if (key instanceof String) {
                if (((String) key).startsWith(prefix)) {
                    vars.put((String) key, (T) entry.getValue());
                }
            }
        }
        return vars;
    }

    /**
     * 删除指定Key的值
     *
     * @param key
     * @return
     */
    public static Object remove(String key) {
        return getLocalMap().remove(key);
    }

    /**
     * 清空里面Map的值
     */
    public static void remove() {
        getLocalMap().clear();
    }

    /**
     * 清空整个上下文
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
