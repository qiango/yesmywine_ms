package com.yesmywine.rabbit.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by by on 2017/8/29.
 */
@Configuration
public class TopicRabbitConfig {

    @Bean
    public Queue topicAQueue() {
        return new Queue("topic.a");
    }

    @Bean
    public Queue topicAnyQueue() {
        return new Queue("topic.any");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Binding bindingExchangeTopicA(Queue topicAQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicAQueue).to(topicExchange).with("topic.a");
    }

    @Bean
    public Binding bindingExchangeTopicAny(Queue topicAnyQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicAnyQueue).to(topicExchange).with("topic.#");
    }


    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/8/29
    *@Param
    *@Description:以下为测试
    *
    */

//    // 发送者A
//    public void send(String message) {
//        rabbitTemplate.convertAndSend("topicExchange", "topic.a", message);
//        logger.info(String.format("send topic a message: %s", message));
//    }
//
//...
//
//    // 发送者B
//    public void send(String message) {
//        rabbitTemplate.convertAndSend("topicExchange", "topic.b", message);
//        logger.info(String.format("send topic b message: %s", message));
//    }
//
//...
//
//    // 发送者Any
//    public void send(String message) {
//        rabbitTemplate.convertAndSend("topicExchange", "topic.any", message);
//        logger.info(String.format("send topic any message: %s", message));
//    }
//
//...
//
//    // 接收者A
//    @RabbitListener(queues = "topic.a")
//    public void process(String message) {
//        logger.info(String.format("receive topic a message: %s", message));
//    }
//
//...
//
//    // 接收者Any
//    @RabbitListener(queues = "topic.any")
//    public void process(String message) {
//        logger.info(String.format("receive topic any message: %s", message));
//    }

//3. 测试结果
//
//    接收到A接收到发送者A的信息。
//
//    接收者Any接收到发送者A、发送者B、发送者Any的信息。
//
//    send topic a message: tag
//    receive topic a message: tag
//    receive topic any message: tag
//
//...
//
//    send topic b message: tag
//    receive topic any message: tag
//
//...
//
//    send topic any message: tag
//    receive topic any message: tag

}
