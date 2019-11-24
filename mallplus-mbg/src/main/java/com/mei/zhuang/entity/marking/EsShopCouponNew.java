package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 新人发券
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-13
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupon_new")
public class EsShopCouponNew extends Model<EsShopCouponNew> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)     private Long id;

    /**
     * 单次推券数量
     */
    @TableField("single_coupon")
    private Integer singleCoupon;
    /**
     * 推券节点1,进店2下单,3支付
     */
    @TableField("vouchers_node")
    private Integer vouchersNode;
    /**
     * 活动有效期1,开启 2关闭
     */
    @TableField("activity_id")
    private Integer activityId;
    /**
     * 活动起始时间
     */
    @TableField("starting_time")
    private Date startingTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 1开启,2关闭
     */
    private Integer status;
    @TableField(exist = false)
    private String time;
    @TableField(exist = false)
    private List<EsShopCouponNewRule> rulesList;
    @TableField(exist = false)
    private String selectrule;
    //优惠券关联id
    @TableField("coupon_newid")
    private String couponNewid;
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
