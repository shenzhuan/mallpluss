package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 10:55
 * @Description:客户数据前一日占比Vo类
 */
@Data
public class CustDataBeforeVo {


    private double uvCountBeforeOnePerc;//访客数前一日比例

    private double newAddCustCountBeforeOnePerc;//新增客户数前一日比例

//    private double totalCustCountBeforeOnePerc;//累计客户数前一日比例

    private double newAddMemberCountBeforeOnePerc;//新增会员数前一日比例

    private double totalMemberCountBeforeOnePerc;//累计会员数前一日比例

    private double totalMemberToPercBeforeOnePerc;//累计会员占比前一日比例


}
