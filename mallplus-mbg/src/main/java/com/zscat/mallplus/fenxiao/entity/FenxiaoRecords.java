package com.zscat.mallplus.fenxiao.entity;

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
 * @date 2019-12-17
 * 分销记录
 */
@Data
@TableName("fenxiao_records")
public class FenxiaoRecords extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @TableField("order_id")
    private Long orderId;


    @TableField("member_id")
    private Long memberId;
    @TableField("type")
    private Integer type;

    @TableField("invite_id")
    private Long inviteId;
    @TableField("goods_id")
    private Long goodsId;

    @TableField("money")
    private BigDecimal money;


    @TableField("level")
    private String level;


    // 1 支付成功 2 结算成功
    @TableField("status")
    private String status;


    @TableField("create_time")
    private Date createTime;


}
