package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 朋友代付
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_friend_paid")
public class EsShopFriendPaid extends Model<EsShopFriendPaid> {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 求助语句
     */
    @TableField("help_statment")
    private String helpStatment;
    /**
     * 代付金额
     */
    @TableField("paid_money")
    private BigDecimal paidMoney;
    /**
     * 满额
     */
    @TableField("top_up")
    private BigDecimal topUp;

    /**
     * 立减
     */
    private BigDecimal knock;

    private Integer status;//0关闭1开启

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
