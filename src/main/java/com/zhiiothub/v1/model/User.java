package com.zhiiothub.v1.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @description: 用户实体类
 * @author: zhcWIN
 * @date: 2023年02月25日 22:16
 */
@Data
@AllArgsConstructor //有参构造
@NoArgsConstructor //无参构造
@ToString //toString
@Accessors(chain = true)//开启链式调用
@TableName("users")
public class User {
    private String id;
    private String username;
    private String password;
    private String role;
    private String user_regtime;
}
