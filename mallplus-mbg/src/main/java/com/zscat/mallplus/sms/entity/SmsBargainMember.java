package com.zscat.mallplus.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 拼团记录
 * </p>
 *
 * @author zscat
 * @since 2019-12-25
 */
@Setter
@Getter
@TableName("sms_bargain_member")
public class SmsBargainMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("bargin_record_id")
    private Long barginRecordId;
    @TableField("bargin_id")
    private Long bargainId;

    @TableField("member_id")
    private Long memberId;

    @TableField("create_time")
    private Date createTime;

    private String name;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 所属店铺
     */
    @TableField("store_id")
    private Integer storeId;

    private String pic;

    @TableField("goods_id")
    private Long goodsId;

    private BigDecimal price;


}
