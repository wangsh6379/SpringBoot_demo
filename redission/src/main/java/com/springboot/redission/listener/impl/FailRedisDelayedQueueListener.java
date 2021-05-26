package com.springboot.redission.listener.impl;

import com.springboot.redission.listener.RedisDelayedQueueListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FailRedisDelayedQueueListener implements RedisDelayedQueueListener<Integer>{
    public static final Logger logger = LoggerFactory.getLogger(FailRedisDelayedQueueListener.class);


    @Override
    public void invoke(Integer integer) {
        logger.info("获取成团失败事件ID为:{}", integer);

    }
}
