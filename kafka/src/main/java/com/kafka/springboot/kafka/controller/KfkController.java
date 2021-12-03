package com.kafka.springboot.kafka.controller;

import com.kafka.springboot.kafka.service.KfkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author lpg
 * @description: 测试kfk生产与消费
 * @date 2020-12-2318:22
 */
@RestController
public class KfkController {
    @Autowired
    private KfkService kfkService;

    @GetMapping("/send")
    public String send() {
        kfkService.sendMsg("topic1", "I am topic msg111111");
        kfkService.sendMsg("topic1", "I am topic msg222222");
        return "success-topic1";
    }
}
