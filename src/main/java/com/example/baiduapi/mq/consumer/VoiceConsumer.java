package com.example.baiduapi.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.baiduapi.config.RabbitMqConfig;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VoiceConsumer {
    private static final String ACCESS_TOKEN = "24.7f030af00258d2c87ade6e6d420549be.2592000.1718337136.282335-70864859";
    private static final String API_URL = "http://vop.baidu.com/server_api";
    private static final ConcurrentHashMap<String, String> VoiceMap = new ConcurrentHashMap<>();

    @RabbitListener(queues = RabbitMqConfig.VOICE_Queue)
    public void receiveMessage(String message) {
        String[] parts = message.split("\\|", 2);
        String requestId = parts[0];
        String base64Image = parts[1];

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Content-Type", "application/json");

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("format","pcm");
            jsonBody.put("rate",16000);
            jsonBody.put("dev_pid",1537);
            jsonBody.put("channel",1);
            jsonBody.put("token",ACCESS_TOKEN);
            jsonBody.put("cuid","baidu_workshop");
            jsonBody.put("len", 129600);
            jsonBody.put("speech", base64Image);
            // 将JSON对象转换为字符串并设置为请求实体
            StringEntity entity = new StringEntity(jsonBody.toJSONString(), StandardCharsets.UTF_8);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            String result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

            VoiceMap.put(requestId, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResult(String requestId) {
        return VoiceMap.get(requestId);
    }
}
