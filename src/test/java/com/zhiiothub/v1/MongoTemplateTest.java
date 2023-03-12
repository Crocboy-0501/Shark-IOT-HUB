package com.zhiiothub.v1;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhiiothub.v1.cfg.InfluxDBTemplate;
import com.zhiiothub.v1.dao.imp.UpDataImp;
import com.zhiiothub.v1.model.Dev;
import com.zhiiothub.v1.model.InfluxMod;
import com.zhiiothub.v1.model.User;
import com.zhiiothub.v1.utils.TopicGen;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class MongoTemplateTest extends V1ApplicationTests{
    @Value("${apiAuth.api_key}")
    private String api_key;

    @Value("${apiAuth.api_secret}")
    private String api_secret;

    @Autowired
    private TopicGen topicGen;
    @Autowired
    private UpDataImp upDataImp;

    @Autowired
    private InfluxDBTemplate influxDBTemplate;
    @Test
    public void test1(){
        topicGen.GetTopics("test", "test");
    }


//    @Test
//    public void test2(){
//        InfluxMod influxMod = new InfluxMod();
//        influxMod.getTags().put("num", "1");
//        influxMod.getFields().put("voltage", "220v");
//        statusDaoImp.save(influxMod);
//    }


    @Test
    public void testQueryIothub(){
        upDataImp.SetMeasureMent("status");
        List<InfluxMod> status = upDataImp.findAll();
        status.forEach(s -> System.out.println(s));
    }

    @Test
    public void testQueryIothub1(){
        upDataImp.SetMeasureMent("iothub");
        List<InfluxMod> status = upDataImp.findAll();
        status.forEach(s -> System.out.println(s));
    }

    @Test
    public void testIsMeasurements(){
        System.out.println(influxDBTemplate.query("show measurements"));

    }

    @Test
    public void testExcelPoi() throws IOException {
        upDataImp.SetMeasureMent("iothub");
        List<InfluxMod> datas = upDataImp.findAll();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("测试", "测试"), InfluxMod.class, datas);
        FileOutputStream outputStream = new FileOutputStream("D:\\test.xls");
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }
    @Test
    public void publisherMsg() {
        rabbitTemplate.convertAndSend("hello","hello world");
        rabbitTemplate.convertAndSend("heihei","ha ha");
        // 生产端没有指定交换机只有routingKey和Object。
        ////消费方产生hello队列，放在默认的交换机(AMQP default)上。
        ////而默认的交换机有一个特点，只要你的routerKey的名字与这个
        ////交换机的队列有相同的名字，他就会自动路由上。
        ////生产端routingKey 叫hello ，消费端生产hello队列。
        ////他们就路由上了
    }
    @Test
    public void testRedis(){
        stringRedisTemplate.opsForSet().add("set","ID");
        System.out.println(stringRedisTemplate.opsForSet().isMember("set", "12"));
    }
    @Test
    public void findAllTest(){
        QueryWrapper<Dev> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid",1);//设置等值查询
        List<Dev> devs = devDao.selectList(queryWrapper);
        devs.forEach(user-> System.out.println("dev = " + user));
    }
}
