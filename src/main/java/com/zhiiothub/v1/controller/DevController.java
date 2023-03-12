package com.zhiiothub.v1.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhiiothub.v1.dao.DevDao;
import com.zhiiothub.v1.dao.imp.TslDaoImp;
import com.zhiiothub.v1.dao.imp.UpDataImp;
import com.zhiiothub.v1.model.*;
import com.zhiiothub.v1.utils.CmdToDevices;
import com.zhiiothub.v1.utils.Logs;
import com.zhiiothub.v1.utils.ShortUuid;
import com.zhiiothub.v1.utils.TopicGen;
import com.zhiiothub.v1.utils.common.ReqResults;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设备restful接口
 * @author zhangh
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class DevController {

    @Autowired
    private TslDaoImp tslDaoImp;
    //读取MongoDB设备注册信息表
    @Autowired
    private CmdToDevices cmdToDevices;
    @Autowired
    private UpDataImp upDataImp;
    @Value("${imgFile.file_path}")
    private String file_path;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;  //对字符串支持比较友好,不能存储对象
    @Autowired
    private RedisTemplate redisTemplate;  //存储对象
    @Autowired
    protected DevDao devDao;
    @Autowired
    Logs logs;

    @Autowired
    private TopicGen topicGen;
    /**
    * @description: 根据设备id号查询设备详情信息
    * @return: dev json字符串
    * @author: zhanghc
    * @time: 2023/3/6 21:37
    */
    @GetMapping("/dev_dtl/{device_id}")
    public Dev getDeviceDetail(@PathVariable("device_id") String id) throws Exception{
        Dev dev = devDao.selectById(id);
        System.out.println(dev.getDevice_name());
        dev.setTsl(tslDaoImp.findOneByDeviceName(dev.getDevice_name()).getDevice_tsl());
        return dev;
    }
    //根据设备名称删除设备
    @DeleteMapping("/dev/{device_id}")
    public void deleteDevices(@PathVariable("device_id") String device_id){
        devDao.deleteById(device_id);
        System.out.println("删除成功");
    }

    //根据userid查询设备数
    @GetMapping("/dev/{user_id}")
    public List<Dev> getAllDevices(@PathVariable String user_id){
        QueryWrapper<Dev> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        List<Dev> devs = devDao.selectList(queryWrapper);
        devs.forEach(dev -> System.out.println("dev = " + dev));
        return devs;
    }
    //emqx上行数据处理
    //正则表达式解析数据主题
    //正则表达式: upload_data\/\w{8}\/(.*?)\/.*
    //upload_data/:productName/:deviceName/:dataType/:messageID
    //存储进时序数据库
    @PostMapping("/up/{DeviceName}")
    public String uploadMessage(@RequestBody UpMessage upMessage, @PathVariable String DeviceName){
        /**
        * @description: 正则表达式处理消息主题，按需存储到设备号命名的influx表中
         * @param upMessage emqx推送的消息 topic和payload
         * @param clientId 客户端ID
        * @return: java.lang.String
        * @author: zhangh
        * @time: 2023/1/1 9:39 AM
        */
        //解析状态数据
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
        InfluxMod influxMod = new InfluxMod();
        for (Map.Entry entry : upMessage.getData().entrySet()) {
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
        if(!stringRedisTemplate.opsForSet().isMember("messageID", upMessage.getData().get("messageId").toString())){
            stringRedisTemplate.opsForSet().add("messageID",upMessage.getData().get("messageId").toString());
            String topic = upMessage.getTopic();
            Pattern pattern = Pattern.compile("upload_data\\/.{8}\\/(.*?)\\/.*");
            Matcher matcher = pattern.matcher(topic);
            if(matcher.matches()){
                System.out.println("group1=" + matcher.group(1));
            }
            System.out.println(matcher.group(1));
            upDataImp.SetMeasureMent(matcher.group(1));
            upDataImp.save(influxMod);
        }
        return deviceName;
    }
    @PostMapping("/upstatus/{DeviceName}")
    public ReqResults uploadStatus(@RequestBody UpMessage upMessage, @PathVariable String DeviceName){
        /**
        * @description: 处理设备上传状态，并放入响应消息队列
 * @param upMessage
 * @param clientId
        * @return: java.lang.String
        * @author: zhanghc
        * @time: 2023/2/22 15:40
        */
        //解析状态数据
        String deviceName = DeviceName;
        System.out.println(deviceName);
        //去mongodb获取物模型
        List<DevTslAcl> devTslAclList = tslDaoImp.findByDeviceName(deviceName);
        System.out.println(devTslAclList.get(0).getDevice_tsl());
        JSONObject jsonObject = JSONObject.parseObject(devTslAclList.get(0).getDevice_tsl());
        JSONObject props =  jsonObject.getJSONObject("data");
        InfluxMod influxMod = new InfluxMod();
        for (Map.Entry entry : props.entrySet()) {
            influxMod.getFields().put(entry.getKey().toString(), upMessage.getData().get(entry.getKey().toString()).toString());
            System.out.println(entry.getKey().toString());
        }
        System.out.println(influxMod.toString());
        System.out.println("收到数据！！！！");
        //对象序列化
        rabbitTemplate.convertAndSend("directs",deviceName,JSONObject.parseObject(influxMod.toString()));
        return ReqResults.success();
    }
    @GetMapping("/get_dinf/{measurement}")
    /**
    * @description: 查找{measurement}库的数据
    * @return: java.util.List<com.zhiiothub.v1.model.InfluxMod>
    * @author: zhanghc
    * @time: 2023/2/10 21:10
    */ 
    public List<InfluxMod> QueryInfluxDB(@PathVariable("measurement") String measurement){
        System.out.println(measurement);
        upDataImp.SetMeasureMent(measurement);
        List<InfluxMod> datas = upDataImp.findAll();
        System.out.println(datas.get(0));
        return datas;
    }


    @GetMapping("/get_dlogs/{measurement}")
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
    /**
    * @description: 更新设备信息
    * @return:
    * @author: zhanghc
    * @time: 2023/3/6 21:22
    */
    @PostMapping("/update_dev")
    public String updateDevInfo(@RequestBody Dev dev){
        devDao.updateById(dev);
        return "ok";
    }
    //更新emqx的post的设备连接信息
    //状态主题:update_status/:productName/:deviceName/:messageId
    //更新时序数据库设备连接信息
    @PostMapping("/up_status")
    public String uploadStatus(@RequestBody DevBridgeInfo devBridgeInfo){
        System.out.println("拿到了桥接数据" + devBridgeInfo.getUsername());
        Dev dev = new Dev();
        dev.setStatus(devBridgeInfo.getEvent());
        QueryWrapper<Dev> updateWrapper = new QueryWrapper<>();
        //修改设备数据状态
        updateWrapper.eq("device_name", devBridgeInfo.getUsername());
        devDao.update(dev, updateWrapper);
        logs.devLogs(devBridgeInfo.getUsername(), devBridgeInfo.getEvent(), "INFO");
        return "ok";
    }
    //设备注册接口，并存储设备信息到Mysql devs表，物模型存储到Mongodb
    @PostMapping("/apply")
    public Dev applyDev(@RequestBody Dev dev){

        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前时间戳
        String date = df.format(new Date());
        System.out.println(date);
        String productName = dev.getProduct_name();
        String sn = dev.getSn();
        String deviceName = ShortUuid.generateShortUuid();
        String secret = ShortUuid.generateShortUuid();
        //String username = product_name + device_name;
        //String client_id = product_name + device_name + sn;
        dev.setClient_id(productName+"/"+deviceName + "/" + sn);
        dev.setDevice_name(deviceName);
        dev.setSecret(secret);
        dev.setDev_regtime(date);
        System.out.println(dev.getTsl());
        //配置设备Acl信息
        //action permission topics
        DevTslAcl devTslAcl = new DevTslAcl();
        devTslAcl.setDevice_tsl(dev.getTsl());
        devTslAcl.setDevice_name(dev.getDevice_name());
        devTslAcl.setTopics(topicGen.GetTopics(productName, deviceName));
        devTslAcl.setAction("all");
        devTslAcl.setPermission("allow");


        tslDaoImp.updateTslByDeviceName(devTslAcl);
        devDao.insert(dev);
        return dev;
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
        String Payload = cmdMessage.getPayLoad();
        String RequestID = ShortUuid.generateShortUuid();
        return cmdToDevices.RestTemplateTestPost(ProductName, DeviceName, CommandName, RequestID, Payload);
//        return cmdMessage.toString();
    }
    @GetMapping("/down_inf/{measurement}")
    public ReqResults influxToExcel(@PathVariable("measurement") String measurement) throws IOException {
        /**
        * @description: 导出influx指定数据库数据
 * @param measurement 要导出的数据库名
        * @return: com.zhiiothub.v1.utils.common.ReqResults
        * @author: zhanghc
        * @time: 2023/2/11 8:13
        */
        upDataImp.SetMeasureMent(measurement);
        List<InfluxMod> datas = upDataImp.findAll();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("测试", "测试"), InfluxMod.class, datas);
        FileOutputStream outputStream = new FileOutputStream("D:\\test1.xls");
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
        return ReqResults.success();
    }

    @PostMapping("/upload_img")
    @ResponseBody
            public ReqResults uploadImag(@RequestParam("img") MultipartFile req, ImgParams imgModel){
        /**
        * @description: 接收对象（上传图片及环境参数）
        * @return: {
         *     "code": "200",
         *     "msg": "",
         *     "data": null
         * }
        * @author: zhanghc
        * @time: 2023/2/9 20:03
        */
        //String fileName = req.getFile().getOriginalFilename();
        String fileName = req.getOriginalFilename();
        System.out.println("完整文件名 = " + fileName);
        System.out.println("filename = " + imgModel.getFilename());
        InputStream inputStream = null;
        FileOutputStream fileOut = null;
        try {
            inputStream = req.getInputStream();
            fileOut = new FileOutputStream(file_path + fileName);//这里可以改路径
            IOUtils.copy(inputStream, fileOut);
            fileOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ReqResults.success();
    }

    /**
    * @description: 向mongodb中存储物模型，位置-tsl
     * @param jsonStr 前端提交的物模型字段
    * @return:
    * @author: zhanghc
    * @time: 2023/2/22 22:35
    */
    @PostMapping("/update_tsl/{DeviceName}")
    public String  upTsl(@RequestBody String jsonStr, @PathVariable("DeviceName") String DeviceName){
        DevTslAcl devTslAcl = new DevTslAcl(DeviceName, jsonStr);
        tslDaoImp.updateTslByDeviceName(devTslAcl);
//        tslDaoImp.addOnedoc(tslMongoDB);
//        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
//        JSONObject props =  jsonObject.getJSONObject("props");
//        for (Map.Entry entry : props.entrySet()) {
//            System.out.println(entry.getKey().toString());
//        }
        System.out.println(tslDaoImp.findByDeviceName(DeviceName).toString());
        return null;
    }
/**
* @description: 业务系统根据设备名获取物模型
* @return: 物模型字符串
* @author: zhanghc
* @time: 2023/2/27 9:19
*/
    @GetMapping("/get_tsl/{DeviceName}")
    public String  getTsl(@PathVariable("DeviceName") String DeviceName){
        System.out.println(tslDaoImp.findOneByDeviceName(DeviceName).getDevice_tsl());
        return tslDaoImp.findOneByDeviceName(DeviceName).getDevice_tsl();
    }


    /******************以下为测试文件*******************************************************/
    //设备注册接口，并存储设备信息到MongoDB
//    @PostMapping("/applym")
//    public DeviceMongoDB applyDevices(@RequestBody DeviceMongoDB deviceMongoDB){
//        //设置日期格式
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        //获取当前时间戳
//        String date = df.format(new Date());
//        System.out.println(date);
//
//        String productName = deviceMongoDB.getProduct_name();
//        String deviceName = ShortUuid.generateShortUuid();
//        String secret = ShortUuid.generateShortUuid();
//        String brokerUsername = productName + deviceName;
//        deviceMongoDB.setDevice_name(deviceName);
//        deviceMongoDB.setSecret(secret);
//        deviceMongoDB.setBroker_username(brokerUsername);
//        deviceMongoDB.setDevRegTime(date);
//        deviceDaoImp.addOnedoc(deviceMongoDB);
//        System.out.println(deviceMongoDB.toString());
//        return deviceMongoDB;
//    }
//    @PostMapping("/test")
//    public String cmd(@RequestBody List<Map<String, Object>> params){
//        System.out.println(params);
//        String ProductName = (String) params.get(0).get("ProductName");
//        System.out.println(ProductName);
//        String DeviceName = (String) params.get(1).get("DeviceName");
//        System.out.println(DeviceName);
//        String CommandName = (String) params.get(2).get("CommandName");
//        System.out.println(CommandName);
//        String Payload = (String) params.get(3).get("Payload");
//        System.out.println(Payload);
//        String RequestID = ShortUuid.generateShortUuid();
//        return cmdToDevices.RestTemplateTestPost(ProductName, DeviceName, CommandName, RequestID, Payload);
//    }
    /**************************************************************************************/
//    @GetMapping("/devs")
//    public List<DeviceMongoDB> getAllDevices(){
//        return deviceDaoImp.findAll();
//    }
}
