package com.zhiiothub.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @description:
 * @author: zhcWIN
 * @date: 2023年02月22日 22:43
 */
@Data
@AllArgsConstructor //有参构造
@NoArgsConstructor //无参构造
@ToString //toString
@Document("tsl")
public class DevTslAcl {
    @Field
    private String device_name;
    @Field
    private String device_tsl;
    @Field
    private String permission;
    @Field
    private String action;
    @Field
    private String topics;

    public DevTslAcl(String device_name, String tsl) {
        this.device_tsl = tsl;
        this.device_name = device_name;
    }
}
