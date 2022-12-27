package com.zhiiothub.v1;

import com.zhiiothub.v1.controller.dto.CmdMessageDto;
import com.zhiiothub.v1.dao.imp.StatusDaoImp;
import com.zhiiothub.v1.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;


public class MongoTemplateTest extends V1ApplicationTests{
    @Value("${apiAuth.api_key}")
    private String api_key;

    @Value("${apiAuth.api_secret}")
    private String api_secret;

    @Autowired
    private StatusDaoImp statusDaoImp;
    @Test
    public void test1(){
        System.out.println(api_key);
        System.out.println(api_secret);
    }
    @Test
    public void RestTemplateTestPost(){
        String url = "http://127.0.0.1:18083/api/v5/publish";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CmdMessageDto cmdMessageDto = new CmdMessageDto();
        cmdMessageDto.setTopic("cmd");
        cmdMessageDto.setPayload("71886161");

        HttpEntity<CmdMessageDto> entityParam = new HttpEntity<CmdMessageDto>(cmdMessageDto, headers);
        String result = restTemplate.postForObject(url, entityParam, String.class);
        System.out.println(result);

//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
//        System.out.println("responseEntity.getbody() = " + responseEntity.getBody());
    }

    @Test
    public void test2(){
        Status status = new Status();
        status.getTags().put("num", "1");
        status.getFields().put("voltage", "220v");
        statusDaoImp.save(status);
    }

    @Test
    public void testQuery(){
        List<Status> status = statusDaoImp.findAll();
        status.forEach(s -> System.out.println(s));
    }
}
