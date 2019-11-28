package com.zscat.mallplus.build.entity;

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
 * 小区
 * </p>
 *
 * @author zscat
 * @since 2019-11-27
 */
@Setter
@Getter
@TableName("building_room")
public class BuildingRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房屋ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房屋编号
     */
    @TableField("room_num")
    private String roomNum;

    @TableField("floor_id")
    private Long floorId;
    @TableField("community_id")
    private Long communityId;
    /**
     * 单元ID
     */
    @TableField("unit_id")
    private String unitId;

    /**
     * 层数
     */
    private Integer layer;

    /**
     * 室
     */
    private String section;

    /**
     * 户型
     */
    private String apartment;

    /**
     * 建筑面积
     */
    @TableField("built_up_area")
    private BigDecimal builtUpArea;

    /**
     * 每平米单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

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
     * 数据状态，详细参考c_status表，S 保存，0, 在用 1失效
     */
    @TableField("status_cd")
    private String statusCd;

    /**
     * 房屋状态，如房屋出售等，请查看state 表
     */
    private String state;



}
