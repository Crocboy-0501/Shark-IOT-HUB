package com.zhiiothub.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @description: MongoDB授权实体
 * @author: zhcWIN
 * @date: 2023年03月08日 7:55
 */
@Data
@AllArgsConstructor //有参构造
@NoArgsConstructor //无参构造
@Document("acl")
public class DeviceAcl {
    @Field
    private String device_name;
    @Field
    private String permission;
    @Field
    private String action;
    @Field
    private String topics;
}
