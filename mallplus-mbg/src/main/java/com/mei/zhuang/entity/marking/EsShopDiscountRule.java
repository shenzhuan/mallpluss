package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_discount_rule")
public class EsShopDiscountRule extends Model<EsShopDiscountRule> {

    private static final long serialVersionUID = 1L;
    @TableField("activity_type")
    private Integer activityType;

    /**
     * 折扣id
     */
    @TableField("discount_id")
    private Long discountId;

    /**
     * 消费金额
     */
    @TableField("consumption_amount")
    private BigDecimal consumptionAmount;

    /**
     * 优惠折扣
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;
    /**
     * 总计
     */
    @TableField("total ")
    private Integer total;

    @TableField("shop_id")
    private Long shopId=1l;
    @TableField("id")
    private Long id;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
