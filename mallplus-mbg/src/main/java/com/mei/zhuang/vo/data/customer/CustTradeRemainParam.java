package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 10:44
 * @Description:客户留存参数Vo类
 */
@Data
public class CustTradeRemainParam {

    private String startTime;//开始时间

    private String endTime;//结束时间

    private String source;//来源

    private Integer remainType;//1.月留存   2.周留存


}
