package com.zscat.mallplus.build.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@TableName("building_community")
public class BuildingCommunity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 小区ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;



    private String pic;

    @Excel(name = "物业编号", orderNum = "1", width = 30)
    @TableField("company_id")
    private Long companyId;

    /**
     * 1申请 2审核失败 3 审核成功
     */
    @Excel(name = "状态", orderNum = "8", width = 30)
    private Integer status;
    @Excel(name = "电话", orderNum = "7", width = 30)
    private String phone;
    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 小区名称
     */
    private String name;

    /**
     * 价格
     */
    private String price;

    /**
     * 所属区县
     */
    @TableField("on_region")
    private String onRegion;

    /**
     * 所属商圈
     */
    @TableField("on_shangquan")
    private String onShangquan;

    /**
     * 小区地址
     */
    private String details;

    /**
     * 建筑类型
     */
    private String jianzhuleixing;

    /**
     * 物业公司
     */
    private String wuyecompany;

    /**
     * 建筑年代
     */
    private String jianzhuniandai;

    /**
     * 物业费
     */
    private String wuyefei;

    /**
     * 开发商
     */
    private String kaifashang;

    /**
     * 容积率
     */
    private String rongjilv;

    /**
     * 绿化率
     */
    private String lvhualv;

    /**
     * 占地面积
     */
    private String zhuandimianj;

    /**
     * 建筑面积
     */
    private String jianzhumianji;

    /**
     * 车位信息
     */
    private String cheweiinfo;

    /**
     * 综合商超
     */
    private String shangchao;

    /**
     * 医院
     */
    private String hosptory;

    /**
     * 邮局
     */
    private String postbank;

    /**
     * 银行
     */
    private String bank;

    /**
     * 其他
     */
    private String others;

    /**
     * 幼儿园
     */
    private String xiaoxue;

    /**
     * 中小学
     */
    private String highschool;

    /**
     * 大学
     */
    private String daxue;

    /**
     * 公交
     */
    private String gongjiao;

    /**
     * 地铁
     */
    private String subway;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    private String memo;

    @TableField(exist = false)
    private double  distance;
}
