package com.mei.zhuang.vo.data.trade;


import lombok.Data;

@Data
public class TrendView {
    private int payOrder;//支付订单总数
    private int payRefund;//退款订单总数
    private double unitPrice;//客单价
    private int views;//浏览量
    private int visitors;//访客数
    private double conversion;//购买转化率
    private String relationDate;//时间日期
    private String datatime;

    private TrendViewVo viewVo;


}
