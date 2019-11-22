package com.mei.zhuang.service.order;


import com.mei.zhuang.entity.marking.*;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.vo.marking.CodeResult;
import com.mei.zhuang.vo.marking.MjDcVo;
import com.mei.zhuang.vo.order.CartMarkingVo;
import com.mei.zhuang.vo.order.CouponFilterParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * @Description:会员服务接口
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2018/11/26
 */

public interface MarkingFegin {


    List<EsMemberCoupon> selectListByMemberId(@RequestParam("memberId") Long memberId);


    boolean isCouponUsable(@RequestParam("couponUser") EsMemberCoupon couponUser, @RequestParam("condition") CouponFilterParam condition);


    BigDecimal selectById(@RequestBody CartMarkingVo vo);


    void shareCouponDiscount(@RequestParam("memberCoupon") EsMemberCoupon memberCoupon, @RequestParam("orderItemList") List<EsShopOrderGoods> orderItemList);


    void releaseCoupon(@RequestParam("id") Long id);



    void sendNewCoupon(@RequestParam("memberId") Long memberId, @RequestParam("type") int type);


    public void sendFillFillCoupon(@RequestBody CartMarkingVo vo);


    public void sendShopCoupon(@RequestBody CartMarkingVo vo);


    public void sendManualCoupon(CartMarkingVo vo) throws ParseException;


    MjDcVo matchManjian(@RequestBody List<EsShopCart> cartList) throws Exception;


    MjDcVo matchDiscount(@RequestBody List<EsShopCart> cartList);


    List<EsShopFirstPurchaseRule> matchFirstPurchase(@RequestBody CartMarkingVo vo) throws Exception;


    List<EsShopFullGift> matchFullGift(@RequestBody List<EsShopCart> cartList) throws Exception;


    List<EsShopFullGift> ChooseFullGift(@RequestBody List<EsShopCart> cartList) throws Exception;


    List<EsShopFullGift> matchFullGift2();



    EsShopGoodsRules matchGoodsRules(@RequestBody List<EsShopCart> cartList);


    EsShopManjianRule isManJianUseAble(@RequestBody CartMarkingVo vo);


    EsShopDiscountRule isDiscountRuleUseAble(@RequestBody CartMarkingVo vo);


    List<EsShopFirstPurchaseRule> isFirstPurchaseUseAble(@RequestBody CartMarkingVo vo);


    List<EsShopFullGiftGoodsMap> isFullGiftGoodsUseAble(@RequestBody CartMarkingVo vo);


    List<EsShopFullGiftGoodsMap> isChooseGiftGoodsUseAble(@RequestBody CartMarkingVo vo);


    List<EsShopSingleGift> matchSingleGift(@RequestBody List<EsShopCart> cartList) throws Exception;


     List<EsShopSingleGiftGoodsMap> isSingleGiftUseAble(@RequestBody CartMarkingVo vo);


     List<EsMemberCoupon> selectUserMemberCoupon(@RequestBody CartMarkingVo vo);


     CodeResult getCodeGoods(@RequestBody CartMarkingVo vo) throws Exception;


     void updateCodeStatus(@RequestParam("code") String code, @RequestParam("status") Integer status);


     void updateMemberCoupon(@RequestParam("couponId") Long couponId, @RequestParam("orderId") long orderId, @RequestParam("orderNo") String orderNo, @RequestParam("status") Integer status);


     List<EsShopCouponGoodsMap> selectSendCouponGift(@RequestParam("couponId") Long couponId);


     EsShopFriendGift list();


     EsShopFriendGiftCard GiftCard(@RequestParam("id") long id);
}
