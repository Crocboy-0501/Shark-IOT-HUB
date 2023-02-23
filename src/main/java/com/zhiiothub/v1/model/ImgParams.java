package com.zhiiothub.v1.model;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 图片参数模型
 * @author: zhcWIN
 * @date: 2023年02月09日 18:29
 */

public class ImgParams {
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
