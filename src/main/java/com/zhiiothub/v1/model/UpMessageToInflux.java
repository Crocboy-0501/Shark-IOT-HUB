package com.zhiiothub.v1.model;

import java.util.HashMap;
import java.util.Map;
/**
 * 上传数据到时序数据库influx模型
 * @author zhangh
 * */
public class UpMessageToInflux {
    private long time;
    private Map<String, String> tags = new HashMap<>();
    private Map<String, Object> fields = new HashMap<>();

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    @Override
    public String toString(){
        return "Status{" +
                "time=" + time +
                ", tags=" + tags +
                ", fields=" + fields +
                "}";
    }
}
