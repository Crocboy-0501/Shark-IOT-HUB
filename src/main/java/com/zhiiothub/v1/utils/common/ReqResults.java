package com.zhiiothub.v1.utils.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 接口测试返回
 * @author: zhcWIN
 * @date: 2023年02月09日 21:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqResults {
    private String code;
    private String msg;
    private Object data;
    public static ReqResults success(){
        return new ReqResults(ReqContants.CODE_200, "", null);
    }
    public static ReqResults success(Object data){
        return new ReqResults(ReqContants.CODE_200, "", data);
    }
    public static ReqResults error(String code, String msg){
        return new ReqResults(code, "", null);
    }

    public static ReqResults error(){
        return new ReqResults(ReqContants.CODE_500, "系统错误", null);
    }
    @Override
    public String toString(){
        return "code: " + code + "msg: " + msg + "data: " + data.toString();
    }
}
