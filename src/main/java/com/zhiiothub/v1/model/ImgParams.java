package com.zhiiothub.v1.model;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 图片参数模型
 * @author: zhcWIN
 * @date: 2023年02月09日 18:29
 */

public class ImgParams {
    private String username;
    private String age;
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
