package com.zhiiothub.v1.utils;

import com.zhiiothub.v1.dao.imp.UpDataImp;
import com.zhiiothub.v1.model.InfluxMod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @description: 日志记录函数，保存到influxdb
 * @author: zhcWIN
 * @date: 2023年03月02日 23:13
 */
@Component
public class LogsUtils {
    @Autowired
    private UpDataImp upDataImp;
    public String userLogs(String username, String event, String level){

        InfluxMod influxMod = new InfluxMod();
        influxMod.getFields().put("username", username);
        influxMod.getFields().put("event", event);
        influxMod.getFields().put("level", level);
//      influxMod.getFields().put("time", LocalDateTime.now().toString());
        upDataImp.SetMeasureMent(username + "Logs");
        upDataImp.save(influxMod);
        return "ok";
    }

    public String devLogs(String username, String event, String level){
        InfluxMod influxMod = new InfluxMod();
        influxMod.getFields().put("username", username);
        influxMod.getFields().put("event", event);
        influxMod.getFields().put("level", level);
//        influxMod.getFields().put("time", LocalDateTime.now().toString());
        upDataImp.SetMeasureMent(username + "Logs");
        upDataImp.save(influxMod);
        return "ok";
    }

    public String sysLogs(String username, String event, String level){
        InfluxMod influxMod = new InfluxMod();
        influxMod.getFields().put("username", username);
        influxMod.getFields().put("event", event);
        influxMod.getFields().put("level", level);
//        influxMod.getFields().put("time", LocalDateTime.now().toString());
        upDataImp.SetMeasureMent("sysLogs");
        upDataImp.save(influxMod);
        return "ok";
    }
}
