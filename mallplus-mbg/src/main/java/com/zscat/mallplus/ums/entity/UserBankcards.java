package com.zscat.mallplus.ums.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zscat
 * @since 2019-09-16
 */
@TableName("user_bankcards")
public class UserBankcards implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 银行缩写
     */
    @TableField("bank_code")
    private String bankCode;

    /**
     * 账号地区ID
     */
    @TableField("bank_area_id")
    private Integer bankAreaId;

    /**
     * 开户行
     */
    @TableField("account_bank")
    private String accountBank;

    /**
     * 账户名
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 卡号
     */
    @TableField("card_number")
    private String cardNumber;

    /**
     * 银行卡类型: 1=储蓄卡 2=信用卡
     */
    @TableField("card_type")
    private Boolean cardType;

    /**
     * 默认卡 1=默认 2=不默认
     */
    @TableField("is_default")
    private Boolean isDefault;

    /**
     * 创建时间
     */
    private Long ctime;

    /**
     * 更新时间
     */
    private Long utime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Integer getBankAreaId() {
        return bankAreaId;
    }

    public void setBankAreaId(Integer bankAreaId) {
        this.bankAreaId = bankAreaId;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Boolean getCardType() {
        return cardType;
    }

    public void setCardType(Boolean cardType) {
        this.cardType = cardType;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
        return "UserBankcards{" +
        ", id=" + id +
        ", userId=" + userId +
        ", bankName=" + bankName +
        ", bankCode=" + bankCode +
        ", bankAreaId=" + bankAreaId +
        ", accountBank=" + accountBank +
        ", accountName=" + accountName +
        ", cardNumber=" + cardNumber +
        ", cardType=" + cardType +
        ", isDefault=" + isDefault +
        ", ctime=" + ctime +
        ", utime=" + utime +
        "}";
    }
}
