package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-25 14:42
 * @Description: 客户头部参数Vo类
 */
@Data
public class CustGroupIndexParam {

    private String source;//来源

    private Integer dayType;//day的季度

    private Integer dayRange;//day的范围 //1.自然日 2.自然周 3.自然月

    private String startTime;//开始时间

    private String endTime;//结束时间

    private Long shopId;//店铺id

}
