package com.zhiiothub.v1.dao;

import com.zhiiothub.v1.model.InfluxMod;

import java.util.List;

public interface UpDataDao {
    void save(InfluxMod influxMod);
    List<InfluxMod> findAll();
}
