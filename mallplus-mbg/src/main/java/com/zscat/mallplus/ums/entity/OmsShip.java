package com.zscat.mallplus.ums.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 配送方式表
 * </p>
 *
 * @author zscat
 * @since 2019-09-16
 */
@TableName("oms_ship")
public class OmsShip implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配送方式名称
     */
    private String name;

    /**
     * 是否货到付款 1=不是货到付款 2=是货到付款
     */
    @TableField("has_cod")
    private Boolean hasCod;

    /**
     * 首重
     */
    private Integer firstunit;

    /**
     * 续重
     */
    private Integer continueunit;

    /**
     * 按地区设置配送费用是否启用默认配送费用 1=启用 2=不启用
     */
    @TableField("def_area_fee")
    private Boolean defAreaFee;

    /**
     * 地区类型 1=全部地区 2=指定地区
     */
    private Boolean type;

    /**
     * 首重费用
     */
    @TableField("firstunit_price")
    private BigDecimal firstunitPrice;

    /**
     * 续重费用
     */
    @TableField("continueunit_price")
    private BigDecimal continueunitPrice;

    /**
     * 配送费用计算表达式
     */
    private String exp;

    /**
     * 物流公司名称
     */
    @TableField("logi_name")
    private String logiName;

    /**
     * 物流公司编码
     */
    @TableField("logi_code")
    private String logiCode;

    /**
     * 是否默认 1=默认 2=不默认
     */
    @TableField("is_def")
    private Boolean isDef;

    /**
     * 配送方式排序 越小越靠前
     */
    private Integer sort;

    /**
     * 状态 1=正常 2=停用
     */
    private Boolean status;

    /**
     * 是否包邮，1包邮，2不包邮
     */
    @TableField("free_postage")
    private Boolean freePostage;

    /**
     * 地区配送费用
     */
    @TableField("area_fee")
    private String areaFee;

    /**
     * 商品总额满多少免运费
     */
    private BigDecimal goodsmoney;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasCod() {
        return hasCod;
    }

    public void setHasCod(Boolean hasCod) {
        this.hasCod = hasCod;
    }

    public Integer getFirstunit() {
        return firstunit;
    }

    public void setFirstunit(Integer firstunit) {
        this.firstunit = firstunit;
    }

    public Integer getContinueunit() {
        return continueunit;
    }

    public void setContinueunit(Integer continueunit) {
        this.continueunit = continueunit;
    }

    public Boolean getDefAreaFee() {
        return defAreaFee;
    }

    public void setDefAreaFee(Boolean defAreaFee) {
        this.defAreaFee = defAreaFee;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public BigDecimal getFirstunitPrice() {
        return firstunitPrice;
    }

    public void setFirstunitPrice(BigDecimal firstunitPrice) {
        this.firstunitPrice = firstunitPrice;
    }

    public BigDecimal getContinueunitPrice() {
        return continueunitPrice;
    }

    public void setContinueunitPrice(BigDecimal continueunitPrice) {
        this.continueunitPrice = continueunitPrice;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getLogiName() {
        return logiName;
    }

    public void setLogiName(String logiName) {
        this.logiName = logiName;
    }

    public String getLogiCode() {
        return logiCode;
    }

    public void setLogiCode(String logiCode) {
        this.logiCode = logiCode;
    }

    public Boolean getDef() {
        return isDef;
    }

    public void setDef(Boolean isDef) {
        this.isDef = isDef;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getFreePostage() {
        return freePostage;
    }

    public void setFreePostage(Boolean freePostage) {
        this.freePostage = freePostage;
    }

    public String getAreaFee() {
        return areaFee;
    }

    public void setAreaFee(String areaFee) {
        this.areaFee = areaFee;
    }

    public BigDecimal getGoodsmoney() {
        return goodsmoney;
    }

    public void setGoodsmoney(BigDecimal goodsmoney) {
        this.goodsmoney = goodsmoney;
    }

    @Override
    public String toString() {
        return "OmsShip{" +
        ", id=" + id +
        ", name=" + name +
        ", hasCod=" + hasCod +
        ", firstunit=" + firstunit +
        ", continueunit=" + continueunit +
        ", defAreaFee=" + defAreaFee +
        ", type=" + type +
        ", firstunitPrice=" + firstunitPrice +
        ", continueunitPrice=" + continueunitPrice +
        ", exp=" + exp +
        ", logiName=" + logiName +
        ", logiCode=" + logiCode +
        ", isDef=" + isDef +
        ", sort=" + sort +
        ", status=" + status +
        ", freePostage=" + freePostage +
        ", areaFee=" + areaFee +
        ", goodsmoney=" + goodsmoney +
        "}";
    }
}
