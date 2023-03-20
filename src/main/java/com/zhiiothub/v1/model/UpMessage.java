package com.zhiiothub.v1.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
@Data
@AllArgsConstructor //有参构造
@NoArgsConstructor //无参构造
@ToString //toString
public class UpMessage {
    //influxmod field类型必须是<String , String>
    private Map<String, Object> params = new HashMap<>();
    private String topic;
    private String messageID;
    private String version;
    private String method;
}
