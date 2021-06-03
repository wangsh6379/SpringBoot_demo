package com.springboot.redission.redlock;

import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 封装
 *
 * @ClassName DistributedLocker
 * @Description DistributedLocker
 * @Author wangsh
 * @Date 2021/3/29 16:35
 * @Version 1.0
 */
public interface DistributedLocker {

    /**
     * 获取锁进行加锁
     *
     * @param lockKey 锁名
     * @return
     */
    RLock lock(String lockKey);

    /**
     * 获取锁进行加锁，超时X秒后解锁
     *
     * @param lockKey 锁名
     * @param timeout 超时时间
     * @return
     */
    RLock lock(String lockKey, int timeout);

    /**
     * 获取锁，自定义超时X时间后解锁
     *
     * @param lockKey 锁名
     * @param unit    时间类型
     * @param timeout 超时时间
     * @return
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * 获取锁，尝试加锁，最多等待waitTime时间，设置超过leaseTime时间后解锁
     *
     * @param lockKey   锁名
     * @param unit      时间类型
     * @param waitTime  等待时间
     * @param leaseTime 超时时间
     * @return
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    /**
     * 解锁操作
     * 先获取锁是否未解锁，再解锁
     *
     * @param lockKey 锁名
     */
    void unlock(String lockKey);

    /**
     * 解锁操作
     * 直接进行解锁，业务处理不当可能出现异常，非指定线程解锁异常
     *
     * @param lock 锁名
     */
    void unlock(RLock lock);
}