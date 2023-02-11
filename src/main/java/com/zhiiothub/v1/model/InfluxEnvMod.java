package com.zhiiothub.v1.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: zhcWIN
 * @date: 2023年02月11日 8:30
 */
@Data
@ExcelTarget("environment")
public class InfluxEnvMod implements Serializable {
    @Excel(name="湿度")
    private String hum;
    @Excel(name="温度")
    private String temp;
    @Excel(name="光照")
    private String lux;
    @Excel(name="电压")
    private String voltage;
}
