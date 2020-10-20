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
import java.util.List;
import java.util.Map;

/**
 * 消费者实际操作消息文件
 *
 * @author wangshenghui
 * @date 2020年10月19日 16:08:10
 */
@Component
public class HelloWorldReceiver {
    private static Logger logger = LoggerFactory.getLogger(HelloWorldReceiver.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 当设置channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false); 失败后放入死信队列
     *
     * @param channel
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = MQConstant.HELLO_WORLD_MESSAGE_ROUTINGKEY, containerFactory = "singleListenerContainer")
    @RabbitHandler
    public void orderUpdatePointsProcess(Channel channel, Message message) throws Exception {
        long startTime = System.currentTimeMillis();
        boolean flag = false;
        int count = 0; //重试次数
        String correlationData = "";
        try {

            String body = new String(message.getBody(), "UTF-8");
//            long retryCount = getRetryCount(message.getMessageProperties());
//            System.out.println(" 当前消息重试次数：" + retryCount + "=========开始处理消息，message content: {}" + message);

            //获取消息唯一ID号
            correlationData =
                    (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
            System.out.println("correlationData：" + correlationData);

            //可以通过correlationID进行存储redis进行消息幂等，判断该消息是否执行过
            //redis消息幂等

            //消息执行结果是否成功
            if (!StringUtils.isEmpty(body)) {
                flag = false;
            }
//            1 当channel.basicNack 第三个参数设为true时，消息签收失败会继续进入消息队列等待消费
//            2 当channel.basicNack 第三个参数设为false时，消息签收失败,此时消息进入死信队列，完成消费

        } catch (Exception e) {
            System.out.println("===== 处理消息 RabbitMQ 失败，message content: {}" + message);
            flag = false;
            e.printStackTrace();
        } finally {
            if (flag) {
                System.out.println("处理成功！" + message);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println("处理失败！" + message);
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);//失败后继续放入消息队列等待消费

                //rabbitmq headers头可存储消息体，重试次数
                Map<String, Object> headers = message.getMessageProperties().getHeaders();
                if (headers.containsKey("failed_count_for_send_to_exchange")) {
                    count = (int) headers.get("failed_count_for_send_to_exchange");
                } else {
                    count = 0;
                }

                //重试次数大于三次则将消息放入死信队列
                if (count >= 3) {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);//失败后继续放入死信队列
                } else {
                    count++;
                    message.getMessageProperties().getHeaders().put("failed_count_for_send_to_exchange", count);
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    // 重试次数不超过3次,则将消息发送到重试队列等待重新被消费
                    rabbitTemplate.convertAndSend(MQConstant.HELLO_WORLD_MESSAGE_EXCHANGE, MQConstant.HELLO_WORLD_MESSAGE_ROUTINGKEY, message, new CorrelationData(correlationData));
                    logger.info("消费失败，重试消息队列;" + "原始消息：" + new String(message.getBody()) + ";第"
                            + (count) + "次重试");
                }
            }
            System.out.println("=========结束处理操作,执行耗时：{}ms，message content: {}" + message + (System.currentTimeMillis() - startTime));
        }
    }

    /**
     * 死信队列.
     *
     * @param message
     */
    @RabbitListener(queues = MQConstant.HELLO_WORLD_DEL_MESSAGE_ROUTINGKEY, containerFactory = "singleListenerContainer")
    public void dealSubscribe(Message message, Channel channel) throws IOException {
        logger.info("消息进入死信队列:" + new String(message.getBody(), "UTF-8"));

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }

    /**
     * 获取消息被重试的次数,目前没有找到可以获取消费次数办法。下面的方法网上查询所得。目前没有证实是否可用
     */
    public long getRetryCount(MessageProperties messageProperties) {
        Long retryCount = 0L;
        if (null != messageProperties) {
            List<Map<String, ?>> deaths = messageProperties.getXDeathHeader();
            if (deaths != null && deaths.size() > 0) {
                Map<String, Object> death = (Map<String, Object>) deaths.get(0);
                retryCount = (Long) death.get("count");
            }
        }
        return retryCount;
    }

}
