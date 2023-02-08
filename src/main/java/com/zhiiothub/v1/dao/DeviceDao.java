package com.zhiiothub.v1.dao;


import com.zhiiothub.v1.model.DeviceMongoDB;

import java.util.List;

public interface DeviceDao {
    void deleteOnedoc(String device_name);
    //增加一行文档
    void addOnedoc(DeviceMongoDB deviceMongoDB);
    List<DeviceMongoDB> findAll();
    List<DeviceMongoDB> findByDeviceName(String DeviceName);

}
