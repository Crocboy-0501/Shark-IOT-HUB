package com.zhiiothub.v1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document("devices")
public class Device {
    @Field
    private String product_name;
    @Field
    private String device_name;

    @Field
    private String broker_username;

    @Field
    private String secret;

    public Device(String product_name, String device_name, String broker_username, String secret) {
        this.product_name = product_name;
        this.device_name = device_name;
        this.broker_username = broker_username;
        this.secret = secret;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getBroker_username() {
        return broker_username;
    }

    public void setBroker_username(String broker_username) {
        this.broker_username = broker_username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString(){
        return "produce_name: " + product_name + "device_name: " + device_name + "secret: " + secret
                + "broker_username: " + broker_username;
    }
}
