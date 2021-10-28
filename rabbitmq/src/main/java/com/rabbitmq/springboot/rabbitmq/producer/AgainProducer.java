package com.rabbitmq.springboot.rabbitmq.producer;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者配置文件,创建延迟重试队列
 * 业务：
 * 1、生产者发送消息给消费者，消费者将消息进行处理。
 * 2、如果处理失败，将消息发送给延迟队列，延迟队列会进行等待
 * 2、延迟中的消息进行等待时间，时间结束后自动进入消费队列，重新进行消费
 *
 * @author wangsh
 * @date 2021年10月28日 17:59:09
 */
@Configuration
public class AgainProducer {

    public static final String X_EXCHANGE = "AX";
    public static final String QUEUE_A = "AQA";
    public static final String Y_AGAIN_LETTER_EXCHANGE = "AY";
    public static final String AGAIN_LETTER_QUEUE = "AQD";

    // 声明 xExchange
    @Bean("axExchange")
    public DirectExchange axExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    // 声明 xExchange
    @Bean("ayExchange")
    public DirectExchange ayExchange() {
        return new DirectExchange(Y_AGAIN_LETTER_EXCHANGE);
    }

    //声明队列 A
    @Bean("aqueueA")
    public Queue aqueueA() {
        return new Queue(QUEUE_A);
    }

    // 声明队列 A 绑定 X 交换机
    @Bean
    public Binding aqueueaBindingX(@Qualifier("aqueueA") Queue queueA,
                                  @Qualifier("axExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("AXA");
    }



    //声明延迟等待队列AQD，当业务队列将消息发给延迟等待队列，将进行等待，等待结束后发给业务队列
    @Bean("aqueueD")
    public Queue aqueueD() {
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", X_EXCHANGE);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", "AXA");
        //声明队列的 TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(AGAIN_LETTER_QUEUE).withArguments(args).build();

    }

    //声明死信队列 QD 绑定关系
    @Bean
    public Binding againLetterBindingQAD(@Qualifier("aqueueD") Queue queueD,
                                        @Qualifier("ayExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("AYD");
    }
}