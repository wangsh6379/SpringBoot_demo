package com.rabbitmq.springboot.rabbitmq.producer;

import com.rabbitmq.springboot.rabbitmq.config.MQConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者配置文件,创建死信队列
 * 业务：
 * 1、生产者发送消息给MQ后，通过配置过期时间，在过期时间内没有被消费，则会将消息发送给死信队列
 * 2、在消费者中，将设置channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false); 也会进入死信队列
 * @author wangsh
 * @date 2020年10月20日 11:33:59
 */
@Configuration
public class DeadProducer {


    // 声明业务Exchange
    @Bean("businessExchange")
    public FanoutExchange businessExchange(){
        return new FanoutExchange(MQConstant.HELLO_WORLD_MESSAGE_EXCHANGE);
    }

    // 声明死信Exchange
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(MQConstant.HELLO_WORLD_DEL_MESSAGE_EXCHANGE);
    }
    //配置业务队列queue
    @Bean("businessQueueA")
    public Queue businessQueueA() {
        Map<String, Object> args = new HashMap<>(3);
        //声明死信交换器
        args.put("x-dead-letter-exchange", MQConstant.HELLO_WORLD_DEL_MESSAGE_EXCHANGE);
        //声明死信路由键
        args.put("x-dead-letter-routing-key", MQConstant.HELLO_WORLD_DEL_MESSAGE_ROUTINGKEY);
        //声明队列消息过期时间 30分钟,30分钟不进行操作即可被丢到死信队列
        args.put("x-message-ttl", 5000);//这次用于测试5秒后会被放入死信队列
        return QueueBuilder.durable(MQConstant.HELLO_WORLD_MESSAGE_ROUTINGKEY).withArguments(args).build();
    }
    /**
     * 声明死信队列 queue
     *
     * @return
     */
    @Bean("deadLetterQueueA")
    public Queue deadLetterQueueA() {
        return new Queue(MQConstant.HELLO_WORLD_DEL_MESSAGE_ROUTINGKEY, true); //队列持久,不会丢失消息，只有消息确认后才会将消息丢掉1

    }

    /**
     * 将业务消息队列与业务交换机绑定
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding businessBindingA(@Qualifier("businessQueueA") Queue queue,
                                    @Qualifier("businessExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    // 声明死信队列A绑定关系
    @Bean
    public Binding deadLetterBindingA(@Qualifier("deadLetterQueueA") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MQConstant.HELLO_WORLD_DEL_MESSAGE_ROUTINGKEY);
    }
}