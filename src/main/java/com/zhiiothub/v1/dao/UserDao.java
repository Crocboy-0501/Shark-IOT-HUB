package com.zhiiothub.v1.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiiothub.v1.model.User;
import org.springframework.stereotype.Service;

/**
* @description: 用户数据库操作接口，使用mybatis-plus去增强接口，BaseMapper中提供了各种CRUD方法
* @return: 
* @author: zhanghc
* @time: 2023/2/25 22:20
*/
@Service
public interface UserDao extends BaseMapper<User> {
}
