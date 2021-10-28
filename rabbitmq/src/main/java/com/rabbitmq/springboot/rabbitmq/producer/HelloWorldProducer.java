//package com.rabbitmq.springboot.rabbitmq.producer;
//
//import com.rabbitmq.springboot.rabbitmq.config.MQConstant;
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 生产者配置文件,普通生产者
// *
// * @author wangshenghui
// * @date 2020年10月19日 16:07:35
// */
//@Configuration
//public class HelloWorldProducer {
//
//    /**
//     * 配置消息交换机
//     * 针对消费者配置
//     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
//     * HeadersExchange ：通过添加属性key-value匹配
//     * DirectExchange:按照routingkey分发到指定队列
//     * TopicExchange:多关键字匹配
//     */
//    @Bean
//    public DirectExchange helloWorldExchange() {
//        return new DirectExchange(MQConstant.HELLO_WORLD_MESSAGE_EXCHANGE, true, false);
//    }
//
//    /**
//     * 配置消息队列
//     * 针对消费者配置
//     *
//     * @return
//     */
//    @Bean
//    public Queue helloWorldQueue() {
//        return new Queue(MQConstant.HELLO_WORLD_MESSAGE_ROUTINGKEY, true); //队列持久,不会丢失消息，只有消息确认后才会将消息丢掉1
//    }
//
//    /**
//     * 将消息队列与交换机绑定
//     * 针对消费者配置
//     *
//     * @return
//     */
//    @Bean
//    public Binding helloWorldBinding() {
//        return BindingBuilder.bind(helloWorldQueue()).to(helloWorldExchange()).with(MQConstant.HELLO_WORLD_MESSAGE_ROUTINGKEY);
//    }
//}