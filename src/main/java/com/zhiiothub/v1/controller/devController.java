package com.zhiiothub.v1.controller;

import com.zhiiothub.v1.dao.imp.DeviceDaoImp;
import com.zhiiothub.v1.dao.imp.UpDataImp;
import com.zhiiothub.v1.model.CmdMessage;
import com.zhiiothub.v1.model.DeviceMongoDB;
import com.zhiiothub.v1.model.UpMessage;
import com.zhiiothub.v1.utils.CmdToDevices;
import com.zhiiothub.v1.utils.ShortUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhiiothub.v1.model.InfluxMod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * restful接口
 * @author zhangh
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class devController {
    @Autowired
    DeviceDaoImp deviceDaoImp;
    //读取MongoDB设备注册信息表
    @Autowired
    CmdToDevices cmdToDevices;
    @Autowired
    UpDataImp upDataImp;
    @GetMapping("/devsDetail/{device_name}")
    public List<DeviceMongoDB> getDeviceDetail(@PathVariable("device_name") String device_name){
        System.out.println(deviceDaoImp.findByDeviceName(device_name));
        return deviceDaoImp.findByDeviceName(device_name);
    }
    //根据设备名称查询
    @DeleteMapping("/devs/{device_name}")
    public void deleteDevices(@PathVariable("device_name") String device_name){
        deviceDaoImp.deleteOnedoc(device_name);
        System.out.println("删除成功");
    }
    @GetMapping("/devs")
    public List<DeviceMongoDB> getAllDevices(){
        return deviceDaoImp.findAll();
    }
    //emqx上行数据处理
    //正则表达式解析数据主题
    //正则表达式: upload_data\/\w{8}\/(.*?)\/.*
    //upload_data/:productName/:deviceName/:dataType/:messageID
    //存储进时序数据库
    @PostMapping("/up/{clientId}")
    public String uploadMessage(@RequestBody UpMessage upMessage, @PathVariable String clientId){
        /**
        * @description: 正则表达式处理消息主题，按需存储到设备号命名的influx表中
         * @param upMessage emqx推送的消息 topic喝payload
         * @param clientId 客户端ID
        * @return: java.lang.String
        * @author: zhangh
        * @time: 2023/1/1 9:39 AM
        */
        String client = clientId;
        System.out.println(client);
        InfluxMod influxMod = new InfluxMod();
        influxMod.getFields().put("voltage", upMessage.getData().get("voltage").toString());
        influxMod.getFields().put("lux", upMessage.getData().get("lux").toString());
        influxMod.getFields().put("temp", upMessage.getData().get("temp").toString());
        influxMod.getFields().put("hum", upMessage.getData().get("hum").toString());
        String topic = upMessage.getTopic().toString();
        Pattern pattern = Pattern.compile("upload_data\\/.{8}\\/(.*?)\\/.*");
        Matcher matcher = pattern.matcher(topic);
        if(matcher.matches()){
            System.out.println("group1=" + matcher.group(1));
        }
        upDataImp.SetMeasureMent(matcher.group(1));
        upDataImp.save(influxMod);
        return client;
    }
    @GetMapping("/influxData/{measurement}")
    public List<InfluxMod> testQueryIothub(@PathVariable("measurement") String measurement){
        upDataImp.SetMeasureMent(measurement);
        List<InfluxMod> datas = upDataImp.findAll();
        return datas;
    }

    //更新emqx的post的设备连接信息
    //状态主题:update_status/:productName/:deviceName/:messageId
    //更新时序数据库设备连接信息
    @PostMapping("/up_status")
    public String uploadStatus(@RequestBody String message){
        System.out.println(message);
        return message;
    }
    //设备注册接口，并存储设备信息到MongoDB
    @PostMapping("/apply1")
    public String applyDevices1(@RequestParam Map<String, Object> params){
        String productName = (String) params.get("product_name");
        String deviceName = ShortUuid.generateShortUuid();
        String secret = ShortUuid.generateShortUuid();
        String brokerUsername = productName + deviceName;
        DeviceMongoDB deviceMongoDB = new DeviceMongoDB(productName, deviceName, brokerUsername, secret);
        deviceDaoImp.addOnedoc(deviceMongoDB);
        return deviceMongoDB.toString();
    }
    //设备注册接口，并存储设备信息到MongoDB
    @PostMapping("/apply")
    public DeviceMongoDB applyDevices(@RequestBody DeviceMongoDB deviceMongoDB){
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前时间戳
        String date = df.format(new Date());
        System.out.println(date);

        String productName = deviceMongoDB.getProduct_name();
        String deviceName = ShortUuid.generateShortUuid();
        String secret = ShortUuid.generateShortUuid();
        String brokerUsername = productName + deviceName;
        deviceMongoDB.setDevice_name(deviceName);
        deviceMongoDB.setSecret(secret);
        deviceMongoDB.setBroker_username(brokerUsername);
        deviceMongoDB.setDevRegTime(date);
        deviceDaoImp.addOnedoc(deviceMongoDB);
        System.out.println(deviceMongoDB.toString());
        return deviceMongoDB;
    }
    //接收前端下发数据,接收为json格式
    //@RequestBody映射实体类json数据首字母不要大写，驼峰命名法'
    //前端请求Content-Type为application/json
    @PostMapping("/cmd")
    public String cmdDevices(@RequestBody CmdMessage cmdMessage){
        System.out.println(cmdMessage.toString());
        String ProductName = cmdMessage.getProductName();
        System.out.println(ProductName);
        String DeviceName = cmdMessage.getDeviceName();
        String CommandName = cmdMessage.getCommandName();
        Map Payload = cmdMessage.getPayLoad();
        String RequestID = ShortUuid.generateShortUuid();
        //return cmdToDevices.RestTemplateTestPost(ProductName, DeviceName, CommandName, RequestID, Payload.toString());
        return cmdMessage.toString();
    }

    @PostMapping("/test")
    public String cmd(@RequestBody List<Map<String, Object>> params){
        System.out.println(params);
        String ProductName = (String) params.get(0).get("ProductName");
        System.out.println(ProductName);
        String DeviceName = (String) params.get(1).get("DeviceName");
        System.out.println(DeviceName);
        String CommandName = (String) params.get(2).get("CommandName");
        System.out.println(CommandName);
        String Payload = (String) params.get(3).get("Payload");
        System.out.println(Payload);
        String RequestID = ShortUuid.generateShortUuid();
        return cmdToDevices.RestTemplateTestPost(ProductName, DeviceName, CommandName, RequestID, Payload);
    }
}
