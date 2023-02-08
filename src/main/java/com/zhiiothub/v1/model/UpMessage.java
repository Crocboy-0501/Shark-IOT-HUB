package com.zhiiothub.v1.model;

import java.util.HashMap;
import java.util.Map;

public class UpMessage {
    private Map<String, String> data = new HashMap<>();
    private String topic;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
