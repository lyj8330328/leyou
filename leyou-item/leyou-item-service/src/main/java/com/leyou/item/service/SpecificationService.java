package com.leyou.item.service;

import com.leyou.item.pojo.Specification;

/**
 * @Author: 98050
 * Time: 2018-08-14 15:26
 * Feature:
 */
public interface SpecificationService {
    /**
     * 根据category id查询规格参数模板
     * @param id
     * @return
     */
    Specification queryById(Long id);

    /**
     * 添加规格参数模板
     * @param specification
     */
    void saveSpecification(Specification specification);

    /**
     * 修改规格参数模板
     * @param specification
     */
    void updateSpecification(Specification specification);

    /**
     * 删除规格参数模板
     * @param specification
     */
    void deleteSpecification(Specification specification);
}
