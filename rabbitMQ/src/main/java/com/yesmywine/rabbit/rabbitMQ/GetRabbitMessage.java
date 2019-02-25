package com.yesmywine.rabbit.rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by by on 2017/8/29.
 */
@Component
public class GetRabbitMessage {
    @Autowired
    private AmqpConfig amqpConfig;

    public String getMessage(String queue) throws IOException, InterruptedException {
        Connection connection = amqpConfig.connection();
        Channel channel = connection.createChannel(false);
        String message = null;
        /* 声明要连接的队列 */
        channel.queueDeclare();
        GetResponse response = channel.basicGet(queue, false);
        if(response!=null){
            byte[] body = response.getBody();
            Long deliveryTag = response.getEnvelope().getDeliveryTag();
            channel.basicAck(deliveryTag,false);
            message = new String(body);
        }
        try {
            channel.close();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        connection.close();
        return message;
    }
}
