package com.zhiiothub.v1.cfg;

import com.zhiiothub.v1.interceptors.JwtTokenInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

///**
// * @description:
// * @author: zhcWIN
// * @date: 2023年03月09日 23:35
// */
//@Component
//public class InterceptorConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new JwtTokenInterceptor())
//                .excludePathPatterns("/user/login")
//                .addPathPatterns("/**");
//    }
//}