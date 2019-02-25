package com.yesmywine.rabbit.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * Created by by on 2017/8/29.
 * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
 */

public class FanoutRabbitConfig {
    @Bean
    public Queue fanoutAQueue() {
        return new Queue("fanout.a");
    }

    @Bean
    public Queue fanoutBQueue() {
        return new Queue("fanout.b");
    }

    @Bean
    public Queue fanoutCQueue() {
        return new Queue("fanout.c");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    public Binding bindingExchangeFanoutA(Queue fanoutAQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutAQueue).to(fanoutExchange);
    }

    @Bean
    public Binding bindingExchangeFanoutB(Queue fanoutBQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutBQueue).to(fanoutExchange);
    }

    @Bean
    public Binding bindingExchangeFanoutC(Queue fanoutCQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutCQueue).to(fanoutExchange);
    }


    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/8/29
    *@Param
    *@Description:以下为测试
    */

//    2. 发送者与接收者
//
//    // 发送者
//    public void send(String message) {
//        rabbitTemplate.convertAndSend("fanoutExchange", "", message);
//        logger.info(String.format("send fanout message: %s", message));
//    }
//
//...
//
//    // 接收者A
//    @RabbitListener(queues = "fanout.a")
//    public void process(String message) {
//        logger.info(String.format("receive fanout a message: %s", message));
//    }
//
//...
//
//    // 接收者B
//    @RabbitListener(queues = "fanout.b")
//    public void process(String message) {
//        logger.info(String.format("receive fanout b message: %s", message));
//    }
//
//...
//
//    // 接收者C
//    @RabbitListener(queues = "fanout.c")
//    public void process(String message) {
//        logger.info(String.format("receive fanout c message: %s", message));
//    }



//    3. 测试结果
//
//    接收者A、B、C均接收到信息。
//
//    send fanout message: email
//    receive fanout b message: email
//    receive fanout a message: email
//    receive fanout c message: email

}
