package com.springboot.redission.config;

import com.springboot.redission.redlock.DistributedLocker;
import com.springboot.redission.redlock.RedissonDistributedLocker;
//import com.springboot.redission.utils.RedissonLockUtil;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonLockConfig {
    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     *
     * @return
     */
    @Bean
    DistributedLocker distributedLocker(RedissonClient redissonClient) {
        DistributedLocker locker = new RedissonDistributedLocker();
//        ((RedissonDistributedLocker) locker).setRedissonClient(redissonClient);
//        RedissonLockUtil.setLocker(locker);
        return locker;
    }
}
