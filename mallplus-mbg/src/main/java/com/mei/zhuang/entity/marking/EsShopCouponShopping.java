package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 购物发券
 * </p>
 * @author arvato team
 * @since 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupon_shopping")
public class EsShopCouponShopping extends Model<EsShopCouponShopping> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 活动开启 1开启，2关闭
     */
    @TableField("activities_open")
    private Integer activitiesOpen;
    /**
     *  参与范围 1.全部,2.指定,3.分类,4.分组
     */
    @TableField("scope_id")
    private Integer scopeId;
    /**
     * 单次购买满
     */
    @TableField("single_buy")
    private Integer singleBuy;
    /**
     * 累计购买满
     */
    @TableField("cumulative_buy")
    private Integer cumulativeBuy;
    /**
     * 单次推券数量
     */
    @TableField("single_number")
    private Integer singleNumber;
    /**
     * 积累数量
     */
    @TableField("cumulative_number")
    private Integer cumulativeNumber;
    /**
     * 发券库存
     */
    private Integer repertory;
    /**
     * 推券节点  订单完成1, 订单支付2
     */
    @TableField("push_id")
    private Integer pushId;
    /**
     * 活动有效期 1开启，2关闭
     */
    @TableField("activities_validity")
    private Integer activitiesValidity;
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


    //商品的添加
    @TableField(exist = false)
    private List<EsShopCouponsTopupGoods> ShopGoodsList;
    @TableField(exist = false)
    private String couponsGoodsList;
    //优惠券添加
    @TableField(exist = false)
    private List<EsShopCouponNewRule> couponShopList;
    @TableField(exist = false)
    private String conponlist;
    @TableField(exist = false)
    private String time;
    //关联优惠券id
    @TableField("coupon_shopid")
    private String couponShopid;
    //关联商品的id
    @TableField("goodshop_id")
    private String goodshopId;
    @TableField(exist = false)
    private Integer total;
    //接收分组和分类
    private String grouping;
    private String classification;
    @TableField("create_time")
    private Date createTime;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
