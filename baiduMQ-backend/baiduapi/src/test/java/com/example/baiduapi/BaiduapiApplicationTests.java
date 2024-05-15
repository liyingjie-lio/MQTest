package com.example.baiduapi;

import com.example.baiduapi.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaiduapiApplicationTests {
    @Autowired
    private OrderService orderService;

    @Test
    void contextLoad(){
        orderService.makeOrder("1","11",12);
    }

    @Test
    void contextLoads() {
    }

}
