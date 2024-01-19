package org.example.rabbitmq.deadletterqueue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.rabbitmq.utils.RabbitMQUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者2
 *
 * @author stars
 */
public class Consumer02 {

    // 死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "UTE-8"));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
    }
}
