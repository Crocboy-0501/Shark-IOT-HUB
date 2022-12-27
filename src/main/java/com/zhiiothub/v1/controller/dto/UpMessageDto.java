package com.zhiiothub.v1.controller.dto;

public class UpMessageDto {
    private String voltage;
    private String topic;
    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
