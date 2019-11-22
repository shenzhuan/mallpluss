package com.mei.zhuang.service.order;


import com.mei.zhuang.entity.marking.*;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.vo.marking.CodeResult;
import com.mei.zhuang.vo.marking.MjDcVo;
import com.mei.zhuang.vo.order.CartMarkingVo;
import com.mei.zhuang.vo.order.CouponFilterParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(value = "/applet/selectListByMemberId", params = "memberId")
    List<EsMemberCoupon> selectListByMemberId(@RequestParam("memberId") Long memberId);

    @PostMapping(value = "/applet/isCouponUsable")
    boolean isCouponUsable(@RequestParam("couponUser") EsMemberCoupon couponUser, @RequestParam("condition") CouponFilterParam condition);

    @PostMapping(value = "/applet/selectById")
    BigDecimal selectById(@RequestBody CartMarkingVo vo);

    @PostMapping(value = "/applet/shareCouponDiscount")
    void shareCouponDiscount(@RequestParam("memberCoupon") EsMemberCoupon memberCoupon, @RequestParam("orderItemList") List<EsShopOrderGoods> orderItemList);

    @PostMapping(value = "/applet/releaseCoupon", params = "id")
    void releaseCoupon(@RequestParam("id") Long id);


    @PostMapping(value = "/applet/sendNewCoupon")
    void sendNewCoupon(@RequestParam("memberId") Long memberId, @RequestParam("type") int type);

    @ApiOperation("满额发券 订单完成1, 订单支付2")
    @PostMapping(value = "/applet/sendFillFillCoupon")
    public void sendFillFillCoupon(@RequestBody CartMarkingVo vo);

    @ApiOperation("购物发券 订单完成1, 订单支付2")
    @PostMapping(value = "/applet/sendShopCoupon")
    public void sendShopCoupon(@RequestBody CartMarkingVo vo);

    @ApiOperation("手工发券 订单完成1, 订单支付2")
    @PostMapping(value = "/applet/sendManualCoupon")
    public void sendManualCoupon(CartMarkingVo vo) throws ParseException;

    @PostMapping(value = "/applet/matchManjian")
    MjDcVo matchManjian(@RequestBody List<EsShopCart> cartList) throws Exception;

    @PostMapping(value = "/applet/matchDiscount")
    MjDcVo matchDiscount(@RequestBody List<EsShopCart> cartList);

    @ApiOperation("首赠礼")
    @PostMapping(value = "/applet/matchFirstPurchase")
    List<EsShopFirstPurchaseRule> matchFirstPurchase(@RequestBody CartMarkingVo vo) throws Exception;

    @ApiOperation("满赠礼")
    @PostMapping(value = "/applet/matchFullGift")
    List<EsShopFullGift> matchFullGift(@RequestBody List<EsShopCart> cartList) throws Exception;

    @ApiOperation("选赠礼")
    @PostMapping(value = "/applet/ChooseFullGift")
    List<EsShopFullGift> ChooseFullGift(@RequestBody List<EsShopCart> cartList) throws Exception;

    @ApiOperation("满赠礼不符合也显示")
    @PostMapping(value = "/applet/matchFullGift2")
    List<EsShopFullGift> matchFullGift2();


    @ApiOperation("规则商品换购")
    @PostMapping(value = "/applet/matchGoodsRules")
    EsShopGoodsRules matchGoodsRules(@RequestBody List<EsShopCart> cartList);

    @ApiOperation("选中的满减是否可以用")
    @PostMapping(value = "/applet/isManJianUseAble")
    EsShopManjianRule isManJianUseAble(@RequestBody CartMarkingVo vo);

    @ApiOperation("选中的折扣是否可以用")
    @PostMapping(value = "/applet/isDiscountRuleUseAble")
    EsShopDiscountRule isDiscountRuleUseAble(@RequestBody CartMarkingVo vo);

    @ApiOperation("选中的首赠礼是否可以用")
    @PostMapping(value = "/applet/isFirstPurchaseUseAble")
    List<EsShopFirstPurchaseRule> isFirstPurchaseUseAble(@RequestBody CartMarkingVo vo);

    @ApiOperation("选中的满赠礼是否可以用")
    @PostMapping(value = "/applet/isFullGiftGoodsUseAble")
    List<EsShopFullGiftGoodsMap> isFullGiftGoodsUseAble(@RequestBody CartMarkingVo vo);

    @ApiOperation("选中的选赠礼是否可以用")
    @PostMapping(value = "/applet/isChooseGiftGoodsUseAble")
    List<EsShopFullGiftGoodsMap> isChooseGiftGoodsUseAble(@RequestBody CartMarkingVo vo);

    @ApiOperation("单品礼赠")
    @PostMapping(value = "/applet/matchSingleGift")
    List<EsShopSingleGift> matchSingleGift(@RequestBody List<EsShopCart> cartList) throws Exception;

    @ApiOperation("选中的单品礼赠")
    @PostMapping(value = "/applet/isSingleGiftUseAble")
    public List<EsShopSingleGiftGoodsMap> isSingleGiftUseAble(@RequestBody CartMarkingVo vo);

    @ApiOperation("用户所有优惠券")
    @PostMapping(value = "/applet/selectUserMemberCoupon")
    public List<EsMemberCoupon> selectUserMemberCoupon(@RequestBody CartMarkingVo vo);

    @ApiOperation("验证码兑换")
    @PostMapping(value = "/applet/getCodeGoods")
    public CodeResult getCodeGoods(@RequestBody CartMarkingVo vo) throws Exception;

    @ApiOperation("更新验证码状态")
    @PostMapping(value = "/applet/updateCodeStatus")
    public void updateCodeStatus(@RequestParam("code") String code, @RequestParam("status") Integer status);

    @ApiOperation("更新用户优惠券")
    @PostMapping(value = "/applet/updateMemberCoupon")
    public void updateMemberCoupon(@RequestParam("couponId") Long couponId, @RequestParam("orderId") long orderId, @RequestParam("orderNo") String orderNo, @RequestParam("status") Integer status);

    @ApiOperation("查询赠品券得赠品")
    @PostMapping(value = "/applet/selectSendCouponGift")
    public List<EsShopCouponGoodsMap> selectSendCouponGift(@RequestParam("couponId") Long couponId);

    @ApiOperation("好友赠礼查询")
    @PostMapping(value = "/friendGiftlist")
    public EsShopFriendGift list();

    @ApiOperation("送礼卡图")
    @PostMapping(value = "/GiftCard")
    public EsShopFriendGiftCard GiftCard(@RequestParam("id") long id);
}
