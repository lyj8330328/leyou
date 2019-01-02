package com.leyou.item.controller;

import com.leyou.item.service.SpecificationService;
import com.leyou.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author li
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 查询商品分类对应的规格参数模板
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("id") Long id){
        Specification spec = this.specificationService.queryById(id);
        if (spec == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }

    /**
     * 保存一个规格参数模板
     * @param specification
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveSpecification(Specification specification){
        this.specificationService.saveSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 修改一个规格参数模板
     * @param specification
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateSpecification(Specification specification){
        this.specificationService.updateSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable("id") Long id){
        Specification specification = new Specification();
        specification.setCategoryId(id);
        this.specificationService.deleteSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}