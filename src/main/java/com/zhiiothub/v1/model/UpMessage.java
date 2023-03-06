package com.zhiiothub.v1.model;

import java.util.HashMap;
import java.util.Map;

public class UpMessage {
    //influxmod field类型必须是<String , String>
    private Map<String, Object> data = new HashMap<>();
    private String topic;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
