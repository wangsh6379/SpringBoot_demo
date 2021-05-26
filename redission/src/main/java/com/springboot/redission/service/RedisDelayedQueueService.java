package com.springboot.redission.service;

import java.util.concurrent.TimeUnit;

public interface RedisDelayedQueueService {
    <T> void addQueue(T t, long delay, TimeUnit timeUnit, String queueName);
}
