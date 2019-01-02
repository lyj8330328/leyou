package com.leyou.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.service.impl.SearchServiceImpl;
import com.leyou.bo.SearchRequest;
import com.leyou.pojo.Goods;
import com.leyou.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 98050
 * Time: 2018-10-12 20:21
 * Feature:
 */
@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchServiceImpl searchService;

    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){
        SearchResult<Goods> result = this.searchService.search(searchRequest);
        if (result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return ResponseEntity.ok(result);
        }
    }
}
