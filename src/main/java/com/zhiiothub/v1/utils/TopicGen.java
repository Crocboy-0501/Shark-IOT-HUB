package com.zhiiothub.v1.utils;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @description: 生成设备默认订阅主题
 * @author: zhcWIN
 * @date: 2023年03月09日 15:07
 */
@Component
public class TopicGen {

    private List<String> topics = new ArrayList<>();
    public String GetTopics(String productName, String deviceName){
        topics.add("\"/sys/" + productName + "/" +deviceName + "/event/property/post\"");
        topics.add("\"/sys/" + productName + "/" +deviceName + "/service/property/set\"");
        topics.add("\"/sys/" + productName + "/" +deviceName + "/event/property/set_reply\"");
        topics.add("\"/sys/" + productName + "/" +deviceName + "/event/log/post\"");
        System.out.println(topics.toString());
        return topics.toString();
    }
}
