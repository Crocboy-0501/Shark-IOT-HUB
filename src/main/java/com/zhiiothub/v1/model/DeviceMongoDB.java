package com.zhiiothub.v1.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * mongodb数据库设备注册信息
 * @author zhangh
 * */
@Data
@Document("devices")
public class DeviceMongoDB {
    @Field
    private String product_name;
    @Field
    private String device_name;
    @Field
    private String broker_username;
    @Field
    private String secret;
    @Field
    private String devRegTime;
    public DeviceMongoDB(String product_name, String device_name, String broker_username, String secret) {
        this.product_name = product_name;
        this.device_name = device_name;
        this.broker_username = broker_username;
        this.secret = secret;
    }
    @Override
    public String toString(){
        return "produce_name: " + product_name + "device_name: " + device_name + "secret: " + secret
                + "broker_username: " + broker_username;
    }
}
