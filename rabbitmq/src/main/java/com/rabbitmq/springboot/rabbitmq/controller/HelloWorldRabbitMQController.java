package com.rabbitmq.springboot.rabbitmq.controller;

import com.rabbitmq.springboot.rabbitmq.config.MQConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloWorldRabbitMQController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("rabbitMq")
    public void rabbitMq() {
        String message = "这是一条helloWorld消息，发送给MQ";
        rabbitTemplate.convertAndSend(MQConstant.HELLO_WORLD_MESSAGE_EXCHANGE, MQConstant.HELLO_WORLD_MESSAGE_ROUTINGKEY, message);

    }
}
