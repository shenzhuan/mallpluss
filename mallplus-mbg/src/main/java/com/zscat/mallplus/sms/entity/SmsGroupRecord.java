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
 * <p>
 * </p>
 *
 * @author zscat
 * @since 2019-12-20
 */
@Setter
@Getter
@TableName("sms_group_record")
public class SmsGroupRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 拼团
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 状态  1>>初始状态 2拼团成功
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    @TableField("store_id")
    private Integer storeId;
    /**
     * 状态  1>>初始状态 2定时任务，将订单状态已更新
     */
    @TableField("handle_status")
    private Integer handleStatus;


    @TableField(exist = false)
    private List<SmsGroupMember> list;
}
