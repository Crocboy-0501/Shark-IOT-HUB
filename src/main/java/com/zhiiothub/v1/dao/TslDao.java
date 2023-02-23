package com.zhiiothub.v1.dao;

import com.zhiiothub.v1.model.DeviceMongoDB;
import com.zhiiothub.v1.model.TslMongoDB;

import java.util.List;

public interface TslDao {
    void deleteOnedoc(String device_name);
    //增加一行文档
    void addOnedoc(TslMongoDB tslMongoDB);
    List<TslMongoDB> findAll();
    List<TslMongoDB> findByDeviceName(String DeviceName);
    String updateTslByDeviceName(TslMongoDB tslMongoDB);
}
