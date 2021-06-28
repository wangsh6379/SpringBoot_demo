package com.springboot.redission.controller;

import com.springboot.redission.redlock.DistributedLocker;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * redisson 读写锁、适用于多写少业务
 * @author wangsh
 */
@RestController
@RequestMapping("/")
public class ReadWriteLockController {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private DistributedLocker distributedLocker;


    /**
     * 读锁.当先访问 写锁，写锁会线程等待5S中后进行修改Key的内容。
     * 在写锁线程等待中访问读锁，我们会发现在访问的同时，读锁会等待进入线程等待。等待写锁操作完毕后获取内容。
     * 由此可看出，读写在执行的同时是互斥锁，仅只有一个在执行，另一个会进行等待对方结束。由此读锁拿到的是最新的数据
     * @return
     */
    @RequestMapping("/read")
    public String read() {

        String lockName = "lockDemo";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockName);
        RLock rLock = readWriteLock.readLock();

        rLock.lock(10, TimeUnit.SECONDS);
        try {
            RBucket<Object> lockName1 = redissonClient.getBucket("lockName");
            Object o = lockName1.get();
            System.out.println(o);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return "ok";
    }

    /**
     * 写锁
     *
     * @return
     */
    @RequestMapping("/write")
    public String write(String name) {
        String lockName = "lockDemo";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockName);
        RLock rLock = readWriteLock.writeLock();
        rLock.lock(10,TimeUnit.SECONDS);
        try {
            RBucket<Object> lockName1 = redissonClient.getBucket("lockName");
            Thread.sleep(5000);
            lockName1.set("nanjing3345");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return "ok";
    }
}
