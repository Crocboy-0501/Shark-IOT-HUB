package com.zhiiothub.v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class V1ApplicationTests {
    @Autowired
    protected RestTemplate restTemplate;
    @Test
    void contextLoads() {
    }

}
