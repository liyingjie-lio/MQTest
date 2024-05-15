package com.example.baiduapi.controller;

import com.example.baiduapi.mq.MessageConsumer;
import com.example.baiduapi.mq.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UploadController {
    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private MessageConsumer messageConsumer;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            // 去除前缀
            base64Image = base64Image.replaceAll("^data:image/[^;]+;base64,", "");
            String requestId = UUID.randomUUID().toString();
            System.out.println(requestId);
            messageProducer.sendMessage(requestId, base64Image);
            return requestId;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload image";
        }
    }

    @GetMapping("/result/{requestId}")
    public String getResult(@PathVariable String requestId) {
        return messageConsumer.getResult(requestId);
    }
}
