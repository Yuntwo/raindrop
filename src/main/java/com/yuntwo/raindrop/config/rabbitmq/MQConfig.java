package com.yuntwo.raindrop.config.rabbitmq;

import com.yuntwo.raindrop.constant.RabbitMQConstant;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ具体配置，会执行创建队列、交换机已经绑定等操作
 */
@Configuration
public class MQConfig {

    @Bean
    public Queue reportQueue(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length", 1000);  //队列可以容纳的消息的最大条数
        args.put("x-max-length-bytes", 134217728);  //队列可以容纳的消息的最大字节数 512M
        return new Queue(RabbitMQConstant.QUEUE_REPORT,true, false, false, args);
    }

    @Bean
    public Queue likeQueue(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length", 5000);  //队列可以容纳的消息的最大条数
        args.put("x-max-length-bytes", 536870912);  //队列可以容纳的消息的最大字节数 128M
        return new Queue(RabbitMQConstant.QUEUE_LIKE,true, false, false, args);
    }

    @Bean
    public Queue verifyCodeQueue(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length", 5000);  //队列可以容纳的消息的最大条数
        args.put("x-max-length-bytes", 536870912);  //队列可以容纳的消息的最大字节数 128M
        return new Queue(RabbitMQConstant.QUEUE_VERIFY_CODE,true, false, false, args);
    }


    @Bean
    DirectExchange reportDirectExchange() {
        return new DirectExchange(RabbitMQConstant.EXCHANGE_DIRECT_REPORT,true,false);
    }

    @Bean
    DirectExchange likeDirectExchange() {
        return new DirectExchange(RabbitMQConstant.EXCHANGE_DIRECT_LIKE,true,false);
    }

    @Bean
    DirectExchange verifyCodeDirectExchange() {
        return new DirectExchange(RabbitMQConstant.EXCHANGE_VERIFY_CODE,true,false);
    }

    /**
     * 设置交换机收到的消息通过routingKey路由到指定的队列
     * 就是用routingKey绑定交换机与队列，这样到达交换机的消息可以通过routingKey到达队列
     */
    @Bean
    Binding bindingReportDirect(){
        return BindingBuilder.bind(reportQueue()).to(reportDirectExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_REPORT);
    }

    @Bean
    Binding bindingLikeDirect(){
        return BindingBuilder.bind(likeQueue()).to(likeDirectExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_LIKE);
    }

    @Bean
    Binding bindingVerifyCodeDirect(){
        return BindingBuilder.bind(verifyCodeQueue()).to(verifyCodeDirectExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_VERIFY_CODE);
    }

}
