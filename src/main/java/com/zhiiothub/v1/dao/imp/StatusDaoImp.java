package com.zhiiothub.v1.dao.imp;

import com.zhiiothub.v1.cfg.InfluxDBTemplate;
import com.zhiiothub.v1.dao.StatusDao;
import com.zhiiothub.v1.model.Status;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class StatusDaoImp implements StatusDao {
    private final String MEASUREMENT = "status";
    private final InfluxDBTemplate influxDBTemplate;
    @Autowired
    public StatusDaoImp(InfluxDBTemplate influxDBTemplate) {
        this.influxDBTemplate = influxDBTemplate;
    }

    @Override
    public void save(Status status) {
        influxDBTemplate.write(MEASUREMENT, status.getTags(), status.getFields());
    }


    @Override
    public List<Status> findAll() {
        return influxDBTemplate.query("SELECT * FROM " + MEASUREMENT, Status.class);
    }
}
