package com.zhiiothub.v1.cfg;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
* @description:  创建influx操作配置类
* @author: zhangh
* @time: 2022/12/31 11:15 AM
*/
@Configuration
public class InfluxDBTemplate {
    private final InfluxdbProperties influxdbProperties;
    private InfluxDB influxDB;
    @Autowired
    public InfluxDBTemplate(InfluxdbProperties influxdbProperties) {
        this.influxdbProperties = influxdbProperties;
        getInfluxDB();
    }
    /**
     * 获取连接
     * */
    public void getInfluxDB(){
        if(influxDB == null){
//            influxDB = InfluxDBFactory.connect(influxdbProperties.getUrl(),
//                    influxdbProperties.getUsername(),
//                    influxdbProperties.getPassword());
            //docker内网使用，不需要用户密码
            influxDB = InfluxDBFactory.connect(influxdbProperties.getUrl());
            if(influxDB.databaseExists(influxdbProperties.getDatabase()))
            {
                influxDB.setDatabase(influxdbProperties.getDatabase());
            }else {
                influxDB.createDatabase(influxdbProperties.getDatabase());
                influxDB.setDatabase(influxdbProperties.getDatabase());
            };

        }
    }
    /**
    * 关闭连接
    * */
    public void close(){
        if(influxDB != null){
            influxDB.close();
        }
    }

    public void write(String measurement, Map<String, String> tags, Map<String, Object> fields, long time, TimeUnit unit){
        /**
        * @description:  指定时间插入
         * @param tags 标签
         * @param fields 字段
         * @param time 时间
         * @param unit 单位
        * @return: void
        * @author: zhangh
        * @time: 2022/12/31 2:19 PM
        */
        Point point = Point.measurement(measurement).tag(tags).fields(fields).time(time, unit).build();
        influxDB.write(point);
        close();
    }

    public void write(String measurement, Map<String, String> tags, Map<String, Object> fields){
        /**
        * @description:  插入数据-自动生成时间
         * @param measurement 表
         * @param tags 标签
         * @param fields 字段
         *
        * @return: void
        * @author: zhangh
        * @time: 2022/12/31 2:27 PM
        */
        write(measurement, tags, fields, System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public <T> java.util.List<T> handleQueryResult(QueryResult queryResult, Class<T> clazz){
        /**
        * @description:  查询封装
         * @param queryResult 查询返回结果
         * @param clazz 封装对象类型
         * @param T 泛型
        * @return: java.util.List<T>
        * @author: zhangh
        * @time: 2022/12/31 2:28 PM
        */
        java.util.List<T> lists = new ArrayList<>();
        java.util.List<QueryResult.Result> results = queryResult.getResults();
        //判断设备表是否为空
        if(results.get(0).getSeries()==null){
            return null;
        }
        results.forEach(result -> {
            java.util.List<QueryResult.Series> seriesList = result.getSeries();
            seriesList.forEach(series -> {
                java.util.List<String> columns = series.getColumns();
                List<List<Object>> values = series.getValues();
                for(int i = 0; i < values.size(); i++){
                    try{
                        T instance = clazz.newInstance();
                        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(instance);
                        HashMap<String, Object> fields = new HashMap<>();
                        for (int j = 0; j < columns.size(); j++) {
                            String column = columns.get(j);
                            Object val = values.get(i).get(j);
                            if("time".equals(column)){
                                beanWrapper.setPropertyValue("time", Timestamp.from(ZonedDateTime.parse(String.valueOf(val)).toInstant()).getTime());
                            }else{
                                fields.put(column, val);
                            }

                        }
                        beanWrapper.setPropertyValue("fields", fields);
                        lists.add(instance);
                    } catch(InstantiationException | IllegalAccessException e){
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        return lists;
    }
    public QueryResult query(String command){
        return influxDB.query(new Query(command));
    }
    public <T> List<T> query(String selectCommand, Class<T> clazz){
        /**
        * @description:
         * @param selectCommand select 语句
         * @param clazz 类型
        * @return: java.util.List<T>
        * @author: zhangh
        * @time: 2022/12/31 2:32 PM
        */
        return handleQueryResult(query(selectCommand), clazz);
    }
}
