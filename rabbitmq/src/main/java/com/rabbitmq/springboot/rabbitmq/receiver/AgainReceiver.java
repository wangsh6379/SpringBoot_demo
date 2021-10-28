package com.rabbitmq.springboot.rabbitmq.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.springboot.rabbitmq.config.MQConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消费者实际操作消息文件
 *
 * @author wangshenghui
 * @date 2020年10月19日 16:08:10
 */
@Component
public class AgainReceiver {
    private static Logger logger = LoggerFactory.getLogger(AgainReceiver.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "AQA")
    @RabbitHandler
    public void receiveD(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        logger.info("当前时间：{},消费者收到生产者队列信息{}", new Date().toString(), msg);
        String correlationData = "";
        int count = 0; //重试次数

        boolean flag = false;

        //重新将消息放入延迟等待队列
        //获取消息唯一ID号
        correlationData =
                (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        logger.info("correlationData：" + correlationData);

        if (!StringUtils.isEmpty(msg)) {
            flag = true;
        }
        if (flag) {
            logger.info("处理成功！" + message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            logger.info("处理失败！" + message);
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            if (headers.containsKey("failed_count_for_send_to_exchange")) {
                count = (int) headers.get("failed_count_for_send_to_exchange");
            } else {
                count = 0;
            }

            //重试次数大于三次则将消息放入延迟重试队列
            if (count >= 3) {
                rabbitTemplate.convertAndSend("AY", "AYD", msg);
            } else {
                count++;
                message.getMessageProperties().getHeaders().put("failed_count_for_send_to_exchange", count);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                // 重试次数不超过3次,则将消息发送到重试队列等待重新被消费
                rabbitTemplate.convertAndSend("AX", "AXA", message, new CorrelationData(correlationData));
                logger.info("消费失败，重试消息队列;" + "原始消息：" + new String(message.getBody()) + ";第"
                        + (count) + "次重试");
            }
        }
    }

//    @RabbitListener(queues = "AQD", containerFactory = "singleListenerContainer")
//    public void dealSubscribe(Message message, Channel channel) throws IOException {
//        logger.info("消息进入延迟等待队列:" + new String(message.getBody(), "UTF-8"));
//    }
}
