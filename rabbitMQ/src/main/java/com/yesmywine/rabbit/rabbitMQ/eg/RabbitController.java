package com.yesmywine.rabbit.rabbitMQ.eg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;
import com.yesmywine.rabbit.rabbitMQ.AmqpConfig;
import com.yesmywine.rabbit.rabbitMQ.GetRabbitMessage;
import com.yesmywine.rabbit.rabbitMQ.RabbitContants;
import com.yesmywine.rabbit.rabbitMQ.Sender;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by by on 2017/8/25.
 */
@RestController
@RequestMapping("/web/rabbit")
public class RabbitController {
    @Autowired
    private Sender sender;

    @Autowired
    private GetRabbitMessage getRabbitMessage;

    @RequestMapping
    public String sender(){
        for(int i=0;i<20;i++){
            sender.sendMsg(i);
        }
        return null;
    }

    @RequestMapping(value = "/res")
    public String reserve(){
        try {
            return getRabbitMessage.getMessage(RabbitContants.PRESELL);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}

