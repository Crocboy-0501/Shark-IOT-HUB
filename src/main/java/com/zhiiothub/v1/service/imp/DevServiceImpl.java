package com.zhiiothub.v1.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhiiothub.v1.dao.DevDao;
import com.zhiiothub.v1.dao.imp.TslDaoImp;
import com.zhiiothub.v1.dao.imp.UpDataImp;
import com.zhiiothub.v1.model.Dev;
import com.zhiiothub.v1.model.DevBridgeInfo;
import com.zhiiothub.v1.model.InfluxMod;
import com.zhiiothub.v1.model.UpMessage;
import com.zhiiothub.v1.service.DevService;
import com.zhiiothub.v1.utils.LogsUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 业务层处理设备相关请求
 * @author: zhcWIN
 * @date: 2023年03月12日 21:47
 */
@Component
@Transactional
public class DevServiceImpl implements DevService {
    @Autowired
    protected DevDao devDao;
    @Autowired
    private TslDaoImp tslDaoImp;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UpDataImp upDataImp;
    @Autowired
    LogsUtils logsUtils;

    @Override
    public Map<String,Object> getDeviceDetail(String id) {
        Map<String,Object> result = new HashMap<>();
        Dev dev = devDao.selectById(id);
        if(dev != null){
            result.put("state", true);
            dev.setTsl(tslDaoImp.findOneByDeviceName(dev.getDevice_name()).getDevice_tsl());
            result.put("msg", dev);
            return result;
        }
        throw new RuntimeException("查找失败~");
    }

    @Override
    public Map<String,Object> deleteDevices(String id) {
        Map<String,Object> result = new HashMap<>();
        try{
            devDao.deleteById(id);
            result.put("state", true);
            result.put("msg", "删除成功~");
        }catch (Exception e){
            result.put("state", false);
            result.put("msg", "删除失败~");
        }
        return result;
    }

    @Override
    public Map<String, Object> getAllDevices(String id) {
        Map<String,Object> result = new HashMap<>();
        QueryWrapper<Dev> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        try {
            List<Dev> devs = devDao.selectList(queryWrapper);
            result.put("state", true);
            result.put("msg", devs);
        }catch (Exception e){
            result.put("state", false);
            result.put("msg", "查找失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> uploadMessage(UpMessage upMessage, String DeviceName) {
        String deviceName = DeviceName;
        System.out.println("deviceName = " + deviceName);
        //去mongodb获取物模型
//        List<DevTslAcl> devTslAclList = tslDaoImp.findByDeviceName(deviceName);
//        System.out.println(devTslAclList.get(0).getDevice_tsl());
//        JSONObject jsonObject = JSONObject.parseObject(devTslAclList.get(0).getDevice_tsl());
//        System.out.println("解析data");
//        JSONObject props =  jsonObject.getJSONObject("data");
//        System.out.println("解析data失败");
//        JSONObject jsonObject = JSONObject.parseObject(upMessage.getData().toString());
        Map<String,Object> result = new HashMap<>();
        InfluxMod influxMod = new InfluxMod();
        for (Map.Entry entry : upMessage.getParams().entrySet()) {
            influxMod.getFields().put(entry.getKey().toString(), entry.getValue().toString());
            System.out.println(entry.getKey().toString());
        }
        //influxmod field类型必须是<String , String>
//        influxMod.setFields(upMessage.getData());
        System.out.println(influxMod.toString());
        System.out.println("收到数据！！！！");
        //对象序列化
        //使用rabbitMQ路由模型 exchange为directs模式 routerkey为deviceName
        rabbitTemplate.convertAndSend("directs",deviceName, JSON.toJSONString(influxMod.getFields()));
        /* 解析messageId，redis查找messageId完成消息去重 */
        if(!stringRedisTemplate.opsForSet().isMember("messageID", upMessage.getMessageID())){
            stringRedisTemplate.opsForSet().add("messageID",upMessage.getMessageID());
            String topic = upMessage.getTopic();
            Pattern pattern = Pattern.compile("\\/sys\\/.*?\\/(.{8})\\/.*");
            Matcher matcher = pattern.matcher(topic);
            if(matcher.matches()){
                System.out.println("group1=" + matcher.group(1));
            }
            System.out.println(matcher.group(1));
            try{
                upDataImp.SetMeasureMent(matcher.group(1));
                upDataImp.save(influxMod);
                result.put("state", true);
                result.put("msg", "存储成功~");
            }catch (Exception e){
                result.put("state", false);
                result.put("msg", "存储失败~");
            }

        }
        return result;
    }

    @Override
    public Map<String, Object> queryInfluxDB(String measurement) {
        Map<String,Object> result = new HashMap<>();
        System.out.println(measurement);
        try{
            upDataImp.SetMeasureMent(measurement);
            List<InfluxMod> datas = upDataImp.findAll();
            result.put("state", true);
            result.put("msg", datas);
        }catch (Exception e){
            result.put("state", false);
            result.put("msg", "查找失败~");
        }
        return result;
    }

    @Override
    public Map<String, Object> queryInfluxDBLogs(String measurement) {
        System.out.println(measurement);
        Map<String,Object> result = new HashMap<>();
        System.out.println(measurement);
        try{
            upDataImp.SetMeasureMent(measurement);
            List<InfluxMod> datas = upDataImp.findAll();
            System.out.println(datas.get(0));
            result.put("state", true);
            result.put("msg", datas);
        }catch (Exception e){
            result.put("state", false);
            result.put("msg", "查找失败~");
        }
        return result;
    }

    @Override
    public Map<String, Object> updateDevInfo(Dev dev) {
        Map<String,Object> result = new HashMap<>();
        try{
            devDao.updateById(dev);
            System.out.println("更新成功");
            result.put("state", true);
            result.put("msg", "状态更新成功~");
        }catch (Exception e){
            result.put("state", false);
            result.put("msg", "状态更新失败~");
        }
        return result;
    }
    //设备上下线连接与断开状态数据
    public Map<String, Object> uploadStatus(DevBridgeInfo devBridgeInfo) {
        Map<String,Object> result = new HashMap<>();
        try{
            System.out.println("拿到了桥接数据" + devBridgeInfo.getUsername());
            Dev dev = new Dev();
            dev.setStatus(devBridgeInfo.getEvent());
            QueryWrapper<Dev> updateWrapper = new QueryWrapper<>();
            //修改设备数据状态
            updateWrapper.eq("device_name", devBridgeInfo.getUsername());
            if(devDao.update(dev, updateWrapper) != 0){
                logsUtils.devLogs(devBridgeInfo.getUsername(), devBridgeInfo.getEvent(), "INFO");
                result.put("state", true);
                result.put("msg", "状态更新成功~");
            }else {
                result.put("state", false);
                result.put("msg", "未查找到数据记录~");
            }
        }catch (Exception e){
            result.put("state", false);
            result.put("msg", "状态更新失败~");
        }
        return result;
    }


}
