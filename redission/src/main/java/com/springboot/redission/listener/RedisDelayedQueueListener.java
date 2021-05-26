package com.springboot.redission.listener;

public interface RedisDelayedQueueListener<T> {
    /**
     * 执行方法
     *
     * @param t
     */
    void invoke(T t);
}
