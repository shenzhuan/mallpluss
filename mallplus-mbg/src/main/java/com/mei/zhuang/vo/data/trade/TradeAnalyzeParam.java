package com.mei.zhuang.vo.data.trade;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-25 14:42
 * @Description:
 */
@Data
public class TradeAnalyzeParam {

    private String source;//来源

    private Integer dayType;//day的季度

    private Integer dayRange;//day的范围

    private String startTime;//开始时间

    private String endTime;//结束时间

    private Long shopId=1l;//店铺id

}
