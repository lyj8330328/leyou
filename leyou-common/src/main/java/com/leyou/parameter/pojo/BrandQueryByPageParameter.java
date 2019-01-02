package com.leyou.parameter.pojo;

/**
 * @Author: 98050
 * Time: 2018-08-08 11:38
 * Feature:
 */
public class BrandQueryByPageParameter {

    /*
    *   - page：当前页，int
        - rows：每页大小，int
        - sortBy：排序字段，String
        - desc：是否为降序，boolean
        - key：搜索关键词，String
    * */

    private Integer page;
    private Integer rows;
    private String sortBy;
    private Boolean desc;
    private String key;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BrandQueryByPageParameter(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        this.page = page;
        this.rows = rows;
        this.sortBy = sortBy;
        this.desc = desc;
        this.key = key;
    }

    public BrandQueryByPageParameter(){
        super();
    }

    @Override
    public String toString() {
        return "BrandQueryByPageParameter{" +
                "page=" + page +
                ", rows=" + rows +
                ", sortBy='" + sortBy + '\'' +
                ", desc=" + desc +
                ", key='" + key + '\'' +
                '}';
    }
}
