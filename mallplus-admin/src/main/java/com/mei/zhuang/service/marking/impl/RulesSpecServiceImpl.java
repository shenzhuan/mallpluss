package com.mei.zhuang.service.marking.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.marking.EsShopGoodsRulesMapper;
import com.mei.zhuang.dao.marking.EsShopGoodsRulesSpecMapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.marking.EsShopGoodsRules;
import com.mei.zhuang.entity.marking.EsShopGoodsRulesSpec;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.service.goods.EsShopGoodsService;
import com.mei.zhuang.service.marking.RulesSpecService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
@Slf4j
@Service
public class RulesSpecServiceImpl extends ServiceImpl<EsShopGoodsRulesMapper, EsShopGoodsRules> implements RulesSpecService {

    @Resource
    private EsShopGoodsRulesMapper rulesMapper;
    @Resource
    private EsShopGoodsRulesSpecMapper rulesSpecMapper;

    @Resource
    private EsShopGoodsService goodsService;

    @Transactional
    @Override
    public boolean update(EsShopGoodsRules entity) throws Exception {
        EsShopGoodsRules GoodsRules = rulesMapper.selectById(entity.getId());
        entity.setGoodsId(GoodsRules.getGoodsId());
        rulesMapper.updateById(entity);
        EsShopGoods goods = new EsShopGoods();
        goods.setTitle(entity.getGoodsname());
        goods.setId(entity.getGoodsId());
        goodsService.updateById(goods);
        rulesSpecMapper.delete(new QueryWrapper<EsShopGoodsRulesSpec>().eq("rules_id", entity.getId()));
        rules(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopGoodsRules entitys)  {
        EsShopGoods entity = new EsShopGoods();
        entity.setTitle(entitys.getGoodsname());
        entity.setType(4);
        entity.setPrice(entitys.getPrice());
        //goods.setCategoryId(coreConfig.getValue());
        entity.setCreateTime(new Date());
        entity.setPutawayTime(System.currentTimeMillis()+"");
        entity.setStatus(-2);
        entity.setIsPutaway(1);
         goodsService.save(entity);
        entitys.setGoodsId(entity.getId());
        entitys.setCreateTime(new Date());
        rulesMapper.insert(entitys);
        rules(entitys);
        return true;
    }

    public void rules(EsShopGoodsRules entity) {
        if (entity.getListrulesgoods() != null && entity.getListrulesgoods().size() > 0) {
            for (GoodsSepcVo vo : entity.getListrulesgoods()) {
                EsShopGoodsRulesSpec rulesSpec = new EsShopGoodsRulesSpec();
                rulesSpec.setGoodsId(vo.getGoodsId());
                rulesSpec.setRulesId(entity.getId());
                rulesSpec.setSpecIds(vo.getSpecIds());
                rulesSpecMapper.insert(rulesSpec);
            }
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleterule(String id) {
        String str[] = id.split(",");
        for (String st : str) {
            EsShopGoodsRules esShopGoodsRules = rulesMapper.selectById(Long.parseLong(st));
            goodsService.removeById(esShopGoodsRules.getGoodsId());
            esShopGoodsRules.setAccording(2);
            rulesMapper.updateById(esShopGoodsRules);
            rulesSpecMapper.delete(new QueryWrapper<EsShopGoodsRulesSpec>().eq("rules_id", Long.parseLong(st)));
        }
        return 1;
    }

    @Override
    public EsShopGoodsRules detailrule(long id) {

        return rulesMapper.selectById(id);
    }

    @Override
    public List<EsShopGoodsRulesSpec> listrulesspec(long rulesId) {
        return rulesSpecMapper.selectList(new QueryWrapper<EsShopGoodsRulesSpec>().eq("rules_id", rulesId));
    }


    @Override
    public List<EsShopGoodsRules> lsitrules(String goodsname) {
        return rulesMapper.lsitrules(goodsname);
    }

    @Override
    public EsShopGoodsRules matchGoodsRules(List<EsShopCart> cartList) {
        EsShopGoodsRules newRule = new EsShopGoodsRules();
        int count = 0;

        boolean falg1 = false;
        boolean falg2 = false;
        boolean falg3 = false;
        for (EsShopCart cart1 : cartList) {
            EsShopGoodsRules query = new EsShopGoodsRules();
            query.setGoodsId(cart1.getGoodsId());
            EsShopGoodsRules rules = rulesMapper.selectOne(new QueryWrapper<>(query));
            if (rules != null) {
                count++;
                List<EsShopGoodsRulesSpec> sepcs = rulesSpecMapper.selectList(new QueryWrapper<EsShopGoodsRulesSpec>().eq("rules_id", rules.getId()));
                if (sepcs != null && sepcs.size() > 0) {
                    if ("1".equals(rules.getGoodsScope())) { //  1.参与换购商品，2.不参与
                        falg1 = false;
                        for (EsShopGoodsRulesSpec manjianGoodsMap : sepcs) {
                            for (EsShopCart cart : cartList) {
                                if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                        || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                    falg1 = true;
                                    break;
                                }
                            }
                        }
                        if (!falg1) {
                            newRule = rules;
                            break;
                        }

                    } else {
                        List<Long> ids = new ArrayList<>();
                        for (EsShopGoodsRulesSpec manjianGoodsMap : sepcs) {
                            for (EsShopCart cart : cartList) {
                                if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                        || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                    falg2 = true;
                                    ids.add(cart.getId());
                                }
                            }
                        }
                        int sanmeCount = 0;
                        for (EsShopCart cart : cartList) {
                            if (!ids.contains(cart.getId())) {
                                sanmeCount++;
                            }
                        }
                        if (falg2  && sanmeCount<=1) {
                            newRule = rules;
                            break;
                        }
                    }
                } else {
                    newRule = rules;
                }
            }
        }
        if (count == cartList.size()) {
            newRule = new EsShopGoodsRules();
            newRule.setId(9999L);
        }
        return newRule;
    }

    @Override
    public Integer delete(long goodsId, int according) {
        EsShopGoodsRules esShopGoodsRules = rulesMapper.selectById(goodsId);
        esShopGoodsRules.setAccording(according);
        return  rulesMapper.updateById(esShopGoodsRules);
    }

    @Override
    public Integer updateRule(String goodsname, long goodsId) {
        return 1;
    }


}
