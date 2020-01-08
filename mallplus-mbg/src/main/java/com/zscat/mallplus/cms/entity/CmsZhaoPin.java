package com.zscat.mallplus.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mallplus
 * @date 2020-01-06
 * 招聘管理
 */
@Data
@TableName("cms_zhao_pin")
public class CmsZhaoPin implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 姓名
     **/
    @TableField("name")
    private String name;


    /**
     * 年龄
     **/
    @TableField("age")
    private Integer age;

    private Integer status;


    /**
     * 性别
     **/
    @TableField("sex")
    private String sex;


    /**
     * 地址
     **/
    @TableField("address")
    private String address;


    /**
     * 学历
     **/
    @TableField("xueli")
    private String xueli;


    /**
     * 备注
     **/
    @TableField("remark")
    private String remark;


    /**
     * 身份证
     **/
    @TableField("idcard")
    private String idcard;


    /**
     * 身份证照片
     **/
    @TableField("idcardpic")
    private String idcardpic;


    /**
     * 期望薪水
     **/
    @TableField("xinshui")
    private String xinshui;


    /**
     * 爱好
     **/
    @TableField("hobby")
    private String hobby;


    /**
     * 区域编号
     **/
    @TableField("area_id")
    private Long areaId;

    @TableField("create_time")
    private Date createTime;

    private String phone;

    private String zhiwei;
}
