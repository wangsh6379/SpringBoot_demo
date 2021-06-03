package com.springboot.redission.controller;

import com.springboot.redission.redlock.DistributedLocker;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lock")
public class LockController {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private DistributedLocker distributedLocker;
    @RequestMapping("joinGroup")
    public String joinGroup(){
        String lockName = "lockDemo";
        RLock lock = distributedLocker.lock(lockName);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            distributedLocker.unlock(lockName);
        }
        return "ok";
    }

    /**
     * 设置加锁后2秒后解锁
     * @return
     */
    @RequestMapping("joinTimeoutGroup")
    public String joinTimeoutGroup(){
        String lockName = "lockDemo";
        RLock lock = distributedLocker.lock(lockName,3);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            distributedLocker.unlock(lockName);
        }
        return "ok";
    }
}
