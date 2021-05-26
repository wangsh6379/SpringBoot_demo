//package com.springboot.redission.config;
//
//
//import com.springboot.redission.config.common.RedissonProperties;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableConfigurationProperties(RedissonProperties.class)
//public class RedissonConfig {
//
//    @Autowired
//    private RedissonProperties redissonProperties;
//
//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        config.useSingleServer()
//                .setAddress(redissonProperties.getAddress())
//                .setPassword(redissonProperties.getPassword())
//                .setDatabase(redissonProperties.getDatabase())
//                .setTimeout(redissonProperties.getTimeout())
//                .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
//                .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
//        return Redisson.create(config);
//    }
//
//}
