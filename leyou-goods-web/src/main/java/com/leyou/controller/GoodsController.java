package com.leyou.controller;

import com.leyou.service.GoodsHtmlService;
import com.leyou.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 98050
 * Time: 2018-10-17 16:06
 * Feature: 商品详情页面跳转
 */
@Controller
@RequestMapping("item")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    private static String KEY_PREFIX = "leyou:goods:detail:";

    @GetMapping(value = "{id}.html",produces = "text/html")
    @ResponseBody
    public String toItemPage(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("id")String id) throws InterruptedException, ExecutionException {
        Long idN = Long.parseLong(id);
        //加载数据
        Map<String, Object> modelMap = this.goodsService.loadModel(idN);
        //把数据放入模型中
        model.addAllAttributes(modelMap);
        //页面静态化
//        this.goodsHtmlService.asyncExecute(idN);
//        return "item";
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX+id);
        String html = (String) hashOperations.get(id);
        /**
         * 先取缓存
         */
        if (StringUtils.isNotEmpty(html)){
            //不空，则返回
            return html;
        }
        //手动渲染模板
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("item",context);
        if (StringUtils.isNotEmpty(html)){
            //不空，放入缓存
            //设置有效期60秒
            hashOperations.put(id,html);
            hashOperations.expire(60, TimeUnit.SECONDS);
        }
        return html;
    }
}
