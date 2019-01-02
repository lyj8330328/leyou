package com.leyou.vo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Brand;

import java.util.List;
import java.util.Map;

/**
 * @Author: 98050
 * Time: 2018-10-12 21:06
 * Feature: 搜索结果存储对象
 */
public class SearchResult<Goods> extends PageResult<Goods> {

    /**
     * 分类的集合
     */
    private List<Category> categories;

    /**
     * 品牌的集合
     */
    private List<Brand> brands;

    /**
     * 规格参数的过滤条件
     */
    private List<Map<String,Object>> specs;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    public SearchResult(List<Category> categories, List<Brand> brands, List<Map<String,Object>> specs){
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> item,List<Category> categories, List<Brand> brands, List<Map<String,Object>> specs){
        super(total,item);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total,Long totalPage, List<Goods> item,List<Category> categories, List<Brand> brands){
        super(total,totalPage,item);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total,Long totalPage, List<Goods> item,List<Category> categories,
                        List<Brand> brands, List<Map<String,Object>> specs){
        super(total,totalPage,item);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
