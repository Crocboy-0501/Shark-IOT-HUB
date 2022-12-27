package com.zhiiothub.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class V1Application {
    public static void main(String[] args) {
        SpringApplication.run(V1Application.class, args);
    }

}
