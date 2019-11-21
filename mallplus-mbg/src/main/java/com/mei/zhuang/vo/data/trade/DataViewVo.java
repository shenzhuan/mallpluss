package com.mei.zhuang.vo.data.trade;

import lombok.Data;

import java.math.BigDecimal;

//数据概览整体看板
@Data
public class DataViewVo {
    private int views;//浏览量
    private int visitors;//访客量
    private int sendGoods;//待发货订单数
    private int payNumber;//付款人数
    private int payOrder;//付款订单数
    private int shopNumber;//加购物车人数
    private BigDecimal byamost;//支付金额
    private BigDecimal guestPrice;   //客单价
    private int perNumber; //人均购买件数
    private int saleNumber;//在售商品数
    private int outNumber;//售罄商品数
    private BigDecimal refundOrder;//退款订单金额


    private Boolean setSetIsDisplayBeforeData = false;

    private DataView Dview;



}
