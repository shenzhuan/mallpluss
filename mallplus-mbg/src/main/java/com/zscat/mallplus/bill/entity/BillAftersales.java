package com.zscat.mallplus.bill.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 退货单表
 * </p>
 *
 * @author zscat
 * @since 2019-09-16
 */
@TableName("bill_aftersales")
public class BillAftersales implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 售后单id
     */
    @TableId("aftersales_id")
    private String aftersalesId;

    /**
     * 订单ID 关联order.id
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 用户ID 关联user.id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 售后类型，1=只退款，2退款退货
     */
    private Boolean type;

    /**
     * 退款金额
     */
    private BigDecimal refund;

    /**
     * 状态 1=未审核 2=审核通过 3=审核拒绝
     */
    private Boolean status;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 卖家备注，如果审核失败了，会显示到前端
     */
    private String mark;

    /**
     * 创建时间
     */
    private Long ctime;

    /**
     * 更新时间
     */
    private Long utime;


    public String getAftersalesId() {
        return aftersalesId;
    }

    public void setAftersalesId(String aftersalesId) {
        this.aftersalesId = aftersalesId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "BillAftersales{" +
        ", aftersalesId=" + aftersalesId +
        ", orderId=" + orderId +
        ", userId=" + userId +
        ", type=" + type +
        ", refund=" + refund +
        ", status=" + status +
        ", reason=" + reason +
        ", mark=" + mark +
        ", ctime=" + ctime +
        ", utime=" + utime +
        "}";
    }
}
