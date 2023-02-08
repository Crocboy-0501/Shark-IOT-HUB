package com.zhiiothub.v1;

import com.zhiiothub.v1.cfg.InfluxDBTemplate;
import com.zhiiothub.v1.dao.imp.UpDataImp;
import com.zhiiothub.v1.model.InfluxMod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;


public class MongoTemplateTest extends V1ApplicationTests{
    @Value("${apiAuth.api_key}")
    private String api_key;

    @Value("${apiAuth.api_secret}")
    private String api_secret;


    @Autowired
    private UpDataImp upDataImp;

    @Autowired
    private InfluxDBTemplate influxDBTemplate;
    @Test
    public void test1(){
        System.out.println(api_key);
        System.out.println(api_secret);
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
}
