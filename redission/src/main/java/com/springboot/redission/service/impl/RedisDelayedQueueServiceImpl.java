package com.springboot.redission.service.impl;

import com.springboot.redission.service.RedisDelayedQueueService;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisDelayedQueueServiceImpl implements RedisDelayedQueueService {

    @Autowired
    RedissonClient redissonClient;

    public static final Logger logger = LoggerFactory.getLogger(RedisDelayedQueueServiceImpl.class);

    /**
     * 添加队列
     *
     * @param t        DTO传输类
     * @param delay    超时时间
     * @param timeUnit 时间单位
     */
    public <T> void addQueue(T t, long delay, TimeUnit timeUnit, String queueName) {
        logger.info("添加队列{},delay:{},timeUnit:{}" + queueName, delay, timeUnit);


        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(queueName);

        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);

        delayedQueue.offer(t, delay, timeUnit);
    }
}
