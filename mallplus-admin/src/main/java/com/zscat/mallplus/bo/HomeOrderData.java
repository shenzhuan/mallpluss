package com.zscat.mallplus.bo;

import com.zscat.mallplus.oms.vo.OrderStstic;
import com.zscat.mallplus.vo.OrderStatusCount;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/2/23 13:12
 * @Description:
 */
@Data
public class HomeOrderData implements Serializable {
    int nowOrderCount = 0; // 今日订单
    BigDecimal nowOrderPay = new BigDecimal(0); //今日销售总额
    int yesOrderCount = 0; // 昨日订单
    BigDecimal yesOrderPay = new BigDecimal(0); //日销售总额
    int qiOrderCount = 0; // 7日订单
    BigDecimal qiOrderPay = new BigDecimal(0); //7日销售总额
    int monthOrderCount = 0; // 本月订单
    BigDecimal monthOrderPay = new BigDecimal(0); //本月销售总额
    int weekOrderCount = 0; // 本月订单
    BigDecimal weekOrderPay = new BigDecimal(0); //本月销售总额
    int yesToatlOrderCount = 0; // 昨日订单
    BigDecimal yesTotalOrderPay = new BigDecimal(0); //日销售总额
    int nowTotalOrderCount = 0; // 今日订单
    BigDecimal nowToatlOrderPay = new BigDecimal(0); //今日销售总额
    private OrderStatusCount orderStatusCount;
    int lastWeekOrderCount = 0; // 上周订单
    BigDecimal lastWeekOrderPay = new BigDecimal(0); //上周销售总额

    int lastMonthOrderCount = 0; // 上月订单
    BigDecimal lastMonthOrderPay = new BigDecimal(0); //上月销售总额

    List<OrderStstic> orderStsticList;
}
