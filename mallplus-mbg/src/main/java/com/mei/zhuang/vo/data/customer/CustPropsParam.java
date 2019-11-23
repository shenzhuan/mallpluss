package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-07-19 10:11
 * @Description:
 */
@Data
public class CustPropsParam {

    private String source;//来源

    private Integer dayType;//day的季度

    private Integer dayRange;//day的范围 //1.最近一天 2.最近7天 3.最近一个月

    private String startTime;//开始时间

    private String endTime;//结束时间

    private Long shopId=1l;//店铺id

}
