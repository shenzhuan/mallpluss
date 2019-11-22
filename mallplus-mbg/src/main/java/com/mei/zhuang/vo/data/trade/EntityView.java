package com.mei.zhuang.vo.data.trade;


import lombok.Data;

import java.math.BigDecimal;

//实体看板
@Data
public class EntityView {
    private int payNumber;//付款人数
    private int payProportion;//昨日付款人数
    private int payOrder;//付款订单数
    private int orProportion;//昨日订单
    private int stayOrder;//待付款订单数
    private int deliveryOrder;//待发货订单数
    private double payRate; //转化率 (下单数量/商品浏览量)
    private double payRatio;//转化率比例
    private BigDecimal unitPrice;//客单价
    private BigDecimal unitRatio;//昨日客单价
    private BigDecimal payAmount = BigDecimal.valueOf(0.0);//支付金额
    private BigDecimal paidAmount = BigDecimal.valueOf(0.0);//昨日金额

    private int views;//浏览量
    private int viewsYd;//昨天浏览量
    private int visitors;//访客量
    private int visitorsYd;//昨天访客量
    private int saleNumber;//在售商品数
    private int outNumber;//售罄商品数
    private int houseGoodNumber;// 仓库商品数
    private int customersNumber;//累计客户数

    private String date;
    private String EnTime;   //当前时间


}
