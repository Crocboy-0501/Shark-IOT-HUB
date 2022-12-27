package com.zhiiothub.v1.dao;


import com.zhiiothub.v1.model.Device;

import java.util.List;

public interface DeviceDao {
    //增加一行文档
    void addOnedoc(Device device);

    List<Device> findAll();
}
