package com.zhiiothub.v1.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhiiothub.v1.dao.DevDao;
import com.zhiiothub.v1.dao.UserDao;
import com.zhiiothub.v1.dao.UserLogDao;
import com.zhiiothub.v1.dao.imp.UpDataImp;
import com.zhiiothub.v1.model.Dev;
import com.zhiiothub.v1.model.InfluxMod;
import com.zhiiothub.v1.model.User;
import com.zhiiothub.v1.utils.JWTUtils;
import com.zhiiothub.v1.utils.LogsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 用户操作接口
 * @author: zhcWIN
 * @date: 2023年02月25日 23:02
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DevDao devDao;
    @Autowired
    private UserLogDao userLogDao;
    @Autowired
    private UpDataImp upDataImp;
    @Autowired
    private LogsUtils logsUtils;
    @PostMapping("/login")
    public Map<String, Object> userLogin(@RequestBody User user){
        Map<String, Object> result = new HashMap<>();
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println(username + password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);//设置等值查询
        queryWrapper.eq("password",password);//设置等值查询
//        int userCount = userDao.selectCount(queryWrapper);
        User userGet = userDao.selectOne(queryWrapper);
        if(userGet != null){
            System.out.println("用户登陆成功");
            logsUtils.userLogs(username, "用户登陆成功", "INFO");
            Map<String, String> map = new HashMap<>();//用来存放payload
            map.put("id",userGet.getId());
            map.put("username", userGet.getUsername());
            String token = JWTUtils.getToken(map);
            result.put("state",true);
            result.put("msg", userGet);
            result.put("token",token); //成功返回token信息
        }
//        System.out.println("usercount=" + userCount);
//        if(userCount != 0){
//            logs.userLogs(username, "用户登陆成功", "INFO");
//        }
        return result;
    }
    @PostMapping("/logout/{Username}")
    public String userLoginOut(@PathVariable String Username){

        logsUtils.userLogs(Username, "用户退出", "INFO");

//        UserLog userLog = new UserLog();
//        userLog.setUsername(username).setEvent("用户退出").setTime(LocalDateTime.now().toString());
//        userLogDao.insert(userLog);
        return "ok";
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        List<User> users = userDao.selectList(null);
        users.forEach(user -> System.out.println("user = " + user));
        return users;
    }
    @DeleteMapping("/user/{user_id}")
    public String delUserById(@PathVariable String user_id){
        userDao.deleteById(user_id);
        QueryWrapper<Dev> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        devDao.delete(queryWrapper);
        System.out.println("删除用户成功");
        return "ok";
    }

    @GetMapping("/get_ulogs/{measurement}")
    /**
     * @description: 查找{measurement}日志库的数据
     * @return: java.util.List<com.zhiiothub.v1.model.InfluxMod>
     * @author: zhanghc
     * @time: 2023/2/10 21:10
     */
        public List<InfluxMod> QueryInfluxDBLogs(@PathVariable("measurement") String measurement){
        System.out.println(measurement);
        upDataImp.SetMeasureMent(measurement);
        List<InfluxMod> datas = upDataImp.findAll();
        System.out.println(datas.get(0));
        return datas;
    }
}
