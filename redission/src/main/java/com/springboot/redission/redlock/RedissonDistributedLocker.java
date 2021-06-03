package com.springboot.redission.redlock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 实现分布式锁的具体实现
 *
 * @ClassName RedissonDistributedLocker
 * @Description RedissonDistributedLocker
 * @Author wangsh
 * @Date 2021/3/29 16:36
 * @Version 1.0
 */
public class RedissonDistributedLocker implements DistributedLocker {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {

        try {
            RLock lock = redissonClient.getLock(lockKey);
            //是否还是锁定状态、且是当前执行线程的锁
            System.out.println(lock.isLocked()+"====");
            //isHeldByCurrentThread() 查询当前线程是否保持此锁。
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Throwable e) {
            String msg = String.format("UNLOCK FAILED: key=%s", lockKey);
            throw new IllegalStateException(msg, e);
        }


    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

//    public void setRedissonClient(RedissonClient redissonClient) {
//        this.redissonClient = redissonClient;
//    }
}
