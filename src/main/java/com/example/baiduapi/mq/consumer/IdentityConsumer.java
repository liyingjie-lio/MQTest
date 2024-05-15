package com.example.baiduapi.mq.consumer;

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
public class IdentityConsumer {
    private static final String ACCESS_TOKEN = "24.923013d76eebba9398f2c1c2d4eac57f.2592000.1718336947.282335-70858591";
    private static final String API_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?access_token=" + ACCESS_TOKEN;
    private static final ConcurrentHashMap<String, String> IdentityMap = new ConcurrentHashMap<>();

    @RabbitListener(queues = RabbitMqConfig.IDENTITY_Queue)
    public void receiveMessage(String message) {
        String[] parts = message.split("\\|", 2);
        String requestId = parts[0];
        String base64Image = parts[1];

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            String urlEncodedImage = URLEncoder.encode(base64Image, StandardCharsets.UTF_8.toString());
            String body = "image=" + urlEncodedImage + "&id_card_side=front&detect_risk=true&detect_quality=true";
            post.setEntity(new StringEntity(body));
            System.out.println(urlEncodedImage);
            HttpResponse response = client.execute(post);
            String result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            IdentityMap.put(requestId, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResult(String requestId) {
        return IdentityMap.get(requestId);
    }
}
