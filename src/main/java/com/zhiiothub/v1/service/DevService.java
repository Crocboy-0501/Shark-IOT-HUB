package com.zhiiothub.v1.service;

import com.zhiiothub.v1.model.Dev;
import com.zhiiothub.v1.model.DevBridgeInfo;
import com.zhiiothub.v1.model.UpMessage;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface DevService {
    Map<String,Object> getDeviceDetail(String id);
    Map<String,Object> deleteDevices(String id);
    Map<String,Object> getAllDevices(String id);
    Map<String,Object> uploadMessage(UpMessage upMessage, String DeviceName);
    Map<String,Object> queryInfluxDB(String measurement);
    Map<String,Object> queryInfluxDBLogs(String measurement);
    Map<String,Object> updateDevInfo(Dev dev);
    Map<String,Object> uploadStatus(DevBridgeInfo devBridgeInfo);

}
