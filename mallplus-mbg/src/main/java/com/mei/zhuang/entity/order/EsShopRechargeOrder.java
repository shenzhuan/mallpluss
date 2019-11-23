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
 * @since 2019-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_recharge_order")
public class EsShopRechargeOrder extends Model<EsShopRechargeOrder> {

    private static final long serialVersionUID = 1L;

    @TableField("member_id")
    private Long memberId;
    @TableField("pay_type")
    private Integer payType;
    @TableField("success_time")
    private Date successTime;
    @TableField("trade_no")
    private String tradeNo;
    private BigDecimal amount;
    @TableField("client_type")
    private String clientType;
    @TableField("create_time")
    private Date createTime;
    private Long id;
    @TableField("shop_id")
    private Long shopId=1l;
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
