package com.mei.zhuang.vo.data.trade;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DataView {

    private double Daviews; //浏览量比例
    private double Davisitors;//访客量
    private double Daorder;//待发货订单
    private BigDecimal DaAmount;//支付金额
    private double DaShop;//购物车人数
    private double DaBynumber;//购买件数
    private double Dasold;//在售商品
    private double Dasoldout;//售罄商品

    private double DaNumber;//付款人数
    private double DaOrder;//付款订单
    private double DaPrice;//客单价
    private double DaRefund;//退款金额

    public DataView(double Daviews,double Davisitors,double Daorder,BigDecimal DaAmount,double DaShop,double DaBynumber,double Dasold,double Dasoldout,double DaNumber
    ,double DaOrder,double DaPrice,double DaRefund){
        this.Daviews=Daviews;
        this.Davisitors=Davisitors;
        this.Daorder=Daorder;
        this.DaAmount=DaAmount;
        this.DaShop=DaShop;
        this.DaBynumber=DaBynumber;
        this.Dasold=Dasold;
        this.Dasoldout=Dasoldout;
        this.DaNumber=DaNumber;
        this.DaOrder=DaOrder;
        this.DaPrice=DaPrice;
        this.DaRefund=DaRefund;
    }






}
