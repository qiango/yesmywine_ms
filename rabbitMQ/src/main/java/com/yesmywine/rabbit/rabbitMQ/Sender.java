package com.yesmywine.rabbit.rabbitMQ;

import com.yesmywine.util.basic.JSONUtil;
import com.yesmywine.util.basic.ValueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by by on 2017/8/17.
 */
@Component
public class Sender implements RabbitTemplate.ConfirmCallback{
    private Logger logger = LoggerFactory.getLogger(Sender.class);
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入
     */
    @Autowired
    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }

    public void sendMsg(String exchange,String routineKey,Object content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routineKey, ValueUtil.toJson(content), correlationId);
    }

    public void sendMsg(Object content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE, AmqpConfig.ROUTINGKEY, content, correlationId);
    }

    /**
     * 回调
     */
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            logger.info("Rabbit 回调id:" + correlationData+"  消息成功消费");
        } else {
            logger.info("Rabbit 回调id:" + correlationData+"  消息消费失败:" + cause);
        }
    }

}
