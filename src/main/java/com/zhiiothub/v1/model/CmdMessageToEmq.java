package com.zhiiothub.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor //有参构造
@NoArgsConstructor //无参构造
@ToString //toString
public class CmdMessageToEmq {
    private String topic;
    private String messageID;
    @Value("1.0")
    private String version;
    private String payload;
}
