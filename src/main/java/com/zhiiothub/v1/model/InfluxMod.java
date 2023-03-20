package com.zhiiothub.v1.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备状态表模型
 * excel导出设置
 * @author zhangh
 * */
@Data
@ExcelTarget("influxdb")
public class InfluxMod implements Serializable {
    @Excel(name="时间")
    private Long time;
    @Excel(name="tags")
    private Map<String, String> tags = new HashMap<>();
    @Excel(name="fields")
    private Map<String, Object> fields = new HashMap<>();
//    @ExcelEntity
//    private InfluxEnvMod influxEnvMod;

//    public void setInfluxEnvMod(InfluxEnvMod influxEnvMod) {
//        this.influxEnvMod = influxEnvMod;
//    }
/**
 * Q: this.influxEnvMod无效
*/
//    public InfluxEnvMod getInfluxEnvMod() {
//        InfluxEnvMod influxEnvMod = new InfluxEnvMod();
//        this.influxEnvMod.setHum("1");
//        this.influxEnvMod.setTemp("1");
//        this.influxEnvMod.setLux("1");
//        this.influxEnvMod.setVoltage("1");
//        return influxEnvMod;
//    }

//    public InfluxEnvMod getInfluxEnvMod() {
//        InfluxEnvMod influxEnvMod = new InfluxEnvMod();
//        influxEnvMod.setHum(fields.get("hum").toString());
//        influxEnvMod.setTemp(fields.get("temp").toString());
//        influxEnvMod.setLux(fields.get("lux").toString());
//        influxEnvMod.setVoltage(fields.get("voltage").toString());
//        return influxEnvMod;
//    }
//    @Excel(name="湿度")
//    private String hum;

//    public String getHum() {
//        Set<String> keySet = fields.keySet();
//        //遍历存放所有key的Set集合
//        Iterator<String> it =keySet.iterator();
//        while(it.hasNext()){
//            //利用了Iterator迭代器
//            //得到每一个key
//            String key = it.next();
//            //通过key获取对应的value
//            Object value = fields.get(key);
//            System.out.println(key+"="+value);
//        }
//        return fields.get("hum").toString();
//    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    @Override
    public String toString(){
        return "{" +
                "time" + ":" + time + "," +
                "fields" + ":" + fields +
                "}";
    }
}
