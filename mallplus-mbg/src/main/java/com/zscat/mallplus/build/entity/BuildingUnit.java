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
@TableName("building_unit")
public class BuildingUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单元ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 单元编号
     */
    @TableField("unit_num")
    private String unitNum;

    /**
     * 楼ID
     */
    @TableField("floor_id")
    private String floorId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;



}
