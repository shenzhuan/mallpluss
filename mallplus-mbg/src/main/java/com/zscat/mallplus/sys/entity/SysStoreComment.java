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
 * @date 2020-04-22
 * 商户分类
 */
@Data
@TableName("sys_store_comment")
public class SysStoreComment extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 名称
     **/
    @TableField("name")
    private String name;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 图片
     **/
    @TableField("pic")
    private String pic;


    /**
     * 排序
     **/
    @TableField("sort")
    private Integer sort;


    /**
     * 备注
     **/
    @TableField("memo")
    private String memo;


    /**
     *
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     *
     **/
    @TableField("member_id")
    private Long memberId;


    /**
     * 等级
     **/
    @TableField("star")
    private Integer star;


}
