package com.example.rabbitmqspringboottest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延时消息
 *
 * @author stars
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    private RabbitTemplate rabbitTemplate;

    // 开始发送消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个TTL队列:{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("");
    }
}
