package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.marking.EsShopActivityPrizeMapper;
import com.mei.zhuang.dao.marking.EsShopCouponMapper;
import com.mei.zhuang.entity.marking.*;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.service.marking.*;
import com.mei.zhuang.service.order.MarkingFegin;
import com.mei.zhuang.service.order.MembersFegin;
import com.mei.zhuang.vo.marking.CodeResult;
import com.mei.zhuang.vo.marking.MjDcVo;
import com.mei.zhuang.vo.order.CartMarkingVo;
import com.mei.zhuang.vo.order.CouponFilterParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MarkingFeiginImpl implements MarkingFegin {

    @Resource
    private MemberCouponService memberCouponService;
    @Resource
    private ManJianService manJianService;
    @Resource
    private DiscountService discountService;
    @Resource
    private FirstPurchaseService firstPurchaseService;
    @Resource
    private FullGiftService fullGiftService;
    @Resource
    private SingleGiftService singleGiftService;
    @Resource
    private RulesSpecService rulesService;
    @Resource
    private CodeGiftService codeGiftService;
    @Resource
    private EsShopFriendGiftService giftService;
    @Resource
    private EsShopActivityService esShopActivityService;
    @Resource
    private EsShopActivityPrizeMapper esShopActivityPrizeMapper;
    @Resource
    private EsMemberActivatyRecordService esMemberActivatyRecordService;
    @Resource
    private MembersFegin membersFegin;
    @Resource
    private EsShopShareService esShopShareService;
    @Resource
    private EsShopCouponMapper esShopCouponMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public List<EsMemberCoupon> selectListByMemberId(@RequestParam("memberId") Long memberId) {
        return memberCouponService.list(new QueryWrapper<EsMemberCoupon>().eq("member_id", memberId));
    }


    @Override
    public List<EsMemberCoupon> selectUserMemberCoupon(CartMarkingVo vo) {
        return memberCouponService.selectUserMemberCoupon(vo);
    }

    @Override
    public boolean isCouponUsable(@RequestParam("couponUser") EsMemberCoupon couponUser, @RequestParam("condition") CouponFilterParam condition) {
        return memberCouponService.isCouponUsable(couponUser, condition);
    }

    @Override
    public List<EsShopCouponGoodsMap> selectSendCouponGift(@RequestParam("couponId") Long couponId) {
        return memberCouponService.selectSendCouponGift(couponId);
    }

    @Override
    public BigDecimal selectById(CartMarkingVo vo) {
        return memberCouponService.selectUserCouponById(vo);
    }

    @Override
    public void updateMemberCoupon(@RequestParam("couponId") Long couponId, @RequestParam("orderId") long orderId, @RequestParam("orderNo") String orderNo, @RequestParam("status") Integer status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EsMemberCoupon coupon = new EsMemberCoupon();
        if (status == 0) {
            coupon.setStatus(0);
            coupon.setOrderNo(orderNo);
            coupon.setOrderId(orderId);
            coupon.setId(couponId);
            coupon.setUsedTime(sdf.format(new Date()));
            coupon.setUesdDate(sdf.format(new Date()));
        } else {
            coupon.setStatus(1);
            coupon.setId(couponId);
            coupon.setUsedTime(null);
            coupon.setUesdDate(null);
        }

        memberCouponService.updateById(coupon);
    }

    @Override
    public void shareCouponDiscount(@RequestParam("memberCoupon") EsMemberCoupon memberCoupon, @RequestParam("orderItemList") List<EsShopOrderGoods> orderItemList) {
        memberCouponService.shareCouponDiscount(memberCoupon, orderItemList);
    }

    @Override
    public void releaseCoupon(@RequestParam("id") Long id) {
        memberCouponService.releaseCoupon(id);
    }


    @Override
    public void sendNewCoupon(@RequestParam("memberId") Long memberId, @RequestParam("type") int type) {
        memberCouponService.saveadd(memberId, type);
    }

    @Override
    public void sendFillFillCoupon(CartMarkingVo vo) {
        memberCouponService.sendFillFillCoupon(vo);
    }

    @Override
    public void sendShopCoupon(CartMarkingVo vo) {
        memberCouponService.sendShopCoupon(vo);
    }

    @Override
    public void sendManualCoupon(CartMarkingVo vo) throws ParseException {
        memberCouponService.sendManualCoupon(vo);
    }

    @Override
    public CodeResult getCodeGoods(CartMarkingVo vo) throws Exception {
        return codeGiftService.getCodeGoods(vo);
    }

    @Override
    public void updateCodeStatus(@RequestParam("code") String code, @RequestParam("status") Integer status) {
        codeGiftService.updateCodeStatus(code, status);
    }

    @Override
    public MjDcVo matchManjian(List<EsShopCart> cartList) throws Exception {
        return manJianService.matchManjian(cartList);
    }

    @Override
    public MjDcVo matchDiscount(List<EsShopCart> cartList) {
        try {
            return discountService.matchDiscount(cartList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EsShopFirstPurchaseRule> matchFirstPurchase(CartMarkingVo vo) throws Exception {
        return firstPurchaseService.matchFirstPurchase(vo);
    }

    @Override
    public List<EsShopFullGift> matchFullGift(List<EsShopCart> cartList) throws Exception {
        return fullGiftService.matchFullGift(cartList);
    }

    @Override
    public List<EsShopFullGift> ChooseFullGift(List<EsShopCart> cartList) throws Exception {
        return fullGiftService.ChooseFullGift(cartList);
    }

    @Override
    public List<EsShopFullGift> matchFullGift2() {
        return fullGiftService.selectrule();
    }

    @Override
    public EsShopGoodsRules matchGoodsRules(List<EsShopCart> cartList) {
        return rulesService.matchGoodsRules(cartList);
    }

    @Override
    public List<EsShopSingleGift> matchSingleGift(List<EsShopCart> cartList) throws Exception {
        return singleGiftService.matchSingleGift(cartList);
    }

    @Override
    public List<EsShopSingleGiftGoodsMap> isSingleGiftUseAble(CartMarkingVo vo) {
        return singleGiftService.isSingleGiftUseAble(vo);
    }

    @Override
    public EsShopManjianRule isManJianUseAble(CartMarkingVo vo) {
        return manJianService.isManJianUseAble(vo);
    }

    @Override
    public EsShopDiscountRule isDiscountRuleUseAble(CartMarkingVo vo) {
        return discountService.isDiscountRuleUseAble(vo);
    }

    @Override
    public List<EsShopFirstPurchaseRule> isFirstPurchaseUseAble(CartMarkingVo vo) {
        return firstPurchaseService.isFirstPurchaseUseAble(vo);
    }

    @Override
    public List<EsShopFullGiftGoodsMap> isFullGiftGoodsUseAble(CartMarkingVo vo) {
        return fullGiftService.isFullGiftGoodsUseAble(vo);
    }

    @Override
    public List<EsShopFullGiftGoodsMap> isChooseGiftGoodsUseAble(CartMarkingVo vo) {
        return fullGiftService.isChooseGiftGoodsUseAble(vo);
    }


    @Override
    public EsShopFriendGift list() {
        try {
            EsShopFriendGift friend = giftService.friend();
            if (friend != null) {
                return friend;
            }
        } catch (Exception e) {
            log.error("好友赠礼查询失败：", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EsShopFriendGiftCard GiftCard(@RequestParam long id) {
        return giftService.giftcard(id);
    }


}
