package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SeckillParameter;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.SeckillGoods;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import com.leyou.parameter.pojo.SpuQueryByPageParameter;
import com.leyou.item.pojo.Sku;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


/**
 * @Author: 98050
 * Time: 2018-08-14 22:13
 * Feature:
 */
@RestController
@RequestMapping("goods")
public class GoodsController{

    @Autowired
    private GoodsService goodsService;


    /**
     * 分页查询
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @param saleable
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable",defaultValue = "true") Boolean saleable){
        SpuQueryByPageParameter spuQueryByPageParameter = new SpuQueryByPageParameter(page,rows,sortBy,desc,key,saleable);
        //分页查询spu信息
        PageResult<SpuBo> result = this.goodsService.querySpuByPageAndSort(spuQueryByPageParameter);
        System.out.println("查询数据量："+result.getTotal());
        return ResponseEntity.ok(result);
    }

    /**
     * 保存商品
     * @param spu
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spu){
        this.goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改商品
     * @param spuBo
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    public ResponseEntity<SpuBo> queryGoodsById(@PathVariable("id") Long id){
        SpuBo spuBo=this.goodsService.queryGoodsById(id);
        if (spuBo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spuBo);
    }

    /**
     * 根据Spu的id查询其下所有的sku
     * @param id
     * @return
     */
    @GetMapping("sku/list/{id}")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@PathVariable("id") Long id){
        List<Sku> list = this.goodsService.querySkuBySpuId(id);
        if (list == null || list.size() < 1){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else {
            return ResponseEntity.ok(list);
        }
    }

    /**
     * 根据spu商品id查询详情
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("id") Long id){
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(id);
        if (spuDetail == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else {
            return ResponseEntity.ok(spuDetail);
        }
    }

    /**
     * 商品上下架
     * @param ids
     * @return
     */
    @PutMapping("/spu/out/{id}")
    public ResponseEntity<Void> goodsSoldOut(@PathVariable("id") String ids){

        String separator="-";
        if (ids.contains(separator)){
            String[] goodsId = ids.split(separator);
            for (String id:goodsId){
                this.goodsService.goodsSoldOut(Long.parseLong(id));
            }
        }
        else {
            this.goodsService.goodsSoldOut(Long.parseLong(ids));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 删除商品
     * @param ids
     * @return
     */
    @DeleteMapping("/spu/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("id") String ids){
        String separator="-";
        if (ids.contains(separator)){
            String[] goodsId = ids.split(separator);
            for (String id:goodsId){
                this.goodsService.deleteGoods(Long.parseLong(id));
            }
        }
        else {
            this.goodsService.deleteGoods(Long.parseLong(ids));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据id查询sku
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id){
        Sku sku = this.goodsService.querySkuById(id);
        if (sku == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sku);
    }

    /**
     * 查询秒杀商品
     * @return
     */
    @GetMapping("/seckill/list")
    public ResponseEntity<List<SeckillGoods>> querySeckillGoods(){
        List<SeckillGoods> list = this.goodsService.querySeckillGoods();
        if (list == null || list.size() < 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 添加秒杀商品
     * @param seckillParameters
     * @return
     * @throws ParseException
     */
    @PostMapping("/seckill/add")
    public ResponseEntity<Boolean> addSeckillGoods(@RequestBody List<SeckillParameter> seckillParameters) throws ParseException {
        if (seckillParameters != null && seckillParameters.size() > 0){
            for (SeckillParameter seckillParameter : seckillParameters){
                this.goodsService.addSeckillGoods(seckillParameter);
            }
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

}
