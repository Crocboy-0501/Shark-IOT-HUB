package com.zhiiothub.v1.dao.imp;

import com.zhiiothub.v1.dao.TslDao;
import com.zhiiothub.v1.model.DeviceMongoDB;
import com.zhiiothub.v1.model.TslMongoDB;
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
        mongoTemplate.remove(Query.query(Criteria.where("device_name").is(device_name)), TslMongoDB.class);
    }

    @Override
    public void addOnedoc(TslMongoDB tslMongoDB) {
        mongoTemplate.save(tslMongoDB);
    }

    @Override
    public List<TslMongoDB> findAll() {
        return mongoTemplate.findAll(TslMongoDB.class);
    }

    @Override
    public List<TslMongoDB> findByDeviceName(String DeviceName) {
        return mongoTemplate.find(Query.query(Criteria.where("device_name").is(DeviceName)), TslMongoDB.class);
    }

    @Override
    public String updateTslByDeviceName(TslMongoDB tslMongoDB) {
        Query query = Query.query(Criteria.where("device_name").is(tslMongoDB.getDevice_name()));
        if((mongoTemplate.count(query, TslMongoDB.class)) != 0){
            Update update = new Update();
            update.set("device_tsl", tslMongoDB.getDevice_tsl());
            mongoTemplate.updateFirst(query, update, TslMongoDB.class);
        }else{
            addOnedoc(tslMongoDB);
            System.out.println("添加成功");
        }
        return "TSL Update Successfully!";
    }

    @Override
    public TslMongoDB findOneByDeviceName(String DeviceName) {
        Query query = Query.query(Criteria.where("device_name").is(DeviceName));
        TslMongoDB tslMongoDB = mongoTemplate.findOne(query, TslMongoDB.class);
        return tslMongoDB;
    }
}
