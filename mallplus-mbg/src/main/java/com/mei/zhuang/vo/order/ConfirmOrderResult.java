package com.mei.zhuang.vo.order;


import com.mei.zhuang.entity.marking.EsShopCodeGiftGoodsMap;
import com.mei.zhuang.entity.marking.EsShopFirstPurchaseRule;
import com.mei.zhuang.entity.marking.EsShopFullGift;
import com.mei.zhuang.entity.marking.EsShopSingleGift;
import com.mei.zhuang.entity.order.EsDeliveryAddresser;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.vo.marking.MjDcVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 确认单信息封装
 * https://github.com/shenzhuan/mallplus on 2018/8/30.
 */
@Data
public class ConfirmOrderResult {

    List<EsShopFirstPurchaseRule> firstPurchaseRuleList;
    List<EsShopFullGift> fullGiftList;
    //选赠礼
    List<EsShopFullGift> ChoosGiftList;
    List<EsShopFullGift> fullGiftListtwo;
    private EsDeliveryAddresser defaultAddress;
    //包含优惠信息的购物车信息
    private List<EsShopCart> cartList;
    //用户收货地址列表
    private List<EsDeliveryAddresser> addresserList;
    //用户可用优惠券列表
    // private List<SmsCouponHistoryDetail> couponHistoryDetailList;
    //积分使用规则
    //  private UmsIntegrationConsumeSetting integrationConsumeSetting;
    //会员持有的积分
    private Integer memberIntegration;
    //计算的金额
    private CalcAmount calcAmount;
    private MjDcVo manjianRule;
    private MjDcVo discountRule;
    private List<EsMemberCoupon> memberCouponList;

    private List<EsShopSingleGift> shopSingleGiftList;

    private List<EsShopCodeGiftGoodsMap> codeGiftGoodsMapList;

    public static class CalcAmount {
        //订单商品总金额
        private BigDecimal totalAmount;
        //运费
        private BigDecimal freightAmount;
        //活动优惠
        private BigDecimal promotionAmount;
        //应付金额
        private BigDecimal payAmount;

        private BigDecimal couponAmount;
        private BigDecimal cardAmount;

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public BigDecimal getFreightAmount() {
            return freightAmount;
        }

        public void setFreightAmount(BigDecimal freightAmount) {
            this.freightAmount = freightAmount;
        }

        public BigDecimal getPromotionAmount() {
            return promotionAmount;
        }

        public void setPromotionAmount(BigDecimal promotionAmount) {
            this.promotionAmount = promotionAmount;
        }

        public BigDecimal getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(BigDecimal payAmount) {
            this.payAmount = payAmount;
        }

        public BigDecimal getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(BigDecimal couponAmount) {
            this.couponAmount = couponAmount;
        }

        public BigDecimal getCardAmount() {
            return cardAmount;
        }

        public void setCardAmount(BigDecimal cardAmount) {
            this.cardAmount = cardAmount;
        }
    }


}
