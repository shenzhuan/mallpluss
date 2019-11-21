package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-07-19 13:59
 * @Description:
 */
@Data
public class CustVisitedDataVo {

    private int uvCount;//访客数

    private int newAddCustCount;//新增客户数

    private int totalCustCount;//累计客户数

    private int newAddMemberCount;//新增会员数

    private int totalMemberCount;//累计会员数

    private double totalMemberToPerc;//累计会员占比

    private Boolean isDisplayBeforeData = false;//是否显示前一日数据
    private CustDataBeforeVo custDataBeforeVo;//客户数据前一日比例数据

//    private Map<String,CustTradeSuccessInfoVo> tradedMap;//成交客户分析

    private String refDate;//关系日期


}
