package com.zhiiothub.v1.model;

import lombok.Data;

/**
 * @description: emqx数据桥接(up_status)模型
 * {
 *     "username": "${username}",
 *     "timestamp": "${timestamp}",
 *     "event": "${event}"
 * }
 * @author: zhcWIN
 * @date: 2023年03月02日 23:27
 */
@Data
public class DevBridgeInfo {
    private String username;
    private String timestamp;
    private String event;
    private String topic;

}
