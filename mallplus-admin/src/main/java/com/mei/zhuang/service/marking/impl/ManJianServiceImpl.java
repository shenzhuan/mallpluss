package com.mei.zhuang.service.marking.impl;

import com.arvato.ec.common.utils.Weekutils;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.arvato.ec.common.vo.marking.MjDcVo;
import com.arvato.ec.common.vo.order.CartMarkingVo;
import com.arvato.service.marking.api.orm.dao.EsShopManjianGoodsMapMapper;
import com.arvato.service.marking.api.orm.dao.EsShopManjianMapper;
import com.arvato.service.marking.api.orm.dao.EsShopManjianRuleMapper;
import com.arvato.service.marking.api.service.ManJianService;
import com.arvato.utils.date.DateUtil;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopManjian;
import com.mei.zhuang.entity.marking.EsShopManjianGoodsMap;
import com.mei.zhuang.entity.marking.EsShopManjianRule;
import com.mei.zhuang.entity.order.EsShopCart;
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
public class ManJianServiceImpl extends ServiceImpl<EsShopManjianMapper, EsShopManjian> implements ManJianService {
    @Resource
    private EsShopManjianMapper manjianMapper;
    @Resource
    private EsShopManjianGoodsMapMapper manjianGoodsMapMapper;
    @Resource
    private EsShopManjianRuleMapper manjianRuleMapper;

    @Override
    public EsShopManjianRule isManJianUseAble(CartMarkingVo vo) {
        try {
            EsShopManjianRule rule = manjianRuleMapper.selectById(vo.getRuleId());
            if (rule != null) {
                EsShopManjian discount = manjianMapper.selectById(rule.getManjianId());
                if (checkManjian(discount)) {
                    BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                    int singCount = 0;
                    if (discount.getGoodsMode() == 2) {
                        List<EsShopManjianGoodsMap> manjianGoodsMaps = manjianGoodsMapMapper.selectList(new QueryWrapper<EsShopManjianGoodsMap>().
                                eq("manjian_id", discount.getId()));
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            for (EsShopManjianGoodsMap manjianGoodsMap : manjianGoodsMaps) {
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
    public MjDcVo matchManjian(List<EsShopCart> cartList) throws Exception {
        MjDcVo vo = new MjDcVo();
        EsShopManjian query = new EsShopManjian();
        query.setStatus(0);
        EsShopManjian manjian = manjianMapper.selectOne(query);
        BigDecimal totalAmount = new BigDecimal("0");//实付金额
        int count = 0;

        /**
         * 商品模式，风格 1 全部商品 2 部分商品
         */
        if (checkManjian(manjian)) {
            vo.setBasicAmount(new BigDecimal(0));
            vo.setId(manjian.getId());
            if (manjian.getGoodsMode() == 1) {
                for (EsShopCart cart : cartList) {
                    totalAmount = totalAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                    count = count + cart.getTotal();
                }
                /**
                 * 类型 1 消费金额 2 购买件数
                 */
                if (manjian.getType() == 1) {
                    List<EsShopManjianRule> list = manjianRuleMapper.selectList(
                            new QueryWrapper<EsShopManjianRule>().eq("manjian_id", manjian.getId()).orderBy("consumption_amount", false));
                    for (EsShopManjianRule rule : list) {
                        if (list != null && list.size() > 0 && totalAmount.compareTo(rule.getConsumptionAmount()) >= 0) {
                            vo.setBasicAmount(rule.getCouponAmount().add(vo.getBasicAmount()));
                            break;
                        }
                    }
                } else {
                    List<EsShopManjianRule> list = manjianRuleMapper.selectList(
                            new QueryWrapper<EsShopManjianRule>().eq("manjian_id", manjian.getId()).orderBy("consumption_amount", false));
                    for (EsShopManjianRule rule : list) {
                        if (list != null && list.size() > 0 && count >= rule.getConsumptionAmount().intValue()) {
                            vo.setBasicAmount(rule.getCouponAmount().add(vo.getBasicAmount()));
                            break;
                        }
                    }
                }

            } else {
                BigDecimal totalSingAmount = new BigDecimal("0");//实付金额
                int singCount = 0;
                List<EsShopManjianGoodsMap> manjianGoodsMaps = manjianGoodsMapMapper.selectList(new QueryWrapper<EsShopManjianGoodsMap>().
                        eq("manjian_id", manjian.getId()));
                if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                    for (EsShopManjianGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                        for (EsShopCart cart : cartList) {
                            if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                    || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                totalSingAmount = totalSingAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                singCount = singCount + cart.getTotal();
                            }
                        }
                    }

                    /**
                     * 类型 1 消费金额 2 购买件数
                     */
                    if (manjian.getType() == 1) {
                        List<EsShopManjianRule> list = manjianRuleMapper.selectList(
                                new QueryWrapper<EsShopManjianRule>().eq("manjian_id", manjian.getId()).orderBy("consumption_amount", false));
                        for (EsShopManjianRule rule : list) {
                            if (list != null && list.size() > 0 && totalSingAmount.compareTo(rule.getConsumptionAmount()) >= 0) {
                                vo.setBasicAmount(rule.getCouponAmount().add(vo.getBasicAmount()));
                                break;
                            }
                        }
                    } else {
                        List<EsShopManjianRule> list = manjianRuleMapper.selectList(
                                new QueryWrapper<EsShopManjianRule>().eq("manjian_id", manjian.getId()).orderBy("consumption_amount", false));
                        for (EsShopManjianRule rule : list) {
                            if (list != null && list.size() > 0 && singCount >= rule.getConsumptionAmount().intValue()) {
                                vo.setBasicAmount(rule.getCouponAmount().add(vo.getBasicAmount()));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return vo;
    }

    /*
           判断是否在高级设置活动范围内
     */
    private boolean checkManjian(EsShopManjian manjian) throws ParseException {
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

    private EsShopManjianRule getCheckManJianList(EsShopManjian manjian, BigDecimal totalSingAmount, int singCount) {
        /**
         * 类型 1 消费金额 2 购买件数
         */
        if (manjian.getType() == 1) {
            List<EsShopManjianRule> list = manjianRuleMapper.selectList(
                    new QueryWrapper<EsShopManjianRule>().eq("manjian_id", manjian.getId()).orderBy("consumption_amount", false));
            for (EsShopManjianRule rule : list) {
                if (list != null && list.size() > 0 && totalSingAmount.compareTo(rule.getConsumptionAmount()) >= 0) {
                    return rule;
                }
            }
        } else {
            List<EsShopManjianRule> list = manjianRuleMapper.selectList(
                    new QueryWrapper<EsShopManjianRule>().eq("manjian_id", manjian.getId()).orderBy("consumption_amount", false));
            for (EsShopManjianRule rule : list) {
                if (list != null && list.size() > 0 && singCount >= rule.getConsumptionAmount().intValue()) {
                    return rule;
                }
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer update(EsShopManjian entity) throws Exception {
        datetime(entity);
        entity.setUpdateTime(new Date());
        manjianMapper.updateById(entity);
        manjianGoodsMapMapper.delete(new QueryWrapper<EsShopManjianGoodsMap>().eq("manjian_id", entity.getId()));
        manjianRuleMapper.delete(new QueryWrapper<EsShopManjianRule>().eq("manjian_id", entity.getId()));
        addExtrInfo(entity);
        return 1;
    }

    //商品明细查询
    @Override
    public List<EsShopManjianGoodsMap> selectgoodsid(Long manjianId) {

        return manjianGoodsMapMapper.selectList(new QueryWrapper<EsShopManjianGoodsMap>().eq("manjian_id", manjianId));
    }

    @Override
    public List<EsShopManjianRule> selectcouponid(Long couponid) {

        return manjianRuleMapper.selectList(new QueryWrapper<EsShopManjianRule>().eq("manjian_id", couponid));
    }

    @Override
    public List<EsShopManjian> slelectMan() {
        return manjianMapper.selectList(new QueryWrapper<EsShopManjian>().orderBy("id", false));
    }

    @Override
    public Integer deleteid(Long id) {
        manjianMapper.deleteById(id);
        manjianGoodsMapMapper.delete(new QueryWrapper<EsShopManjianGoodsMap>().eq("manjian_id", id));
        manjianRuleMapper.delete(new QueryWrapper<EsShopManjianRule>().eq("manjian_id", id));
        return 1;
    }

    @Override
    public Integer deletegoodsid(Long id) {

        return manjianGoodsMapMapper.delete(new QueryWrapper<EsShopManjianGoodsMap>().eq("goods_id", id));
    }

    public void datetime(EsShopManjian en) throws Exception {
        String[] times = en.getTime().split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        en.setExpiryBeginTime(sdf.parse(times[0]));
        en.setExpiryEndTime(sdf.parse(times[1]));

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopManjian entity) throws Exception {
        // entity.setSource(1);
        // 0:启用 1：禁用
        entity.setStatus(1);
        entity.setShopId((long) 1);
        entity.setCreateTime(new Date());
        datetime(entity);
        manjianMapper.insert(entity);
        addExtrInfo(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addExtrInfo(EsShopManjian entity) {
        // 处理部分优惠商品
        if (entity.getGoodsMode() == 2) { // 1 全部商品 2 指定商品
            if (entity.getGoodsSepcVoList() != null && entity.getGoodsSepcVoList().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoList()) {
                    EsShopManjianGoodsMap group = new EsShopManjianGoodsMap();
                    group.setSpecIds(vo.getSpecIds());
                    group.setGoodsId(vo.getGoodsId());
                    group.setManjianId(entity.getId());
                    manjianGoodsMapMapper.insert(group);
                }
            }
        }
        if (entity.getRuleList() != null && entity.getRuleList().size() > 0) {
            for (EsShopManjianRule rule : entity.getRuleList()) {
                rule.setActivityType(entity.getType());
                rule.setManjianId(entity.getId());
                rule.setShopId(entity.getShopId());
                manjianRuleMapper.insert(rule);
            }
        }
        return true;
    }

    @Override
    public int updateShowStatus(Long id, Integer showStatus) {
        if (showStatus == 0) {
            EsShopManjian record = new EsShopManjian();
            record.setId(id);
            record.setStatus(showStatus);
            record.setUpdateTime(new Date());
            return manjianMapper.updateById(record);
        } else {
            EsShopManjian record = new EsShopManjian();
            record.setId(id);
            record.setUpdateTime(new Date());
            record.setStatus(1);
            return manjianMapper.updateById(record);
        }

    }

    @Override
    public Integer selectstatus() {
        return manjianMapper.selectstatus();
    }

}
