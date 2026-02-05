package com.qyhstech.core.queue;

public interface Computable<V> {
    V compute(String k);
}