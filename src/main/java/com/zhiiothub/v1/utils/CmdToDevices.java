package com.zhiiothub.v1.utils;

import com.zhiiothub.v1.model.CmdMessageToEmq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhangh
 * 数据下行，主要设备控制指令
 * 主题格式：cmd/ProductName/DeviceName/CommandName/RequestID
 * */
@Component
public class CmdToDevices {

    @Autowired
    protected RestTemplate restTemplate;
    /**
     * @param ProductName 产品名称
     * @param DeviceName 设备名称
     * @param CommandName 命令类型
     * @param RequestID 请求序列号
     * @param Payload 命令内容
     * @return emqx返回一个含有id的json字符串
     * */
    public String RestTemplateTestPost(String ProductName, String DeviceName, String CommandName,
                                    String RequestID, String Payload){
        String url = "http://101.43.193.226:18083/api/v5/publish";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CmdMessageToEmq cmdMessageToEmq = new CmdMessageToEmq();
        cmdMessageToEmq.setTopic("cmd/" + ProductName + "/" + DeviceName + "/"
                                + CommandName + "/" + RequestID);
        cmdMessageToEmq.setPayload(Payload);
        cmdMessageToEmq.setMessageID(RequestID);

        HttpEntity<CmdMessageToEmq> entityParam = new HttpEntity<CmdMessageToEmq>(cmdMessageToEmq, headers);
        String result = restTemplate.postForObject(url, entityParam, String.class);
        return result;
    }
}
