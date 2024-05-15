package com.example.baiduapi.controller;

import com.example.baiduapi.mq.consumer.ObjectConsumer;
import com.example.baiduapi.mq.consumer.VoiceConsumer;
import com.example.baiduapi.mq.producer.ObjectProducer;
import com.example.baiduapi.mq.producer.VoiceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class VoiceController {
    @Autowired
    private VoiceProducer voiceProducer;

    @Autowired
    private VoiceConsumer voiceConsumer;


    @PostMapping("/voice")
    public String uploadVoice(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            // 去除前缀
            base64Image = base64Image.replaceAll("^data:image/[^;]+;base64,", "");
            Integer s = base64Image.length();
            System.out.println("length=================" + s);
            String requestId = UUID.randomUUID().toString();
            System.out.println(requestId);
            voiceProducer.sendMessage(requestId, base64Image);
            return requestId;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload image";
        }
    }

    @GetMapping("/voice/{requestId}")
    public String getResult(@PathVariable String requestId) {
        return voiceConsumer.getResult(requestId);
    }
}
