package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("es_shop_order_activity")
public class EsShopOrderActivity extends Model<EsShopOrderActivity> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 规则指数
     */
    @TableField("rule_index")
    private Integer ruleIndex;
    /**
     * 商品id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 活动id
     */
    @TableField("activity_id")
    private Long activityId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
