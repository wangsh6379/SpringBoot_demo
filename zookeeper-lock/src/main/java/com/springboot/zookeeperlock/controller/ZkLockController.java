package com.springboot.zookeeperlock.controller;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("zk")
public class ZkLockController {

    @Autowired
    private CuratorFramework curatorFramework;

    private int  count;
    @RequestMapping("lock")
    public String lock(){
        String lockPath = "/lock";
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, lockPath);
        this.count = 0;
        //模拟50个线程抢锁
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                try {
                    lock.acquire();

                    //等到1秒后释放锁
                    Thread.sleep(100);
                    this.count++;
                    System.out.println("第"+Thread.currentThread().getName()+"线程获取到了锁,COUNT"+count);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return "OK";
    }
}
