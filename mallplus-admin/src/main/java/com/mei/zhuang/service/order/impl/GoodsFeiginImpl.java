package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.constant.RedisConstant;
import com.mei.zhuang.dao.goods.EsShopGoodsGroupMapMapper;
import com.mei.zhuang.dao.goods.EsShopGoodsMapper;
import com.mei.zhuang.dao.goods.EsShopGoodsOptionMapper;
import com.mei.zhuang.entity.goods.*;
import com.mei.zhuang.redis.template.RedisRepository;
import com.mei.zhuang.service.goods.*;
import com.mei.zhuang.service.order.GoodsFegin;
import com.mei.zhuang.service.order.ShopOrderService;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class GoodsFeiginImpl implements GoodsFegin {

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
    private EsShopCustomizedPacketServer esShopCustomizedPacketServer;
    @Resource
    private EsShopCustomizedLegendService esShopCustomizedLegendService;

    @Resource
    private ShopOrderService orderService;
    @Resource
    private EsShopCustomizedBasicService esShopCustomizedBasicService;
    @Resource
    private EsShopGoodsOptionService optionService;

    @Resource
    private EsShopGoodsSpecService esShopGoodsSpecService;
    @Resource
    private EsShopGoodsSpecItemService esShopGoodsSpecItemService;


    @Override
    public EsShopGoodsOption getSkuById(Long optionId) {
        return skuService.getById(optionId);
    }

    @Override
    public EsShopGoods getGoodsById(Long goodsId) {
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
        } catch (Exception e) {
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

    @Override
    public void updateSkuById(EsShopGoodsOption entity) {
        redisRepository.del(String.format(RedisConstant.GOODSDETAIL, entity.getGoodsId() + ""));
        redisRepository.del(String.format(RedisConstant.GOODS, entity.getGoodsId() + ""));
        skuService.updateById(entity);
    }

    @Override
    public void updateGoodsById(EsShopGoods entity) {
        redisRepository.del(String.format(RedisConstant.GOODSDETAIL, entity.getId() + ""));
        redisRepository.del(String.format(RedisConstant.GOODS, entity.getId() + ""));
        shopGoodsService.updateById(entity);
    }

    @Override
    public int decrSkuStock(Long optionId, Integer total, Long goodsId, Integer type) {
        //判断是否是定制礼盒
        EsShopGoods goods = goodsMapper.selectById(goodsId);
        if (type == 6) {
            orderService.updvituralStock(goods.getSmallBeautyBoxId(), total);
        }
        return skuMapper.decrSkuStock(optionId, total);
    }

    @Override
    public int decrGoodsStock(Long goodsId, Integer total) {
        return goodsMapper.decrGoodsStock(goodsId, total);
    }

    @Override
    public void addSkuStock(Long optionId, Integer total, Long goodsId) {
        skuMapper.addSkuStock(optionId, total);
    }

    @Override
    public void addGoodsStock(Long goodsId, Integer total) {
        goodsMapper.addGoodsStock(goodsId, total);
    }

    @Override
    public Object detailLegend(Long id) {
        return esShopCustomizedLegendService.getById(id);
    }

    @Override
    public Object detailBasics(EsShopCustomizedBasic entity) {
        return esShopCustomizedBasicService.detail(entity.getId());
    }

    @Override
    public Object detail(Long id) {
        return null;
    }

    @Override
    public Object details(Long id) {
        return null;
    }

    @Override
    public Object selectCust(Long id) {
        return null;
    }

    @Override
    public List<EsShopCustomizedPacket> getPackInfo(List<Long> packIds) {
        return (List<EsShopCustomizedPacket>) esShopCustomizedPacketServer.listByIds(packIds);
    }

    @Override
    public EsShopCustomizedPacket getPackInfoByOne(Long packId) {
        return esShopCustomizedPacketServer.getById(packId);
    }

    @Override
    public EsShopCustomizedBasic detailBasics(Long id) {
        return esShopCustomizedBasicService.detail(id);
    }

    @Override
    public EsShopCustomizedBasic getCustBasicById(Long basicId) {
        return esShopCustomizedBasicService.getById(basicId);
    }

    @Override
    public List<EsShopCustomizedBasic> getCustBasicByIds(List<Long> basicIds) {
        return (List<EsShopCustomizedBasic>) esShopCustomizedBasicService.listByIds(basicIds);
    }

    @Override
    public boolean insSmallBeautyBoxCust(String boxName, String boxCode, String boxImg, Integer vituralStock, BigDecimal price, Long id, String productSn) {
        boolean bool = false;
        try {
            //1.添加商品
            EsShopGoods goods = new EsShopGoods();
            goods.setTitle(boxName);
            goods.setGoodsCode(boxCode);
            goods.setThumb(boxImg);
            goods.setVituralStock(vituralStock);
            goods.setPrice(price);
            goods.setType(6);
            goods.setStatus(1);
            goods.setSmallBeautyBoxId(id);//定制礼盒id
            goods.setEnableSpec(1);
            goods.setPutawayTime(String.valueOf(System.currentTimeMillis()));
            goods.setPutawayType("微信小程序");
            goods.setDisplayOrder(0);
            goods.setDefaultSpec("规格:规格值");
            goods.setCreateTime(new Date());
            bool = shopGoodsService.save(goods);
            //1添加规格规格值
            //2.添加规格
            EsShopGoodsSpec spec = new EsShopGoodsSpec();
            spec.setGoodsId(goods.getId());
            spec.setIsMainSpec(1);
            spec.setTitle("规格");
            spec.setDisplayOrder(0);
            bool = esShopGoodsSpecService.save(spec);
            //3.添加规格值
            EsShopGoodsSpecItem specItem = new EsShopGoodsSpecItem();
            specItem.setGoodsId(goods.getId());
            specItem.setSpecId(spec.getId());
            specItem.setTitle("规格值");
            specItem.setDisplayOrder(0);
            specItem.setShow("1");
            specItem.setStatus(1);
            specItem.setType(1);
            specItem.setTypeword("1");
            specItem.setMoney(goods.getPrice());
            specItem.setTitleItem(spec.getTitle());
            bool = esShopGoodsSpecItemService.save(specItem);
            //3.option
            EsShopGoodsOption option = new EsShopGoodsOption();
            option.setGoodsName(goods.getTitle());
            option.setSpecIds(specItem.getId().toString());
            option.setTitle(specItem.getTitle());
            option.setDisplayOrder(0);
            option.setPrice(goods.getPrice());
            option.setSpecs("规格:规格值");
            option.setStock(goods.getVituralStock());
            option.setStockWarning(0);
            option.setBanner("[\"\"]");
            option.setVirtualStock(goods.getVituralStock());
            option.setMarketprice(goods.getPrice());
            option.setGoodsId(goods.getId());
            option.setThumb(boxImg);
            option.setGoodsCode(boxCode);
            option.setProductsn(productSn);//条码
            bool = optionService.save(option);
        } catch (Exception e) {
            e.printStackTrace();
            bool = false;
        }
        return bool;

    }

    @Override
    public boolean updSmallBeautyBoxCust(String boxName, String boxCode, String boxImg, Integer vituralStock, BigDecimal price, Long id, String productSn) {
        boolean bool = false;
        try {
            //查询商品id
            EsShopGoods goods = new EsShopGoods();
            goods.setSmallBeautyBoxId(id);
            EsShopGoods esShopGoods = shopGoodsService.getOne(new QueryWrapper<>(goods));
            //1.添加商品
            goods.setTitle(boxName);
            goods.setGoodsCode(boxCode);
            goods.setThumb(boxImg);
            goods.setVituralStock(vituralStock);
            goods.setPrice(price);
            goods.setType(6);
            goods.setSmallBeautyBoxId(id);//定制礼盒id
            goods.setId(esShopGoods.getId());
            goods.setEnableSpec(1);
            bool = shopGoodsService.updateById(goods);
            //2.添加规格
            EsShopGoodsSpec spec = new EsShopGoodsSpec();
            spec.setGoodsId(goods.getId());
            EsShopGoodsSpec specs = esShopGoodsSpecService.getOne(new QueryWrapper<>(spec));
            spec.setIsMainSpec(1);
            spec.setDisplayOrder(0);
            spec.setId(specs.getId());
            bool = esShopGoodsSpecService.updateById(spec);
            //3.添加规格值
            EsShopGoodsSpecItem specItem = new EsShopGoodsSpecItem();
            specItem.setGoodsId(goods.getId());
            EsShopGoodsSpecItem specItem1 = esShopGoodsSpecItemService.getOne(new QueryWrapper<>(specItem));
            specItem.setSpecId(spec.getId());
            specItem.setDisplayOrder(0);
            specItem.setShow("1");
            specItem.setStatus(1);
            specItem.setType(1);
            specItem.setMoney(goods.getPrice());
            specItem.setTitleItem(spec.getTitle());
            specItem.setId(specItem1.getId());
            bool = esShopGoodsSpecItemService.updateById(specItem);
            //3.option
            EsShopGoodsOption option = new EsShopGoodsOption();
            option.setGoodsId(goods.getId());
            EsShopGoodsOption opt = optionService.getOne(new QueryWrapper<>(option));
            option.setGoodsName(goods.getTitle());
            option.setSpecIds(specItem.getId().toString());
            option.setTitle(specItem.getTitle());
            option.setDisplayOrder(0);
            option.setPrice(goods.getPrice());
            option.setSpecs(spec.getTitle() + ":" + specItem.getTitle());
            option.setStock(goods.getVituralStock());
            option.setStockWarning(0);
            option.setVirtualStock(goods.getVituralStock());
            option.setMarketprice(goods.getPrice());
            option.setId(opt.getId());
            option.setThumb(boxImg);
            option.setGoodsCode(boxCode);
            option.setProductsn(productSn);//条码
            bool = optionService.updateById(option);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return bool;

    }

    @Override
    public boolean delSmallBeautyBoxCust(Long id) {
        EsShopGoods goods = new EsShopGoods();
        goods.setSmallBeautyBoxId(id);//定制礼盒id
        return shopGoodsService.remove(new QueryWrapper<>(goods));
    }

    @Override
    public EsShopGoods goodsDetail(Long id) {
        EsShopGoods goods = new EsShopGoods();
        goods.setSmallBeautyBoxId(id);//定制礼盒id
        return shopGoodsService.getOne(new QueryWrapper<>(goods));
    }

    @Override
    public EsShopGoodsOption goodsOption(Long goodsId) {
        EsShopGoodsOption option = new EsShopGoodsOption();
        option.setGoodsId(goodsId);
        return optionService.getOne(new QueryWrapper<>(option));
    }

    @Override
    public List<EsShopGoods> selShopGoodsList(TradeAnalyzeParam param) {
        //  param.setEndTime(param.getEndTime() + " 23:59:59.999");
        //status商品状态（1为出售中，3为已售罄，-2为仓库中，-1为回收站）
        //条件
        QueryWrapper<EsShopGoods> condition = new QueryWrapper();
        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        if (ValidatorUtils.notEmpty(param.getShopId())) {
            condition.eq("shop_id", param.getShopId());
        }
        return shopGoodsService.list(condition);
    }
}
