package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_pay_log")
public class EsShopPayLog extends Model<EsShopPayLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 完成时间
     */
    @TableField("finish_time")
    private Date finishTime;
    /**
     * 通知数据
     */
    @TableField("notify_data")
    private String notifyData;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 支付类型
     */
    @TableField("pay_type")
    private Long payType;
    /**
     * 付款id
     */
    @TableField("payment_id")
    private Long paymentId;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    @TableField("out_trade_no")
    private String outTradeNo;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 交易编号
     */
    @TableField("trade_no")
    private String tradeNo;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 错误信息
     */
    @TableField("error_info")
    private String errorInfo;
    private Long id;
    /**
     * 支付金额
     */
    @TableField("pay_price")
    private String payPrice;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
