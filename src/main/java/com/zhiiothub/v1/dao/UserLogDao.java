package com.zhiiothub.v1.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiiothub.v1.model.UserLog;
import org.springframework.stereotype.Service;

/**
* @description: 用户日志数据库操作接口
* @author: zhanghc
* @time: 2023/3/2 19:20
*/
@Service
public interface UserLogDao extends BaseMapper<UserLog> {
}
