package com.zhiiothub.v1.dao.imp;

import com.zhiiothub.v1.dao.DeviceDao;
import com.zhiiothub.v1.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceDaoImp implements DeviceDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addOnedoc(Device device) {
        mongoTemplate.save(device);
    }

    @Override
    public List<Device> findAll() {
        return mongoTemplate.findAll(Device.class);
    }

}
