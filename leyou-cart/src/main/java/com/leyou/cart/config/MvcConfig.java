package com.leyou.cart.config;

import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: 98050
 * @Time: 2018-10-25 19:48
 * @Feature: 配置过滤器
 */
@Configuration
//@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProperties;

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor(jwtProperties);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
    }

//    @Bean
//    public FilterRegistrationBean someFilterRegistration1() {
//        //新建过滤器注册类
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        // 添加我们写好的过滤器
//        registration.setFilter( new CartFilter());
//        // 设置过滤器的URL模式
//        registration.addUrlPatterns("/*");
//        return registration;
//    }

}
