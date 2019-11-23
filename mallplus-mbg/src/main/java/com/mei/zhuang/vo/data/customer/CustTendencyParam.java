package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 10:43
 * @Description: 客户趋势选择参数Vo类
 */
@Data
public class CustTendencyParam {

    private Integer dataType;//1.成交次数 2. 支付订单数 3. 客单价 4.支付金额

    private String startTime;//开始时间

    private String endTime;//结束时间

    private String source;//来源

    private Long shopId=1l;//店铺id


}
