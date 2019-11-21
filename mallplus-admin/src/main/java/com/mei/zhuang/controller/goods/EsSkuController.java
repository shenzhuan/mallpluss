package com.mei.zhuang.controller.goods;


import com.arvato.common.redis.template.RedisRepository;
import com.arvato.ec.common.constant.RedisConstant;
import com.arvato.service.goods.api.feigin.OrderFegin;
import com.arvato.service.goods.api.mq.Sender;
import com.arvato.service.goods.api.orm.dao.EsShopGoodsGroupMapMapper;
import com.arvato.service.goods.api.orm.dao.EsShopGoodsMapper;
import com.arvato.service.goods.api.orm.dao.EsShopGoodsOptionMapper;
import com.arvato.service.goods.api.service.EsShopGoodsCategoryService;
import com.arvato.service.goods.api.service.EsShopGoodsService;
import com.arvato.service.goods.api.service.EsShopSkuService;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsGroupMap;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品管理
 */
@Slf4j
@Api(value = "商品规格管理", description = "", tags = {"商品规格管理"})
@RestController
@RequestMapping("/api/sku")
public class EsSkuController {

    @Resource
    private EsShopGoodsService shopGoodsService;
    @Resource
    private EsShopGoodsCategoryService esShopGoodsCategoryService;
    @Resource
    private EsShopSkuService skuService;
    @Resource
    private EsShopGoodsOptionMapper skuMapper;
    @Resource
    private EsShopGoodsMapper goodsMapper;
    @Resource
    private EsShopGoodsGroupMapMapper goodsGroupMapMapper;
    @Resource
    private RedisRepository redisRepository;
    @Resource
    private OrderFegin orderFegin;

    @Autowired
    private Sender sender;

    @SysLog(MODULE = "商品规格管理", REMARK = "查询sku明细")
    @ApiOperation("查询sku明细")
    @PostMapping(value = "/detail")
    public EsShopGoodsOption getSkuById(@RequestParam("optionId") Long optionId) {
        return skuService.selectById(optionId);
    }

    @SysLog(MODULE = "商品规格管理", REMARK = "查询商品明细")
    @ApiOperation("查询商品明细")
    @PostMapping(value = "/goods/detail")
    public EsShopGoods getGoodsById(@RequestParam("goodsId") Long goodsId) {
        EsShopGoods goods = null;
        try {
             goods = (EsShopGoods) redisRepository.get(String.format(RedisConstant.GOODS, goodsId + ""));
            if (ValidatorUtils.empty(goods) || ValidatorUtils.empty(goods.getId())) {
                goods = goodsMapper.selectById(goodsId);
                if (goods != null) {
                    List<EsShopGoodsOption> optionList = skuMapper.selectList(new QueryWrapper<EsShopGoodsOption>().eq("goods_id", goodsId));
                    goods.setOptions(optionList);
                    redisRepository.set(String.format(RedisConstant.GOODS, goodsId + ""), goods);
                }

            }
        }catch (Exception e){
            goods = goodsMapper.selectById(goodsId);
            if (goods != null) {
                List<EsShopGoodsOption> optionList = skuMapper.selectList(new QueryWrapper<EsShopGoodsOption>().eq("goods_id", goodsId));
                goods.setOptions(optionList);
                redisRepository.set(String.format(RedisConstant.GOODS, goodsId + ""), goods);
            }
        }

        if (ValidatorUtils.notEmpty(goods) && ValidatorUtils.notEmpty(goods.getId())) {
            List<EsShopGoodsGroupMap> goodsGroupMaps = goodsGroupMapMapper.selectList(new QueryWrapper<EsShopGoodsGroupMap>().eq("goods_id", goods.getId()));
            if (goodsGroupMaps != null && goodsGroupMaps.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (EsShopGoodsGroupMap map : goodsGroupMaps) {
                    sb.append(map.getGroupId() + ",");
                }
                sb.substring(0, sb.length() - 1);
                goods.setGroupId(sb.toString());
            }
        }
        return goods;
    }

    @SysLog(MODULE = "商品规格管理", REMARK = "更新商品sku")
    @ApiOperation("更新商品sku")
    @PostMapping(value = "/updateSkuById")
    public Object updateSkuById(@RequestBody EsShopGoodsOption entity) {
        redisRepository.del(String.format(RedisConstant.GOODSDETAIL, entity.getGoodsId() + ""));
        redisRepository.del(String.format(RedisConstant.GOODS, entity.getGoodsId() + ""));
        return skuService.updateById(entity);
    }

    @SysLog(MODULE = "商品规格管理", REMARK = "添加商品")
    @ApiOperation("添加商品")
    @PostMapping(value = "/addGoods")
    public long addGoods(@RequestBody EsShopGoods entity) {
        shopGoodsService.insert(entity);
        return entity.getId();
    }

    @SysLog(MODULE = "商品规格管理", REMARK = "更新商品")
    @ApiOperation("更新商品")
    @PostMapping(value = "/updateGoodsById")
    public Object updateGoodsById(@RequestBody EsShopGoods entity) {
        redisRepository.del(String.format(RedisConstant.GOODSDETAIL, entity.getId() + ""));
        redisRepository.del(String.format(RedisConstant.GOODS, entity.getId() + ""));
        return shopGoodsService.updateById(entity);
    }

    @SysLog(MODULE = "商品规格管理", REMARK = "删除规则商品的关联商品")
    @ApiOperation("删除规则商品的关联商品")
    @PostMapping(value = "/deleteGoodsById")
    public Object deleteGoodsById(@RequestParam Long id) {
        redisRepository.del(String.format(RedisConstant.GOODSDETAIL, id + ""));
        redisRepository.del(String.format(RedisConstant.GOODS, id + ""));
        return shopGoodsService.deleteById(id);
    }

    //@SysLog(MODULE = "商品规格管理", REMARK = "减sku库存")
    @ApiOperation("减sku库存")
    @PostMapping(value = "/decrSkuStock")
    public int decrSkuStock(@RequestParam("optionId") Long optionId, @RequestParam("total") Integer total,@RequestParam("goodsId") Long goodsId,@RequestParam("type") Integer type) {
        sender.goodsStockUpdateMq("减sku库存");
        //判断是否是定制礼盒
        EsShopGoods goods = goodsMapper.selectById(goodsId);
        if(type == 6){
            orderFegin.updvituralStock(goods.getSmallBeautyBoxId(),total);
        }
        return skuMapper.decrSkuStock(optionId, total);
    }

    //@SysLog(MODULE = "商品规格管理", REMARK = "减商品库存")
    @ApiOperation("减商品库存")
    @PostMapping(value = "/decrGoodsStock")
    public int decrGoodsStock(@RequestParam("goodsId") Long goodsId, @RequestParam("total") Integer total) {
        sender.goodsStockUpdateMq("减商品库存");
        return goodsMapper.decrGoodsStock(goodsId, total);
    }

    @SysLog(MODULE = "商品规格管理", REMARK = "增加商品Sku库存")
    @ApiOperation("增加商品Sku库存")
    @PostMapping(value = "/addSkuStock")
    void addSkuStock(@RequestParam("optionId") Long optionId, @RequestParam("total") Integer total,@RequestParam("goodsId") Long goodsId) {
        sender.goodsStockUpdateMq("增加商品Sku库存");
        skuMapper.addSkuStock(optionId, total);
    }

    @SysLog(MODULE = "商品规格管理", REMARK = "增加商品库存")
    @ApiOperation("增加商品库存")
    @PostMapping(value = "/addGoodsStock")
    void addGoodsStock(@RequestParam("goodsId") Long goodsId, @RequestParam("total") Integer total) {
        sender.goodsStockUpdateMq("增加商品库存");
        goodsMapper.addGoodsStock(goodsId, total);
    }
}
