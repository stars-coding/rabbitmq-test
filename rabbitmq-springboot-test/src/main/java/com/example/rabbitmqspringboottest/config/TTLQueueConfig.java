package com.example.rabbitmqspringboottest.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TTLQueueConfig {

    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列的名称
    public static final String NORMAL_QUEUE_A = "normal_queue_a";
    public static final String NORMAL_QUEUE_B = "normal_queue_b";
    // 死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    // 声明普通交换机
    @Bean("normalExchange")
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    // 声明死信交换机
    @Bean("deadExchange")
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    // 声明普通队列A
    @Bean("normalQueueA")
    public Queue normalQueueA() {
        Map<String, Object> arguments = new HashMap<>(4);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        // 设置TTL 单位毫秒
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE_A).withArguments(arguments).build();
    }

    // 声明普通队列B
    @Bean("normalQueueB")
    public Queue normalQueueB() {
        Map<String, Object> arguments = new HashMap<>(4);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        // 设置TTL 单位毫秒
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(NORMAL_QUEUE_B).withArguments(arguments).build();
    }

    // 死信队列
    @Bean("deadQueue")
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    // 绑定
    @Bean
    public Binding normalQueueABindingNormalExchange(@Qualifier("normalQueueA") Queue normalQueueA,
                                                     @Qualifier("normalExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(normalQueueA).to(directExchange).with("XA");
    }

    // 绑定
    @Bean
    public Binding normalQueueBBindingNormalExchange(@Qualifier("normalQueueB") Queue normalQueueB,
                                                     @Qualifier("normalExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(normalQueueB).to(directExchange).with("XB");
    }

    // 绑定
    @Bean
    public Binding deadQueueBindingDeaDExchange(@Qualifier("deadQueue") Queue deadQueue,
                                                     @Qualifier("deadExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(deadQueue).to(directExchange).with("YD");
    }
}
