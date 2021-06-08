package com.springboot.thread.service.impl;

import com.springboot.thread.service.AsyncService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class AsyncServiceImpl implements AsyncService {
    private static Logger logger = LogManager.getLogger(AsyncServiceImpl.class.getName());

    /**
     * @Async 它是刚刚我们在线程池配置类的里的那个配制方法的名字，加上这个后每次执行这个方法都会开启一个线程放入线程池中。
     * 默认情况下，Spring 使用SimpleAsyncTaskExecutor去执行这些异步方法（此执行器没有限制线程数）
     * 可以使用方法级别覆盖与系统级别覆盖两种方式去限制线程的数量
     * 本次使用的是方法级别的覆盖，使用asyncServiceExecutor在config中配置最大线程数量。
     */
    @Override
    @Async
//            ("asyncServiceExecutor")
    public void writeTxt(CountDownLatch countDownLatch) {
        try {
            System.out.println("线程" + Thread.currentThread().getId() + "开始=====执行");
            for (int i=1;i<1000000000;i++){
                Integer integer = new Integer(i);
                int l = integer.intValue();
                for (int x=1;x<10;x++){
                    Integer integerx = new Integer(x);
                    int j = integerx.intValue();
                }
            }
            System.out.println("线程" + Thread.currentThread().getId() + "执行结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //减少锁存器的计数，如果计数达到零，释放所有等待的线程。
            // 如果当前计数大于零，则它将递减。 如果新计数为零，则所有等待的线程都将被重新启用以进行线程调度。
            // 如果当前计数等于零，那么没有任何反应。
            countDownLatch.countDown();
        }
    }
}
