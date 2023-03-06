package com.zhiiothub.v1;

import com.zhiiothub.v1.dao.DevDao;
import com.zhiiothub.v1.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class V1ApplicationTests {
    @Autowired
    protected RestTemplate restTemplate;
    @Autowired
    protected UserDao userDao;
    @Autowired
    protected DevDao devDao;
    @Autowired
    protected RabbitTemplate rabbitTemplate;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;  //对字符串支持比较友好,不能存储对象
    @Test
    void contextLoads() {
    }

}
