package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("es_shop_order_price_change_log")
public class EsShopOrderPriceChangeLog extends Model<EsShopOrderPriceChangeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * uid
     */
    private Long uid;
    /**
     * 之后的价格
     */
    @TableField("after_price")
    private BigDecimal afterPrice;
    /**
     * 之前的价格
     */
    @TableField("before_price")
    private BigDecimal beforePrice;
    /**
     * 改变的价格
     */
    @TableField("change_price")
    private BigDecimal changePrice;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
