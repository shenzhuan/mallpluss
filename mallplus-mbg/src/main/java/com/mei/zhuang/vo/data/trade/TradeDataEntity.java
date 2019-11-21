package com.mei.zhuang.vo.data.trade;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: Tiger
 * @Date: 2019-06-25 15:29
 * @Description:交易设置实体类
 */
@Data
public class TradeDataEntity{

    private BigDecimal payAmount = new BigDecimal("0.00");//支付金额

    private int payCount;//支付订单数 （后台支付 + 实际付款数）

    private int payCountByPeople;//付款人数

    private int actualPayCount;//付款订单数（实际付款数）

    private BigDecimal unitPriceByOne = new BigDecimal("0.00");//客单价

    private BigDecimal refundPrice = new BigDecimal("0.00");//退款订单金额

    private String relationDate;//数据关系日期

}
