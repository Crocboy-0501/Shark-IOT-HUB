package com.zhiiothub.v1.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @description:
 * @author: zhcWIN
 * @date: 2023年02月22日 22:43
 */
@Data
@Document("tsl")
public class TslMongoDB {
    @Field
    private String device_name;
    @Field
    private String device_tsl;

    public TslMongoDB(String device_name, String device_tsl) {
        this.device_name = device_name;
        this.device_tsl = device_tsl;
    }
}
