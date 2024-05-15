package com.example.baiduapi.controller;

import com.example.baiduapi.mq.consumer.IdentityConsumer;
import com.example.baiduapi.mq.producer.IdentityProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.UUID;
@RestController
@RequestMapping("/api")
public class IdentityController {
    @Autowired
    private IdentityProducer identityProducer;

    @Autowired
    private IdentityConsumer identityConsumer;
    @PostMapping("/identity")
    public String uploadIdentity(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            // 去除前缀
            base64Image = base64Image.replaceAll("^data:image/[^;]+;base64,", "");
            String requestId = UUID.randomUUID().toString();
            System.out.println(requestId);
            identityProducer.sendMessage(requestId, base64Image);
            return requestId;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload image";
        }
    }

    @GetMapping("/identity/{requestId}")
    public String getIdentity(@PathVariable String requestId) {
        return identityConsumer.getResult(requestId);
    }
}
