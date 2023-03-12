package com.zhiiothub.v1.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @description:
 * @author: zhcWIN
 * @date: 2023年02月25日 22:34
 */
@Data
@AllArgsConstructor //有参构造
@NoArgsConstructor //无参构造
@ToString //toString
@Accessors(chain = true)//开启链式调用
@TableName("devs")
public class Dev{
    private int id;
    private String device_name;
    private String protocol;
    private String status;
    private String secret;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String product_name;
    private String user_id;
    private String dev_regtime;
    private String latitude;
    private String longitude;
    private String client_id;
    private String sn;
    @TableField(exist = false)//不映射数据库表中的任何字段
    private String tsl;
}
