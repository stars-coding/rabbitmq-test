package org.example.rabbitmq.deadletterqueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.example.rabbitmq.utils.RabbitMQUtils;

/**
 * 生产者
 *
 * @author stars
 */
public class Producer {

    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {

        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();

        // 设置TTL时间，单位毫秒，参数，用于指定消息存活时间
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 0; i < 10; i++) {
            String massage = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "normal-binding-key", properties, massage.getBytes());
        }
    }
}
