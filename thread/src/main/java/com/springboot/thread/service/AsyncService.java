package com.springboot.thread.service;

import java.util.concurrent.CountDownLatch;

public interface AsyncService {

    /**
     *  执行异步任务
     */
    void writeTxt(CountDownLatch countDownLatch);
}
