package com.leyou.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.leyou.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: 98050
 * @Time: 2018-10-24 16:21
 * @Feature: 登录拦截器
 */
@Component
//@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties properties;

    @Autowired
    private FilterProperties filterProperties;

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        //1.获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //2.获取request
        HttpServletRequest request = context.getRequest();
        //3.获取路径
        String requestUri = request.getRequestURI();
        logger.info(requestUri);
        //4.判断白名单
        return !isAllowPath(requestUri);
    }

    private boolean isAllowPath(String requestUri) {
        //1.定义一个标记
        boolean flag = false;

        //2.遍历允许访问的路径
        List<String> paths = Arrays.asList(this.filterProperties.getAllowPaths().split(" "));
        for (String path : paths){
            if (requestUri.startsWith(path)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public Object run() throws ZuulException {
        //1.获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //2.获取request
        HttpServletRequest request = context.getRequest();
        //3.获取token
        String token = CookieUtils.getCookieValue(request,this.properties.getCookieName());
        //4.校验
        try{
            //4.1 校验通过，放行
            JwtUtils.getInfoFromToken(token,this.properties.getPublicKey());
        }catch (Exception e){
            //4.2 校验不通过，返回403
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }
        return null;
    }
}
