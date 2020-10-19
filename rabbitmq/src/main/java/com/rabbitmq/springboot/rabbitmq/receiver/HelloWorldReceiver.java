package com.rabbitmq.springboot.rabbitmq.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.springboot.rabbitmq.config.MQConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 消费者实际操作消息文件
 *
 * @author wangshenghui
 * @date 2020年10月19日 16:08:10
 */
@Component
public class HelloWorldReceiver {


    @RabbitListener(queues = MQConstant.HELLO_WORLD_MESSAGE_ROUTINGKEY, containerFactory = "singleListenerContainer")
    @RabbitHandler
    public void orderUpdatePointsProcess(Channel channel, Message message) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("=========开始处理消息，message content: {}" + message);
        try {
            String body = new String(message.getBody(), "UTF-8");
            System.out.println("消费者得到的消息是：" + body);
            boolean flag = false;
            if (!StringUtils.isEmpty(body)) {
                flag = false;
            }
//            1 当channel.basicNack 第三个参数设为true时，消息签收失败会继续进入消息队列等待消费
//            2 当channel.basicNack 第三个参数设为false时，消息签收失败,此时消息进入死信队列，完成消费
            if (flag) {
                System.out.println("处理成功！"+message);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println("处理失败！"+message);
                Thread.sleep(2000);
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);//失败后继续放入消息队列等待消费
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);//失败后继续放入死信队列

            }
        } catch (Exception e) {
            System.out.println("===== 处理消息 RabbitMQ 失败，message content: {}" + message);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            e.printStackTrace();
        }
        System.out.println("=========结束处理操作,执行耗时：{}ms，message content: {}"+ message +(System.currentTimeMillis() - startTime));
    }


    /**
     * 死信队列.
     *
     * @param message
     */
    @RabbitListener(queues = MQConstant.HELLO_WORLD_DEL_MESSAGE_ROUTINGKEY, containerFactory = "singleListenerContainer")
    public void dealSubscribe(Message message, Channel channel) throws IOException {
        System.out.println("消息进入死信队列:" + new String(message.getBody(), "UTF-8"));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }
}
