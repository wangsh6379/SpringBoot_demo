package com.rabbitmq.springboot.rabbitmq.receiver;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * 消费者实际操作消息文件
 *
 * @author wangshenghui
 * @date 2020年10月19日 16:08:10
 */
@Component
public class DelayReceiver {
    private static Logger logger = LoggerFactory.getLogger(DelayReceiver.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "QD")
    @RabbitHandler
    public void receiveD(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        logger.info("当前时间：{},收到死信队列信息{}", new Date().toString(), msg);

        //重新将消息放入X队列
//        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10S 的队列: " + msg);
    }
}
