package com.zhiiothub.v1.dao.imp;

import com.zhiiothub.v1.dao.TslDao;
import com.zhiiothub.v1.model.DevTslAcl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 对物模型的增删改查操作
 * @author: zhcWIN
 * @date: 2023年02月22日 23:09
 */
@Service
public class TslDaoImp implements TslDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteOnedoc(String device_name) {
        mongoTemplate.remove(Query.query(Criteria.where("device_name").is(device_name)), DevTslAcl.class);
    }

    @Override
    public void addOnedoc(DevTslAcl devTslAcl) {
        mongoTemplate.save(devTslAcl);
    }

    @Override
    public List<DevTslAcl> findAll() {
        return mongoTemplate.findAll(DevTslAcl.class);
    }

    @Override
    public List<DevTslAcl> findByDeviceName(String DeviceName) {
        return mongoTemplate.find(Query.query(Criteria.where("device_name").is(DeviceName)), DevTslAcl.class);
    }

    @Override
    public String updateTslByDeviceName(DevTslAcl devTslAcl) {
        Query query = Query.query(Criteria.where("device_name").is(devTslAcl.getDevice_name()));
        if((mongoTemplate.count(query, DevTslAcl.class)) != 0){
            Update update = new Update();
            update.set("device_tsl", devTslAcl.getDevice_tsl());
            mongoTemplate.updateFirst(query, update, DevTslAcl.class);
        }else{
            addOnedoc(devTslAcl);
            System.out.println("添加成功");
        }
        return "TSL Update Successfully!";
    }

    @Override
    public DevTslAcl findOneByDeviceName(String DeviceName) {
        Query query = Query.query(Criteria.where("device_name").is(DeviceName));
        DevTslAcl devTslAcl = mongoTemplate.findOne(query, DevTslAcl.class);
        return devTslAcl;
    }
}
