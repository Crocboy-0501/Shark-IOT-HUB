package com.zhiiothub.v1.dao;

import com.zhiiothub.v1.model.DevTslAcl;

import java.util.List;

public interface TslDao {
    void deleteOnedoc(String device_name);
    //增加一行文档
    void addOnedoc(DevTslAcl devTslAcl);
    List<DevTslAcl> findAll();
    List<DevTslAcl> findByDeviceName(String DeviceName);
    String updateTslByDeviceName(DevTslAcl devTslAcl);
    DevTslAcl findOneByDeviceName(String DeviceName);
}
