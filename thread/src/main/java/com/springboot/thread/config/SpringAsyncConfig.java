package com.springboot.thread.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

/**
 * 应用级别覆盖默认的@Async注解不限制线程数量的问题。
 */
//@Configuration
//@EnableAsync
//public class SpringAsyncConfig implements AsyncConfigurer {
//
//    @Override
//    public Executor getAsyncExecutor() {
//         ThreadPoolTaskExecutor executor= new ThreadPoolTaskExecutor();
//         executor.initialize();
//         executor.setCorePoolSize(5);
//         executor.setMaxPoolSize(10);
//         executor.setQueueCapacity(25);
//         return executor;
//    }
//}
