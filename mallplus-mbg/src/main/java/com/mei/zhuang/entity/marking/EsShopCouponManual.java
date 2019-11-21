package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *      手工发券
 * </p>
 *
 * @author arvato team
 * @since 2019-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupon_manual")
public class EsShopCouponManual extends Model<EsShopCouponManual> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;
    /**
     * 推券数量
     */
    @TableField("stamps_number")
    private Integer stampsNumber;

    //发券数量
    @TableField("send_number")
    private Integer sendNumber;
    /**
     * 发券对象 1,全部用户2，指定用户，3仅会员4，会员等级
     */
    @TableField("send_object")
    private Integer sendObject;
    /**
     * 推券消息 1,订单完成后推送，2订单付款后推送  3.立即推送
     */
    private Integer message;
    /**
     * 活动开启 1，立即开启，2指定时间，3关闭
     */
    @TableField("activity_open")
    private Integer activityOpen;
    /**
     * 状态 1 已完成，2 发送中 3，未开始 4.已停止
     */
    private Integer status;
    //  1.发送 2.停止
    private Integer statusissue;

    @TableField(exist = false)
    private List<EsShopCouponNewRule> newRuleList;
    @TableField(exist = false)
    private String RuleList;
    //关联优惠类型id
    @TableField("coupon_manualid")
    private String couponManualid;
    @TableField(exist = false)
    private String startingTime;
    @TableField(exist = false)
    private String endTime;

    private String time;

    //优惠券类型id
    @TableField(exist = false)
    private Integer couponTypes;
    //指定用户open上传链接
    private String path;
  /*  //对应用户发券
    @TableField("user_openid")
    private String userOpenid;*/
    @TableField(exist = false)
    private Integer total;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
