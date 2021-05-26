package com.springboot.redission.contextAware;

import com.springboot.redission.listener.RedisDelayedQueueListener;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RedisDelayedQueueContextAware implements ApplicationContextAware {

    public static final Logger logger = LoggerFactory.getLogger(RedisDelayedQueueContextAware.class);

    @Autowired
    RedissonClient redissonClient;

    /**
     * 初始化监听队列
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, RedisDelayedQueueListener> map = applicationContext.getBeansOfType(RedisDelayedQueueListener.class);
        for (Map.Entry<String, RedisDelayedQueueListener> taskEventListenerEntry : map.entrySet()) {
            String listenerName = taskEventListenerEntry.getValue().getClass().getName();
            startThread(listenerName, taskEventListenerEntry.getValue());
        }


    }

    /**
     * 启动线程获取队列*
     *
     * @param queueName                 queueName
     * @param redisDelayedQueueListener 任务回调监听
     * @param <T>                       泛型
     * @return
     */
    private <T> void startThread(String queueName, RedisDelayedQueueListener redisDelayedQueueListener) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(queueName);

        //TODO 这里后续进行更改为线程池的方法

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //executorService.su

        //由于此线程需要常驻，可以新建线程，不用交给线程池管理
        Thread thread = new Thread(() -> {
            logger.info("启动监听队列线程" + queueName);
            while (true) {
                try {
                    T t = blockingFairQueue.take();
                    //logger.info("监听队列线程{},获取到值:{}", queueName, JSON.toJSONString(t));
//                    new Thread(() -> {
//                        redisDelayedQueueListener.invoke(t);
//                    }).start();
                    //这样实际还是单线程在跑
                    executorService.submit(new Thread(() -> {
                        redisDelayedQueueListener.invoke(t);

                    }));

                } catch (Exception e) {
                    logger.info("监听队列线程错误:{},", e);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        thread.setName(queueName);
        thread.start();
    }
}
