package com.qyhstech.core.threads;

@FunctionalInterface
public interface PollCallback<T> {
    /**
     * @return 返回非 null 表示轮询成功，可以结束并返回；
     *         返回 null 表示还没准备好，继续轮询。
     */
    T check() throws Exception;
}