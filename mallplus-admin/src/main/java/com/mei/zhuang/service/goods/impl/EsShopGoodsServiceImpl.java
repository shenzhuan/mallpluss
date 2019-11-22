package com.mei.zhuang.service.goods.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mei.zhuang.constant.RedisConstant;
import com.mei.zhuang.dao.goods.*;
import com.mei.zhuang.entity.goods.*;
import com.mei.zhuang.service.member.impl.RedisUtil;
import com.mei.zhuang.service.goods.EsShopGoodsService;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.utils.ImgBase64Util;
import com.mei.zhuang.utils.JsonUtils;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.goods.EsShopGoodsParam;
import com.mei.zhuang.vo.goods.GoodsDetail;
import com.mei.zhuang.vo.goods.GoodsQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Slf4j
@Api(value = "商品管理", description = "", tags = {"商品管理"})
@Service
public class EsShopGoodsServiceImpl extends ServiceImpl<EsShopGoodsMapper, EsShopGoods> implements EsShopGoodsService {

    @Resource
    private EsShopGoodsMapper esShopGoodsMapper;

    @Resource
    private EsShopGoodsCateMapMapper esShopGoodsCateMapMapper;
    @Resource
    private EsShopGoodsSpecMapper esShopGoodsSpecMapper;
    @Resource
    private EsShopGoodsSpecItemMapper esShopGoodsSpecItemMapper;
    @Resource
    private EsShopGoodsOptionMapper esShopGoodsOptionMapper;
    @Resource
    private EsShopGoodsRecomMapper goodsRecomMapper;
    @Resource
    private EsShopGoodsRecomMapper esShopGoodsRecomMapper;
    @Resource
    private EsShopGoodsCategoryMapper esShopGoodsCategoryMapper;
    @Resource
    private EsShopDiypageMapper esShopDiypageMapper;
    @Resource
    private EsShopGoodsDiypageMapMapper esShopGoodsDiypageMapMapper;
    @Resource
    private EsShopGoodsGroupMapper esShopGoodsGroupMapper;
    @Resource
    private RedisUtil redisRepository;
    @Resource
    private EsShopGoodsQRCodeMapper esShopGoodsQRCodeMapper;
    @Resource
    private EsShopGoodsCategoryRecomMapper esShopGoodsCategoryRecomMapper;
    @Resource
    private EsShopCustomizedCardMapper esShopCustomizedCardMapper;
    @Resource
    private EsShopCustomizedPacketMapper esShopCustomizedPacketMapper;
    @Resource
    private EsShopCustomizedLegendMapper esShopCustomizedLegendMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void descartes(List<List<EsShopGoodsSpecItem>> dimvalue, List<List<EsShopGoodsSpecItem>> result, int layer, List<EsShopGoodsSpecItem> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                descartes(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<EsShopGoodsSpecItem> list = new ArrayList<EsShopGoodsSpecItem>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    descartes(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<EsShopGoodsSpecItem> list = new ArrayList<EsShopGoodsSpecItem>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }

    @ApiOperation("根据条件查询所有商品列表：出售中、已售罄等模块调用该方法就可以，给定对应的状态")
    @Override
    public Object getGoodsByPage(EsShopGoods entity) {
        try {
            Page p = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
            IPage<EsShopGoods> page = esShopGoodsMapper.selectPage(p, new QueryWrapper<>(entity));

            for (EsShopGoods lis : page.getRecords()) {
                //根据商品分类id查询商品分类名称
                if (lis.getCategoryId() != null && !lis.getCategoryId().equals("")) {
                    String[] cateId = lis.getCategoryId().split(",");
                    String cateName = "";//分类名称
                    for (int i = 0; i < cateId.length; i++) {
                        if (!cateId[i].equals("") && cateId[i] != null) {
                            Long var = Long.parseLong(cateId[i]);
                            String name = esShopGoodsMapper.selCategoryName(var);
                            if (name != null && !name.equals("")) {
                                cateName += name + " ";
                            }
                        }
                    }
                    lis.setCategoryName(cateName);
                }
            }
            return new CommonResult().success(page);
        } catch (Exception e) {
            log.error("根据条件查询所有商品列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("保存商品")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object saveGoods(EsShopGoodsParam entity) {
        try {
            Date da = new Date();
            String str = sdf.format(da);
            entity.setCreateTime(sdf.parse(str));
            Integer num = 0;
            if (entity.getEnableSpec() != null && !entity.getEnableSpec().equals("")) {
                if (entity.getEnableSpec() == 1) {
                    entity.setVituralStock(num);
                }
            }

            if (entity.getVituralStock() == null || entity.getVituralStock() == 0) {
                entity.setStatus(3);
            }

            if (entity.getCategoryId() != null && !entity.getCategoryId().equals("")) {

                List<String> lists = new ArrayList<String>();
                String[] id = entity.getCategoryId().split(",");
                for (int i = 0; i < id.length; i++) {
                    String name = "";
                    EsShopGoodsCategory essOne = null;
                    EsShopGoodsCategory essTwo = null;

                    EsShopGoodsCategory ess = esShopGoodsCategoryMapper.selDetail(Long.parseLong(id[i]));
                    if (ess.getParentId() != null) {
                        essTwo = esShopGoodsCategoryMapper.selDetail(ess.getParentId());
                        if (essTwo != null) {
                            essOne = esShopGoodsCategoryMapper.selDetail(essTwo.getParentId());
                        }
                    }
                    if (essOne != null) {
                        name += essOne.getName() + "/" + essTwo.getName() + "/" + ess.getName();
                        essOne = null;
                        essTwo = null;
                        ess = null;
                    } else if (essTwo != null) {
                        name += essTwo.getName() + "/" + ess.getName();
                        essTwo = null;
                        ess = null;
                    } else {
                        name += ess.getName();
                        ess = null;
                    }
                    if (entity.getCategoryName() != null && !entity.getCategoryName().equals("")) {
                        entity.setCategoryName(entity.getCategoryName() + "," + name);
                    } else {
                        entity.setCategoryName(name);
                    }
                }
            }
            if (entity.getIsPutaway() == 0) {
                entity.setStatus(-2);
            }
            this.save(entity);
            if (entity.getId() != null) {
                EsShopGoodsQRCode qrCode = new EsShopGoodsQRCode();
                qrCode.setGoodsId(entity.getId());
                qrCode.setImgBase(ImgBase64Util.StringUtil(entity.getId()));
                esShopGoodsQRCodeMapper.insert(qrCode);
            }


            if (entity.getEnableSpec() != null) {
                if (entity.getEnableSpec() == 1) {
                    //3 添加商品商品规格
                    if (entity.getGoodsSpecList() != null && entity.getGoodsSpecList().size() > 0) {
                        Map<Integer, Object> map = new HashMap<Integer, Object>();
                        List<List<EsShopGoodsSpecItem>> list = new ArrayList<List<EsShopGoodsSpecItem>>();
                        Integer nums = 1;
                        for (EsShopGoodsSpec rule : entity.getGoodsSpecList()) {
                            rule.setGoodsId(entity.getId());
                            rule.setShopId(entity.getShopId());
                            esShopGoodsSpecMapper.insert(rule);
                            //4 添加商品规格值
                            if (entity.getItemList() == null || entity.getItemList().size() <= 0) {
                            } else {
                                List<EsShopGoodsSpecItem> listSub = new ArrayList<EsShopGoodsSpecItem>();
                                for (EsShopGoodsSpecItem item : entity.getItemList()) {
                                    if (rule.getTitle().equals(item.getTitleItem())) {
                                        item.setGoodsId(entity.getId());
                                        item.setSpecId(rule.getId());
                                        item.setShopId(entity.getShopId());
                                        esShopGoodsSpecItemMapper.insert(item);
                                        listSub.add(item);
                                        if (item.getStatus() != null && item.getStatus() == 1) {
                                            String defaultSpec = "";
                                            EsShopGoods goods = esShopGoodsMapper.selectById(entity.getId());
                                            if (goods.getDefaultSpec() != null && !goods.getDefaultSpec().equals("")) {
                                                defaultSpec = goods.getDefaultSpec() + item.getTitle();
                                            } else {
                                                defaultSpec = item.getTitle();
                                            }
                                            goods = new EsShopGoods();
                                            goods.setId(entity.getId());
                                            goods.setDefaultSpec(defaultSpec);
                                            esShopGoodsMapper.updateById(goods);
                                        }
                                    }
                                }
                                map.put(nums, listSub);
                            }
                            nums += 1;
                        }
                        for (Object values : map.values()) {
                            list.add((List<EsShopGoodsSpecItem>) values);
                        }
                        List<List<EsShopGoodsSpecItem>> result = new ArrayList<List<EsShopGoodsSpecItem>>();
                        descartes(list, result, 0, new ArrayList<EsShopGoodsSpecItem>());
                        for (List<EsShopGoodsSpecItem> lis : result) {
                            String specs = "";
                            String title = "";
                            Integer count = 1;
                            String specItemIds = "";
                            EsShopGoodsOption option = new EsShopGoodsOption();
                            option.setMarketprice(new BigDecimal("0"));
                            for (int i = 0; i < lis.size(); i++) {
                                EsShopGoodsSpecItem it = (EsShopGoodsSpecItem) lis.get(i);
                                if (count == lis.size()) {
                                    count += 1;
                                    specs += it.getTitleItem() + ":" + it.getTitle();
                                    title += it.getTitle();
                                    specItemIds += it.getId();
                                } else {
                                    count += 1;
                                    specs += it.getTitleItem() + ":" + it.getTitle() + ",";
                                    title += it.getTitle() + ",";
                                    specItemIds += it.getId() + ",";
                                }
                                option.setMarketprice(option.getMarketprice().add(it.getMoney()));
                                option.setPrice(option.getMarketprice().add(it.getMoney()));
                            }

                            option.setTitle(title);
                            option.setSpecs(specs);
                            option.setVirtualStock(0);
                            option.setGoodsId(entity.getId());
                            option.setSpecIds(specItemIds);
                            option.setGoodsName(entity.getTitle());
                            Integer sun = esShopGoodsOptionMapper.insert(option);
                        }
                    }
                }
            }

            addGoodsOtherInfo(entity);

            return new CommonResult().success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存商品：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    private void addGoodsOtherInfo(EsShopGoodsParam entity) {
        // 1 添加商品分类
        if (entity.getCategoryId() != null && !entity.getCategoryId().equals("")) {
            String[] cateIds = entity.getCategoryId().split(",");
            for (String cid : cateIds) {
                EsShopGoodsCateMap cate = new EsShopGoodsCateMap();
                cate.setCategoryId(Long.valueOf(cid));
                cate.setGoodsId(entity.getId());
                cate.setShopId(entity.getShopId());
                esShopGoodsCateMapMapper.insert(cate);

            }
        }
        // 5 添加商品模板
        if (entity.getDiyPageId() != null) {
            EsShopGoodsDiyPageMap diyPageMap = new EsShopGoodsDiyPageMap();
            diyPageMap.setGoodsId(entity.getId());
            diyPageMap.setDiyPageId(entity.getDiyPageId());
            diyPageMap.setData(entity.getData());
            esShopGoodsDiypageMapMapper.insert(diyPageMap);
        }

        /*if (entity.getEnableSpec() != null) {
            if (entity.getEnableSpec() == 1) {
                //2。添加商品规格SKu
               *//* if(entity.getItemList() != null && entity.getItemList() != null){
                    List<List<EsShopGoodsSpecItem>> list = new ArrayList<List<EsShopGoodsSpecItem>>();
                    list.add(entity.getItemList());
                    List<List<String>> result = new ArrayList<List<String>>();
                    descartes(list,result,0,new ArrayList<String>());
                }*//*
                if (entity.getGoodsOptionList() != null && entity.getGoodsOptionList().size() > 0) {
                    *//*for (EsShopGoodsOption rule : entity.getGoodsOptionList()) {
                        rule.setGoodsId(entity.getId());
                        rule.setShopId(entity.getShopId());
                        rule.setGoodsName(entity.getTitle());
                        rule.setPrice(rule.getMarketprice());
                        esShopGoodsOptionMapper.insert(rule);
                    }*//*

                }
            }

        }*/
        //4 添加商品详情 推荐商品列表
        if (entity.getRecomGoodsId() != null && !entity.getRecomGoodsId().equals("") && entity.getRecomType().equals("0")) {
            String[] ids = entity.getRecomGoodsId().split(",");
            for (int i = 0; i < ids.length; i++) {
                EsShopGoodsRecom recom = new EsShopGoodsRecom();
                recom.setGoodsid(entity.getId().longValue());
                recom.setRecomGoodsId(Long.parseLong(ids[i]));
                goodsRecomMapper.insert(recom);
            }
        }
        redisRepository.delete(String.format(RedisConstant.GOODS, entity.getId() + ""));
        redisRepository.delete(String.format(RedisConstant.GOODSDETAIL, entity.getId()));
    }

    @ApiOperation("更新商品")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object updateGoods(EsShopGoodsParam entity) {
        try {

            if (entity.getCategoryId() != null && !entity.getCategoryId().equals("")) {

                List<String> lists = new ArrayList<String>();
                String[] id = entity.getCategoryId().split(",");
                for (int i = 0; i < id.length; i++) {
                    String name = "";
                    EsShopGoodsCategory essOne = null;
                    EsShopGoodsCategory essTwo = null;

                    EsShopGoodsCategory ess = esShopGoodsCategoryMapper.selDetail(Long.parseLong(id[i]));
                    if (ess != null) {
                        if (ess.getParentId() != null) {
                            essTwo = esShopGoodsCategoryMapper.selDetail(ess.getParentId());
                            if (essTwo != null) {
                                essOne = esShopGoodsCategoryMapper.selDetail(essTwo.getParentId());
                            }
                        }
                        if (essOne != null) {
                            name += essOne.getName() + "/" + essTwo.getName() + "/" + ess.getName();
                            essOne = null;
                            essTwo = null;
                            ess = null;
                        } else if (essTwo != null) {
                            name += essTwo.getName() + "/" + ess.getName();
                            essTwo = null;
                            ess = null;
                        } else {
                            name += ess.getName();
                            ess = null;
                        }
                    }


                    if (entity.getCategoryName() != null && !entity.getCategoryName().equals("")) {
                        entity.setCategoryName(entity.getCategoryName() + "," + name);
                    } else {
                        entity.setCategoryName(name);
                    }
                }

            } else {
                entity.setCategoryName("");
            }

            if (entity.getIsPutaway() == 0) {
                entity.setStatus(-2);
            }
            Integer virtualStock = 0;
            EsShopGoodsOption options = new EsShopGoodsOption();
            options.setGoodsId(entity.getId());
            List<EsShopGoodsOption> listOption = esShopGoodsOptionMapper.selectList(new QueryWrapper<>(options));
            if (listOption != null) {
                for (EsShopGoodsOption option : listOption) {
                    if (option.getVirtualStock() == null || option.getVirtualStock().equals("")) {
                        option.setVirtualStock(0);
                    }
                    virtualStock += option.getVirtualStock();
                }
                if (virtualStock > 0) {
                    entity.setStatus(1);
                }
                entity.setVituralStock(virtualStock);
            }

            if (this.updateById(entity)) {
                if (ValidatorUtils.notEmpty(entity.getOldCateIds())) {
                    esShopGoodsCateMapMapper.delete(new QueryWrapper<EsShopGoodsCateMap>().eq("goods_id", entity.getId()).in("category_id", entity.getCategoryId().split(",")));
                }

                //esShopGoodsOptionMapper.delete(new QueryWrapper<EsShopGoodsOption>().eq("goods_id", entity.getId()));
                /*esShopGoodsSpecMapper.delete(new QueryWrapper<EsShopGoodsSpec>().eq("goods_id", entity.getId()));
                esShopGoodsSpecItemMapper.delete(new QueryWrapper<EsShopGoodsSpecItem>().eq("goods_id", entity.getId()));*/
                goodsRecomMapper.delete(new QueryWrapper<EsShopGoodsRecom>().eq("goodsid", entity.getId()));
                esShopGoodsDiypageMapMapper.delete(new QueryWrapper<EsShopGoodsDiyPageMap>().eq("goods_id", entity.getId()));

                List<EsShopGoodsOption> list1 = esShopGoodsOptionMapper.selectOption(entity.getId());
                if (entity.getEnableSpec() != null) {
                    if (entity.getEnableSpec() == 1) {
                        //3 添加商品商品规格
                        //esShopGoodsOptionMapper.delete(new QueryWrapper<EsShopGoodsOption>().eq("goods_id",entity.getId()));
                        List<EsShopGoodsOption> list3 = new ArrayList<EsShopGoodsOption>();
                        List<EsShopGoodsOption> list4 = new ArrayList<EsShopGoodsOption>();
                        Integer numCount = 0;
                        if (entity.getGoodsSpecList() != null && entity.getGoodsSpecList().size() > 0) {
                            Map<Integer, Object> map = new HashMap<Integer, Object>();
                            List<List<EsShopGoodsSpecItem>> list = new ArrayList<List<EsShopGoodsSpecItem>>();
                            Integer nums = 1;
                            for (EsShopGoodsSpec rule : entity.getGoodsSpecList()) {
                                rule.setGoodsId(entity.getId());
                                rule.setShopId(entity.getShopId());
                                if (rule.getId() != null && !rule.getId().equals("")) {

                                    esShopGoodsSpecMapper.updateById(rule);
                                    esShopGoodsSpecItemMapper.updates(rule.getId(), rule.getTitle());
                                } else {
                                    numCount = esShopGoodsSpecMapper.insert(rule);
                                }

                                //4 添加商品规格值
                                if (entity.getItemList() != null && entity.getItemList().size() > 0) {
                                    List<EsShopGoodsSpecItem> listSub = new ArrayList<EsShopGoodsSpecItem>();
                                    for (EsShopGoodsSpecItem item : entity.getItemList()) {
                                        if (item.getTitleItem().equals(rule.getTitle())) {
                                            item.setGoodsId(entity.getId());
                                            item.setSpecId(rule.getId());
                                            item.setShopId(entity.getShopId());
                                            if (item.getId() != null) {
                                                esShopGoodsSpecItemMapper.updateById(item);
                                            } else {
                                                esShopGoodsSpecItemMapper.insert(item);
                                            }
                                            listSub.add(item);
                                        }
                                    }
                                    map.put(nums, listSub);
                                }
                                nums += 1;
                            }
                            for (Object values : map.values()) {
                                list.add((List<EsShopGoodsSpecItem>) values);
                            }


                            List<List<EsShopGoodsSpecItem>> result = new ArrayList<List<EsShopGoodsSpecItem>>();
                            descartes(list, result, 0, new ArrayList<EsShopGoodsSpecItem>());
                            for (List<EsShopGoodsSpecItem> lis : result) {
                                String specs = "";
                                String title = "";
                                String specItemIds = "";
                                Integer count = 1;
                                EsShopGoodsOption option = new EsShopGoodsOption();
                                option.setMarketprice(new BigDecimal("0"));
                                for (int i = 0; i < lis.size(); i++) {
                                    EsShopGoodsSpecItem it = (EsShopGoodsSpecItem) lis.get(i);
                                    if (count == lis.size()) {
                                        count += 1;
                                        specs += it.getTitleItem() + ":" + it.getTitle();
                                        title += it.getTitle();
                                        specItemIds += it.getId();
                                        option.setGoodsId(entity.getId());

                                    } else {
                                        count += 1;
                                        specs += it.getTitleItem() + ":" + it.getTitle() + ",";
                                        title += it.getTitle() + ",";
                                        specItemIds += it.getId() + ",";
                                    }
                                    if (it.getMoney() != null) {
                                        option.setMarketprice(option.getMarketprice().add(it.getMoney()));
                                        option.setPrice(option.getMarketprice().add(it.getMoney()));
                                    }
                                }
                                option.setGoodsId(entity.getId());
                                option.setSpecs(specs);
                                option.setTitle(title);
                                option.setSpecIds(specItemIds);
                                option.setGoodsName(entity.getTitle());
                                list3.add(option);
                            }
                        }
                        if (numCount > 0) {
                            esShopGoodsOptionMapper.deletes(entity.getId());
                            for (EsShopGoodsOption option : list3) {
                                esShopGoodsOptionMapper.insert(option);
                            }
                        } else {
                            for (EsShopGoodsOption option : list3) {
                                for (EsShopGoodsOption opt : list1) {
                                    if (opt.getSpecIds().equals(option.getSpecIds())) {
                                        opt.setTitle(option.getTitle());
                                        opt.setSpecs(option.getSpecs());
                                        opt.setMarketprice(option.getMarketprice());
                                        opt.setPrice(option.getMarketprice());
                                        esShopGoodsOptionMapper.updateById(opt);
                                    } else {
                                        int count = 0;
                                        List<EsShopGoodsOption> lis1 = esShopGoodsOptionMapper.selectOption(entity.getId());
                                        for (EsShopGoodsOption opts : lis1) {
                                            if (opts.getSpecIds().equals(option.getSpecIds())) {
                                                count = 1;
                                            }
                                        }
                                        if (count == 0) {
                                            esShopGoodsOptionMapper.insert(option);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //判断规格值是否变更
                    EsShopGoodsSpecItem item = new EsShopGoodsSpecItem();
                    item.setGoodsId(entity.getId());
                    List<EsShopGoodsSpecItem> list = esShopGoodsSpecItemMapper.selectList(new QueryWrapper<>(item));
                    for (EsShopGoodsSpecItem specItem : list) {
                        //获取option张规格值id的位置，根据规格值id的位置找到规格值的位置并修改
                        if (specItem.getId() != null) {
                            List<EsShopGoodsOption> optis = esShopGoodsOptionMapper.selectLikeSpecIds(specItem.getId());
                            if (optis != null) {
                                for (EsShopGoodsOption opt : optis) {
                                    Integer tagg = 0;
                                    String attTitle = "";//title
                                    String names = "";//specs
                                    //ids
                                    String[] attrSpecIds = opt.getSpecIds().split(",");
                                    for (int i = 0; i < attrSpecIds.length; i++) {
                                        if (Long.parseLong(attrSpecIds[i]) == specItem.getId()) {
                                            tagg = i;
                                        }
                                    }
                                    //title
                                    String[] attrTitle = opt.getTitle().split(",");
                                    for (int i = 0; i < attrTitle.length; i++) {
                                        if (i == tagg) {
                                            attrTitle[i] = specItem.getTitle();
                                        }
                                    }
                                    int pTitle = 1;
                                    for (int i = 0; i < attrTitle.length; i++) {
                                        if (pTitle == attrTitle.length) {
                                            attTitle += attrTitle[i];
                                        } else {
                                            attTitle += attrTitle[i] + ",";
                                        }
                                        pTitle += 1;
                                    }
                                    //specs
                                    //替换数组
                                    String name = "";
                                    String[] attrspecs = opt.getSpecs().split(",");
                                    for (int i = 0; i < attrspecs.length; i++) {
                                        if (i == tagg) {
                                            String[] attrattr = attrspecs[i].toString().split(":");
                                            attrattr[1] = specItem.getTitle();
                                            name = attrattr[0] + ":" + attrattr[1];
                                            List<String> lists = Arrays.asList(attrspecs);
                                            Collections.replaceAll(lists, attrspecs[i], name);
                                        }
                                    }
                                    //去除，
                                    int k = 1;
                                    for (int i = 0; i < attrspecs.length; i++) {
                                        if (k == attrspecs.length) {
                                            names += attrspecs[i];
                                        } else {
                                            names += attrspecs[i] + ",";
                                        }
                                        k += 1;
                                    }
                                    opt.setTitle(attTitle);
                                    opt.setSpecs(names);
                                    esShopGoodsOptionMapper.updateById(opt);
                                }
                            }
                        }
                    }


                }
                addGoodsOtherInfo(entity);

            }

            return new CommonResult().success("success");
        } catch (Exception e) {
            log.error("更新商品：%s", e.getMessage(), e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }


    @ApiOperation("删除商品")
    @Override
    public Object deleteGoods(Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品id");
            }
            if (this.removeById(id)) {
                redisRepository.delete(String.format(RedisConstant.GOODSDETAIL, id + ""));
                redisRepository.delete(String.format(RedisConstant.GOODS, id + ""));
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除商品：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @Override
    public List<EsShopGoods> selShopGoodsList(TradeAnalyzeParam param) {
        //status商品状态（1为出售中，3为已售罄，-2为仓库中，-1为回收站）
        //条件
        QueryWrapper<EsShopGoods> condition = new QueryWrapper();
        param.setEndTime(param.getEndTime() + " 23:59:59.999");
        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        if (ValidatorUtils.notEmpty(param.getShopId())) {
            condition.eq("shop_id", param.getShopId());
        }
        System.out.println(esShopGoodsMapper.selectList(condition).toString());
        return null;
    }

    @ApiOperation("查询商品明细")
    @Override
    public Object getGoodsById(Long id) {
        List<EsShopGoodsRecom> list = new ArrayList<EsShopGoodsRecom>();
        List<EsShopGoodsCategory> lists = new ArrayList<EsShopGoodsCategory>();
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品id");
            }
            EsShopGoods coupon = this.getById(id);
            if (coupon != null) {
                if (coupon.getRecomGoodsId() != null && !coupon.getRecomGoodsId().equals("")) {
                    String[] ids = coupon.getRecomGoodsId().split(",");
                    EsShopGoods esShopGoods = new EsShopGoods();
                    if (ids.length > 0) {
                        for (int i = 0; i < ids.length; i++) {
                            if (ids[i] != null) {
                                esShopGoods = esShopGoodsMapper.selEsShopGoods(Long.parseLong(ids[i]));
                                if (esShopGoods != null) {
                                    EsShopGoodsRecom recom = new EsShopGoodsRecom();
                                    recom.setTitle(esShopGoods.getTitle());
                                    recom.setPrice(esShopGoods.getPrice());
                                    //recom.setGoodsid();
                                    recom.setRecomGoodsId(Long.parseLong("123"));
                                    recom.setVituralStock(esShopGoods.getVituralStock());
                                    recom.setThumb(esShopGoods.getThumb());
                                    recom.setId(esShopGoods.getId());
                                    if (recom != null) {
                                        list.add(recom);
                                    }

                                }
                            }
                        }
                    }
                }
                //查询商品分类
                EsShopGoodsCateMap cateMap = new EsShopGoodsCateMap();
                cateMap.setGoodsId(coupon.getId());
                List<EsShopGoodsCateMap> listCateMap = esShopGoodsCateMapMapper.selectList(new QueryWrapper<>(cateMap));
                if (listCateMap != null) {
                    for (EsShopGoodsCateMap map : listCateMap) {
                        EsShopGoodsCategory category = new EsShopGoodsCategory();
                        category.setId(map.getCategoryId());
                        EsShopGoodsCategory category1 = esShopGoodsCategoryMapper.selectById(category.getId());
                        if (category1 != null) {
                            lists.add(category1);
                        }
                    }
                }
                //查询规格
                EsShopGoodsSpec spec = new EsShopGoodsSpec();
                spec.setGoodsId(id);
                List<EsShopGoodsSpec> listSpec = esShopGoodsSpecMapper.selectList(new QueryWrapper<>(spec));
                coupon.setSpecs(listSpec);
                for (EsShopGoodsSpec lis : listSpec) {
                    //查询规格属性
                    lis.setItemList(esShopGoodsSpecItemMapper.selectSpecItems(id, lis.getId()));
                }
                //查询规格sku
                List<EsShopGoodsOption> lis = esShopGoodsOptionMapper.selectOption(id);
                coupon.setOptions(lis);
                coupon.setGoodsCategoryList(lists);
                coupon.setRecomList(list);

                //查询模板
                coupon.setDiypageList(esShopDiypageMapper.selectAll());
                EsShopGoodsDiyPageMap pageMap = new EsShopGoodsDiyPageMap();
                pageMap.setGoodsId(id);
                coupon.setDiypageALl(esShopGoodsDiypageMapMapper.selectOne(new QueryWrapper<>(pageMap)));
            }


            return coupon;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询商品明细：%s", e.getMessage(), e);
            return null;
        }

    }

    @ApiOperation("商品批量上下架")
    @Override
    public Object updEsShopGoodsStatus(String ids, Integer status) {
        try {
            Long var = Long.parseLong(ids);
            Integer obj = null;
            if (status == 1) {//出售中
                //判断商品库存是否为0
                EsShopGoods goo = esShopGoodsMapper.selShopGoodsDetail(var);
                if (goo != null) {
                    if (goo.getType() == 4 || goo.getType() == 5) {
                        obj = esShopGoodsMapper.updPutawayTime(var, System.currentTimeMillis());
                    } else {
                        if (goo.getVituralStock() == 0 || goo.getVituralStock() == null) {
                            obj = esShopGoodsMapper.updEsShopGoodsStatus(var, 3, null);
                        } else {
                            obj = esShopGoodsMapper.updPutawayTime(var, System.currentTimeMillis());
                        }
                    }
                }


            } else if (status == -1) {//回收站
                Date da = new Date();
                String str = sdf.format(da);
                Date date = sdf.parse(str);//删除时间
                obj = esShopGoodsMapper.updEsShopGoodsStatusTime(var, status, date);
            } else {
                obj = esShopGoodsMapper.updEsShopGoodsStatus(var, status, null);
            }
            redisRepository.delete(String.format(RedisConstant.GOODSDETAIL, ids + ""));
            redisRepository.delete(String.format(RedisConstant.GOODS, ids + ""));
            return new CommonResult().success("success", new Date());

        } catch (Exception e) {
            log.error("修改商品上下架状态：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }


    @ApiOperation("商品批量逻辑刪除（放到回收站）")
    @Override
    public Object UpdShopDelID(String ids) {
        try {
            Long var = Long.parseLong(ids);
            Date da = new Date();
            String str = sdf.format(da);
            Date date = sdf.parse(str);
            Integer count = esShopGoodsMapper.updEsShopGoodsIsDel(var, date);
            redisRepository.delete(String.format(RedisConstant.GOODSDETAIL, ids + ""));
            redisRepository.delete(String.format(RedisConstant.GOODS, ids + ""));
            return new CommonResult().success("success", count);
        } catch (Exception e) {
            log.error("删除商品（逻辑删除）异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量刪除回收站商品")
    @Override
    public Object delRecycleShop(String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("商品Id");
            } else {
                String[] idAttr = ids.split(",");
                for (int i = 0; i < idAttr.length; i++) {
                    Long id = Long.parseLong(idAttr[i]);
                    Integer stu = esShopGoodsMapper.delRecycleShop(id);
                    if (stu > 0) {
                        // 1 删除分类下的商品
                        Integer num = esShopGoodsMapper.delGoodsCateMap(id);
                        // 2 删除商品的推荐商品
                        EsShopGoodsRecom recom = new EsShopGoodsRecom();
                        recom.setGoodsid(id);
                        esShopGoodsRecomMapper.delete(new QueryWrapper<>(recom));
                        //删除商品模板
                        EsShopGoodsDiyPageMap pageMap = new EsShopGoodsDiyPageMap();
                        pageMap.setGoodsId(id);
                        esShopGoodsDiypageMapMapper.delete(new QueryWrapper<>(pageMap));
                        //删除商品规格
                        EsShopGoodsSpec spec = new EsShopGoodsSpec();
                        spec.setGoodsId(id);
                        esShopGoodsSpecMapper.delete(new QueryWrapper<>(spec));
                        //删除商品规格值
                        EsShopGoodsSpecItem specItem = new EsShopGoodsSpecItem();
                        specItem.setGoodsId(id);
                        esShopGoodsSpecItemMapper.delete(new QueryWrapper<>(specItem));
                        //删除商品SKU
                        EsShopGoodsOption option = new EsShopGoodsOption();
                        option.setGoodsId(id);
                        esShopGoodsOptionMapper.delete(new QueryWrapper<>(option));
                        //删除商品分组
                        EsShopGoodsGroup group = new EsShopGoodsGroup();
                        group.setGoodsId(id);
                        esShopGoodsGroupMapper.delete(new QueryWrapper<>(group));
                        redisRepository.delete(String.format(RedisConstant.GOODS, id + ""));
                        redisRepository.delete(String.format(RedisConstant.GOODSDETAIL, id + ""));
                    }

                }
                return new CommonResult().success("success", new Date());
            }
        } catch (Exception e) {
            log.error("批量刪除回收站商品異常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量移除回收站商品到仓库")
    @Override
    public Object updRecycleShopStatus(String ids, Integer status) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("商品Id");
            } else {
                String[] id = ids.split(",");
                for (int i = 0; i < id.length; i++) {
                    Long var = Long.parseLong(id[i]);
                    esShopGoodsMapper.updEsShopGoodsStatus(var, status, null);
                    redisRepository.delete(String.format(RedisConstant.GOODSDETAIL, var + ""));
                    redisRepository.delete(String.format(RedisConstant.GOODS, id + ""));
                }
                return new CommonResult().success("success", new Date());
            }
        } catch (Exception e) {
            log.error("批量移除回收站商品到倉庫異常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询商品一级分类")
    @Override
    public Object selDisplayShopGoodsCategoryOne() {
        try {
            return esShopGoodsMapper.selDisplayShopGoodsCategoryOne();
        } catch (Exception e) {
            log.error("查询一级分类异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation("查询商品一级分类")
    @Override
    public Object selShopGoodsCategoryOne() {
        try {
            return esShopGoodsMapper.selShopGoodsCategoryOne();
        } catch (Exception e) {
            log.error("查询一级分类异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation("查询商品二级分类")
    @Override
    public Object selShopGoodsCategoryTwo(Long parentId, Integer level) {
        try {
            if (ValidatorUtils.empty(parentId)) {
                return new CommonResult().failed("商品Id");
            } else {
                return esShopGoodsMapper.selShopGoodsCategoryTwo(parentId, level);
            }
        } catch (Exception e) {
            log.error("查询二级分类异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查詢商品分組")
    @Override
    public Object selShopGoodsGroup() {
        try {
            return esShopGoodsMapper.selShopGoodsGroup();
        } catch (Exception e) {
            log.error("查詢商品分組異常：", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    //赠品商品查询
    @ApiOperation("查詢赠品商品")
    @Override
    public List<EsShopGoods> selectgiftsgoods(String title) {

        return esShopGoodsMapper.selectgiftsgoods(title);

    }

    @Override
    public Map<String, Object> searchGoods(String keywords) {
        Map<String, Object> map = new HashedMap();
        List<Map<String, Object>> selectmapcoupon = esShopGoodsMapper.searchGoods(keywords);
        map.put("rows", selectmapcoupon);
        return map;
    }


    @Override
    public Map<String, Object> goodsListByCatePageList(GoodsQuery esShopGoods) {

        Map<String, Object> result = new HashMap<>();
        try {
            PageHelper.startPage(esShopGoods.getCurrent(), esShopGoods.getSize());
            List<EsShopGoods> list = esShopGoodsMapper.selGoodsPageList(esShopGoods);
            for (EsShopGoods goods : list) {
                if (ValidatorUtils.notEmpty(goods.getDefaultSpec())) {
                    EsShopGoodsOption query = new EsShopGoodsOption();
                    query.setGoodsId(goods.getId());
                    query.setSpecs(goods.getDefaultSpec());
                    EsShopGoodsOption option = esShopGoodsOptionMapper.selectOne(new QueryWrapper<>(query));
                    goods.setOption(option);
                }
            }
            int count = esShopGoodsMapper.count(esShopGoods);
            result.put("rows", sampleGoodsList(list));
            result.put("total", count);
            result.put("current", esShopGoods.getCurrent());
            result.put("size", esShopGoods.getSize());
        } catch (Exception e) {
            log.error("查詢商品分組異常：", e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public List<EsShopGoods> selGoodsPutAwayTime() {
        return esShopGoodsMapper.selGoodsPutAwayTime();
    }

    @Override
    public List<EsShopDiypage> selectAll() {
        return esShopDiypageMapper.selectAll();
    }

    @Override
    public Map<String, Object> selGoodsPutaway(GoodsQuery goodsQuery) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageHelper.startPage(goodsQuery.getCurrent(), goodsQuery.getSize());
            List<EsShopGoods> list = esShopGoodsMapper.selGoodsPutaway(goodsQuery);
            Integer count = esShopGoodsMapper.selGoodsPutawayCount(goodsQuery);
            map.put("rows", list);
            map.put("total", count);
            map.put("current", goodsQuery.getCurrent());
            map.put("size", goodsQuery.getSize());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public Map<String, Object> selCustService() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("card", esShopCustomizedCardMapper.list());
        EsShopCustomizedPacket packet = new EsShopCustomizedPacket();
        List<EsShopCustomizedPacket> listPackage = esShopCustomizedPacketMapper.list(1);
        map.put("package", listPackage);
        List<EsShopCustomizedPacket> listPackBox = esShopCustomizedPacketMapper.list(2);
        map.put("packBox", listPackBox);
        map.put("legend", esShopCustomizedLegendMapper.selectList(new QueryWrapper()));
        return map;
    }

    @Override
    public EsShopGoods detail(Long id) {
        EsShopGoods es = esShopGoodsMapper.selectById(id);
        List<EsShopGoods> liGoods = new ArrayList<EsShopGoods>();
        if (es != null) {
            EsShopGoodsRecom recom = new EsShopGoodsRecom();
            recom.setGoodsid(id);
            List<EsShopGoodsRecom> lis = esShopGoodsRecomMapper.selectList(new QueryWrapper<>(recom));
            if (lis != null) {
                for (EsShopGoodsRecom re : lis) {
                    EsShopGoods goods = esShopGoodsMapper.selectById(re.getGoodsid());
                    liGoods.add(goods);
                }
            }
            es.setRecomGoodsList(liGoods);
        }
        return es;
    }

    @Override
    public Map<String, Object> lists(GoodsQuery esShopGoods) {
        Page<EsShopGoods> page = new Page<EsShopGoods>(esShopGoods.getCurrent(), esShopGoods.getSize());
        Map<String, Object> result = new HashMap<>();
        try {
            if (esShopGoods.getCategoryId() != null && !esShopGoods.getCategoryId().equals("")) {
                if (esShopGoods.getCategoryId().equals("1")) {
                    esShopGoods.setCategoryId(null);
                }
            }
            PageHelper.startPage(esShopGoods.getCurrent(), esShopGoods.getSize());
            List<EsShopGoods> list = esShopGoodsMapper.lists(esShopGoods);
            int count = esShopGoodsMapper.counts(esShopGoods);
            result.put("rows", list);
            result.put("total", count);
            result.put("current", esShopGoods.getCurrent());
            result.put("size", esShopGoods.getSize());
        } catch (Exception e) {
            log.error("查詢商品异常：", e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
        return result;
    }


    @Override
    public Map<String, Object> selGoodsPageList(GoodsQuery esShopGoods) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (esShopGoods.getCategoryId() != null && !esShopGoods.getCategoryId().equals("")) {
                if (esShopGoods.getCategoryId().equals("1")) {
                    esShopGoods.setCategoryId(null);
                }
            }
            PageHelper.startPage(esShopGoods.getCurrent(), esShopGoods.getSize());
            List<EsShopGoods> list = esShopGoodsMapper.selGoodsPageList(esShopGoods);
            for (EsShopGoods es : list) {
                if (es.getStatus() == null) {
                } else {
                    if (es.getStatus() == 1) {
                        if (es.getVituralStock() != null) {
                            if (es.getVituralStock() == 0) {
                                //如果库存为0将商品添加到售罄列表
                                String date = sdf.format(new Date());
                                esShopGoodsMapper.updEsShopGoodsStatus(es.getId(), 3, sdf.parse(date));
                            }

                        }
                    }
                    if (es.getStatus() == 3) {
                        if (es.getPutawayTime() == null) {

                        } else {
                            if (es.getSelloutTime() == null || es.getSelloutTime().equals("")) {
                                String date = sdf.format(new Date());
                                esShopGoodsMapper.updEsShopGoodsStatus(es.getId(), 3, sdf.parse(date));
                            }
                        }

                    }
                }


            }
            int count = esShopGoodsMapper.count(esShopGoods);
            result.put("rows", list);
            result.put("total", count);
            result.put("current", esShopGoods.getCurrent());
            result.put("size", esShopGoods.getSize());
        } catch (Exception e) {
            log.error("查詢商品分組異常：", e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
        return result;


    }

    @Override
    public GoodsDetail goodsDetail(Long id) {
        GoodsDetail vo = null;
        try {
            vo = getGoodsDetail(id);
            //(GoodsDetail) redisRepository.get(String.format(RedisConstant.GOODSDETAIL, id));
            if (vo == null) {
                log.info("goodsDetail redsi11" + id);
                vo = getGoodsDetail(id);
            }
        } catch (Exception e) {
            log.info("goodsDetail redsi" + id);
            log.info("goodsDetail " + e.getMessage());
            vo = getGoodsDetail(id);
        }
        return vo;
    }

    private GoodsDetail getGoodsDetail(Long id) {
        GoodsDetail vo;
        vo = new GoodsDetail();
        EsShopGoods goods = (EsShopGoods) JsonUtils.fromJson(redisRepository.get(String.format(RedisConstant.GOODS, id + "")),EsShopGoods.class);
        if (ValidatorUtils.empty(goods) || ValidatorUtils.empty(goods.getId())) {
            goods = esShopGoodsMapper.selectById(id);
            if (goods != null) {
                List<EsShopGoodsOption> optionList = esShopGoodsOptionMapper.selectList(new QueryWrapper<EsShopGoodsOption>().eq("goods_id", id));
                goods.setOptions(optionList);
                redisRepository.set(String.format(RedisConstant.GOODS, id + ""), goods);
            }
        }
        if (goods == null) {
            return vo;
        }
        vo.setGoods(sampleGoods(goods));
        vo.setGoodsOptionList(goods.getOptions());
        List<EsShopGoods> recomGoodsList = new ArrayList<>();
        List<EsShopGoods> recomGoodsList1 = new ArrayList<>();
        List<Long> statusList = Arrays.asList(1L, 3L);
        // 单品
        if ("0".equals(goods.getRecomType())) {
            if (ValidatorUtils.notEmpty(goods.getRecomGoodsId())) {
                List<Long> ids = Arrays.asList(goods.getRecomGoodsId().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                if (ids != null && ids.size() > 0) {
                    recomGoodsList1 = esShopGoodsMapper.selectList(new QueryWrapper<EsShopGoods>().in("id", ids).in("status", statusList).notIn("type", 3).notIn("id", id));
                    for (Long idLk : ids) {
                        for (EsShopGoods esShopGoods : recomGoodsList1) {
                            if (esShopGoods.getId().equals(idLk)) {
                                recomGoodsList.add(esShopGoods);
                            }
                        }
                    }
                }
            }
        } else if ("1".equals(goods.getRecomType())) {

            Integer recomCategoryNum = 0;
            if (ValidatorUtils.notEmpty(goods.getRecomCategoryNum())) {
                recomCategoryNum = goods.getRecomCategoryNum();
                EsShopGoodsCategory cate = esShopGoodsCategoryMapper.selectById(Long.parseLong(goods.getRecomCategoryName()));
                if (cate != null) {
                    List<EsShopGoodsCategoryRecom> goodsRecomList = esShopGoodsCategoryRecomMapper.selectList(new QueryWrapper<EsShopGoodsCategoryRecom>().eq("category_id", cate.getId()));
                    if (goodsRecomList != null && goodsRecomList.size() > 0) {
                        List<Long> stIdList2 = goodsRecomList.stream()
                                .map(EsShopGoodsCategoryRecom::getGoodsId).distinct()
                                .collect(Collectors.toList());
                        Page<EsShopGoods> page = new Page<EsShopGoods>(0, recomCategoryNum);
                        recomGoodsList = esShopGoodsMapper.selectPage(page, new QueryWrapper<EsShopGoods>().notIn("type", 3).in("id", stIdList2).in("status", statusList).notIn("id", id).orderByDesc("display_order")).getRecords();

                    }
                }

            }

        }


        List<EsShopGoods> goodsRecomsList = new ArrayList<>();
        if (recomGoodsList != null) {
            if (recomGoodsList.size() >= 10) {
                goodsRecomsList = recomGoodsList.subList(0, 10);
            } else {
                goodsRecomsList = recomGoodsList.subList(0, recomGoodsList.size());
            }
        }
        List<EsShopGoods> newList = new ArrayList<EsShopGoods>();
        newList.addAll(goodsRecomsList);
        vo.setGoodsRecomsList(sampleGoodsList(newList));

        List<EsShopGoodsSpec> goodsSpecList = esShopGoodsSpecMapper.selectList(new QueryWrapper<EsShopGoodsSpec>().eq("goods_id", id));
        for (EsShopGoodsSpec spec : goodsSpecList) {
            List<EsShopGoodsSpecItem> goodsSpecItemList = esShopGoodsSpecItemMapper.selectList(new QueryWrapper<EsShopGoodsSpecItem>().eq("spec_id", spec.getId()));
            spec.setItemList(goodsSpecItemList);
        }
        vo.setGoodsSpecList(goodsSpecList);
        //       redisRepository.set(String.format(RedisConstant.GOODSDETAIL, id + ""), vo);
        //    redisRepository.willExpire(String.format(RedisConstant.GOODSDETAIL, id), 5 * 60);
        return vo;
    }

    private List<EsShopGoods> sampleGoodsList(List<EsShopGoods> list) {
        return list;
    }

    private EsShopGoods sampleGoods(EsShopGoods goods) {

        return goods;
    }

    @Override
    public EsShopGoods selShopGoodsDetail(Long id) {
        return esShopGoodsMapper.selShopGoodsDetail(id);
    }

    @Override
    public Integer selCountShopTitle(String title) {
        return esShopGoodsMapper.selCountShopTitle(title);
    }

    @Override
    public Integer updGoodsDisplayOrder(Long id, Integer num) {
        return esShopGoodsMapper.updGoodsDisplayOrder(id, num);
    }

    @Override
    public Object getGoodsSku(Long id, String specsParam) {
        String[] split = specsParam.split(",");
        Map<String, String> mapParam = listToMap(split);
        List<EsShopGoodsOption> skuList = esShopGoodsOptionMapper.selectList(new QueryWrapper<EsShopGoodsOption>().eq("goods_id", id));
        for (EsShopGoodsOption sku : skuList) {
            if (sku.getSpecs() != null) {
                Map<String, String> map = listToMap(sku.getSpecs().split(","));
                if (map.equals(mapParam)) {
                    return new CommonResult().success(sku);
                }
            }
        }
        return new CommonResult().success();
    }

    private Map<String, String> listToMap(String[] split) {
        Map<String, String> map = new HashMap<>();
        for (String s : split) {
            String[] spec = s.split(":");
            map.put(spec[0], spec[1]);
        }
        return map;
    }

    @Override
    public List<EsShopGoods> selectgoodsListByCateIds(List<String> ids) {
        return esShopGoodsMapper.selectgoodsListByCateIds(ids);
    }

    @Override
    public boolean delSpec(EsShopGoodsSpec spec) {
        //删除商品下的规格
        spec = esShopGoodsSpecMapper.selectById(spec.getId());

        //1、删除spec
        Integer num = esShopGoodsSpecMapper.deleteById(spec.getId());
        if (num == 1) {
            //2、删除specitem
            List<EsShopGoodsSpecItem> listSpecItem = esShopGoodsSpecItemMapper.selectSpecItems(spec.getGoodsId(), spec.getId());
            for (EsShopGoodsSpecItem specItem : listSpecItem) {
                esShopGoodsSpecItemMapper.deleteById(specItem.getId());
            }
            //3、重新生成列表
            EsShopGoodsSpec es = new EsShopGoodsSpec();
            es.setGoodsId(spec.getGoodsId());
            List<EsShopGoodsSpec> listSpec = esShopGoodsSpecMapper.selectList(new QueryWrapper<>(es));
            if (listSpec != null && listSpec.size() > 0) {
                Map<Integer, Object> map = new HashMap<Integer, Object>();
                List<List<EsShopGoodsSpecItem>> list = new ArrayList<List<EsShopGoodsSpecItem>>();
                Integer nums = 1;
                for (EsShopGoodsSpec rule : listSpec) {
                    EsShopGoodsSpecItem esShopGoodsSpecItem = new EsShopGoodsSpecItem();
                    esShopGoodsSpecItem.setGoodsId(rule.getGoodsId());
                    esShopGoodsSpecItem.setSpecId(rule.getId());
                    List<EsShopGoodsSpecItem> listSub = esShopGoodsSpecItemMapper.selectList(new QueryWrapper<>(esShopGoodsSpecItem));
                    map.put(nums, listSub);
                    nums += 1;
                }
                for (Object values : map.values()) {
                    list.add((List<EsShopGoodsSpecItem>) values);
                }
                List<List<EsShopGoodsSpecItem>> result = new ArrayList<List<EsShopGoodsSpecItem>>();
                descartes(list, result, 0, new ArrayList<EsShopGoodsSpecItem>());
                esShopGoodsOptionMapper.delete(new QueryWrapper<EsShopGoodsOption>().eq("goods_id", spec.getGoodsId()));
                for (List<EsShopGoodsSpecItem> lis : result) {
                    String specs = "";
                    String title = "";
                    Integer count = 1;
                    String specItemIds = "";
                    EsShopGoodsOption option = new EsShopGoodsOption();
                    option.setMarketprice(new BigDecimal("0"));
                    for (int i = 0; i < lis.size(); i++) {
                        EsShopGoodsSpecItem it = (EsShopGoodsSpecItem) lis.get(i);
                        if (count == lis.size()) {
                            count += 1;
                            specs += it.getTitleItem() + ":" + it.getTitle();
                            title += it.getTitle();
                            specItemIds += it.getId();
                        } else {
                            count += 1;
                            specs += it.getTitleItem() + ":" + it.getTitle() + ",";
                            title += it.getTitle() + ",";
                            specItemIds += it.getId() + ",";
                        }
                        option.setMarketprice(option.getMarketprice().add(it.getMoney()));
                        option.setPrice(option.getMarketprice().add(it.getMoney()));
                    }
                    option.setTitle(title);
                    option.setSpecs(specs);
                    option.setGoodsId(spec.getGoodsId());
                    option.setSpecIds(specItemIds);
                    Integer sun = esShopGoodsOptionMapper.insert(option);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delSpecItem(EsShopGoodsSpecItem item) {
        //删除商品下的规格
        //1、删除option
        List<EsShopGoodsOption> listOPtion = esShopGoodsOptionMapper.selectOption(item.getGoodsId());
        for (EsShopGoodsOption option : listOPtion) {
            String[] attr = option.getSpecIds().split(",");
            for (int i = 0; i < attr.length; i++) {
                if (Long.parseLong(attr[i]) == item.getId()) {
                    esShopGoodsOptionMapper.deleteById(option.getId());
                }
            }
        }
        //2、删除specitem
        EsShopGoodsSpecItem listSpecItem = esShopGoodsSpecItemMapper.selectSpecItem(item.getGoodsId(), item.getId());
        if (listSpecItem.getId() != null) {
            Integer num = esShopGoodsSpecItemMapper.deleteById(listSpecItem.getId());
            if (num > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}

