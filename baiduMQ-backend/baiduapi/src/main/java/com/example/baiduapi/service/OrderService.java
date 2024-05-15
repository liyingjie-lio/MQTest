package com.example.baiduapi.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 模拟用户下单
     * @param userId
     * @param productId
     * @param num
     */
    public void makeOrder(String userId,String productId,int num){
        // 1:根据id查看是否库存充足
        // 2:保存订单
        String orderId = UUID.randomUUID().toString();
        // 3:通过MQ实现消息分发
        // 参数1：交换机名称 参数2：路由key 参数3：消息内容
        String exchangeName = "fanout_order_change";
        String routingKey = ""; // 因为fanout不需要路由
        System.out.println("订单创建成功，订单ID："+orderId);
        rabbitTemplate.convertAndSend(exchangeName,routingKey,orderId);
    }
}
