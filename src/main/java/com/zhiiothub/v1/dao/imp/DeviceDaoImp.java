package com.zhiiothub.v1.dao.imp;

import com.zhiiothub.v1.dao.DeviceDao;
import com.zhiiothub.v1.model.Device;
import com.zhiiothub.v1.model.DeviceMongoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceDaoImp implements DeviceDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteOnedoc(String device_name) {
        mongoTemplate.remove(Query.query(Criteria.where("device_name").is(device_name)), DeviceMongoDB.class);
    }

    @Override
    public void addOnedoc(DeviceMongoDB deviceMongoDB) {
        mongoTemplate.save(deviceMongoDB);
    }

    @Override
    public List<DeviceMongoDB> findAll() {
        return mongoTemplate.findAll(DeviceMongoDB.class);
    }

    @Override
    public List<DeviceMongoDB> findByDeviceName(String DeviceName) {
        return mongoTemplate.find(Query.query(Criteria.where("device_name").is(DeviceName)), DeviceMongoDB.class);
    }

}
