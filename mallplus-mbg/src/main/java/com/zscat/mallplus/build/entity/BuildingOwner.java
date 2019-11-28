package com.zscat.mallplus.build.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 业主成员表
 * </p>
 *
 * @author zscat
 * @since 2019-11-27
 */
@Setter
@Getter
@TableName("building_owner")
public class BuildingOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业主成员ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业主ID
     */
    @TableField("owner_id")
    private Long ownerId;
    @TableField("room_id")
    private Long roomId;
    /**
     * 业主名称
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 数据状态，详细参考c_status表，0, 在用 1失效
     */
    private String status;

    /**
     * 1 业主本人 2 家庭成员
     */
    private Integer type;



}
