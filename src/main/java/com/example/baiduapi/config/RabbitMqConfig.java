package com.example.baiduapi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String IDENTITY_Queue = "identityQueue";
    public static final String VOICE_Queue = "voiceQueue";
    public static final String PIC_Queue = "picQueue";
    public static final String FACE_Queue = "faceQueue";
    @Bean
    public Queue faceQueue() {
        return new Queue(FACE_Queue, true);
    }
    @Bean
    public Queue identityQueue() {
        return new Queue(IDENTITY_Queue, true);
    }
    @Bean
    public Queue voiceQueue() {
        return new Queue(VOICE_Queue, true);
    }
    @Bean
    public Queue picQueue() {
        return new Queue(PIC_Queue, true);
    }
}
