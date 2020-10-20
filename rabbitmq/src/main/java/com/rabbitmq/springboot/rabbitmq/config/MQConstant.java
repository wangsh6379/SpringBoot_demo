package com.rabbitmq.springboot.rabbitmq.config;

/**
 * MQ 常量
 *
 * @author wangsh
 * @date 2020年10月19日 16:10:00
 */
public class MQConstant {
    // 交换机
    public static final String HELLO_WORLD_MESSAGE_EXCHANGE = "hello_world_message_exchange";
    // 实际队列Key
    public static final String HELLO_WORLD_MESSAGE_ROUTINGKEY = "hello_world_message_routingkey";

    //死信交换机
    public static final  String HELLO_WORLD_DEL_MESSAGE_EXCHANGE = "hello_del_message_exchange";
    public static final String HELLO_WORLD_DEL_MESSAGE_ROUTINGKEY = "hello_del_message_routingkey";

}
