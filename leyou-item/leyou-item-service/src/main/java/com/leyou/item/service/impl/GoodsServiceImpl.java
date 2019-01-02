package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SeckillParameter;
import com.leyou.parameter.pojo.SpuQueryByPageParameter;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 98050
 * Time: 2018-08-14 22:15
 * Feature:
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "leyou:seckill:stock";

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);

    /**
     * 分页查询
     * @param spuQueryByPageParameter
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPageAndSort(SpuQueryByPageParameter spuQueryByPageParameter) {

        //1.查询spu，分页查询，最多查询100条
        PageHelper.startPage(spuQueryByPageParameter.getPage(),Math.min(spuQueryByPageParameter.getRows(),100));

        //2.创建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //3.条件过滤
        //3.1 是否过滤上下架
        if (spuQueryByPageParameter.getSaleable() != null){
            System.out.println(spuQueryByPageParameter.getSaleable());
            criteria.orEqualTo("saleable",spuQueryByPageParameter.getSaleable());
        }
        //3.2 是否模糊查询
        if (StringUtils.isNotBlank(spuQueryByPageParameter.getKey())){
            criteria.andLike("title","%"+spuQueryByPageParameter.getKey()+"%");
        }
        //3.3 是否排序
        if (StringUtils.isNotBlank(spuQueryByPageParameter.getSortBy())){
            example.setOrderByClause(spuQueryByPageParameter.getSortBy()+(spuQueryByPageParameter.getDesc()? " DESC":" ASC"));
        }
        Page<Spu> pageInfo = (Page<Spu>) this.spuMapper.selectByExample(example);


        //将spu变为spubo
        List<SpuBo> list = pageInfo.getResult().stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            //1.属性拷贝
            BeanUtils.copyProperties(spu,spuBo);

            //2.查询spu的商品分类名称，各级分类
            List<String> nameList = this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            //3.拼接名字,并存入
            spuBo.setCname(StringUtils.join(nameList,"/"));
            //4.查询品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(),list);
    }

    /**
     * 保存商品
     * @param spu
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveGoods(SpuBo spu) {
        //保存spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insert(spu);

        //保存spu详情
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        System.out.println(spuDetail.getSpecifications().length());
        this.spuDetailMapper.insert(spuDetail);

        //保存sku和库存信息
        saveSkuAndStock(spu.getSkus(),spu.getId());

        //发送消息到mq
        this.sendMessage(spu.getId(),"insert");
    }

    /**
     * 根据id查询商品信息
     * @param id
     * @return
     */
    @Override
    public SpuBo queryGoodsById(Long id) {
        /**
         * 第一页所需信息如下：
         * 1.商品的分类信息、所属品牌、商品标题、商品卖点（子标题）
         * 2.商品的包装清单、售后服务
         */
        Spu spu=this.spuMapper.selectByPrimaryKey(id);
        SpuDetail spuDetail = this.spuDetailMapper.selectByPrimaryKey(spu.getId());

        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",spu.getId());
        List<Sku> skuList = this.skuMapper.selectByExample(example);
        List<Long> skuIdList = new ArrayList<>();
        for (Sku sku : skuList){
            skuIdList.add(sku.getId());
        }

        List<Stock> stocks = this.stockMapper.selectByIdList(skuIdList);

        for (Sku sku:skuList){
            for (Stock stock : stocks){
                if (sku.getId().equals(stock.getSkuId())){
                    sku.setStock(stock.getStock());
                }
            }
        }

        SpuBo spuBo = new SpuBo(spu.getBrandId(),spu.getCid1(),spu.getCid2(),spu.getCid3(),spu.getTitle(),
                spu.getSubTitle(),spu.getSaleable(),spu.getValid(),spu.getCreateTime(),spu.getLastUpdateTime());
        spuBo.setSpuDetail(spuDetail);
        spuBo.setSkus(skuList);
        return spuBo;
    }

    /**
     * 更新商品信息
     * @param spuBo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateGoods(SpuBo spuBo) {
        /**
         * 更新策略：
         *      1.判断tb_spu_detail中的spec_template字段新旧是否一致
         *      2.如果一致说明修改的只是库存、价格和是否启用，那么就使用update
         *      3.如果不一致，说明修改了特有属性，那么需要把原来的sku全部删除，然后添加新的sku
         */

        //更新spu
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        //更新spu详情
        SpuDetail spuDetail = spuBo.getSpuDetail();
        String oldTemp = this.spuDetailMapper.selectByPrimaryKey(spuBo.getId()).getSpecTemplate();
        if (spuDetail.getSpecTemplate().equals(oldTemp)){
            //相等，sku update
            //更新sku和库存信息
            updateSkuAndStock(spuBo.getSkus(),spuBo.getId(),true);
        }else {
            //不等，sku insert
            //更新sku和库存信息
            updateSkuAndStock(spuBo.getSkus(),spuBo.getId(),false);
        }
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.updateByPrimaryKeySelective(spuDetail);

        //发送消息到mq
        this.sendMessage(spuBo.getId(),"update");
    }

    /**
     * 商品下架
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void goodsSoldOut(Long id) {
        //下架或者上架spu中的商品

        Spu oldSpu = this.spuMapper.selectByPrimaryKey(id);
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",id);
        List<Sku> skuList = this.skuMapper.selectByExample(example);
        if (oldSpu.getSaleable()){
            //下架
            oldSpu.setSaleable(false);
            this.spuMapper.updateByPrimaryKeySelective(oldSpu);
            //下架sku中的具体商品
            for (Sku sku : skuList){
                sku.setEnable(false);
                this.skuMapper.updateByPrimaryKeySelective(sku);
            }
        }else {
            //上架
            oldSpu.setSaleable(true);
            this.spuMapper.updateByPrimaryKeySelective(oldSpu);
            //上架sku中的具体商品
            for (Sku sku : skuList){
                sku.setEnable(true);
                this.skuMapper.updateByPrimaryKeySelective(sku);
            }
        }

        //发送消息到mq
        this.sendMessage(id,"update");
    }

    /**
     * 商品删除二合一（多个单个）
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteGoods(long id) {
        //删除spu表中的数据
        this.spuMapper.deleteByPrimaryKey(id);

        //删除spu_detail中的数据
        Example example = new Example(SpuDetail.class);
        example.createCriteria().andEqualTo("spuId",id);
        this.spuDetailMapper.deleteByExample(example);


        List<Sku> skuList = this.skuMapper.selectByExample(example);
        for (Sku sku : skuList){
            //删除sku中的数据
            this.skuMapper.deleteByPrimaryKey(sku.getId());
            //删除stock中的数据
            this.stockMapper.deleteByPrimaryKey(sku.getId());
        }

        //发送消息到mq
        this.sendMessage(id,"delete");

    }

    @Override
    public SpuDetail querySpuDetailBySpuId(long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Sku> querySkuBySpuId(Long id) {
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",id);
        List<Sku> skuList = this.skuMapper.selectByExample(example);
        for (Sku sku : skuList){
            Example temp = new Example(Stock.class);
            temp.createCriteria().andEqualTo("skuId", sku.getId());
            Stock stock = this.stockMapper.selectByExample(temp).get(0);
            sku.setStock(stock.getStock());
        }
        return skuList;
    }

    /**
     * 发送消息到mq，生产者
     * @param id
     * @param type
     */
    @Override
    public void sendMessage(Long id, String type) {
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        }catch (Exception e){
            LOGGER.error("{}商品消息发送异常，商品id：{}",type,id,e);
        }
    }

    /**
     * 根据skuId查询sku
     * @param id
     * @return
     */
    @Override
    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }

    private void updateSkuAndStock(List<Sku> skus,Long id,boolean tag) {
        //通过tag判断是insert还是update
        //获取当前数据库中spu_id = id的sku信息
        Example e = new Example(Sku.class);
        e.createCriteria().andEqualTo("spuId",id);
        //oldList中保存数据库中spu_id = id 的全部sku
        List<Sku> oldList = this.skuMapper.selectByExample(e);
        if (tag){
            /**
             * 判断是更新时是否有新的sku被添加：如果对已有数据更新的话，则此时oldList中的数据和skus中的ownSpec是相同的，否则则需要新增
             */
            int count = 0;
            for (Sku sku : skus){
                if (!sku.getEnable()){
                    continue;
                }
                for (Sku old : oldList){
                    if (sku.getOwnSpec().equals(old.getOwnSpec())){
                        System.out.println("更新");
                        //更新
                        List<Sku> list = this.skuMapper.select(old);
                        if (sku.getPrice() == null){
                            sku.setPrice(0L);
                        }
                        if (sku.getStock() == null){
                            sku.setStock(0L);
                        }
                        sku.setId(list.get(0).getId());
                        sku.setCreateTime(list.get(0).getCreateTime());
                        sku.setSpuId(list.get(0).getSpuId());
                        sku.setLastUpdateTime(new Date());
                        this.skuMapper.updateByPrimaryKey(sku);
                        //更新库存信息
                        Stock stock = new Stock();
                        stock.setSkuId(sku.getId());
                        stock.setStock(sku.getStock());
                        this.stockMapper.updateByPrimaryKeySelective(stock);
                        //从oldList中将更新完的数据删除
                        oldList.remove(old);
                        break;
                    }else{
                        //新增
                        count ++ ;
                    }
                }
                if (count == oldList.size() && count != 0){
                    //当只有一个sku时，更新完因为从oldList中将其移除，所以长度变为0，所以要需要加不为0的条件
                    List<Sku> addSku = new ArrayList<>();
                    addSku.add(sku);
                    saveSkuAndStock(addSku,id);
                    count = 0;
                }else {
                    count =0;
                }
            }
            //处理脏数据
            if (oldList.size() != 0){
                for (Sku sku : oldList){
                    this.skuMapper.deleteByPrimaryKey(sku.getId());
                    Example example = new Example(Stock.class);
                    example.createCriteria().andEqualTo("skuId",sku.getId());
                    this.stockMapper.deleteByExample(example);
                }
            }
        }else {
            List<Long> ids = oldList.stream().map(Sku::getId).collect(Collectors.toList());
            //删除以前的库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId",ids);
            this.stockMapper.deleteByExample(example);
            //删除以前的sku
            Example example1 = new Example(Sku.class);
            example1.createCriteria().andEqualTo("spuId",id);
            this.skuMapper.deleteByExample(example1);
            //新增sku和库存
            saveSkuAndStock(skus,id);
        }


    }

    private void saveSkuAndStock(List<Sku> skus, Long id) {
        for (Sku sku : skus){
            if (!sku.getEnable()){
                continue;
            }
            //保存sku
            sku.setSpuId(id);
            //默认不参加任何促销
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);

            //保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        }
    }


    /**
     * 查询秒杀商品
     * @return
     */
    @Override
    public List<SeckillGoods> querySeckillGoods() {
        Example example = new Example(SeckillGoods.class);
        example.createCriteria().andEqualTo("enable",true);
        List<SeckillGoods> list = this.seckillMapper.selectByExample(example);
        list.forEach(goods -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(goods.getSkuId());
            goods.setStock(stock.getSeckillStock());
            goods.setSeckillTotal(stock.getSeckillTotal());
        });
        return list;
    }

    /**
     * 添加秒杀商品
     * @param seckillParameter
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSeckillGoods(SeckillParameter seckillParameter) throws ParseException {

        SimpleDateFormat sdf =  new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        //1.根据spu_id查询商品
        Long id = seckillParameter.getId();
        Sku sku = this.querySkuById(id);
        //2.插入到秒杀商品表中
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setEnable(true);
        seckillGoods.setStartTime(sdf.parse(seckillParameter.getStartTime().trim()));
        seckillGoods.setEndTime(sdf.parse(seckillParameter.getEndTime().trim()));
        seckillGoods.setImage(sku.getImages());
        seckillGoods.setSkuId(sku.getId());
        seckillGoods.setStock(seckillParameter.getCount());
        seckillGoods.setTitle(sku.getTitle());
        seckillGoods.setSeckillPrice(sku.getPrice()*seckillParameter.getDiscount());
        this.seckillMapper.insert(seckillGoods);
        //3.更新对应的库存信息，tb_stock
        Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
        System.out.println(stock);
        if (stock != null) {
            stock.setSeckillStock(stock.getSeckillStock() != null ? stock.getSeckillStock() + seckillParameter.getCount() : seckillParameter.getCount());
            stock.setSeckillTotal(stock.getSeckillTotal() != null ? stock.getSeckillTotal() + seckillParameter.getCount() : seckillParameter.getCount());
            stock.setStock(stock.getStock() - seckillParameter.getCount());
            this.stockMapper.updateByPrimaryKeySelective(stock);
        }else {
            LOGGER.info("更新库存失败！");
        }

        //4.更新redis中的秒杀库存
        updateSeckillStock();
    }



    /**
     * 更新秒杀商品数量
     * @throws Exception
     */
    public void updateSeckillStock(){
        //1.查询可以秒杀的商品
        List<SeckillGoods> seckillGoods = this.querySeckillGoods();
        if (seckillGoods == null || seckillGoods.size() == 0){
            return;
        }
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX);
        if (hashOperations.hasKey(KEY_PREFIX)){
            hashOperations.delete(KEY_PREFIX);
        }
        seckillGoods.forEach(goods -> hashOperations.put(goods.getSkuId().toString(),goods.getStock().toString()));
    }

}

