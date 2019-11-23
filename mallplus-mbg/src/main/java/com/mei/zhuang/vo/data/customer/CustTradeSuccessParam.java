package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-07-29 17:55
 * @Description:
 */
@Data
public class CustTradeSuccessParam {

    private String startTime;

    private String endTime;

    private Long shopId=1l;

    private String source;

}
