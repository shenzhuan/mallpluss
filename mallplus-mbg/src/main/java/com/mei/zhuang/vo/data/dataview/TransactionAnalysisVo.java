package com.mei.zhuang.vo.data.dataview;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/9/12 17:36
 * @Version 1.0
 **/
@Data
public class TransactionAnalysisVo {

    private Date time;//时间
    private BigDecimal paymentAmount = new BigDecimal("0");//支付金额
    private Integer paymentOrderNum;//支付订单数
    private Integer paymentNumber;//付款人数
    private Integer paymentOrder;//付款订单数
    private BigDecimal customerUnitPrice;//客单价（元）
    private BigDecimal refundOrder = new BigDecimal("0");//退款订单金额（元）
}
