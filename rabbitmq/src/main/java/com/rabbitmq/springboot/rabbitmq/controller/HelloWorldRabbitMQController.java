package com.rabbitmq.springboot.rabbitmq.controller;

import com.rabbitmq.springboot.rabbitmq.config.MQConstant;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("hello")
public class HelloWorldRabbitMQController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 重试队列，没有延迟，消息发给生产者 -> 消费者 -> 重试3次失败后 -> 死信队列 ->结束
     */
    @RequestMapping("rabbitMq")
    public void rabbitMq() {
        String message = "这是一条helloWorld消息，发送给MQ";
        CorrelationData data = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(MQConstant.HELLO_WORLD_MESSAGE_EXCHANGE, MQConstant.HELLO_WORLD_MESSAGE_ROUTINGKEY, message,data);

    }

    /**
     * 延迟队列，消息发给延迟队列等待，等待结束 -> 生产者 -> 消费者
     */
    @RequestMapping("delayMsg")
    public void delayMsg() {
        String message = "这是一条helloWorld消息，发送给MQ";
        CorrelationData data = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10S 的队列: " + message);
    }

    /**
     * 延迟重试队列，没有延迟，消息发给生产者 -> 消费者 -> 重试3次失败后 -> 等待队列 -> 等待时间结束后进入生产者
     *
     */
    @RequestMapping("againMsg")
    public void againMsg() {
        String message = "这是一条helloWorld消息，发送给MQ";
        CorrelationData data = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("AX", "AXA",message,data);
    }
}
