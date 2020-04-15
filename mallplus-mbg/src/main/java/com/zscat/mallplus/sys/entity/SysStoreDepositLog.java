package com.zscat.mallplus.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mallplus
 * @date 2020-04-10
 * 门店管理
 */
@Data
@TableName("sys_store_deposit_log")
public class SysStoreDepositLog extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     *
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     *
     **/
    @TableField("update_time")
    private Date updateTime;


    /**
     * 当前余额
     **/
    @TableField("balance")
    private BigDecimal balance;


    /**
     * 收入金额
     **/
    @TableField("credit")
    private BigDecimal credit;


    /**
     * 支出金额
     **/
    @TableField("debit")
    private BigDecimal debit;


    /**
     *
     **/
    @TableField("memo")
    private String memo;


    /**
     *
     **/
    @TableField("type")
    private Integer type;


    /**
     * 类型
     */
    public enum Type {

        /**
         * 预存款充值 1
         */
        RECHARGE,

        /**
         * 预存款调整 2
         */
        ADJUSTMENT,

        /**
         * 订单支付 3
         */
        ORDER_PAYMENT,

        /**
         * 服务支付 4
         */
        SVC_PAYMENT,

        /**
         * 订单退款 5
         */
        ORDER_REFUNDS,

        /**
         * 订单结算 6
         */
        ORDER_SETTLEMENT,

        /**
         * 提现 7
         */
        CASH
    }

}
