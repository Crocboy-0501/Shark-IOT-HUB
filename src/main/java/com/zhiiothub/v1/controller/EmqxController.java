package com.zhiiothub.v1.controller;

import com.zhiiothub.v1.model.DevBridgeInfo;
import com.zhiiothub.v1.utils.LogsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: zhcWIN
 * @date: 2023年03月03日 0:30
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class EmqxController {
    @Autowired
    LogsUtils logsUtils;
    @PostMapping("/msg_dropped")
    public String msgDropped(@RequestBody DevBridgeInfo devBridgeInfo){
        System.out.println(devBridgeInfo.getUsername());
        logsUtils.devLogs(devBridgeInfo.getUsername(), devBridgeInfo.getEvent(), "WARN");
        return "ok";
    }
    @PostMapping("/session/sub")
    public String sessionSub(@RequestBody DevBridgeInfo devBridgeInfo){
        System.out.println(devBridgeInfo.getUsername());
        logsUtils.devLogs(devBridgeInfo.getUsername(), devBridgeInfo.getEvent(), "INFO");
        return "ok";
    }

    @PostMapping("/session/unsub")
    public String sessionUnSub(@RequestBody DevBridgeInfo devBridgeInfo){
        System.out.println(devBridgeInfo.getUsername());
        logsUtils.devLogs(devBridgeInfo.getUsername(), devBridgeInfo.getEvent(), "INFO");
        return "ok";
    }
}
