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
public class FaceConsumer {
    private static final String ACCESS_TOKEN = "24.a1ad2eac633c9749b7a2414d9e8ad11a.2592000.1718349090.282335-70917699";
    private static final String API_URL = "https://aip.baidubce.com/rest/2.0/face/v3/detect?access_token=" + ACCESS_TOKEN;
    private static final ConcurrentHashMap<String, String> FaceMap = new ConcurrentHashMap<>();

    @RabbitListener(queues = RabbitMqConfig.FACE_Queue)
    public void receiveMessage(String message) {
        String[] parts = message.split("\\|", 2);
        String requestId = parts[0];
        String base64Image = parts[1];

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Content-Type", "application/json");
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("image", base64Image);
            jsonBody.put("image_type", "BASE64");

            // 将JSON对象转换为字符串并设置为请求实体
            StringEntity entity = new StringEntity(jsonBody.toJSONString(), StandardCharsets.UTF_8);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            String result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

            FaceMap.put(requestId, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResult(String requestId) {
        return FaceMap.get(requestId);
    }
}
