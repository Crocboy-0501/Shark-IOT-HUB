package com.zhiiothub.v1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@MapperScan("com.zhiiothub.v1.dao")
public class V1Application {
    public static void main(String[] args) {
        SpringApplication.run(V1Application.class, args);
    }

}
