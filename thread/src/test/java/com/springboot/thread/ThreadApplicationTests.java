package com.springboot.thread;

import com.springboot.thread.service.AsyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CountDownLatch;


@SpringBootTest
@EnableAsync
class ThreadApplicationTests {

    @Autowired
    AsyncService asyncService;

    /**
     * CountDownLatch是一个同步工具类，它允许一个或多个线程一直等待，直到其他线程执行完后再执行。
     * 它可以使主线程一直等到所有的子线程执行完之后再执行。我们修改下代码，创建一个CountDownLatch实例，
     * 大小是所有运行线程的数量，然后在异步类的方法中的finally里面对它进行减1，在主线程最后调用await()方法，
     * 这样就能确保所有的子线程运行完后主线程才会继续执行。
     */
    private final CountDownLatch countDownLatch = new CountDownLatch(20);


    @Test
    void contextLoads() {
        try {
            for (int i = 0; i < 20; i++) {
                asyncService.writeTxt(countDownLatch);
            }
            //返回当前计数器
            System.out.println(countDownLatch.getCount());

            //当计数器大于0则进行休眠等待状态。如果等于0则立即继续执行下去。
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
