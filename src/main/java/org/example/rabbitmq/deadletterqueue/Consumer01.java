package org.example.rabbitmq.deadletterqueue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.rabbitmq.utils.RabbitMQUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者1
 *
 * @author stars
 */
public class Consumer01 {

    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列的名称
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();

        // 声明普通交换机，类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明死信交换机，类型为direct
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 死信转发依据
        Map<String, Object> arguments = new HashMap<>();
        // 1、转发到哪个交换机？
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 2、依据哪个路由RoutingKey？
        arguments.put("x-dead-letter-routing-key", "deal-binding-key");
        // 3、消息多久过期称为死信？缺点：TTL写死了
        // arguments.put("x-message-ttl", 10000);

        // 声明普通队列
        // 消息称为死信后，依据参数将死信转发到死信交换机
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定关系bindingkey
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "normal-binding-key");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "dead-binding-key");


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "UTE-8"));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, cancelCallback);
    }
}
