package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order")
public class EsShopOrder extends Model<EsShopOrder> {

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private int goodsCount;
    @TableField(exist = false)
    private String statusEn;

    /**
     * 付费频道
     */
    @TableField("pay_channel")
    private Integer payChannel;
    /**
     * 订单改价金额
     */
    @TableField("change_price")
    private BigDecimal changePrice;
    /**
     * 商品信息
     */
    @TableField("goods_info")
    private String goodsInfo;
    /**
     * 原件急件价格1
     */
    @TableField("original_dispatch_price")
    private BigDecimal originalDispatchPrice;
    /**
     * 商店id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 地址详情  赋值到这里 ， 1. 方便修改 ，2. 意外情况
     */
    @TableField("address_detail")
    private String addressDetail;
    /**
     * 急件价格
     */
    @TableField("dispatch_price")
    private BigDecimal dispatchPrice;
    /**
     * 会员号
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 原件商品价格
     */
    @TableField("original_goods_price")
    private BigDecimal originalGoodsPrice;
    /**
     * 退款价格
     */
    @TableField("refund_price")
    private BigDecimal refundPrice;
    /**
     * 完成时间
     */
    @TableField("auto_finish_time")
    private Date autoFinishTime;
    /**
     * 买家姓名
     */
    @TableField("member_nickname")
    private String memberNickname;
    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 付款价格
     */
    @TableField("pay_price")
    private BigDecimal payPrice;
    @TableField("money_basic")
    private BigDecimal moneyBasic;
    /**
     * 地址信息
     */
    @TableField("address_info")
    private String addressInfo;
    /**
     * 商品价格(商品累加价格)
     */
    @TableField("goods_price")
    private BigDecimal goodsPrice;
    /**
     * 形式数据
     */
    @TableField("form_data")
    private String formData;
    /**
     * 订单类型 订单类型（1.普通订单 2. 好友赠礼）
     */
    @TableField("order_type")
    private Integer orderType;
    /**
     * 订单状态：0->待付款；1->待发货；2->待收货；3->已完成；->4已退款；->5维权中；->6维权已完成；->7已取消；->8已关闭；->9无效订单；->10已删除->12.待领取
     */
    @TableField("status")
    private Integer status;
    /*
     * 自动关闭时间
     */
    @TableField("auto_close_time")
    private Date autoCloseTime;
    /**
     * 附加价格包裹
     */
    @TableField("extra_price_package")
    private String extraPricePackage;
    /**
     * 完成时间
     */
    @TableField("finish_time")
    private Date finishTime;
    /**
     * 额外折扣条列包裹
     */
    @TableField("extra_discount_rules_package")
    private String extraDiscountRulesPackage;
    /**
     * 付款时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 评论发送
     */
    @TableField("remark_send")
    private String remarkSend;
    /**
     * 车递送内容
     */
    @TableField("auto_delivery_content")
    private String autoDeliveryContent;
    /**
     * 收货人名称
     */
    @TableField("buyer_name")
    private String buyerName;
    /**
     * 城市分配类型
     */
    @TableField("city_distribution_type")
    private Integer cityDistributionType;
    /**
     * 地方省
     */
    @TableField("address_province")
    private String addressProvince;
    /**
     * 会员头像
     */
    @TableField("member_avatar")
    private String memberAvatar;
    /**
     * 付款id
     */
    @TableField("payment_id")
    private Long paymentId;
    /**
     * 初始价格
     */
    @TableField("original_price")
    private BigDecimal originalPrice;
    /**
     * 类型
     */
    @TableField("type")
    private Long type;
    /**
     * 优惠卷id
     */
    @TableField("coupon_id")
    private Long couponId;
    @TableField("id")
    private Long id;
    /**
     * 是否退款
     */
    @TableField("is_refund")
    private Integer isRefund;
    /**
     * 评论结束
     */
    @TableField("remark_close_saler")
    private String remarkCloseSaler;
    /**
     * 地区区域
     */
    @TableField("address_area")
    private String addressArea;
    /**
     * 急派类型
     */
    @TableField("dispatch_type")
    private Long dispatchType;
    /**
     * 成员真实姓名
     */
    @TableField("member_realname")
    private String memberRealname;
    /**
     * 买主备注
     */
    @TableField("remark_buyer")
    private String remarkBuyer;
    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date sendTime;
    /**
     * 找回的零钱急派的价格
     */
    @TableField("change_dispatch_price")
    private BigDecimal changeDispatchPrice;
    /**
     * 删除
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 成员移动
     */
    @TableField("member_mobile")
    private String memberMobile;
    /**
     * 交易id
     */
    @TableField("trade_no")
    private String tradeNo;
    /**
     * 城市地址
     */
    @TableField("address_city")
    private String addressCity;
    /**
     * 发票信息
     */
    @TableField("invoice_info")
    private String invoiceInfo;
    /**
     * 城市分布
     */
    @TableField("is_city_distribution")
    private Integer isCityDistribution;
    /**
     * 买家关闭备注
     */
    @TableField("remark_close_buyer")
    private String remarkCloseBuyer;
    /**
     * 改变价格计数
     */
    @TableField("change_price_count")
    private Long changePriceCount;
    /**
     * 派遣信息
     */
    @TableField("dispatch_info")
    private String dispatchInfo;
    /**
     * 出售的交易
     */
    @TableField("out_trade_no")
    private String outTradeNo;
    /**
     * 支付类型 0:未支付 1：微信 2： 支付宝 3:后台支付
     */
    @TableField("pay_type")
    private Integer payType;
    /**
     * 收货人电话
     */
    @TableField("buyer_mobile")
    private String buyerMobile;
    /**
     * 关闭时间
     */
    @TableField("close_time")
    private Date closeTime;
    /**
     * 关闭类型
     */
    @TableField("close_type")
    private Integer closeType;
    /**
     * 评论状态: 0:开启  1：禁用
     */
    @TableField("comment_status")
    private Integer commentStatus;
    /**
     * 卖家备注
     */
    @TableField("remark_saler")
    private String remarkSaler;
    /**
     * uid
     */
    @TableField("uid")
    private Long uid;
    /**
     * 地址id
     */
    @TableField("address_id")
    private Long addressId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 来源类型 1 小程序
     */
    @TableField("soure_type")
    private Integer soureType;
    /**
     * 评论，备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 快递编号
     */
    @TableField("express_sn")
    private String expressSn;
    /**
     * 是否部分发货标识
     */
    @TableField("is_part_delivery")
    private Integer isPartDelivery;
    /**
     * 配送方式
     */
    @TableField("receive_type")
    private String receiveType;

    /**
     * 订单商品集合(类似售货信息)
     */
    @TableField(exist = false)
    private List<EsShopOrderGoods> orderGoodsList;

    /**
     * 商品信息
     */
   /*@TableField(exist = false)
    private List<EsShopGoods> goodsList;*/

    /**
     * 订单包裹信息
     */
    @TableField(exist = false)
    private List<EsShopOrderPackage> packageList;

    /**
     * 收货地址信息
     */
    /*@TableField(exist = false)
    private EsDeliveryAddresser addresser;*/

    /**
     * 存储的多个商品
     */
    @TableField("goods_ids")
    private String goodsIds;
    /**
     * 赠礼验证码
     */
    private String code;

    //送礼id
    @TableField("gift_id")
    private Long giftId;

    //1.未领取  2.领取
    @TableField("gift_giving")
    private Integer giftGiving;


    // manjianId,ruleId组合
    @TableField("manjian_info")
    private String manjianInfo;
    // discountId,ruleId组合
    @TableField("discount_info")
    private String discountInfo;
    // firstPurchaseId,ruleId组合
    @TableField("firstPurchase_info")
    private String firstpurchaseInfo;
    // fullGiftId,ruleId组合
    @TableField("fullGift_info")
    private String fullgiftInfo;
    @TableField("discount_amount")
    private BigDecimal discountAmount;
    @TableField("manjian_or_discountid")
    private Long manjianOrDiscountid;
    //分享图片
    @TableField(exist = false)
    private String sharePhotos;
    //分享标题
    @TableField(exist = false)
    private String shareTitle;

    @TableField("goods_name")
    private String goodsName;
    /**
     * 快递公司名称 针对非部分发货
     */

    @TableField(exist = false)
    private EsShopOrderBatchSendDetail sendBatchDetail;
    @TableField(exist = false)
    private List<EsShopOrderGoods> gift1 = new ArrayList<>();
    @TableField(exist = false)
    private List<EsShopOrderGoods> gift2 = new ArrayList<>();
    @TableField(exist = false)
    private List<EsShopOrderGoods> gift3 = new ArrayList<>();
    @TableField(exist = false)
    private List<EsShopOrderGoods> gift4 = new ArrayList<>();
    @TableField(exist = false)
    private List<EsShopOrderGoods> gift5 = new ArrayList<>();
    @TableField(exist = false)
    private List<EsShopOrderGoods> gift6 = new ArrayList<>();

    @TableField("cart_ids")
    private String cartIds;//购物车ids
    @TableField(exist = false)
    private BigDecimal custPrice;//刻字价格
    @TableField(exist = false)
    private String beginTime;//开始时间
    @TableField(exist = false)
    private String endTime;//结束时间


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

}
