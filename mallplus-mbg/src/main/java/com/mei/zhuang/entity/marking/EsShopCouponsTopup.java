package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 满额发券
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupons_topup")
public class EsShopCouponsTopup extends Model<EsShopCouponsTopup> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;
    /**
     * 满足金额
     */
    @TableField("meet_amount")
    private BigDecimal meetAmount;

    /**
     * 单次推券数量
     */
    @TableField("single_coupon")
    private Integer singleCoupon;
    /**
     * 发券库存
     */
    private Integer inventory;
    /**
     * 推券节点 订单完成1, 订单支付2
     */
    private Integer node;
    /**
     * 不参与发券商品
     */
    @TableField("coupon_goods")
    private Integer couponGoods;
    /**
     * 活动有效期 状态 1，开启 2.关闭
     */
    @TableField("activity_status")
    private Integer activityStatus;
    /**
     * 起始时间
     */
    @TableField("starting_time")
    private Date startingTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    //活动开启 1开启 2关闭
    @TableField("activities_open")
    private Integer activitiesOpen;
    //商品的添加
    @TableField(exist = false)
    private List<EsShopCouponsTopupGoods> couponsTopupGoodsList;
    @TableField(exist = false)
    private String couponGoodsList;
    //优惠券添加
    @TableField(exist = false)
    private List<EsShopCouponNewRule> couponsList;
    @TableField(exist = false)
    private String couponList;
    @TableField(exist = false)
    private String time;
    //关联优惠券id
    @TableField("coupon_topupid")
    private String couponTopupid;
    //关联商品id
    @TableField("goods_id")
    private String goodsId;

    @TableField(exist = false)
    private int total;

    public EsShopCouponsTopup() {
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }


}
