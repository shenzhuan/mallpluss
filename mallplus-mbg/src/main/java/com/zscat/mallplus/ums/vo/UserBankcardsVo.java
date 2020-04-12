package com.zscat.mallplus.ums.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author zscat
 * @since 2019-09-16
 */
@Data

public class UserBankcardsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    /**
     * CITIC
     */

    private String bank;

    /**
     * 银行缩写
     */
    private String bankCode;

    /**
     * DC
     */
    private String cardType;

    /**
     * 开户行
     */
    private String accountBank;

    /**
     * "储蓄卡
     */
    private String typeName;

    private Integer type;


}
