package com.zhiiothub.v1.dao.imp;

import com.zhiiothub.v1.cfg.InfluxDBTemplate;
import com.zhiiothub.v1.dao.UpDataDao;
import com.zhiiothub.v1.model.InfluxMod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: zhangh
 * @time: 2022/12/31 3:02 PM
 */
@Service
public class UpDataImp implements UpDataDao {
    private String MEASUREMENT;
    private final InfluxDBTemplate influxDBTemplate;
    public void SetMeasureMent(String measurement){
        this.MEASUREMENT = measurement;
    }
    @Autowired
    public UpDataImp(InfluxDBTemplate influxDBTemplate) {
        this.influxDBTemplate = influxDBTemplate;
    }
    @Override
    public void save(com.zhiiothub.v1.model.InfluxMod influxMod) {
        influxDBTemplate.write(MEASUREMENT, influxMod.getTags(), influxMod.getFields());
    }
    @Override
    public List<InfluxMod> findAll() {
        return influxDBTemplate.query("SELECT * FROM " + MEASUREMENT, InfluxMod.class);
    }
}
