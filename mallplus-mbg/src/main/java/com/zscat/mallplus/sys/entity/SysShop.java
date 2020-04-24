package com.zscat.mallplus.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mallplus
 * @date 2020-03-29
 * 门店管理
 */
@Data
@TableName("sys_shop")
public class SysShop extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 门店名称
     **/
    @TableField("store_name")
    private String storeName;


    /**
     * 门店电话/手机号
     **/
    @TableField("mobile")
    private String mobile;


    /**
     * 门店联系人
     **/
    @TableField("linkman")
    private String linkman;


    /**
     * 门店logo
     **/
    @TableField("logo")
    private String logo;


    /**
     * 门店地区id
     **/
    @TableField("area_id")
    private Integer areaId;


    /**
     * 门店详细地址
     **/
    @TableField("address")
    private String address;


    /**
     *
     **/
    @TableField("coordinate")
    private String coordinate;


    /**
     * 纬度
     **/
    @TableField("latitude")
    private String latitude;


    /**
     * 经度
     **/
    @TableField("longitude")
    private String longitude;


    /**
     * 创建时间
     **/
    @TableField("ctime")
    private Date ctime;


    /**
     * 更新时间
     **/
    @TableField("utime")
    private Date utime;

    @TableField(exist = false)
    private double  distance;
}
