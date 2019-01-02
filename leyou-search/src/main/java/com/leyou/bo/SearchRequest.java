package com.leyou.bo;

import java.util.Map;

/**
 * @Author: 98050
 * Time: 2018-10-12 20:08
 * Feature: 搜索业务对象
 */
public class SearchRequest {

    /**
     * 搜索条件
     */
    private String key;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 排序字段
     */
    private String sortBy;

    /**
     * 是否降序
     */
    private Boolean descending;

    /**
     * 过滤字段
     */
    private Map<String,String> filter;

    /**
     * 每页大小，不从页面接收，而是固定大小
    */
    private static final Integer DEFAULT_SIZE = 20;

    /**
     * 默认页
     */
    private static final Integer DEFAULT_PAGE = 1;


    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page == null){
            return DEFAULT_PAGE;
        }
        /**
         * 获取页码时做一些校验，不能小于1
         */
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getDefaultSize() {
        return DEFAULT_SIZE;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "key='" + key + '\'' +
                ", page=" + page +
                ", sortBy='" + sortBy + '\'' +
                ", descending=" + descending +
                ", filter=" + filter +
                '}';
    }
}
