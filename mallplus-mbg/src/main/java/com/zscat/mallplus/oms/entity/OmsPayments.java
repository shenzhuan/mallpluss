package com.zscat.mallplus.oms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 支付方式表
 * </p>
 *
 * @author zscat
 * @since 2019-09-14
 */
@TableName("oms_payments")
public class OmsPayments implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支付类型编码
     */
    private String code;

    /**
     * 支付类型名称
     */
    private String name;

    /**
     * 是否线上支付 1=线上支付 2=线下支付
     */
    @TableField("is_online")
    private Boolean isOnline;

    /**
     * 参数
     */
    private String params;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 支付方式描述
     */
    private String memo;

    /**
     * 启用状态 1=启用 2=停用
     */
    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OmsPayments{" +
        ", id=" + id +
        ", code=" + code +
        ", name=" + name +
        ", isOnline=" + isOnline +
        ", params=" + params +
        ", sort=" + sort +
        ", memo=" + memo +
        ", status=" + status +
        "}";
    }
}
