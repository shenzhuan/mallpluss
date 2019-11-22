package com.mei.zhuang.service.marking.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.marking.EsShopSingleGiftGoodsMapMapper;
import com.mei.zhuang.dao.marking.EsShopSingleGiftMapper;
import com.mei.zhuang.dao.marking.EsShopSingleGiftRuleMapper;
import com.mei.zhuang.entity.marking.EsShopSingleGift;
import com.mei.zhuang.entity.marking.EsShopSingleGiftGoodsMap;
import com.mei.zhuang.entity.marking.EsShopSingleGiftRule;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.service.marking.SingleGiftService;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.utils.Weekutils;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.mei.zhuang.vo.order.CartMarkingVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Service
public class SingleGifterviceImpl extends ServiceImpl<EsShopSingleGiftMapper, EsShopSingleGift> implements SingleGiftService {
    @Resource
    private EsShopSingleGiftMapper singleGiftMapper;
    @Resource
    private EsShopSingleGiftGoodsMapMapper singleGiftGoodsMapMapper;
    @Resource
    private EsShopSingleGiftRuleMapper singleGiftRuleMapper;


    @Override
    public List<EsShopSingleGiftGoodsMap> isSingleGiftUseAble(CartMarkingVo vo) {

        List<EsShopSingleGiftGoodsMap> rules = new ArrayList<>();
        if (ValidatorUtils.notEmpty(vo.getRuleIds())){
            String[] split = vo.getRuleIds().split(";");
            for (int i=0;i<split.length;i++){
                EsShopSingleGiftGoodsMap g =  singleGiftGoodsMapMapper.selectById(Long.parseLong(split[i].split(":")[0]));
                g.setCount(Integer.valueOf(split[i].split(":")[1]));
                rules.add(g);
            }
            return rules;
        }
        return null;

    }

    @Override
    public List<EsShopSingleGift> matchSingleGift(List<EsShopCart> cartList) throws Exception {

        List<EsShopSingleGift> relManjianList = new ArrayList<>();
        List<EsShopSingleGift> manjianList = singleGiftMapper.selectList(new QueryWrapper<EsShopSingleGift>().eq("status", 0));
        BigDecimal totalAmount = new BigDecimal("0");//实付金额
        int count = 0;
        for (EsShopCart cart : cartList) {
            totalAmount = totalAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
            count = count + cart.getTotal();
        }
        if (manjianList != null && manjianList.size() > 0) {
            for (EsShopSingleGift manjian : manjianList) {
                /**
                 * 商品模式，风格 1 单品购买 2 组合购买
                 */
                boolean falg = false;
                boolean falg1 = false;
                if (checkManjian(manjian)) {
                    List<EsShopCart> singCartList = new ArrayList<>();
                    List<EsShopSingleGiftGoodsMap> giftGoodsMapsList = new ArrayList<>();
                    List<EsShopSingleGiftGoodsMap> manjianGoodsMaps = singleGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopSingleGiftGoodsMap>().
                            eq("single_gift_id", manjian.getId()));
                    if (manjian.getGoodsMode() == 1) {
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {

                            for (EsShopSingleGiftGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : cartList) {
                                    if (manjianGoodsMap.getGoodsType() == 1 && manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        falg = true;
                                        singCartList.add(cart);
                                    }
                                }
                                if (manjianGoodsMap.getGoodsType() == 3) {
                                    /**
                                     * 类型1 仅送一件 2 按购买件数 3 指定件数送
                                     */
                                    if (manjian.getType() == 1) {
                                        manjianGoodsMap.setCount(1);
                                    }
                                    if (manjian.getType() == 2) {
                                        int count1 = 0;
                                        for (EsShopCart cart : singCartList) {
                                            count1 = count1 + cart.getTotal();
                                        }
                                        EsShopSingleGiftRule q = new EsShopSingleGiftRule();
                                        q.setSingleGiftId(manjian.getId());
                                        EsShopSingleGiftRule rule = singleGiftRuleMapper.selectOne(new QueryWrapper<>(q));
                                        if (ValidatorUtils.notEmpty(rule.getMaxAmount()) && rule.getMaxAmount() > 0) {
                                            if (count1 > rule.getMaxAmount()) {
                                                count1 = rule.getMaxAmount();
                                            }
                                        }
                                        manjianGoodsMap.setCount(count1);
                                    }
                                    if (manjian.getType() == 3) {
                                        EsShopSingleGiftRule q = new EsShopSingleGiftRule();
                                        q.setSingleGiftId(manjian.getId());
                                        EsShopSingleGiftRule rule = singleGiftRuleMapper.selectOne(new QueryWrapper<>(q));
                                        int count2 = 0;
                                        for (EsShopCart cart : singCartList) {
                                            if (cart.getTotal() >= rule.getAlreadAmount()) {
                                                count2 = count2 + cart.getTotal() / rule.getAlreadAmount() * rule.getSongAmount();
                                            }
                                        }
                                        if (ValidatorUtils.notEmpty(rule.getMaxAmount()) && rule.getMaxAmount() > 0) {
                                            if (count2 > rule.getMaxAmount()) {
                                                count2 = rule.getMaxAmount();
                                            }
                                        }
                                        manjianGoodsMap.setCount(count2);
                                    }
                                    giftGoodsMapsList.add(manjianGoodsMap);
                                }

                            }
                            if (falg) {
                                manjian.setGiftGoodsMapsList(giftGoodsMapsList);
                                List<EsShopSingleGiftRule> list = singleGiftRuleMapper.selectList(
                                        new QueryWrapper<EsShopSingleGiftRule>().eq("single_gift_id", manjian.getId()));
                                if (list != null && list.size() > 0) {
                                    manjian.setShopSingleGiftRuleList(list);
                                    relManjianList.add(manjian);
                                }
                            }

                        }
                    } else {
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            int giftCount = 0 ;
                            List<EsShopCart> singCartList1 = new ArrayList<>();
                            List<EsShopCart> singCartList2 = new ArrayList<>();
                            for (EsShopSingleGiftGoodsMap manjianGoodsMap : manjianGoodsMaps) {

                                for (EsShopCart cart : cartList) {
                                    if (manjianGoodsMap.getGoodsType() == 1 && manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        falg = true;
                                        singCartList.add(cart);
                                        singCartList1.add(cart);
                                    }
                                    if (manjianGoodsMap.getGoodsType() == 2 && manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        falg1 = true;
                                        singCartList.add(cart);
                                        singCartList2.add(cart);
                                    }
                                }
                                if (manjianGoodsMap.getGoodsType() == 3) {
                                    /**
                                     * 类型1 仅送一件 2 按购买件数 3 指定件数送
                                     */
                                    if (manjian.getType() == 1) {
                                        manjianGoodsMap.setCount(1);
                                        giftCount =1;
                                    }
                                    if (manjian.getType() == 2) {
                                        EsShopSingleGiftRule q = new EsShopSingleGiftRule();
                                        q.setSingleGiftId(manjian.getId());
                                        EsShopSingleGiftRule rule = singleGiftRuleMapper.selectOne(new QueryWrapper<>(q));
                                        int count1 = 0;
                                        for (EsShopCart cart1 : singCartList1) {
                                            count1 = count1 + cart1.getTotal();
                                        }
                                        int count2 = 0;
                                        for (EsShopCart cart2 : singCartList2) {
                                            count2 = count2 + cart2.getTotal();
                                        }
                                        if (count1 > 0 && count2 > 0) {
                                            if (count1 > count2) {
                                                count1 = count2;
                                            }
                                        } else {
                                            count1 = 0;
                                        }
                                        if (ValidatorUtils.notEmpty(rule.getMaxAmount()) && rule.getMaxAmount()>0) {
                                            if (count1 > rule.getMaxAmount()) {
                                                count1 = rule.getMaxAmount();
                                            }
                                        }
                                        giftCount = count1;
                                        manjianGoodsMap.setCount(count1);
                                    }
                                    if (manjian.getType() == 3) {
                                        EsShopSingleGiftRule q = new EsShopSingleGiftRule();
                                        q.setSingleGiftId(manjian.getId());
                                        EsShopSingleGiftRule rule = singleGiftRuleMapper.selectOne(new QueryWrapper<>(q));
                                        int count1 = 0;
                                        for (EsShopCart cart1 : singCartList1) {
                                            if (cart1.getTotal() >= rule.getAlreadAmount()) {
                                                count1 = count1 + cart1.getTotal() / rule.getAlreadAmount() * rule.getSongAmount();
                                            }
                                        }
                                        int count2 = 0;
                                        for (EsShopCart cart2 : singCartList2) {
                                            if (cart2.getTotal() >= rule.getAlreadAmount()) {
                                                count2 = count2 + cart2.getTotal() / rule.getAlreadAmount() * rule.getSongAmount();
                                            }
                                        }
                                        if (count1 > 0 && count2 > 0) {
                                            if (count1 > count2) {
                                                count1 = count2;
                                            }
                                        } else {
                                            count1 = 0;
                                        }
                                        if (ValidatorUtils.notEmpty(rule.getMaxAmount()) && rule.getMaxAmount()>0) {
                                            if (count1 > rule.getMaxAmount()) {
                                                count1 = rule.getMaxAmount();
                                            }
                                        }
                                        giftCount = count1;
                                        manjianGoodsMap.setCount(count1);
                                    }
                                    giftGoodsMapsList.add(manjianGoodsMap);
                                }
                            }
                            if (falg && falg1 && giftCount>0) {
                                manjian.setGiftGoodsMapsList(giftGoodsMapsList);
                                List<EsShopSingleGiftRule> list = singleGiftRuleMapper.selectList(
                                        new QueryWrapper<EsShopSingleGiftRule>().eq("single_gift_id", manjian.getId()));
                                if (list != null && list.size() > 0) {
                                    manjian.setShopSingleGiftRuleList(list);
                                    relManjianList.add(manjian);
                                }
                            }

                        }
                    }


                }

            }
        }


        return relManjianList;
    }

    private boolean checkManjian(EsShopSingleGift manjian) throws ParseException {
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

    private void getCheckManJianList(List<EsShopSingleGift> relManjianList, EsShopSingleGift manjian, BigDecimal totalSingAmount, int singCount) {
        List<EsShopSingleGiftRule> list = singleGiftRuleMapper.selectList(
                new QueryWrapper<EsShopSingleGiftRule>().eq("single_gift_id", manjian.getId()));
        if (list != null && list.size() > 0) {
            manjian.setShopSingleGiftRuleList(list);
            relManjianList.add(manjian);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(EsShopSingleGift entity) throws Exception {
        datetime(entity);
        entity.setUpdateTime(new Date());
        singleGiftMapper.updateById(entity);
        singleGiftGoodsMapMapper.delete(new QueryWrapper<EsShopSingleGiftGoodsMap>().eq("single_gift_id", entity.getId()));
        singleGiftRuleMapper.delete(new QueryWrapper<EsShopSingleGiftRule>().eq("single_gift_id", entity.getId()));
        addExtrInfo(entity);
        return true;
    }

    @Override
    public List<EsShopSingleGiftRule> selectsing(Long singleGiftId) {
        return singleGiftRuleMapper.selectList(new QueryWrapper<EsShopSingleGiftRule>().eq("single_gift_id", singleGiftId));
    }

    //单品和多组
    @Override
    public Map<String, Object> selectsinggoods(Long singleGiftId) {
        Map<String, Object> map = new HashMap<>();
        EsShopSingleGift esShopSingleGift = singleGiftMapper.selectById(singleGiftId);
        if (esShopSingleGift.getGoodsMode() == 1) {
            List<EsShopSingleGiftGoodsMap> esShopSingleGiftGoodsMaps = singleGiftGoodsMapMapper.selectsinggoods1(singleGiftId);
            map.put("rows", esShopSingleGiftGoodsMaps);
            return map;
        } else {
            List<EsShopSingleGiftGoodsMap> esShopSingleGiftGoods1 = singleGiftGoodsMapMapper.selectsinggoods1(singleGiftId);
            List<EsShopSingleGiftGoodsMap> esShopSingleGiftGoods2 = singleGiftGoodsMapMapper.selectsinggoods2(singleGiftId);
            map.put("rows", esShopSingleGiftGoods1);
            map.put("rows2", esShopSingleGiftGoods2);
            return map;
        }
    }

    //赠品
    @Override
    public List<EsShopSingleGiftGoodsMap> selectsinggoods3(Long singleGiftId) {
        return singleGiftGoodsMapMapper.selectsinggoods3(singleGiftId);
    }

    @Override
    public Integer deleteid(Long id) {
        singleGiftMapper.deleteById(id);
        singleGiftGoodsMapMapper.delete(new QueryWrapper<EsShopSingleGiftGoodsMap>().eq("single_gift_id", id));
        singleGiftRuleMapper.delete(new QueryWrapper<EsShopSingleGiftRule>().eq("single_gift_id", id));

        return 1;
    }

    @Override
    public List<EsShopSingleGift> slelectPurchase() {
        return singleGiftMapper.selectList(new QueryWrapper<EsShopSingleGift>().orderByDesc("id"));
    }

    public void datetime(EsShopSingleGift en) throws Exception {
        String[] times = en.getTime().split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        en.setExpiryBeginTime(sdf.parse(times[0]));
        en.setExpiryEndTime(sdf.parse(times[1]));

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopSingleGift entity)  {
        entity.setSource(1);
        entity.setStatus(1);
        entity.setShopId((long) 1);
        entity.setCreateTime(new Date());
        try {
            datetime((entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        singleGiftMapper.insert(entity);
        addExtrInfo(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addExtrInfo(EsShopSingleGift entity) {
        if (entity.getGoodsMode() == 1) { // 1 1 单品购买 2 组合购买
            if (entity.getGoodsSepcVoList1() != null && entity.getGoodsSepcVoList1().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoList1()) {
                    EsShopSingleGiftGoodsMap group = new EsShopSingleGiftGoodsMap();
                    group.setGoodsId(vo.getGoodsId());
                    group.setSpecIds(vo.getSpecIds());
                    group.setSingleGiftId(entity.getId());
                    group.setGoodsType(1);
                    group.setActivityName(entity.getTitles());
                    singleGiftGoodsMapMapper.insert(group);
                }
            }
        }
        if (entity.getGoodsMode() == 2) { // 1 单品购买 2 组合购买
            // 选择组合商品一
            if (entity.getGoodsSepcVoList1() != null && entity.getGoodsSepcVoList1().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoList1()) {
                    EsShopSingleGiftGoodsMap group = new EsShopSingleGiftGoodsMap();
                    group.setGoodsId(vo.getGoodsId());
                    group.setSpecIds(vo.getSpecIds());
                    group.setSingleGiftId(entity.getId());
                    group.setGoodsType(1);
                    group.setActivityName(entity.getTitles());
                    singleGiftGoodsMapMapper.insert(group);
                }
            }// 选择组合商品二
            if (entity.getGoodsSepcVoList2() != null && entity.getGoodsSepcVoList2().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoList2()) {
                    EsShopSingleGiftGoodsMap group = new EsShopSingleGiftGoodsMap();
                    group.setGoodsId(vo.getGoodsId());
                    group.setSpecIds(vo.getSpecIds());
                    group.setSingleGiftId(entity.getId());
                    group.setGoodsType(2);
                    group.setActivityName(entity.getTitles());
                    singleGiftGoodsMapMapper.insert(group);
                }
            }
        }
        //赠品商品id 添加
        if (entity.getGiftGoodsList() != null && entity.getGiftGoodsList().size() > 0) {
            for (EsShopSingleGiftGoodsMap gid : entity.getGiftGoodsList()) {
                EsShopSingleGiftGoodsMap group = new EsShopSingleGiftGoodsMap();
                group.setSingleGiftId(entity.getId());
                group.setGoodsId(gid.getGoodsId());
                group.setGoodsName(gid.getGoodsName());
                group.setPic(gid.getPic());
                group.setActivityName(entity.getTitles());
                group.setGoodsType(3);
                singleGiftGoodsMapMapper.insert(group);
            }
        }
        if (entity.getRuleList() != null && entity.getRuleList().size() > 0) {
            for (EsShopSingleGiftRule rule : entity.getRuleList()) {
                rule.setActivityType(entity.getType());
                rule.setSingleGiftId(entity.getId());
                rule.setShopId(entity.getShopId());
                rule.setActivityName(entity.getTitles());
                singleGiftRuleMapper.insert(rule);
            }
        }

        return false;
    }

    @Override
    public int updateShowStatus(Long id, Integer showStatus) {
        if (showStatus == 0) {
            EsShopSingleGift record = new EsShopSingleGift();
            record.setId(id);
            record.setStatus(showStatus);
            record.setUpdateTime(new Date());
            return singleGiftMapper.updateById(record);
        } else {

            EsShopSingleGift record = new EsShopSingleGift();
            record.setId(id);
            record.setUpdateTime(new Date());
            record.setStatus(1);
            return singleGiftMapper.updateById(record);
        }

    }
}
