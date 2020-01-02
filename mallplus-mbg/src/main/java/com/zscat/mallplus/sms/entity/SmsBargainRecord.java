package com.zscat.mallplus.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 砍价免单记录表
 * </p>
 *
 * @author zscat
 * @since 2019-12-25
 */
@Setter
@Getter
@TableName("sms_bargain_record")
public class SmsBargainRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    List<SmsBargainMember> list;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 收货人
     */
    private String name;
    private String shareimg;
    private String spreadpic;
    /**
     * 状态 0:砍价中 1:砍价成功 2:逾期失效 3:生成订单
     */
    private Integer status;
    @TableField("store_id")
    private Integer storeId;
    @TableField("bargin_id")
    private Long barginId;
    @TableField("order_id")
    private Long orderId;
}
