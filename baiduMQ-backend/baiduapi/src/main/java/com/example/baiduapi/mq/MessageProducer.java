package com.example.baiduapi.mq;

import com.example.baiduapi.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param requestId
     * @param base64Image
     */
    public void sendMessage(String requestId, String base64Image) {
        String message = requestId + "|" + base64Image;
        rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE_NAME, message);
    }
}
