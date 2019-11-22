package com.mei.zhuang.service.marking.impl;

import com.mei.zhuang.utils.Weekutils;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.mei.zhuang.vo.order.CartMarkingVo;
import com.mei.zhuang.dao.marking.EsShopFirstPurchaseGoodsMapMapper;
import com.mei.zhuang.dao.marking.EsShopFirstPurchaseMapper;
import com.mei.zhuang.dao.marking.EsShopFirstPurchaseRuleMapper;
import com.mei.zhuang.service.marking.FirstPurchaseService;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.utils.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopFirstPurchase;
import com.mei.zhuang.entity.marking.EsShopFirstPurchaseGoodsMap;
import com.mei.zhuang.entity.marking.EsShopFirstPurchaseRule;
import com.mei.zhuang.entity.order.EsShopCart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Service
public class FirstPurchaseServiceImpl extends ServiceImpl<EsShopFirstPurchaseMapper, EsShopFirstPurchase> implements FirstPurchaseService {
    @Resource
    private EsShopFirstPurchaseMapper firstPurchaseMapper;
    @Resource
    private EsShopFirstPurchaseGoodsMapMapper firstPurchaseGoodsMapMapper;
    @Resource
    private EsShopFirstPurchaseRuleMapper firstPurchaseRuleMapper;

    @Override
    public List<EsShopFirstPurchaseRule> isFirstPurchaseUseAble(CartMarkingVo vo) {
        try {
            List<Long> ids = Arrays.asList(vo.getRuleIds().split(";")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<EsShopFirstPurchaseRule> rules = firstPurchaseRuleMapper.selectList(new QueryWrapper<EsShopFirstPurchaseRule>().in("id", ids));
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
    public List<EsShopFirstPurchaseRule> matchFirstPurchase(CartMarkingVo vo) throws Exception {
        List<EsShopCart> cartList = vo.getCartList();
        List<EsShopFirstPurchase> relManjianList = new ArrayList<>();
        List<Long> onejianList = new ArrayList<>(); // 第一单
        List<Long> alljianList = new ArrayList<>(); // 所有单
        List<EsShopFirstPurchase> manjianList = firstPurchaseMapper.selectList(new QueryWrapper<EsShopFirstPurchase>().eq("status", 0));
        List<EsShopFirstPurchaseRule> list = new ArrayList<>();
        if (manjianList != null && manjianList.size() > 0) {

            for (EsShopFirstPurchase manjian : manjianList) {
                if (checkManjian(manjian)) {
                    /**
                     * 商品模式，风格 1 全部商品 2 部分商品
                     */
                    if (manjian.getGoodsMode() == 1) {
                        relManjianList.add(manjian);
                    } else {
                        List<EsShopFirstPurchaseGoodsMap> manjianGoodsMaps = firstPurchaseGoodsMapMapper.selectList(new QueryWrapper<EsShopFirstPurchaseGoodsMap>().
                                eq("first_purchase_id", manjian.getId()));
                        boolean falg =false;
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            for (EsShopFirstPurchaseGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : cartList) {
                                    if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        relManjianList.add(manjian);
                                        falg=true;
                                        break;
                                    }
                                }
                                if (falg){
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (relManjianList != null && relManjianList.size() > 0) {
                    for (EsShopFirstPurchase purchase : relManjianList) {
                        if (purchase.getType() == 1) {
                            onejianList.add(purchase.getId());
                        } else {
                            alljianList.add(purchase.getId());
                        }
                    }
                    if (onejianList != null && onejianList.size() > 0 && vo.getType() == 1) {
                        list = firstPurchaseRuleMapper.selectList(
                                new QueryWrapper<EsShopFirstPurchaseRule>().in("first_purchase_id", onejianList).eq("activity_type", 1));
                    } else  if (alljianList != null && alljianList.size() > 0 && vo.getType() == 2) {
                        list = firstPurchaseRuleMapper.selectList(
                                new QueryWrapper<EsShopFirstPurchaseRule>().in("first_purchase_id", alljianList).eq("activity_type", 2));
                    }
                }
            }

        return list;
    }

    private boolean checkManjian(EsShopFirstPurchase manjian) throws ParseException {
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(EsShopFirstPurchase entity) throws Exception {
        datetime(entity);
        entity.setUpdateTime(new Date());
        firstPurchaseMapper.updateById(entity);
        firstPurchaseGoodsMapMapper.delete(new QueryWrapper<EsShopFirstPurchaseGoodsMap>().eq("first_purchase_id", entity.getId()));
        firstPurchaseRuleMapper.delete(new QueryWrapper<EsShopFirstPurchaseRule>().eq("first_purchase_id", entity.getId()));
        addExtrInfo(entity);
        return true;
    }

    @Override
    public Integer deleteid(Long id) {
        firstPurchaseMapper.deleteById(id);
        firstPurchaseGoodsMapMapper.delete(new QueryWrapper<EsShopFirstPurchaseGoodsMap>().eq("first_purchase_id", id));
        firstPurchaseRuleMapper.delete(new QueryWrapper<EsShopFirstPurchaseRule>().eq("first_purchase_id", id));
        return 1;
    }

    @Override
    public List<EsShopFirstPurchaseGoodsMap> selectgoodsid(Long firstPurchaseId) {
        return firstPurchaseGoodsMapMapper.selectList(new QueryWrapper<EsShopFirstPurchaseGoodsMap>().eq("first_purchase_id", firstPurchaseId));
    }

    @Override
    public List<EsShopFirstPurchaseRule> selectcouponid(Long firstPurchaseId) {
        return firstPurchaseRuleMapper.selectList(new QueryWrapper<EsShopFirstPurchaseRule>().eq("first_purchase_id", firstPurchaseId));
    }

    @Override
    public Integer deletegoodsid(Long id) {
        firstPurchaseGoodsMapMapper.delete(new QueryWrapper<EsShopFirstPurchaseGoodsMap>().eq("goods_id", id));
        return 1;
    }

    @Override
    public Integer deletegiftid(Long giftsId) {
        firstPurchaseRuleMapper.delete(new QueryWrapper<EsShopFirstPurchaseRule>().eq("gifts_id", giftsId));
        return 1;
    }

    @Override
    public List<EsShopFirstPurchase> slelectPurchase() {
        return firstPurchaseMapper.selectList(new QueryWrapper<EsShopFirstPurchase>().orderByDesc("id"));
    }

    public void datetime(EsShopFirstPurchase en) throws Exception {
        String[] times = en.getTime().split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        en.setExpiryBeginTime(sdf.parse(times[0]));
        en.setExpiryEndTime(sdf.parse(times[1]));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopFirstPurchase entity)  {
        entity.setSource(1);
        entity.setStatus(1);
        entity.setShopId((long) 1);
        entity.setCreateTime(new Date());
        try {
            datetime(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        firstPurchaseMapper.insert(entity);
        addExtrInfo(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addExtrInfo(EsShopFirstPurchase entity) {
        if (entity.getGoodsMode() == 2) { // 1 全部商品 2 部分商品
            if (entity.getGoodsSepcVoList() != null && entity.getGoodsSepcVoList().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoList()) {
                    EsShopFirstPurchaseGoodsMap group = new EsShopFirstPurchaseGoodsMap();
                    group.setGoodsId(vo.getGoodsId());
                    group.setSpecIds(vo.getSpecIds());
                    group.setActivityName(entity.getTitles());
                    group.setFirstPurchaseId(entity.getId());
                    firstPurchaseGoodsMapMapper.insert(group);
                }
            }
        }
        //赠品id 添加
        if (entity.getFirstPurchaseRuleList() != null && entity.getFirstPurchaseRuleList().size() > 0) {
            for (EsShopFirstPurchaseRule gid : entity.getFirstPurchaseRuleList()) {
                EsShopFirstPurchaseRule group = new EsShopFirstPurchaseRule();
                group.setActivityName(entity.getTitles());
                group.setFirstPurchaseId(entity.getId());
                group.setGiftsId(gid.getGiftsId());
                group.setActivityType(entity.getType());
                group.setGoodsName(gid.getGoodsName());
                group.setPic(gid.getPic());
                firstPurchaseRuleMapper.insert(group);
            }
        }
        return false;
    }

    @Override
    public int updateShowStatus(Long id, Integer showStatus) {
        if (showStatus == 0) {
            EsShopFirstPurchase record = new EsShopFirstPurchase();
            record.setId(id);
            record.setStatus(showStatus);
            record.setUpdateTime(new Date());
            return firstPurchaseMapper.updateById(record);
        } else {
            EsShopFirstPurchase record = new EsShopFirstPurchase();
            record.setId(id);
            record.setUpdateTime(new Date());
            record.setStatus(1);
            return firstPurchaseMapper.updateById(record);
        }

    }
}
