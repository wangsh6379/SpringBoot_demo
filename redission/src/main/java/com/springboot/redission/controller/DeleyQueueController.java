package com.springboot.redission.controller;


import com.springboot.redission.listener.impl.FailRedisDelayedQueueListener;
import com.springboot.redission.service.RedisDelayedQueueService;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * redis延迟队列实现延迟接收消息通知
 * <p>
 * 实时性: 允许存在一定时间内的秒级误差
 * 高可用性:支持单机,支持集群
 * 支持消息删除:业务费随时删除指定消息
 * 消息可靠性: 保证至少被消费一次
 * 消息持久化: 基于Redis自身的持久化特性,上面的消息可靠性基于Redis的持久化,所以如果redis数据丢失,意味着延迟消息的丢失,不过可以做主备和集群保证;
 */
@RestController
@RequestMapping("deley")
public class DeleyQueueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleyQueueController.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    RedisDelayedQueueService redisDelayedQueueService;

    @RequestMapping("producer")
    public String pruducer() {
//        long ttl = DateUtil.between(DateUtil.date(), parse, DateUnit.SECOND, false) + random.nextInt(60);
        redisDelayedQueueService.addQueue(1000, 10, TimeUnit.SECONDS, FailRedisDelayedQueueListener.class.getName());
        return null;
    }
}
