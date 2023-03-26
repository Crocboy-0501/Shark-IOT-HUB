package com.zhiiothub.v1.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description: 记录用户登录退出状态
 * @author: zhcWIN
 * @date: 2023年03月02日 19:06
 */
@Data
@AllArgsConstructor //有参构造
@NoArgsConstructor //无参构造
@ToString //toString
@Accessors(chain = true)//开启链式调用
@TableName("userlogs")
public class UserLog {
    private int id;
    private String username;
    private String time;
    private String event;
}