package com.mei.zhuang.entity.order;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.marking.EsShopFriendGift;
import com.mei.zhuang.entity.marking.EsShopFriendGiftCard;
import com.mei.zhuang.vo.order.OrderGoodsCustMakingInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_goods")
public class EsShopOrderGoods extends Model<EsShopOrderGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 规格选项的类型（1-文字、2-颜色、3-图片）
     */
    @TableField("typeoption")
    private Integer typeoption;

    /**
     * 规格选项的内容（和type一一对应）
     */
    private String typeword;
    /**
     * 商品分类
     */
    @TableField("category_id")
    private String categoryId;
    /**
     * 商品分组
     */
    @TableField("group_id")
    private String groupId;
    /**
     * 退款id
     */
    @TableField("refund_id")
    private Long refundId;
    @TableField("cargo_time")
    private String cargoTime;//收货时间
    /**
     * 标题
     */
    private String title;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 价格变动
     */
    @TableField("price_change")
    private BigDecimal priceChange;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 会员价格
     */
    @TableField("member_price")
    private BigDecimal memberPrice;
    /**
     * 选项标题
     */
    @TableField("option_title")
    private String optionTitle;
    /**
     * 退款状态
     */
    @TableField("refund_status")
    private Integer refundStatus;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 活动包裹
     */
    @TableField("activity_package")
    private String activityPackage;
    /**
     * 商品编码
     */
    @TableField("goods_code")
    private String goodsCode;
    /**
     * 急件
     */
    private String dispatch;
    /**
     * 退款类型
     */
    @TableField("refund_type")
    private Integer refundType;
    /**
     * 会员号
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 选项id
     */
    @TableField("option_id")
    private Long optionId;
    /**
     * 价格（最终的折扣价格）
     */
    private BigDecimal price;
    /**
     * 价格折扣
     */
    @TableField("price_discount")
    private BigDecimal priceDiscount;
    /**
     * 总金额
     */
    private BigDecimal total;
    /**
     * 增信用
     */
    @TableField("add_credit")
    private Integer addCredit;
    /**
     * 加入会员价格  0：否 1：是
     */
    @TableField("is_join_member_price")
    private Integer isJoinMemberPrice;
    /**
     * 来源数据
     */
    @TableField("form_data")
    private String formData;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 初始价格
     */
    @TableField("price_original")
    private BigDecimal priceOriginal;
    /**
     * 价格单位 （单价）
     */
    @TableField("price_unit")
    private BigDecimal priceUnit;
    /**
     * 评论状态
     */
    @TableField("comment_status")
    private Integer commentStatus;
    /**
     * 优惠卷价格
     */
    @TableField("coupon_price")
    private BigDecimal couponPrice;
    /**
     * 单位
     */
    private String unit;
    /**
     * 订单商品状态：0：已发货 1：待发货
     */
    private Integer status;
    /**
     * 商品头像
     */
    private String thumb;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 撤销收货理由
     */
    @TableField("package_cancel_reason")
    private String packageCancelReason;
    private Long uid;
    /**
     * 扣除信用
     */
    @TableField("deduct_credit")
    private Integer deductCredit;
    /**
     * 成员折扣价格
     */
    @TableField("member_discount_price")
    private BigDecimal memberDiscountPrice;

    /**
     * 是否赠品   0:非赠品  1：赠品
     */
    @TableField("is_gifts")
    private Integer isGifts;

    /**
     * 赠礼类型
     * 1 首赠礼 2 满赠礼 3 单品礼赠 4 验证码赠品 5赠品券赠品 6选赠礼
     */
    @TableField("gift_type")
    private Integer giftType;
    /**
     * 商品数量
     */
    @TableField("count")
    private Integer count;

    @TableField(exist = false)
    private EsShopGoods goods;
    /**
     * 规格信息
     */
    /*@TableField("spec_info")
    private String SpecInfo;*/

    //复购人数
    @TableField(exist = false)
    private Integer purchaseNumber = 0;
    //数量
    @TableField(exist = false)
    private Integer goodscount = 0;
    //商品访客数
    @TableField(exist = false)
    private int goodsUV = 0;
    //商品浏览量
    @TableField(exist = false)
    private int gooodsPU = 0;


    /**
     * 购物车id
     */
    @TableField("cart_id")
    private Long cartId;

    @TableField("cust_id")
    private Long custId;//定制服务id
    //送礼备注
    @TableField(exist = false)
    private EsShopOrder order;
    @TableField(exist = false)
    private EsShopFriendGiftCard GiftCardList;

    @TableField(exist = false)
    private EsShopFriendGift FriendGift;
    @TableField(exist = false)
    private OrderGoodsCustMakingInfo makingInfo;

    @Override
    protected Serializable pkVal() {
        return null;
    }


}
