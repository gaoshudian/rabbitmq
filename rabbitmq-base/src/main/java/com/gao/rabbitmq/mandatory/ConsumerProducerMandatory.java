package com.gao.rabbitmq.mandatory;

import com.gao.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者——失败确认模式(消费者只绑定了king)
 */
public class ConsumerProducerMandatory {

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(ProducerMandatory.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        //只关注king的
        String routekey="king";
        channel.queueBind(queueName, ProducerMandatory.EXCHANGE_NAME, routekey);

        System.out.println(" [*] Waiting for messages......");

        // 创建队列消费者
        final Consumer consumerB = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                //记录日志到文件：
                System.out.println( "Received ["+ envelope.getRoutingKey() + "] "+message);
            }
        };
        channel.basicConsume(queueName, true, consumerB);
    }
}