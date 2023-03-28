package com.zhiiothub.v1.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
/**
 * 下发命令json模型
 * */
@Data
public class CmdMessage {
    private String productName;
    private String deviceName;
    private String commandName;

    private Map<String, String> payLoad = new HashMap<>();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getPayLoad(){
        this.payLoad.forEach((key, value) -> System.out.println(key + "\t" + value));
        return "{" + "}";
    }

    public void setPayLoad(Map<String, String> payLoad) {
        this.payLoad = payLoad;
    }

    @Override
    public String toString(){
        return "produce_name: " + productName + " device_name: " + deviceName + " commandName: " + commandName
                + " payLoad: " + payLoad;
    }
}
