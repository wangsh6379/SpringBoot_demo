package com.redis.springboot.redis.controller;

import com.redis.springboot.redis.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("redis")
public class LockController {
    @Autowired
    private RedisService redisService;

    /**
     * redis实现分布式锁,保证根据顺序执行，防止由于线程安全问题引起的数据错乱
     * 本业务实现是在MQ中，将数据通过获取MQ数据进行操作。由于存在多个消费者，消费顺序不固定所导致的最后数据不正确问题。
     * 1、将MQ修改成顺序消费，将队列变成一个消费者。则肯定在执行同一条数据的时候没有其他消费者在执行。消费效率太低，当数据倍增的时候无法保证消费效率。冗余数据
     * 2、采用redis分布式锁进行，在消费当前数据的时候，通过数据的标识来判断当前数据是否正在被其他消费者所操作。进行等待，当没有其他消费者操作时再进行消费。
     */
    @RequestMapping("lock")
    public void lockRedis() {

        String rediskey = "updatePoints" + "accountID";

        Boolean flag = redisService.setIfAbsent(rediskey, 1, 2, TimeUnit.SECONDS);
        int wordcount = 0;
        while (wordcount < 22) {
            wordcount++;
            if (!flag) {
                flag = redisService.setIfAbsent(rediskey, 1, 2, TimeUnit.SECONDS);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("线程休眠出现问题");
                }
            } else {
                System.out.println("总共循环啦" + wordcount + "次");
                break;
            }
        }

        //如果是true则成功
        if (flag) {

        }
    }
}
