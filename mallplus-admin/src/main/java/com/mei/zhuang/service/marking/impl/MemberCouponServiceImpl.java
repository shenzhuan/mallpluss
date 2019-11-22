package com.mei.zhuang.service.marking.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.marking.*;
import com.mei.zhuang.entity.marking.*;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.exception.BusinessException;
import com.mei.zhuang.service.marking.MemberCouponService;
import com.mei.zhuang.service.order.ShopOrderService;
import com.mei.zhuang.util.DateUtils;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.utils.Weekutils;
import com.mei.zhuang.vo.marking.AllMemberCoupon;
import com.mei.zhuang.vo.marking.CouponStatus;
import com.mei.zhuang.vo.order.CartMarkingVo;
import com.mei.zhuang.vo.order.CouponFilterParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Slf4j
@Service
public class MemberCouponServiceImpl extends ServiceImpl<EsMemberCouponMapper, EsMemberCoupon> implements MemberCouponService {
    @Resource
    private EsMemberCouponMapper couponMapper;

    @Resource
    private ShopOrderService orderfegin;
    @Resource
    private EsShopCouponNewMapper couponNewMapper;
    @Resource
    private EsShopCouponNewRuleMapper couponNewRuleMapper;

    @Resource
    private EsShopCouponMapper escouponMapper;
    @Resource
    private EsShopCouponRuleMapper ruleMapper;
    @Resource
    private EsShopCouponGoodsMapMapper goodsMapMapper;

    @Resource
    private EsShopCouponsTopupMapper couponsTopupMapper;
    @Resource
    private EsShopCouponsTopupGoodsMapper couponsTopupGoodsMapper;
    @Resource
    private EsShopCouponNewRuleMapper couponsNewRuleMapper;

    @Resource
    private EsShopCouponShoppingMapper couponShoppingMapper;
    @Resource
    private EsShopCouponManualMapper couponManualMapper;


    @Resource
    private EsShopCouponManualUserMapper manualUserMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 查询赠品券得赠品
     *
     * @param couponId
     */
    @Override
    public List<EsShopCouponGoodsMap> selectSendCouponGift(Long couponId) {
        EsMemberCoupon memberCoupon = couponMapper.selectById(couponId);
        if (memberCoupon != null) {
            return goodsMapMapper.selectList(new QueryWrapper<EsShopCouponGoodsMap>().
                    eq("coupon_id", memberCoupon.getCouponId()).eq("goodslimit", 3));
        }
        return null;
    }

    /**
     * 查询用户所有优惠券
     *
     * @param memberId
     * @return
     * @throws ParseException
     */
    @Override
    public AllMemberCoupon selectUserMemberCouponList(Long memberId) throws ParseException {
        AllMemberCoupon allMemberCoupon = new AllMemberCoupon();
        //未使用 已使用 已过期
        List<EsMemberCoupon> noCouponList = new ArrayList<>();
        List<EsMemberCoupon> alCouponList = new ArrayList<>();
        List<EsMemberCoupon> exCouponList = new ArrayList<>();
        EsMemberCoupon query = new EsMemberCoupon();
        query.setMemberId(memberId);
        List<EsMemberCoupon> list = couponMapper.selectList(new QueryWrapper<>(query));
        for (EsMemberCoupon memberCoupon : list) {
            EsShopCoupon coupon = escouponMapper.selectById(memberCoupon.getCouponId());
            if (coupon != null) {
                memberCoupon.setCoupon(coupon);
                memberCoupon.setDonUsePic(coupon.getDonUsePic());
                memberCoupon.setExpiredPic(coupon.getExpiredPic());
                memberCoupon.setToUsePic(coupon.getToUsePic());
                int exipre = 0;
                if (coupon.getAmount() == 1) {
                    exipre = checkCoupon(coupon);
                    memberCoupon.setIsExpire(exipre);
                } else if (coupon.getAmount() == 2) {
                    exipre = checkCoupon1(coupon, memberCoupon.getCreateTime(), 1);
                    memberCoupon.setIsExpire(exipre);
                } else if (coupon.getAmount() == 3) {
                    exipre = checkCoupon1(coupon, memberCoupon.getCreateTime(), 2);
                    memberCoupon.setIsExpire(exipre);
                }
                if (exipre == 2) {
                    exCouponList.add(memberCoupon);
                }
                if (memberCoupon.getStatus() == 0) {
                    alCouponList.add(memberCoupon);
                }
                if (memberCoupon.getStatus() == 1) {
                    noCouponList.add(memberCoupon);
                }
            }
        }
        allMemberCoupon.setAlCouponList(alCouponList);
        allMemberCoupon.setExCouponList(exCouponList);
        allMemberCoupon.setNoCouponList(noCouponList);
        return allMemberCoupon;
    }

    /**
     * 计算优惠券 优惠
     *
     * @param vo
     * @return
     */
    @Override
    public BigDecimal selectUserCouponById(CartMarkingVo vo) {
        EsMemberCoupon memberCoupon = couponMapper.selectById(vo.getMemberCouponId());
        try {
            if (memberCoupon != null) {
                EsShopCoupon coupon = escouponMapper.selectById(memberCoupon.getCouponId());
                if (coupon != null) {
                    // 优惠券类型 2折扣，1现金，3实物券 4 赠品
                    if (coupon.getType() == 1) {
                        return vo.getPayAmount().subtract(coupon.getEnough());
                    } else if (coupon.getType() == 2) {
                        return vo.getPayAmount().multiply(coupon.getEnough()).divide(new BigDecimal(10));
                    } else if (coupon.getType() == 3) {
                        BigDecimal mk = new BigDecimal(0);
                        List<EsShopCouponGoodsMap> goodsMapList = goodsMapMapper.selectList(new QueryWrapper<EsShopCouponGoodsMap>().
                                eq("coupon_id", coupon.getId()).eq("goodslimit", 1));
                        if (goodsMapList != null && goodsMapList.size() > 0) {
                            for (EsShopCart cart : vo.getCartList()) {
                                for (EsShopCouponGoodsMap manjianGoodsMap : goodsMapList) {
                                    if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        mk = mk.add(cart.getPrice());
                                        break;
                                    }
                                }

                            }
                        }
                        return vo.getPayAmount().subtract(mk);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo.getPayAmount();
    }


    /**
     * 手工发券
     *
     * @param vo
     */
    @Transactional
    @Override
    public void sendManualCoupon(CartMarkingVo vo) throws ParseException {
        EsMemberCoupon member = new EsMemberCoupon();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<EsShopCouponManual> list = couponManualMapper.selectList(new QueryWrapper<EsShopCouponManual>().eq("status", 2).eq("message", vo.getScope()));
        for (EsShopCouponManual topup : list) {
            // 活动开启 1，开启，2指定时间，3关闭
            if (topup.getActivityOpen() == 3) {
                continue;
            }
            boolean falg = true;
            Date nowD = new Date();
            if (topup.getActivityOpen() == 2) {
                Date date = sdf.parse(DateUtils.addDateHour(topup.getTime(), 1));
                if (date.getTime() < nowD.getTime()) {
                    falg = false;
                }
            }
            if (falg) {
                if (topup.getSendObject() == 2) {
                    List<EsShopCouponManualUser> userList = manualUserMapper.selectList(new QueryWrapper<EsShopCouponManualUser>().eq("manual_id", topup.getId()));
                    if (userList != null && userList.size() > 0) {
                        List<String> stIdList2 = userList.stream()
                                .map(EsShopCouponManualUser::getUserId).distinct()
                                .collect(Collectors.toList());
                        if (stIdList2.contains(vo.getOpenId())) {
                            addManualCoupon(vo, member, sdf, topup);
                        }
                    }
                } else {
                    addManualCoupon(vo, member, sdf, topup);
                }
            }
        }
    }



    void addManualCoupon(CartMarkingVo vo, EsMemberCoupon member, SimpleDateFormat sdf, EsShopCouponManual topup) {
        List<EsShopCouponNewRule> couponNewRules = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponManualid()));
        for (EsShopCouponNewRule rule : couponNewRules) {
            EsShopCoupon coupon = escouponMapper.selectById(rule.getCouponid());
            Integer coupon3 = 0;
            if (coupon.getStock() > 0) {
                if (coupon.getStock() < topup.getStampsNumber()) {
                    coupon3 = Math.toIntExact(coupon.getStock());
                } else {
                    coupon3 = topup.getStampsNumber();
                }
                for (int i = 0; i < coupon3; i++) {
                    member.setFroms(5);
                    member.setCreateTime(sdf.format(new Date()));
                    member.setCouponId(rule.getCouponid());
                    member.setTitle(rule.getCouponName());
                    member.setStartTime(coupon.getExpiryBeginTime());
                    member.setEndTime(coupon.getExpiryEndTime());
                    member.setUserOpenid(vo.getOpenId());
                    //有效期 1固定，2当前，3次日
                    Date createTime = new Date();
                    if (coupon.getAmount() == 2) {
                        member.setStartTime(createTime);
                        member.setEndTime(this.addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
                    }
                    if (coupon.getAmount() == 3) {
                        member.setStartTime(this.addDays(createTime, 1));
                        member.setEndTime(this.addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
                    }
                    member.setConditions(1);
                    //用户id
                    member.setMemberId(vo.getMemberId());
                    //金额或折扣
                    member.setAmount(coupon.getEnough());
                    member.setShopId((long) 1);
                    member.setType(coupon.getType());
                    //有效期状态
                    member.setEffective(coupon.getStatus());
                    //券说明
                    member.setDescription(coupon.getDescription());
                    member.setStatus(1);
                    couponMapper.insert(member);
                }

                coupon.setStock((coupon.getStock() - coupon3) > 0 ? (coupon.getStock() - coupon3) : 0);
                rule.setInventory(Math.toIntExact((rule.getInventory() - coupon3) > 0 ? (rule.getInventory() - coupon3) : 0l));
                couponNewRuleMapper.update(rule, new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponManualid()));
                escouponMapper.updateById(coupon);
            }
        }
    }

    /**
     * 满额发券
     *
     * @param vo
     */
    @Transactional
    @Override
    public void sendFillFillCoupon(CartMarkingVo vo) {
        EsMemberCoupon member = new EsMemberCoupon();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<EsShopCouponsTopup> list = couponsTopupMapper.selectList(new QueryWrapper<EsShopCouponsTopup>().eq("activity_status", 1).eq("node", vo.getScope()));
        Date nowD = new Date();
        for (EsShopCouponsTopup topup : list) {
            if (topup.getActivityStatus() == 1 && topup.getActivitiesOpen() == 1) {
                if (topup.getStartingTime().getTime() <= nowD.getTime() &&
                        topup.getEndTime().getTime() >= nowD.getTime()) {

                } else {
                    continue;
                }
            }
            if (topup.getActivitiesOpen() == 2) {
                break;
            }
            if (vo.getPayAmount().compareTo(topup.getMeetAmount()) >= 0) {
                List<EsShopCouponNewRule> couponNewRules = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponTopupid()));
                for (EsShopCouponNewRule rule : couponNewRules) {
                    EsShopCoupon coupon = escouponMapper.selectById(rule.getCouponid());
                    if (coupon.getStock() > 0
                            && topup.getInventory() >= topup.getSingleCoupon()) {

                        if (true) {
                            // 不参与发券商品
                            boolean falg = false;
                            if (topup.getCouponGoods() == 2) {
                                List<EsShopCouponsTopupGoods> goodss = couponsTopupGoodsMapper.selectList(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid", topup.getGoodsId()));
                                for (EsShopOrderGoods cart : vo.getShopOrderGoodsList()) {
                                    if (cart.getIsGifts() == 1) {
                                        continue;
                                    }
                                    for (EsShopCouponsTopupGoods manjianGoodsMap : goodss) {
                                        if (manjianGoodsMap.getGoodId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecificationsId())
                                                || manjianGoodsMap.getSpecificationsId().contains(cart.getOptionId() + ""))) {

                                        } else {
                                            addFullCoupon(vo, member, sdf, topup, rule, coupon);
                                            falg = true;
                                            break;

                                        }
                                    }
                                    if (falg) {
                                        break;
                                    }
                                }
                            } else {
                                addFullCoupon(vo, member, sdf, topup, rule, coupon);

                            }
                        }
                    }
                }
            }
        }
    }

    private void addFullCoupon(CartMarkingVo vo, EsMemberCoupon member, SimpleDateFormat sdf, EsShopCouponsTopup topup, EsShopCouponNewRule rule, EsShopCoupon coupon) {
        Integer coupon3 = 0;

        if (coupon.getStock() < topup.getSingleCoupon()) {
            coupon3 = Math.toIntExact(coupon.getStock());
        } else {
            coupon3 = topup.getSingleCoupon();
        }
        for (int i = 0; i < coupon3; i++) {
            member.setFroms(3);
            member.setCreateTime(sdf.format(new Date()));
            member.setCouponId(rule.getCouponid());
            member.setTitle(rule.getCouponName());
            member.setStartTime(coupon.getExpiryBeginTime());
            member.setEndTime(coupon.getExpiryEndTime());
            //有效期 1固定，2当前，3次日
            Date createTime = new Date();
            if (coupon.getAmount() == 2) {
                member.setStartTime(createTime);
                member.setEndTime(this.addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
            }
            if (coupon.getAmount() == 3) {
                member.setStartTime(this.addDays(createTime, 1));
                member.setEndTime(this.addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
            }
            member.setConditions(1);
            //用户id
            member.setMemberId(vo.getMemberId());
            //金额或折扣
            member.setAmount(coupon.getEnough());
            member.setShopId((long) 1);
            member.setType(coupon.getType());
            //有效期状态
            member.setEffective(coupon.getStatus());
            //券说明
            member.setDescription(coupon.getDescription());
            member.setStatus(1);
            couponMapper.insert(member);
        }


        topup.setInventory(Math.toIntExact((topup.getInventory() - coupon3) > 0 ? (topup.getInventory() - coupon3) : 0));
        coupon.setStock((coupon.getStock() - coupon3) > 0 ? (coupon.getStock() - coupon3) : 0);
        rule.setInventory(Math.toIntExact((rule.getInventory() - coupon3) > 0 ? rule.getInventory() - coupon3 : 0l));

        couponNewRuleMapper.update(rule, new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponTopupid()));
        escouponMapper.updateById(coupon);
        couponsTopupMapper.updateById(topup);
    }

    /**
     * 购物发券
     *
     * @param vo
     */
    @Override
    public void sendShopCoupon(CartMarkingVo vo) {
        EsMemberCoupon member = new EsMemberCoupon();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<EsShopCouponShopping> list = couponShoppingMapper.selectList(new QueryWrapper<EsShopCouponShopping>().eq("activities_open", 1).eq("push_id", vo.getScope()));
        Date nowD = new Date();
        for (EsShopCouponShopping topup : list) {
            if (topup.getActivitiesValidity() == 1 && topup.getActivitiesOpen() == 1) {
                if (topup.getStartingTime().getTime() <= nowD.getTime() &&
                        topup.getEndTime().getTime() >= nowD.getTime()) {

                } else {
                    continue;
                }
            }
            if (topup.getActivitiesOpen() == 2) {
                continue;
            }

            List<EsShopCouponNewRule> couponNewRules = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponShopid()));
            for (EsShopCouponNewRule rule : couponNewRules) {
                EsShopCoupon coupon = escouponMapper.selectById(rule.getCouponid());
                if (topup.getRepertory() < topup.getSingleNumber() || topup.getRepertory() > coupon.getStock()) {
                    continue;
                }
                EsMemberCoupon query = new EsMemberCoupon();
                query.setMemberId(vo.getMemberId());
                query.setCouponId(coupon.getId());
                int couponCount = couponMapper.selectCount(new QueryWrapper<>(query));
                int userCount = 0;
                if (ValidatorUtils.notEmpty(topup.getCumulativeNumber()) && topup.getCumulativeNumber() > 0) {
                    if (ValidatorUtils.notEmpty(couponCount) && couponCount >= topup.getCumulativeNumber()) {
                        continue;
                    } else {
                        userCount = topup.getCumulativeNumber() - couponCount;
                    }
                } else {
                    userCount = 999;
                }
                if (coupon.getStock() > 0
                        && topup.getRepertory() >= topup.getSingleNumber()) {

                    if (true) {
                        //参与范围 1.全部,2.指定,3.分类,4.分组
                        if (topup.getScopeId() == 3) {
                            for (EsShopOrderGoods cart : vo.getShopOrderGoodsList()) {
                                if (cart.getIsGifts() == 1) {
                                    continue;
                                }
                                if (ValidatorUtils.empty(topup.getClassification()) || ValidatorUtils.empty(cart.getCategoryId())) {
                                    continue;
                                }
                                if (numJewelsInStones1(cart.getCategoryId(), topup.getClassification()) > 0) {
                                    if (ValidatorUtils.notEmpty(topup.getSingleBuy()) && topup.getSingleBuy() > 0 && cart.getCount() >= topup.getSingleBuy()) {
                                        addShopCoupon(vo, member, sdf, topup, rule, coupon, userCount);
                                        break;
                                    } else if (ValidatorUtils.notEmpty(topup.getCumulativeBuy()) && topup.getCumulativeBuy() > 0) {
                                        Integer count = 0;
                                        if (topup.getActivitiesValidity() == 1) {
                                            count = orderfegin.sumByGoods(cart.getMemberId(), cart.getGoodsId(), topup.getStartingTime(), topup.getEndTime());
                                        } else {
                                            count = orderfegin.sumByGoods(cart.getMemberId(), cart.getGoodsId(), null, null);
                                        }
                                        if (ValidatorUtils.empty(count)) {
                                            count = 0;
                                        }
                                        if (count >= topup.getCumulativeBuy()) {
                                            addShopCoupon(vo, member, sdf, topup, rule, coupon, userCount);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (topup.getScopeId() == 2) {
                            List<EsShopCouponsTopupGoods> goodss = couponsTopupGoodsMapper.selectList(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid", topup.getGoodshopId()));
                            boolean falg = false;
                            for (EsShopCouponsTopupGoods manjianGoodsMap : goodss) {
                                for (EsShopOrderGoods cart : vo.getShopOrderGoodsList()) {
                                    if (cart.getIsGifts() == 1) {
                                        continue;
                                    }
                                    if (manjianGoodsMap.getGoodId().equals(cart.getGoodsId())) {
                                        if (ValidatorUtils.notEmpty(topup.getSingleBuy()) && topup.getSingleBuy() > 0 && cart.getCount() >= topup.getSingleBuy()) {
                                            addShopCoupon(vo, member, sdf, topup, rule, coupon, userCount);
                                            falg = true;
                                            break;
                                        } else if (ValidatorUtils.notEmpty(topup.getCumulativeBuy()) && topup.getCumulativeBuy() > 0) {
                                            Integer count = 0;
                                            if (topup.getActivitiesValidity() == 1) {
                                                count = orderfegin.sumByGoods(cart.getMemberId(), cart.getGoodsId(), topup.getStartingTime(), topup.getEndTime());
                                            } else {
                                                count = orderfegin.sumByGoods(cart.getMemberId(), cart.getGoodsId(), null, null);
                                            }
                                            if (ValidatorUtils.empty(count)) {
                                                count = 0;
                                            }
                                            if (count >= topup.getCumulativeBuy()) {
                                                addShopCoupon(vo, member, sdf, topup, rule, coupon, userCount);
                                                falg = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (falg) {
                                    break;
                                }
                            }
                        } else if (topup.getScopeId() == 4) {
                            for (EsShopOrderGoods cart : vo.getShopOrderGoodsList()) {
                                if (cart.getIsGifts() == 1) {
                                    continue;
                                }
                                if (ValidatorUtils.empty(topup.getGrouping()) || ValidatorUtils.empty(cart.getGroupId())) {
                                    continue;
                                }
                                if (numJewelsInStones1(topup.getGrouping(), cart.getGroupId()) > 0) {
                                    if (ValidatorUtils.notEmpty(topup.getSingleBuy()) && topup.getSingleBuy() > 0 && cart.getCount() >= topup.getSingleBuy()) {
                                        addShopCoupon(vo, member, sdf, topup, rule, coupon, userCount);
                                        break;
                                    } else if (ValidatorUtils.notEmpty(topup.getCumulativeBuy()) && topup.getCumulativeBuy() > 0) {
                                        Integer count = 0;
                                        if (topup.getActivitiesValidity() == 1) {
                                            count = orderfegin.sumByGoods(cart.getMemberId(), cart.getGoodsId(), topup.getStartingTime(), topup.getEndTime());
                                        } else {
                                            count = orderfegin.sumByGoods(cart.getMemberId(), cart.getGoodsId(), null, null);
                                        }
                                        if (ValidatorUtils.empty(count)) {
                                            count = 0;
                                        }
                                        if (count >= topup.getCumulativeBuy()) {
                                            addShopCoupon(vo, member, sdf, topup, rule, coupon, userCount);
                                            break;
                                        }
                                    }
                                }
                            }

                        } else {
                            for (EsShopOrderGoods cart : vo.getShopOrderGoodsList()) {
                                if (cart.getIsGifts() == 1) {
                                    continue;
                                }
                                if (ValidatorUtils.notEmpty(topup.getSingleBuy()) && topup.getSingleBuy() > 0 && cart.getCount() >= topup.getSingleBuy()) {
                                    addShopCoupon(vo, member, sdf, topup, rule, coupon, userCount);
                                    break;
                                } else if (ValidatorUtils.notEmpty(topup.getCumulativeBuy()) && topup.getCumulativeBuy() > 0) {
                                    Integer count = 0;
                                    if (topup.getActivitiesValidity() == 1) {
                                        count = orderfegin.sumByGoods(cart.getMemberId(), cart.getGoodsId(), topup.getStartingTime(), topup.getEndTime());
                                    } else {
                                        count = orderfegin.sumByGoods(cart.getMemberId(), cart.getGoodsId(), null, null);
                                    }
                                    if (ValidatorUtils.empty(count)) {
                                        count = 0;
                                    }
                                    if (count >= topup.getCumulativeBuy()) {
                                        addShopCoupon(vo, member, sdf, topup, rule, coupon, userCount);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private void addShopCoupon(CartMarkingVo vo, EsMemberCoupon member, SimpleDateFormat sdf, EsShopCouponShopping topup, EsShopCouponNewRule rule,
                               EsShopCoupon coupon, Integer userCount) {
        Integer coupon3 = 0;

        if (coupon.getStock() < topup.getSingleNumber()) {
            coupon3 = Math.toIntExact(coupon.getStock());
        } else {
            coupon3 = topup.getSingleNumber();
        }
        if (coupon3 > userCount) {
            coupon3 = userCount;
        }
        for (int i = 0; i < coupon3; i++) {
            member.setFroms(4);
            member.setCreateTime(sdf.format(new Date()));
            member.setCouponId(rule.getCouponid());
            member.setTitle(rule.getCouponName());
            member.setStartTime(coupon.getExpiryBeginTime());
            member.setEndTime(coupon.getExpiryEndTime());
            //有效期 1固定，2当前，3次日
            Date createTime = new Date();
            if (coupon.getAmount() == 2) {
                member.setStartTime(createTime);
                member.setEndTime(this.addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
            }
            if (coupon.getAmount() == 3) {
                member.setStartTime(this.addDays(createTime, 1));
                member.setEndTime(this.addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
            }
            //用户id
            member.setMemberId(vo.getMemberId());
            member.setConditions(1);
            //金额或折扣
            member.setAmount(coupon.getEnough());
            member.setShopId((long) 1);
            member.setType(coupon.getType());
            //有效期状态
            member.setEffective(coupon.getStatus());
            //券说明
            member.setDescription(coupon.getDescription());
            member.setStatus(1);
            couponMapper.insert(member);
        }

        topup.setRepertory(Math.toIntExact((topup.getRepertory() - coupon3) > 0 ? topup.getRepertory() - coupon3 : 0));
        coupon.setStock((coupon.getStock() - coupon3) > 0 ? (coupon.getStock() - coupon3) : 0);
        rule.setInventory(Math.toIntExact((rule.getInventory() - coupon3) > 0 ? rule.getInventory() - coupon3 : 0l));

        couponNewRuleMapper.update(rule, new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponShopid()));
        escouponMapper.updateById(coupon);
        couponShoppingMapper.updateById(topup);
    }

    @Override
    public List<EsMemberCoupon> selectUserMemberCoupon(CartMarkingVo vo) throws ParseException {
        BigDecimal totalAmount = new BigDecimal(0);
        int count = 0;

        for (EsShopCart cart : vo.getCartList()) {
            totalAmount = totalAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
            count = count + cart.getTotal();
        }
        EsMemberCoupon query = new EsMemberCoupon();
        query.setStatus(1);
        query.setMemberId(vo.getMemberId());
        List<EsMemberCoupon> list = couponMapper.selectList(new QueryWrapper<>(query));
        List<EsMemberCoupon> newList = new ArrayList<>();
        for (EsMemberCoupon memberCoupon : list) {
            BigDecimal singAmount = new BigDecimal(0);
            int singCount = 0;
            EsShopCoupon coupon = escouponMapper.selectById(memberCoupon.getCouponId());
            if (coupon != null) {
                memberCoupon.setDonUsePic(coupon.getDonUsePic());
                memberCoupon.setExpiredPic(coupon.getExpiredPic());
                memberCoupon.setToUsePic(coupon.getToUsePic());
                if (coupon.getAmount() == 1) {
                    memberCoupon.setIsExpire(checkCoupon(coupon));
                } else if (coupon.getAmount() == 2) {
                    memberCoupon.setIsExpire(checkCoupon1(coupon, memberCoupon.getCreateTime(), 1));
                } else if (coupon.getAmount() == 3) {
                    memberCoupon.setIsExpire(checkCoupon1(coupon, memberCoupon.getCreateTime(), 2));
                }
                // 优惠券类型 2折扣，1现金，3实物券 4赠品券
                if (coupon.getType() == 3) {
                    List<EsShopCouponGoodsMap> goodsMapList = goodsMapMapper.selectList(new QueryWrapper<EsShopCouponGoodsMap>().
                            eq("coupon_id", coupon.getId()).eq("goodslimit", 1));
                    if (goodsMapList != null && goodsMapList.size() > 0) {
                        boolean falgS = false;
                        for (EsShopCouponGoodsMap manjianGoodsMap : goodsMapList) {
                            for (EsShopCart cart : vo.getCartList()) {
                                if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                        || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                    falgS = true;
                                    break;
                                }
                            }
                            if (falgS) {
                                break;
                            }
                        }
                        if (!falgS) {
                            continue;
                        }
                    }
                }
                if (coupon.getType() == 4) {
                    List<EsShopCouponGoodsMap> goodsMapList = goodsMapMapper.selectList(new QueryWrapper<EsShopCouponGoodsMap>().
                            eq("coupon_id", coupon.getId()).eq("goodslimit", 3));
                    memberCoupon.setGiftGoodsList(goodsMapList);
                }
                List<EsShopCouponRule> couponRuleList = ruleMapper.selectList(new QueryWrapper<EsShopCouponRule>().eq("coupon_id", coupon.getId()));
                memberCoupon.setCoupon(coupon);
                memberCoupon.setCouponRuleList(couponRuleList);
                List<EsShopCouponGoodsMap> goodsMapList = goodsMapMapper.selectList(new QueryWrapper<EsShopCouponGoodsMap>().
                        eq("coupon_id", coupon.getId()).eq("goodslimit", 2));
                if (couponRuleList != null && couponRuleList.size() > 0) {
                    boolean falgT = false;
                    EsShopCouponRule rule = couponRuleList.get(0);

                    //优惠使用限制 1,2
                    if (rule.getCouponLimit() == 2) {
                        if (ValidatorUtils.notEmpty(rule.getMarketingId()) && ValidatorUtils.notEmpty(vo.getMarketingId())) {
                            if (numJewelsInStones(rule.getMarketingId().replaceAll(",", ""), vo.getMarketingId()) > 0) {
                                continue;
                            }
                        }
                    }
                    //商品使用限制1，2，3
                    if (rule.getGoodsLimitedId() == 2) {
                        boolean falg = false;
                        //商品限制类型1,2
                        if (rule.getExtra() == 2) {
                            if (goodsMapList != null && goodsMapList.size() > 0) {
                                for (EsShopCouponGoodsMap manjianGoodsMap : goodsMapList) {
                                    for (EsShopCart cart : vo.getCartList()) {
                                        if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                                || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                            falg = true;
                                            singAmount = singAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                            singCount = singCount + cart.getTotal();
                                        }
                                    }
                                }
                            }
                        } else {
                            for (EsShopCart cart : vo.getCartList()) {
                                if (rule.getClassificationId().contains(cart.getCategoryId())) {
                                    falg = true;
                                    singAmount = singAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                    singCount = singCount + cart.getTotal();
                                }
                            }
                        }
                        if (!falg) {
                            continue;
                        }
                        if (rule.getActivityType() == 2) {
                            if ((rule.getConditions() == 1 && (singAmount.compareTo(rule.getSingleConsumer()) >= 0 && singCount >= rule.getSingleBatch())) ||
                                    (rule.getConditions() == 2 && (singAmount.compareTo(rule.getSingleConsumer()) >= 0 || singCount >= rule.getSingleBatch())) ||
                                    (rule.getConditions() == 0 &&
                                            ((singAmount.compareTo(rule.getSingleConsumer()) >= 0 && rule.getSingleConsumer().compareTo(new BigDecimal(0)) > 0) ||
                                                    (singCount >= rule.getSingleBatch() && rule.getSingleBatch() > 0)))) {
                                falgT = true;
                            }
                            if (!falgT) {
                                continue;
                            }
                        }
                    }
                    if (rule.getGoodsLimitedId() == 1) {
                        if (rule.getActivityType() == 2) {
                            if ((rule.getConditions() == 1 && (totalAmount.compareTo(rule.getSingleConsumer()) >= 0 && count >= rule.getSingleBatch())) ||
                                    (rule.getConditions() == 2 && (totalAmount.compareTo(rule.getSingleConsumer()) >= 0 || count >= rule.getSingleBatch())) ||
                                    (rule.getConditions() == 0 &&
                                            ((totalAmount.compareTo(rule.getSingleConsumer()) >= 0 && rule.getSingleConsumer().compareTo(new BigDecimal(0)) > 0) ||
                                                    (count >= rule.getSingleBatch() && rule.getSingleBatch() > 0)))) {
                                falgT = true;
                            }
                            if (!falgT) {
                                continue;
                            }
                        }
                    }
                    if (rule.getGoodsLimitedId() == 3) {
                        List<Long> ids = new ArrayList<>();
                        boolean falg = false;
                        //商品限制类型1,2
                        if (rule.getExtra() == 2) {
                            if (goodsMapList != null && goodsMapList.size() > 0) {
                                int count1 = 0;
                                for (EsShopCouponGoodsMap manjianGoodsMap : goodsMapList) {
                                    for (EsShopCart cart : vo.getCartList()) {
                                        if (manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                                || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {

                                            ids.add(cart.getId());
                                            count1++;
                                        }
                                    }
                                }
                                for (EsShopCart cart : vo.getCartList()) {
                                    if (!ids.contains(cart.getId())) {
                                        falg = true;
                                        singAmount = singAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                        singCount = singCount + cart.getTotal();
                                    }
                                }

                            }
                        } else {
                            for (EsShopCart cart : vo.getCartList()) {
                                if (!rule.getClassificationId().contains(cart.getCategoryId())) {
                                    falg = true;
                                    singAmount = singAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                    singCount = singCount + cart.getTotal();
                                }
                            }
                        }
                        if (!falg) {
                            continue;
                        }
                        if (rule.getActivityType() == 2) {
                            if ((rule.getConditions() == 1 && (singAmount.compareTo(rule.getSingleConsumer()) >= 0 && singCount >= rule.getSingleBatch())) ||
                                    (rule.getConditions() == 2 && (singAmount.compareTo(rule.getSingleConsumer()) >= 0 || singCount >= rule.getSingleBatch())) ||
                                    (rule.getConditions() == 0 &&
                                            ((singAmount.compareTo(rule.getSingleConsumer()) >= 0 && rule.getSingleConsumer().compareTo(new BigDecimal(0)) > 0) ||
                                                    (singCount >= rule.getSingleBatch() && rule.getSingleBatch() > 0)))) {
                                falgT = true;
                            }

                            if (!falgT) {
                                continue;
                            }
                        }
                    }
                }


            }
            newList.add(memberCoupon);
        }


        return newList;
    }

    /**
     * 判断有没有重复的字符
     *
     * @param J
     * @param S
     * @return
     */
    public int numJewelsInStones(String J, String S) {
        if (ValidatorUtils.empty(J) || ValidatorUtils.empty(S)) {
            return 0;
        }
        return S.replaceAll("[^" + J + "]", "").length();
    }

    public int numJewelsInStones1(String J, String S) {
        if (ValidatorUtils.empty(J) || ValidatorUtils.empty(S)) {
            return 0;
        }
        String a[] = J.split(",");
        String b[] = S.split(",");
        for (int i = 0; i < a.length; i++)//要将数组短的放在外面，这样能减少循环次数
        {
            String str3 = a[i];
            for (int j = 0; j < b.length; j++) {
                String str4 = b[j];
                if (str4.equals(str3)) {
                    return 1;
                }
            }
        }
        return 0;
    }

    private Integer checkCoupon1(EsShopCoupon coupon, String createTime, int type) {
        SimpleDateFormat FORMATER_DATE_YMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date exTime = null;
        Date exTime1 = null;
        try {
            exTime = FORMATER_DATE_YMD.parse(this.addDay(DateUtil.parse(createTime, null), Integer.parseInt(coupon.getTimeInterval())));
            exTime1 = FORMATER_DATE_YMD.parse(this.addDay(DateUtil.parse(createTime, null), 1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (type == 1) {
            if (exTime.after(new Date())) {
                return 0;
            }
            return 1;
        } else {
            if (exTime.after(new Date())) {
                return 0;
            }
            if (exTime1.before(new Date())) {
                return 2;
            }
            return 1;
        }

    }

    public static String addDay(Date s, int n) {
        SimpleDateFormat FORMATER_DATE_YMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cd = Calendar.getInstance();
        cd.setTime(s);
        cd.add(5, n);
        return FORMATER_DATE_YMD.format(cd.getTime());
    }

    public static Date addDays(Date s, int n) {

        try {
            SimpleDateFormat FORMATER_DATE_YMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cd = Calendar.getInstance();
            cd.setTime(s);
            cd.add(5, n);
            return FORMATER_DATE_YMD.parse(FORMATER_DATE_YMD.format(cd.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    private int checkCoupon(EsShopCoupon coupon) throws ParseException {
        int isExire = 0;
        if (coupon != null) {
            Date da = new Date();
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            String format = sdf2.format(da);
            Date f = sdf2.parse(format);
            // 未开始
            if (ValidatorUtils.notEmpty(coupon.getStartTime())) {
                String statime = DateUtil.format(coupon.getStartTime(), "HH:mm:ss");
                Date starttime = DateUtil.parse(statime, "HH:mm:ss");
                if (f.getTime() < starttime.getTime()) {
                    return 1;
                }
            }
            // 已过期
            if (ValidatorUtils.notEmpty(coupon.getEndTime())) {
                String entime = DateUtil.format(coupon.getEndTime(), "HH:mm:ss");
                Date endtime = DateUtil.parse(entime, "HH:mm:ss");
                if (f.getTime() > endtime.getTime()) {
                    return 2;
                }
            }
            int week = Weekutils.getWeekofDay(da);
            if (ValidatorUtils.empty(coupon.getWeekdays()) || coupon.getWeekdays().contains(week + "")) {
                if (coupon.getExpiryBeginTime().getTime() > da.getTime()) {
                    return 1;
                }
                if (coupon.getExpiryEndTime().getTime() < da.getTime()) {
                    return 2;
                }
            }
        }
        return isExire;
    }

    @Override
    public boolean lockCoupon(EsMemberCoupon coupon) {
        int n = couponMapper.lockCoupon(coupon);
        return n == 1;
    }

    @Override
    public void useCoupon(long orderId) {
        EsMemberCoupon coupon = couponMapper.selectCouponByOrderId(orderId);
        if (coupon == null) {
            return;
        }
        int n = couponMapper.useCoupon(coupon.getCouponId());
        if (n == 0) {
            throw new BusinessException("use coupon failed. orderId=" + orderId + ", couponId=" + coupon.getCouponId() + ", couponStatus=" + coupon.getStatus());
        }
    }

    @Override
    public void releaseCoupon(long orderId) {
        EsMemberCoupon coupon = couponMapper.selectCouponByOrderId(orderId);
        if (coupon == null) {
            return;
        }
        int n = couponMapper.releaseCoupon(coupon.getCouponId());
        if (n == 0) {
            log.error("release coupon failed. orderId=" + orderId + ", couponId=" + coupon.getCouponId() + ", couponStatus=" + coupon.getStatus());
            throw new BusinessException("释放优惠券失败");
        }
    }

    @Override
    public boolean isCouponUsable(EsMemberCoupon coupon, CouponFilterParam condition) {
        if (!coupon.getMemberId().equals(condition.getAccount())) {
            log.info("[isCouponUsable]account not equal. " + coupon.getMemberId() + "|" + condition.getAccount());
            return false;
        }
        if (coupon.getStatus() != CouponStatus.NORMAL.getValue()) {
            log.info("[isCouponUsable]coupon not usable. couponId=" + coupon.getCouponId() + ", status=" + coupon.getStatus());
            return false;
        }
        int day = Weekutils.getWeekofDay(new Date());
        if (ValidatorUtils.notEmpty(coupon.getWeekdays()) && !coupon.getWeekdays().contains(day + "")) {
            return false;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (coupon.getStartTime().after(now) || coupon.getEndTime().before(now)) {
            log.info("[isCouponUsable]coupon is not active or expired. couponId=" + coupon.getCouponId() + ", getStartTime=" + coupon.getStartTime() + ", getEndTime=" + coupon.getEndTime());
            return false;
        }


       /* List<CouponRule> ruleList = couponRuleMapper.selectCouponRuleList(coupon.getCouponTemplateId());
        if (ruleList == null ||  ruleList.size() == 0) {
            log.info("[isCouponUsable]no rules found. couponId=" + coupon.getCouponId() + ", couponTemplateId=" + coupon.getCouponTemplateId());
        } else {
            for (CouponRule rule : ruleList) {
                CouponRuleHandler filter = SpringContextHolder.getBean(rule.getRuleType().toLowerCase() + "Filter");
                if (!filter.isUsable(rule, condition)) {
                    log.info("[isCouponUsable]rule not pass. couponId=" + coupon.getCouponId() + ", rule=" + rule.getRuleType());
                    return false;
                }
            }
        }

        if (condition.getOrderAmount().compareTo(coupon.getThreshold()) < 0) {
            log.info("[isCouponUsable]not reach threshold. couponId=" + coupon.getCouponId() + ", threshold=" + coupon.getThreshold() + ", orderAmount=" + condition.getOrderAmount());
            return false;
        }*/
        return true;
    }

    @Override
    public void shareCouponDiscount(EsMemberCoupon coupon, List<EsShopOrderGoods> orderItemList) {

        BigDecimal payAmountOrigin = BigDecimal.ZERO;
        for (EsShopOrderGoods orderItem : orderItemList) {
            payAmountOrigin = payAmountOrigin.add(orderItem.getPrice());
        }
        BigDecimal couponAmount = coupon.getAmount();
        if (couponAmount.compareTo(payAmountOrigin) > 0) {
            couponAmount = payAmountOrigin;
        }
        BigDecimal tempSaveAmount = BigDecimal.ZERO;
        for (int i = 0; i < orderItemList.size() - 1; i++) {
            EsShopOrderGoods orderItem = orderItemList.get(i);
            BigDecimal saveAmount = orderItem.getPrice().multiply(couponAmount).divide(payAmountOrigin, 2,
                    BigDecimal.ROUND_DOWN);
            tempSaveAmount = tempSaveAmount.add(saveAmount);
            orderItem.setPrice(orderItem.getPrice().subtract(saveAmount));
        }

        BigDecimal lastSaveAmount = couponAmount.subtract(tempSaveAmount);
        EsShopOrderGoods orderItem = orderItemList.get(orderItemList.size() - 1);
        orderItem.setPrice(orderItem.getPrice().subtract(lastSaveAmount));
    }


    @Override
    public List<Map<String, Object>> selectMemberCoupon(EsMemberCoupon coupon) throws ParseException {
        if (coupon.getCreateTime() != null && !coupon.getCreateTime().equals("")) {
            String substring = coupon.getCreateTime();
            String format2 = substring + " 23:59:59";
            String format = substring + " 00:00:00";
            coupon.setCreateDate(format);
            coupon.setUesdDate(format2);
        }
        if (coupon.getUsedTime() != null && !coupon.getUsedTime().equals("")) {
            String substring = coupon.getUsedTime();
            String format2 = substring + " 23:59:59";
            String format = substring + " 00:00:00";
            coupon.setCreateDate(format);
            coupon.setUesdDate(format2);
        }
        return couponMapper.selectMemberCoupon(coupon);
    }

    @Override
    public List<Map<String, Object>> selectMemberCoupon2() {
        return couponMapper.selectMemberCoupon2();
    }

    //新人发券
    @Override
    public Object saveadd(Long memberId, Integer type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //新人发券---查出coupon_newid
        List<EsShopCouponNew> couponnews = couponNewMapper.couponnew(type);
        Date nowD = new Date();
        for (EsShopCouponNew couponnew : couponnews) {
            if (couponnew.getActivityId() == 1 && couponnew.getStatus() == 1) {
                if (couponnew.getStartingTime().getTime() <= nowD.getTime() &&
                        couponnew.getEndTime().getTime() >= nowD.getTime()) {
                } else {
                    continue;
                }
            }
            if (couponnew.getStatus() == 2) {
                break;
            }
            //新人优惠券---查询对应优惠券
            List<EsShopCouponNewRule> listrule = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", couponnew.getCouponNewid()));
            EsShopCoupon ru = new EsShopCoupon();
            EsShopCouponNewRule newr = new EsShopCouponNewRule();
            for (EsShopCouponNewRule newrule : listrule) {
                EsMemberCoupon member = new EsMemberCoupon();
                EsShopCoupon esShopCoupon = escouponMapper.selectById(newrule.getCouponid());
                if (esShopCoupon != null) {
                    //限制条件
                    List<EsShopCouponRule> coupond = ruleMapper.selectList(new QueryWrapper<EsShopCouponRule>().eq("coupon_id", esShopCoupon.getId()));
                    Integer coupon3 = Math.toIntExact(esShopCoupon.getStock()) - couponnew.getSingleCoupon();
                    if (coupon3 >= 0) {
                        //修改库存数量
                        ru.setStock((long) coupon3);
                        ru.setId(esShopCoupon.getId());
                        escouponMapper.updateById(ru);
                        newr.setInventory(coupon3);
                        couponNewRuleMapper.update(newr, new QueryWrapper<EsShopCouponNewRule>().eq("couponid", esShopCoupon.getId()));
                        //发放获取
                        for (int i = 0; i < couponnew.getSingleCoupon(); i++) {
                            member.setFroms(2);
                            member.setCreateTime(sdf.format(new Date()));
                            member.setCouponId(newrule.getCouponid());
                            member.setTitle(esShopCoupon.getCouponsName());
                            member.setStartTime(esShopCoupon.getExpiryBeginTime());
                            member.setEndTime(esShopCoupon.getExpiryEndTime());
                            //有效期 1固定，2当前，3次日
                            Date createTime = new Date();
                            if (esShopCoupon.getAmount() == 2) {
                                member.setStartTime(createTime);
                                member.setEndTime(this.addDays(createTime, Integer.parseInt(esShopCoupon.getTimeInterval())));
                            }
                            if (esShopCoupon.getAmount() == 3) {
                                member.setStartTime(this.addDays(createTime, 1));
                                member.setEndTime(this.addDays(createTime, Integer.parseInt(esShopCoupon.getTimeInterval())));
                            }
                            //用户id
                            member.setMemberId(memberId);
                            member.setConditions(1);
                            //金额或折扣
                            member.setAmount(esShopCoupon.getEnough());
                            member.setShopId((long) 1);
                            member.setType(esShopCoupon.getType());
                            //满多少消费------门槛
                            if (coupond.size() > 0 && coupond != null) {
                                member.setEnough(coupond.get(0).getSingleConsumer());
                                member.setEnoughtwo(coupond.get(0).getSingleBatch());
                                member.setConditions(coupond.get(0).getConditions());
                            }
                            //有效期状态
                            member.setEffective(esShopCoupon.getStatus());
                            //券说明
                            member.setDescription(esShopCoupon.getDescription());
                            member.setStatus(1);
                            couponMapper.insert(member);
                        }
                    }
                }
            }
        }
        return null;
    }

    //发放记录显示
    @Override
    public Map<String, Object> record() {
        Map<String, Object> map = new HashMap<>();
        EsMemberCoupon couponeff = couponMapper.couponeff();
        EsMemberCoupon couponsta = couponMapper.couponsta();
        EsMemberCoupon couponmember = couponMapper.couponmember();
        map.put("rows", couponeff);
        map.put("rows2", couponsta);
        map.put("rows3", couponmember);
        return map;
    }

    @Override
    public boolean updatecoupon(EsMemberCoupon updaecoupon) {

        couponMapper.updateById(updaecoupon);

        return true;
    }

    @Override
    public List<EsMemberCoupon> selectCountMax(EsMemberCoupon entity) {
        entity.setBeginTime(sdf.format(new Date())+" 00:00:00");
        entity.setEndTimes(sdf.format(new Date())+" 59:59:59");
        return couponMapper.selectCountMax(entity);
    }


}
