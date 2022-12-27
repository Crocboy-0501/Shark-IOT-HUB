package com.zhiiothub.v1.cfg;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class InfluxDBTemplate {
    private final InfluxdbProperties influxdbProperties;
    private InfluxDB influxDB;
    @Autowired
    public InfluxDBTemplate(InfluxdbProperties influxdbProperties) {
        this.influxdbProperties = influxdbProperties;
        getInfluxDB();
    }

    public void getInfluxDB(){
        if(influxDB == null){
            influxDB = InfluxDBFactory.connect(influxdbProperties.getUrl());
            influxDB.setDatabase(influxdbProperties.getDatabase());
        }
    }

    public void close(){
        if(influxDB != null){
            influxDB.close();
        }
    }

    public void write(String measurement, Map<String, String> tags, Map<String, Object> fields, long time, TimeUnit unit){
        Point point = Point.measurement(measurement).tag(tags).fields(fields).time(time, unit).build();
        influxDB.write(point);
        close();
    }

    public void write(String measurement, Map<String, String> tags, Map<String, Object> fields){
        write(measurement, tags, fields, System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public <T> java.util.List<T> handleQueryResult(QueryResult queryResult, Class<T> clazz){
        java.util.List<T> lists = new ArrayList<>();
        java.util.List<QueryResult.Result> results = queryResult.getResults();
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
        return handleQueryResult(query(selectCommand), clazz);
    }
}
