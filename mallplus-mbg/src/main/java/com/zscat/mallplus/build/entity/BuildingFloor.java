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
 *
 * </p>
 *
 * @author zscat
 * @since 2019-11-27
 */
@Setter
@Getter
@TableName("building_floor")
public class BuildingFloor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 楼ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("community_id")
    private Long communityId;
    /**
     * 楼编号
     */
    @TableField("floor_num")
    private String floorNum;

    /**
     * 小区楼名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    @TableField("layer_count")
    private Integer layerCount;

    /**
     * 1 有电梯
     */
    private Integer lift;



}
