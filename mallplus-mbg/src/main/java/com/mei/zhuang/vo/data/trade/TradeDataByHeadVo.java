package com.mei.zhuang.vo.data.trade;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: Tiger
 * @Date: 2019-06-26 16:01
 * @Description:交易数据头部总体数据
 */
@Data
//@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class TradeDataByHeadVo implements Serializable {

    private BigDecimal payAmount = BigDecimal.valueOf(0.0);//支付金额

    private int payCount;//支付订单数 （后台支付 + 实际付款数）

    private int payCountByPeople;//付款人数(唯一U)

    private int actualPayCount;//付款订单数（实际付款数）

    private BigDecimal unitPriceByOne = BigDecimal.valueOf(0.0);//客单价

    private BigDecimal refundPrice = BigDecimal.valueOf(0.0);//退款订单金额


    private BigDecimal newConsumeAmount = BigDecimal.valueOf(0.0);//新客户金额
    private BigDecimal oldConsumeAmount = BigDecimal.valueOf(0.0);//老客户金额
    private double newPayAmountPerc;//新客户支付金额占比
    private double oldPayAmountPerc;//老客户支付金额占比


    private double newPerc;//新客户占比例

    private double oldPerc;//老客户占比例

    private int uv;//访客数

    private int bookOrderCount;//下单人数

    private double uvToBookPerc;//uv转订单转化率

    private double uvToPayPerc;//uv转支付转化率

    private double bookToPayPerc;//订单-》支付转化率

    private Boolean isDisplayBeforeData = false;//是否显示前一日比例数据

    private TradeDataBeforeVo beforeData = null;//前一日比例数据


}
