package com.zhiiothub.v1.controller;

import com.zhiiothub.v1.dao.imp.DeviceDaoImp;
import com.zhiiothub.v1.model.Device;
import com.zhiiothub.v1.utils.ShortUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    DeviceDaoImp deviceDaoImp;

    @GetMapping("/devs")
    public List<Device> getAllDevices(){
        return deviceDaoImp.findAll();
    }

    @PostMapping("/{clientId}")
    public String uploadMessage(@RequestBody String message, @PathVariable String clientId){
        String client = clientId;
        System.out.println(message);
        return client + ":" + message;
    }

    @PostMapping("/apply")
    public String applyDevices(@RequestParam Map<String, Object> params){
        String productName = (String) params.get("product_name");
        String deviceName = ShortUuid.generateShortUuid();
        String secret = ShortUuid.generateShortUuid();
        String brokerUsername = productName + deviceName;
        Device device = new Device(productName, deviceName, brokerUsername, secret);
        deviceDaoImp.addOnedoc(device);
        return device.toString();
    }
}
