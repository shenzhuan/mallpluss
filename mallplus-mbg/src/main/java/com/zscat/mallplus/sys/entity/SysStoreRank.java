package com.zscat.mallplus.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mallplus
 * @date 2020-04-11
 * 商户等级
 */
@Data
@TableName("sys_store_rank")
public class SysStoreRank implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     *
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     *
     **/
    @TableField("sort")
    private Integer sort;


    /**
     *
     **/
    @TableField("memo")
    private String memo;


    /**
     *
     **/
    @TableField("name")
    private String name;


    /**
     *
     **/
    @TableField("quantity")
    private Long quantity;


    /**
     *
     **/
    @TableField("service_fee")
    private BigDecimal serviceFee;


}
