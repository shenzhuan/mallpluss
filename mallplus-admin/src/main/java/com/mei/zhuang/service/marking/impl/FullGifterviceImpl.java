package com.mei.zhuang.service.marking.impl;

import com.arvato.ec.common.utils.Weekutils;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.arvato.ec.common.vo.order.CartMarkingVo;
import com.arvato.service.marking.api.orm.dao.EsShopFullGiftGoodsMapMapper;
import com.arvato.service.marking.api.orm.dao.EsShopFullGiftMapper;
import com.arvato.service.marking.api.orm.dao.EsShopFullGiftRuleMapper;
import com.arvato.service.marking.api.service.FullGiftService;
import com.arvato.utils.date.DateUtil;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopFullGift;
import com.mei.zhuang.entity.marking.EsShopFullGiftGoodsMap;
import com.mei.zhuang.entity.marking.EsShopFullGiftRule;
import com.mei.zhuang.entity.order.EsShopCart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Service
public class FullGifterviceImpl extends ServiceImpl<EsShopFullGiftMapper, EsShopFullGift> implements FullGiftService {
    @Resource
    private EsShopFullGiftMapper fullGiftMapper;
    @Resource
    private EsShopFullGiftGoodsMapMapper fullGiftGoodsMapMapper;
    @Resource
    private EsShopFullGiftRuleMapper fullGiftRuleMapper;

    @Override
    public List<EsShopFullGiftGoodsMap> isFullGiftGoodsUseAble(CartMarkingVo vo) {
        try {
            List<Long> ids = Arrays.asList(vo.getRuleIds().split(";")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<EsShopFullGiftGoodsMap> rules = fullGiftGoodsMapMapper.selectList(
                    new QueryWrapper<EsShopFullGiftGoodsMap>().in("id", ids).eq("goods_type", 2));
            return rules;
           /* if (rules!=null && rules.size()>0){
                for (EsShopFirstPurchaseRule rule : rules){
                    EsShopFirstPurchase discount = firstPurchaseMapper.selectById(rule.getFirstPurchaseId());
                    if (checkManjian(discount)){
                        BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                        int singCount = 0;
                        if(discount.getGoodsMode() == 2){
                            List<EsShopFirstPurchaseGoodsMap> manjianGoodsMaps = firstPurchaseGoodsMapMapper.selectList(new QueryWrapper<EsShopFirstPurchaseGoodsMap>().
                                    eq("first_purchase_id",discount.getId()));
                            if (manjianGoodsMaps!=null && manjianGoodsMaps.size()>0){
                                for (EsShopFirstPurchaseGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                    for (EsShopCart cart : vo.getCartList()) {
                                        if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                                || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                            totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                            singCount = singCount + cart.getTotal();
                                        }
                                    }
                                }

                            }
                        }else {
                            for (EsShopCart cart : vo.getCartList()) {
                                totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                singCount = singCount+cart.getTotal();
                            }
                        }
                        *//**
             * 类型1 消费金额 2 购买件数
             *//*
                        if (discount.getType()==1){
                            if (rule.getConsumptionAmount().compareTo(totalSingAmount)<=0){
                                return rule;
                            }
                        }else {
                            if (singCount>rule.getTotal()){
                                return rule;
                            }
                        }
                    }
                }

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EsShopFullGiftGoodsMap> isChooseGiftGoodsUseAble(CartMarkingVo vo) {
        try {
            List<Long> ids = Arrays.asList(vo.getRuleIds().split(";")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<EsShopFullGiftGoodsMap> rules = fullGiftGoodsMapMapper.selectList(
                    new QueryWrapper<EsShopFullGiftGoodsMap>().in("id", ids).eq("goods_type", 2));
            return rules;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //满赠礼
    @Override
    public List<EsShopFullGift> matchFullGift(List<EsShopCart> cartList) throws Exception {
        List<EsShopFullGift> relManjianList = new ArrayList<>();
        List<EsShopFullGift> manjianList = fullGiftMapper.selectList(new QueryWrapper<EsShopFullGift>().eq("status", 0).eq("type",2));
        BigDecimal totalAmount = new BigDecimal("0");//实付金额
        int count = 0;
        for (EsShopCart cart : cartList) {
            totalAmount = totalAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
            count = count + cart.getTotal();
        }
        if (manjianList != null && manjianList.size() > 0) {
            for (EsShopFullGift manjian : manjianList) {
                if (checkManjian(manjian)) {
                    /**
                     * 商品模式，风格 1 全部商品 2 部分商品 3 不参与商品
                     */
                    if (manjian.getGoodsMode() == 1) {
                        getCheckManJianList(relManjianList, manjian, totalAmount, count);
                    } else if (manjian.getGoodsMode() == 2) {
                        BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                        int singCount = 0;
                        List<EsShopFullGiftGoodsMap> manjianGoodsMaps = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopFullGiftGoodsMap>().
                                eq("full_gift_id", manjian.getId()).eq("goods_type", 1));
                        boolean falg = false;
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            for (EsShopFullGiftGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : cartList) {
                                    if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                        singCount = singCount + cart.getTotal();
                                        falg = true;
                                    }
                                }
                            }
                            if (falg) {
                                getCheckManJianList(relManjianList, manjian, totalSingAmount, singCount);
                                break;
                            }
                        }
                    } else if (manjian.getGoodsMode() == 3) {
                        BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                        int singCount = 0;
                        List<EsShopFullGiftGoodsMap> manjianGoodsMaps = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopFullGiftGoodsMap>().
                                eq("full_gift_id", manjian.getId()).eq("goods_type", 1));
                        boolean falg = false;
                        int sizeM =manjianGoodsMaps.size();
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            int countS = 0;
                            List<Long> ids = new ArrayList<>();
                            for (EsShopFullGiftGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : cartList) {
                                    if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        countS++;
                                        ids.add(cart.getId());
                                    } else {
                                        falg = true;
                                    }
                                }
                            }
                            for (EsShopCart cart : cartList) {
                                if (!ids.contains(cart.getId())){
                                    totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                    singCount = singCount + cart.getTotal();
                                }
                            }
                            if (falg && cartList.size() > countS) {
                                getCheckManJianList(relManjianList, manjian, totalSingAmount, singCount);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return relManjianList;
    }

    //选赠礼
    @Override
    public List<EsShopFullGift> ChooseFullGift(List<EsShopCart> cartList) throws Exception {
        List<EsShopFullGift> relManjianList = new ArrayList<>();
        List<EsShopFullGift> manjianList = fullGiftMapper.selectList(new QueryWrapper<EsShopFullGift>().eq("status", 0).eq("type",1));
        BigDecimal totalAmount = new BigDecimal("0");//实付金额
        int count = 0;
        for (EsShopCart cart : cartList) {
            totalAmount = totalAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
            count = count + cart.getTotal();
        }
        if (manjianList != null && manjianList.size() > 0) {
            for (EsShopFullGift manjian : manjianList) {
                if (checkManjian(manjian)) {
                    /**
                     * 商品模式，风格 1 全部商品 2 部分商品 3 不参与商品
                     */
                    if (manjian.getGoodsMode() == 1) {
                        ChooseFullList(relManjianList, manjian, totalAmount, count);
                    } else if (manjian.getGoodsMode() == 2) {
                        BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                        int singCount = 0;
                        List<EsShopFullGiftGoodsMap> manjianGoodsMaps = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopFullGiftGoodsMap>().
                                eq("full_gift_id", manjian.getId()).eq("goods_type", 1));
                        boolean falg = false;
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            for (EsShopFullGiftGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : cartList) {
                                    if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                        singCount = singCount + cart.getTotal();
                                        falg = true;
                                    }
                                }
                            }
                            if (falg) {
                                ChooseFullList(relManjianList, manjian, totalSingAmount, singCount);
                                break;
                            }
                        }
                    } else if (manjian.getGoodsMode() == 3) {
                        BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                        int singCount = 0;
                        List<EsShopFullGiftGoodsMap> manjianGoodsMaps = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopFullGiftGoodsMap>().
                                eq("full_gift_id", manjian.getId()).eq("goods_type", 1));
                        boolean falg = false;
                        int sizeM =manjianGoodsMaps.size();
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            int countS = 0;
                            List<Long> ids = new ArrayList<>();
                            for (EsShopFullGiftGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : cartList) {
                                    if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        countS++;
                                        ids.add(cart.getId());
                                    } else {
                                        falg = true;
                                    }
                                }
                            }
                            for (EsShopCart cart : cartList) {
                                if (!ids.contains(cart.getId())){
                                    totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                    singCount = singCount + cart.getTotal();
                                }
                            }
                            if (falg && cartList.size() > countS) {
                                ChooseFullList(relManjianList, manjian, totalSingAmount, singCount);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return relManjianList;
    }
    private void ChooseFullList(List<EsShopFullGift> relManjianList, EsShopFullGift manjian, BigDecimal totalSingAmount, int singCount) {
        List<EsShopFullGiftRule> relfullGiftRuleList = new ArrayList<>();
        boolean falg = false;
        List<EsShopFullGiftRule> list = fullGiftRuleMapper.selectList(
                new QueryWrapper<EsShopFullGiftRule>().eq("full_gift_id", manjian.getId()).orderBy("full_level", false));
        if (list != null && list.size() > 0) {
            for (EsShopFullGiftRule rule : list) {
                if (rule.getConditions() == 1 && totalSingAmount.compareTo(rule.getPrice()) >= 0 && singCount >= rule.getAmount()) {
                    List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList = fullGiftGoodsMapMapper.selectList(
                            new QueryWrapper<EsShopFullGiftGoodsMap>().eq("rule_id", rule.getId()).eq("goods_type", 2));
                    rule.setFullGiftGoodsMapList(fullGiftGoodsMapList);
                    relfullGiftRuleList.add(rule);
                    falg = true;
                    break;
                }
                if (rule.getConditions() == 2 && (totalSingAmount.compareTo(rule.getPrice()) >= 0 || singCount >= rule.getAmount())) {
                    List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList = fullGiftGoodsMapMapper.selectList(
                            new QueryWrapper<EsShopFullGiftGoodsMap>().eq("rule_id", rule.getId()).eq("goods_type", 2));
                    rule.setFullGiftGoodsMapList(fullGiftGoodsMapList);
                    relfullGiftRuleList.add(rule);
                    falg = true;
                    break;
                }
                if (rule.getConditions() == 0 && ((totalSingAmount.compareTo(rule.getPrice()) >= 0 && rule.getPrice().compareTo(new BigDecimal(0)) > 0) || (singCount >= rule.getAmount() && rule.getAmount() > 0))) {
                    List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList = fullGiftGoodsMapMapper.selectList(
                            new QueryWrapper<EsShopFullGiftGoodsMap>().eq("rule_id", rule.getId()).eq("goods_type", 2));
                    rule.setFullGiftGoodsMapList(fullGiftGoodsMapList);
                    relfullGiftRuleList.add(rule);
                    falg = true;
                    break;
                }
            }
            if (falg) {
                manjian.setFullGiftRuleList(relfullGiftRuleList);
                relManjianList.add(manjian);
            }
        }
    }
    private boolean checkManjian(EsShopFullGift manjian) throws ParseException {
        if (manjian != null) {
            Date da = new Date();
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            String format = sdf2.format(da);
            Date f = sdf2.parse(format);
            if (ValidatorUtils.notEmpty(manjian.getStartTime())) {
                String statime = DateUtil.format(manjian.getStartTime(), "HH:mm:ss");
                Date starttime = DateUtil.parse(statime, "HH:mm:ss");
                if (f.getTime() < starttime.getTime()) {
                    return false;
                }
            }
            if (ValidatorUtils.notEmpty(manjian.getEndTime())) {
                String entime = DateUtil.format(manjian.getEndTime(), "HH:mm:ss");
                Date endtime = DateUtil.parse(entime, "HH:mm:ss");
                if (f.getTime() > endtime.getTime()) {
                    return false;
                }
            }
            int week = Weekutils.getWeekofDay(da);
            if (ValidatorUtils.empty(manjian.getWeekdays()) || manjian.getWeekdays().contains(week + "")) {
                if (manjian.getExpiryBeginTime().getTime() <= da.getTime() && manjian.getExpiryEndTime().getTime() >= da.getTime()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getCheckManJianList(List<EsShopFullGift> relManjianList, EsShopFullGift manjian, BigDecimal totalSingAmount, int singCount) {
        List<EsShopFullGiftRule> relfullGiftRuleList = new ArrayList<>();
        boolean falg = false;

        /**
         *  类型1 选赠礼 2 满赠礼
         */
           /* List<EsShopFullGiftRule> list = fullGiftRuleMapper.selectList(
                    new QueryWrapper<EsShopFullGiftRule>().eq("full_gift_id", manjian.getId()).orderBy("song_amount", false));
            if (list != null && list.size() > 0) {
                for (EsShopFullGiftRule rule : list) {
                    if (rule.getConditions() == 1 && totalSingAmount.compareTo(rule.getPrice()) >= 0 && singCount >= rule.getAmount()) {
                        relfullGiftRuleList.add(rule);
                        falg = true;
                        break;
                    }
                    if (rule.getConditions() == 2 && (totalSingAmount.compareTo(rule.getPrice()) >= 0 || singCount >= rule.getAmount())) {
                        relfullGiftRuleList.add(rule);
                        falg = true;
                        break;
                    }
                    if (rule.getConditions() == 0 && ((totalSingAmount.compareTo(rule.getPrice()) >= 0 && rule.getPrice().compareTo(new BigDecimal(0)) > 0) || (singCount >= rule.getAmount() && rule.getAmount() > 0))) {
                        relfullGiftRuleList.add(rule);
                        falg = true;
                        break;
                    }
                }
            }
            if (falg) {
                List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList = fullGiftGoodsMapMapper.selectList(
                        new QueryWrapper<EsShopFullGiftGoodsMap>().eq("full_gift_id", manjian.getId()).eq("goods_type", 2));
                manjian.setFullGiftGoodsMapList(fullGiftGoodsMapList);
                manjian.setFullGiftRuleList(relfullGiftRuleList);
                relManjianList.add(manjian);
            }*/
            List<EsShopFullGiftRule> list = fullGiftRuleMapper.selectList(
                    new QueryWrapper<EsShopFullGiftRule>().eq("full_gift_id", manjian.getId()).orderBy("full_level", false));
            if (list != null && list.size() > 0) {
                for (EsShopFullGiftRule rule : list) {
                    if (rule.getConditions() == 1 && totalSingAmount.compareTo(rule.getPrice()) >= 0 && singCount >= rule.getAmount()) {
                        List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList = fullGiftGoodsMapMapper.selectList(
                                new QueryWrapper<EsShopFullGiftGoodsMap>().eq("rule_id", rule.getId()).eq("goods_type", 2));
                        rule.setFullGiftGoodsMapList(fullGiftGoodsMapList);
                        relfullGiftRuleList.add(rule);
                        falg = true;
                        break;
                    }
                    if (rule.getConditions() == 2 && (totalSingAmount.compareTo(rule.getPrice()) >= 0 || singCount >= rule.getAmount())) {
                        List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList = fullGiftGoodsMapMapper.selectList(
                                new QueryWrapper<EsShopFullGiftGoodsMap>().eq("rule_id", rule.getId()).eq("goods_type", 2));
                        rule.setFullGiftGoodsMapList(fullGiftGoodsMapList);
                        relfullGiftRuleList.add(rule);
                        falg = true;
                        break;
                    }
                    if (rule.getConditions() == 0 && ((totalSingAmount.compareTo(rule.getPrice()) >= 0 && rule.getPrice().compareTo(new BigDecimal(0)) > 0) || (singCount >= rule.getAmount() && rule.getAmount() > 0))) {
                        List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList = fullGiftGoodsMapMapper.selectList(
                                new QueryWrapper<EsShopFullGiftGoodsMap>().eq("rule_id", rule.getId()).eq("goods_type", 2));
                        rule.setFullGiftGoodsMapList(fullGiftGoodsMapList);
                        relfullGiftRuleList.add(rule);
                        falg = true;
                        break;
                    }
                }
                if (falg) {
                    manjian.setFullGiftRuleList(relfullGiftRuleList);
                    relManjianList.add(manjian);
                }
            }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(EsShopFullGift entity) throws Exception {
        datetime(entity);
        entity.setUpdateTime(new Date());
        fullGiftMapper.updateById(entity);
        fullGiftGoodsMapMapper.delete(new QueryWrapper<EsShopFullGiftGoodsMap>().eq("full_gift_id", entity.getId()));
        fullGiftRuleMapper.delete(new QueryWrapper<EsShopFullGiftRule>().eq("full_gift_id", entity.getId()));
        addExtrInfo(entity);
        return true;
    }

    @Override
    public List<EsShopFullGiftGoodsMap> selectgift(Long fullGiftId) {
        return fullGiftGoodsMapMapper.selectgift(fullGiftId);
    }
    //满赠礼赠品与优惠设置的明细

    public List<Map<String, Object>> selectgift2(Long fullGiftId) {
        //优惠设置
        List<EsShopFullGiftRule> fullgiftids = fullGiftRuleMapper.selectfullgif(fullGiftId);
        //1选增的赠品2.满赠的赠品
       /* if (fullgiftids.get(0).getActivityType() == 1) {
            List<EsShopFullGiftGoodsMap> esShopFullGiftGoodsMaps = fullGiftGoodsMapMapper.selectgift2(fullGiftId);
            map.put("rows", esShopFullGiftGoodsMaps);
            map.put("rows2", fullgiftids);
            listmap.add(map);
            return listmap;
        } else {*/
            List<Map<String, Object>> listmap2 = new ArrayList<>();
            for (EsShopFullGiftRule rule : fullgiftids) {
                Map<String, Object> map2 = new HashMap<>();
                Map<String, Object> map3 = new HashMap<>();
                List<EsShopFullGiftGoodsMap> ruleId = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopFullGiftGoodsMap>().eq("rule_id", rule.getId()));
                map2.put("rows", ruleId);
                map2.put("rows2", rule);
                map3.put("rows3", map2);
                listmap2.add(map3);
            }
            return listmap2;
      //  }

    }

    @Override
    public List<EsShopFullGift> slelectPurchase() {
        return fullGiftMapper.selectList(new QueryWrapper<EsShopFullGift>().eq("type",2).orderBy("id", false));
    }

    @Override
    public List<EsShopFullGift> slelectPurchase2() {
        return fullGiftMapper.selectList(new QueryWrapper<EsShopFullGift>().eq("type",1).orderBy("id", false));
    }

    public void datetime(EsShopFullGift en) throws Exception {
        String[] times = en.getTime().split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        en.setExpiryBeginTime(sdf.parse(times[0]));
        en.setExpiryEndTime(sdf.parse(times[1]));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer save(EsShopFullGift entity) throws Exception {
        entity.setSource(1);
        entity.setStatus(1);
        entity.setShopId((long) 1);
        entity.setCreateTime(new Date());
        datetime(entity);
        fullGiftMapper.insert(entity);
        // 扩展信息
        addExtrInfo(entity);
        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addExtrInfo(EsShopFullGift entity) {
        // 1：全部商品 2：指定商品 3：不参与商品
        if (entity.getGoodsMode() == 2 || entity.getGoodsMode() == 3) { // 1 全部商品 2 部分商品 3不参与商品
            if (entity.getGoodsSepcVoList() != null && entity.getGoodsSepcVoList().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoList()) {
                    EsShopFullGiftGoodsMap group = new EsShopFullGiftGoodsMap();
                    group.setGoodsId(vo.getGoodsId());
                    group.setSpecIds(vo.getSpecIds());
                    group.setFullGiftId(entity.getId());
                    group.setActivityName(entity.getTitles());
                    group.setGoodsType(1);
                    fullGiftGoodsMapMapper.insert(group);
                }
            }
        }
        //类型1 选赠礼 2 满赠礼
      /*  if (entity.getType() == 1) {
            //赠品添加
            if (entity.getFullGiftGoodsList() != null && entity.getFullGiftGoodsList().size() > 0) {
                for (EsShopFullGiftGoodsMap gid : entity.getFullGiftGoodsList()) {
                    EsShopFullGiftGoodsMap group = new EsShopFullGiftGoodsMap();
                    group.setFullGiftId(entity.getId());
                    group.setGoodsId(gid.getGoodsId());
                    group.setGoodsName(gid.getGoodsName());
                    group.setPic(gid.getPic());
                    group.setActivityName(entity.getTitles());
                    //赠品类型
                    group.setGoodsType(2);
                    fullGiftGoodsMapMapper.insert(group);
                }
            }
            //优惠设置添加
            if (entity.getRuleList() != null && entity.getRuleList().size() > 0) {
                for (EsShopFullGiftRule rule : entity.getRuleList()) {
                    rule.setActivityType(entity.getType());
                    rule.setFullGiftId(entity.getId());
                    rule.setShopId(entity.getShopId());
                    rule.setActivityName(entity.getTitles());
                    fullGiftRuleMapper.insert(rule);
                }
            }
        } else {*/
            if ( entity.getFullGiftList() != null&&entity.getFullGiftList().size() > 0) {
                for (EsShopFullGift gift : entity.getFullGiftList()) {
                    if (gift.getRuleList() != null && gift.getRuleList().size() > 0) {
                        for (EsShopFullGiftRule rule : gift.getRuleList()) {
                            rule.setActivityType(entity.getType());
                            rule.setFullGiftId(entity.getId());
                            rule.setShopId(entity.getShopId());
                            rule.setActivityName(entity.getTitles());
                            fullGiftRuleMapper.insert(rule);
                            if (gift.getFullGiftGoodsList() != null&&gift.getFullGiftGoodsList().size()> 0) {
                                for (EsShopFullGiftGoodsMap gid : gift.getFullGiftGoodsList()) {
                                    EsShopFullGiftGoodsMap group = new EsShopFullGiftGoodsMap();
                                    group.setFullGiftId(entity.getId());
                                    group.setGoodsId(gid.getGoodsId());
                                    group.setRuleId(rule.getId());
                                    group.setGoodsName(gid.getGoodsName());
                                    group.setActivityName(entity.getTitles());
                                    group.setPic(gid.getPic());
                                    //赠品类型
                                    group.setGoodsType(2);
                                    fullGiftGoodsMapMapper.insert(group);
                                }
                            }
                        }
                    }
                }
            }
      //  }

        return true;
    }

    @Override
    public int updateShowStatus(Long id, Integer showStatus) {
        if (showStatus == 0) {
            EsShopFullGift record = new EsShopFullGift();
            record.setId(id);
            record.setStatus(showStatus);
            record.setUpdateTime(new Date());
            return fullGiftMapper.updateById(record);
        } else {
            //1 禁用所有满赠礼
            //  fullGiftMapper.stopFullGift();

            EsShopFullGift record = new EsShopFullGift();
            record.setId(id);
            record.setUpdateTime(new Date());
            record.setStatus(1);
            return fullGiftMapper.updateById(record);
        }

    }

    @Override
    public List<EsShopFullGift> selectrule() {
        EsShopFullGift manjian =new  EsShopFullGift();
        List<EsShopFullGift> relManjianList=new ArrayList<EsShopFullGift>();
        EsShopFullGift selectfull = fullGiftMapper.selectfull();
        if(selectfull!=null){
            try {
                if(checkManjian(selectfull)){
                    List<EsShopFullGiftRule> selectgif = fullGiftRuleMapper.selectgif(selectfull.getId());
                    manjian.setFullGiftRuleList2(selectgif);
                    List<EsShopFullGiftGoodsMap> slecetgoods = fullGiftGoodsMapMapper.slecetgoods(selectgif.get(0).getId());
                    manjian.setFullGiftGoodsMapList(slecetgoods);
                    relManjianList.add(manjian);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return relManjianList;
    }

    @Override
    public Integer deleteid(Long id) {
        fullGiftMapper.deleteById(id);
        fullGiftGoodsMapMapper.delete(new QueryWrapper<EsShopFullGiftGoodsMap>().eq("full_gift_id", id));
        fullGiftRuleMapper.delete(new QueryWrapper<EsShopFullGiftRule>().eq("full_gift_id", id));

        return 1;
    }

    @Override
    public Integer selectstatus() {
        return fullGiftMapper.selectstatus();
    }
    @Override
    public Integer selectstatus2() {
        return fullGiftMapper.selectstatus2();
    }
}
