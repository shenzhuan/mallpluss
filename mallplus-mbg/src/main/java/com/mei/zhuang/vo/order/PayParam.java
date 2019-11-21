package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 15:30
 * @Description:
 */
@Data
public class PayParam {

    private Integer status;//支付开启状态  0:开启， 1：关闭

    private Integer remainStatus;//余额支付状态 0:开启， 1：关闭

    private Long payId;//支付id
}
