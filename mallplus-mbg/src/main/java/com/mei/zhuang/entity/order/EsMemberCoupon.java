package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.entity.marking.EsShopCoupon;
import com.mei.zhuang.entity.marking.EsShopCouponGoodsMap;
import com.mei.zhuang.entity.marking.EsShopCouponRule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_coupon")
public class EsMemberCoupon extends Model<EsMemberCoupon> {

    private static final long serialVersionUID = 1L;
    /**
     * 赠品券的赠品
     */
    @TableField(exist = false)
    List<EsShopCouponGoodsMap> giftGoodsList;
    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 消费门槛：满足此金额才可使用
     */
    private BigDecimal enough;
    /**
     * 1新人,2满额,3购物 ,4手工获取方式:get自行获取，manual手动发放，6抽奖发卷 其他字段自定
     */
    private Integer froms;
    /**
     * 优惠券类型 1现金2折扣3实物 4赠品
     */
    private Integer type;
    /**
     * 可用的周天：1,2,3...
     */
    private String weekdays;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;
    /**
     * 不可用日期
     */
    @TableField("disable_time_interval")
    private String disableTimeInterval;
    /**
     * 自增
     */
    private Long id;
    /**
     * 最高优惠金额
     */
    @TableField("max_cut")
    private BigDecimal maxCut;
    /**
     * 用户id
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 商城id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;
    /**
     * 可用时间段[["00:00","23:59"],["00:00","23:59"]]
     */
    @TableField("time_interval")
    private String timeInterval;
    /**
     * 券说明
     */
    private String description;
    /**
     * 可用的商品，如全场通用则为NULL
     */
    @TableField("goods_ids")
    private String goodsIds;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 使用时间
     */
    @TableField("used_time")
    private String usedTime;
    /**
     * 券面额（抵扣金额）
     */
    private BigDecimal amount;
    /**
     * 券编号
     */
    @TableField("coupon_id")
    private Long couponId;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 券标题
     */
    private String title;
    /**
     * 0使用,1未使用,2是锁定
     */
    private Integer status;
    @TableField(exist = false)
    private Integer total;
    @TableField(exist = false)
    private String nickname;
    //条件 0单个值，1且，2或 3是无限制条件
    private Integer conditions;
    //件数
    private Integer enoughtwo;
    //有效状态1有效  2，未生效，3失效
    private Integer effective;
    //用户opendid信息
    @TableField("user_openid")
    private String userOpenid;
    //时间条件
    @TableField(exist = false)
    private String createDate;
    @TableField(exist = false)
    private String uesdDate;
    /**
     * 0 正常 1未开始 2已过期
     */
    @TableField(exist = false)
    private Integer isExpire;
    @TableField(exist = false)
    private EsShopCoupon coupon;
    @TableField(exist = false)
    private List<EsShopCouponRule> couponRuleList;
    //未使用图片
    @TableField(exist = false)
    private String donUsePic;
    //已使用图片
    @TableField(exist = false)
    private String toUsePic;
    //已过期图片
    @TableField(exist = false)
    private String expiredPic;
    @TableField("type_id")
    private Long typeId;//活动id
    @TableField(exist = false)
    private String beginTime;
    @TableField(exist = false)
    private String endTimes;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
