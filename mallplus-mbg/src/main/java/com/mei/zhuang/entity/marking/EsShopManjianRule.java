package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_manjian_rule")
public class EsShopManjianRule extends Model<EsShopManjianRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 类型 1 消费金额 2 购买件数
     */
    @TableField("activity_type")
    private Integer activityType;
    /**
     * 满减id
     */
    @TableField("manjian_id")
    private Long manjianId;

    private Long id;
    @TableField("shop_id")
    private Long shopId;
    /**
     * 总数,总计
     */
    private Integer total;
    //消费满元或件
    @TableField("consumption_amount")
    private BigDecimal consumptionAmount;
    //优惠金额
    @TableField("coupon_amount")
    private BigDecimal couponAmount;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
