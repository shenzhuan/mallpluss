package com.mei.zhuang.service.marking.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.marking.EsShopDiscountGoodsMapMapper;
import com.mei.zhuang.dao.marking.EsShopDiscountMapper;
import com.mei.zhuang.dao.marking.EsShopDiscountRuleMapper;
import com.mei.zhuang.entity.marking.EsShopDiscount;
import com.mei.zhuang.entity.marking.EsShopDiscountGoodsMap;
import com.mei.zhuang.entity.marking.EsShopDiscountRule;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.service.marking.DiscountService;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.utils.Weekutils;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.mei.zhuang.vo.marking.MjDcVo;
import com.mei.zhuang.vo.order.CartMarkingVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Service
public class DiscountServiceImpl extends ServiceImpl<EsShopDiscountMapper, EsShopDiscount> implements DiscountService {
    @Resource
    private EsShopDiscountMapper discountMapper;
    @Resource
    private EsShopDiscountGoodsMapMapper discountGoodsMapMapper;

    @Resource
    private EsShopDiscountRuleMapper discountRuleMapper;


    @Transactional(rollbackFor = Exception.class)


    @Override
    public Integer update(EsShopDiscount entity) throws Exception {
        datetime(entity);
        entity.setUpdateTime(new Date());
        discountMapper.updateById(entity);
        discountGoodsMapMapper.delete(new QueryWrapper<EsShopDiscountGoodsMap>().eq("discount_id", entity.getId()));
        discountRuleMapper.delete(new QueryWrapper<EsShopDiscountRule>().eq("discount_id", entity.getId()));
        addExtrInfo(entity);
        return 1;
    }

    @Override
    public Integer deleteid(Long id) {
        // 删除主体信息
        discountMapper.deleteById(id);
        // 删除指定优惠商品
        discountGoodsMapMapper.delete(new QueryWrapper<EsShopDiscountGoodsMap>().eq("discount_id", id));
        // 删除优惠规则
        discountRuleMapper.delete(new QueryWrapper<EsShopDiscountRule>().eq("discount_id", id));
        return 1;
    }

    @Override
    public Integer selectstatus() {
        return discountMapper.selectstatus();
    }

    @Override
    public EsShopDiscountRule isDiscountRuleUseAble(CartMarkingVo vo) {
        try {
            EsShopDiscountRule rule = discountRuleMapper.selectById(vo.getRuleId());
            if (rule != null) {
                EsShopDiscount discount = discountMapper.selectById(rule.getDiscountId());
                if (checkManjian(discount)) {
                    BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                    int singCount = 0;
                    if (discount.getGoodsMode() == 2) {
                        List<EsShopDiscountGoodsMap> manjianGoodsMaps = discountGoodsMapMapper.selectList(new QueryWrapper<EsShopDiscountGoodsMap>().
                                eq("discount_id", discount.getId()));
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            for (EsShopDiscountGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : vo.getCartList()) {
                                    if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                        singCount = singCount + cart.getTotal();
                                    }
                                }
                            }

                        }
                    } else {
                        for (EsShopCart cart : vo.getCartList()) {
                            totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                            singCount = singCount + cart.getTotal();
                        }
                    }
                    /**
                     * 类型1 消费金额 2 购买件数
                     */
                    if (discount.getType() == 1) {
                        if (rule.getConsumptionAmount().compareTo(totalSingAmount) <= 0) {
                            return rule;
                        }
                    } else {
                        if (singCount > rule.getTotal()) {
                            return rule;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MjDcVo matchDiscount(List<EsShopCart> cartList) throws ParseException {
        EsShopDiscount query = new EsShopDiscount();
        MjDcVo vo = new MjDcVo();
        vo.setBasicAmount(new BigDecimal(0));
        query.setStatus(0);
        EsShopDiscount manjian = discountMapper.selectOne(new QueryWrapper<>(query));

        BigDecimal totalAmount = new BigDecimal("0");//实付金额
        int count = 0;
        if (checkManjian(manjian)) {
            vo.setId(manjian.getId());
            /**
             * 商品模式，风格 1 全部商品 2 部分商品
             */
            if (manjian.getGoodsMode() == 1) {
                for (EsShopCart cart : cartList) {
                    totalAmount = totalAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                    count = count + cart.getTotal();
                }

                /**
                 * 类型1 消费金额 2 购买件数
                 */
                if (manjian.getType() == 1) {
                    List<EsShopDiscountRule> list = discountRuleMapper.selectList(
                            new QueryWrapper<EsShopDiscountRule>().eq("discount_id", manjian.getId()).orderByDesc("consumption_amount"));
                    for (EsShopDiscountRule rule : list) {
                        if (list != null && list.size() > 0 && totalAmount.compareTo(rule.getConsumptionAmount()) >= 0) {
                            vo.setBasicAmount(totalAmount.multiply(new BigDecimal(10).subtract(rule.getDiscountAmount())).divide(new BigDecimal(10)));
                            break;
                        }
                    }
                } else {
                    List<EsShopDiscountRule> list = discountRuleMapper.selectList(
                            new QueryWrapper<EsShopDiscountRule>().eq("discount_id", manjian.getId()).orderByDesc("consumption_amount"));
                    for (EsShopDiscountRule rule : list) {
                        if (list != null && list.size() > 0 && count >= rule.getConsumptionAmount().intValue()) {
                            vo.setBasicAmount(totalAmount.multiply(new BigDecimal(10).subtract(rule.getDiscountAmount())).divide(new BigDecimal(10)));
                            break;
                        }
                    }
                }


            } else {
                BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                int singCount = 0;
                List<EsShopDiscountGoodsMap> manjianGoodsMaps = discountGoodsMapMapper.selectList(new QueryWrapper<EsShopDiscountGoodsMap>().
                        eq("discount_id", manjian.getId()));
                if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                    for (EsShopCart cart : cartList) {
                        for (EsShopDiscountGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                            if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                    || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                singCount = singCount + cart.getTotal();
                            }
                        }
                    }
                    /**
                     * 类型1 消费金额 2 购买件数
                     */
                    if (manjian.getType() == 1) {
                        List<EsShopDiscountRule> list = discountRuleMapper.selectList(
                                new QueryWrapper<EsShopDiscountRule>().eq("discount_id", manjian.getId()).orderByDesc("consumption_amount"));
                        for (EsShopDiscountRule rule : list) {
                            if (list != null && list.size() > 0 && totalSingAmount.compareTo(rule.getConsumptionAmount()) >= 0) {
                                vo.setBasicAmount(totalSingAmount.multiply(new BigDecimal(10).subtract(rule.getDiscountAmount())).divide(new BigDecimal(10)));
                                break;
                            }
                        }
                    } else {
                        List<EsShopDiscountRule> list = discountRuleMapper.selectList(
                                new QueryWrapper<EsShopDiscountRule>().eq("discount_id", manjian.getId()).orderByDesc("consumption_amount"));
                        for (EsShopDiscountRule rule : list) {
                            if (list != null && list.size() > 0 && singCount >= rule.getConsumptionAmount().intValue()) {
                                vo.setBasicAmount(totalSingAmount.multiply(new BigDecimal(10).subtract(rule.getDiscountAmount())).divide(new BigDecimal(10)));
                                break;
                            }
                        }
                    }

                }
            }
        }
        return vo;
    }

    private boolean checkManjian(EsShopDiscount manjian) throws ParseException {
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


    public void datetime(EsShopDiscount en) throws Exception {
        String[] times = en.getTime().split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        en.setExpiryBeginTime(sdf.parse(times[0]));
        en.setExpiryEndTime(sdf.parse(times[1]));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopDiscount entity)  {
        entity.setSource(1);
        // 1：未启用 0：启用
        entity.setStatus(1);
        entity.setShopId((long) 1);
        entity.setCreateTime(new Date());
        try {
            datetime((entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 新增限时折扣主体信息
        discountMapper.insert(entity);

        addExtrInfo(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addExtrInfo(EsShopDiscount entity) {
        if (entity.getGoodsMode() == 2) { // 1 全部商品 2 部分商品
            if (entity.getGoodsSepcVoList() != null && entity.getGoodsSepcVoList().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoList()) {
                    EsShopDiscountGoodsMap group = new EsShopDiscountGoodsMap();
                    group.setGoodsId(vo.getGoodsId());
                    group.setSpecIds(vo.getSpecIds());
                    group.setDiscountId(entity.getId());
                    discountGoodsMapMapper.insert(group);
                }
            }
        }
        if (entity.getRuleList() != null && entity.getRuleList().size() > 0) {
            for (EsShopDiscountRule rule : entity.getRuleList()) {
                rule.setActivityType(entity.getType());
                rule.setShopId(entity.getShopId());
                rule.setDiscountId(entity.getId());
                discountRuleMapper.insert(rule);
            }
        }

        return false;
    }

    @Override
    public int updateShowStatus(Long id, Integer showStatus) {
        if (showStatus == 0) {
            EsShopDiscount record = new EsShopDiscount();
            record.setId(id);
            record.setStatus(showStatus);
            record.setUpdateTime(new Date());
            return discountMapper.updateById(record);
        } else {
            //1 禁用所有满减
            // discountMapper.stopAllDiscount();
            EsShopDiscount record = new EsShopDiscount();
            record.setId(id);
            record.setUpdateTime(new Date());
            record.setStatus(1);
            return discountMapper.updateById(record);
        }

    }

    //商品明细
    @Override
    public List<EsShopDiscountGoodsMap> selectgoodsid(Long discountId) {

        return discountGoodsMapMapper.selectList(new QueryWrapper<EsShopDiscountGoodsMap>().eq("discount_id", discountId));
    }

    //优惠券明细
    @Override
    public List<EsShopDiscountRule> selectcouponid(Long discountId) {
        return discountRuleMapper.selectList(new QueryWrapper<EsShopDiscountRule>().eq("discount_id", discountId));
    }

    @Override
    public List<EsShopDiscount> slelectDiscount() {
        return discountMapper.selectList(new QueryWrapper<EsShopDiscount>().orderByDesc("id"));
    }
}
