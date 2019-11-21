package com.mei.zhuang.vo.data.trade;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TrendViewVo {
    private double Usequential;//客单价环比
    private double Ucompared;//客单价同比
    private BigDecimal Utotal;//合计
    private BigDecimal Uaverage;//平均
    private double Rsequential;//转换率环比
    private double Rcompared;//转换率同比
    private double Raverage;//平均转换率

    public TrendViewVo(double Usequential,double Ucompared,BigDecimal Utotal,BigDecimal Uaverage){
        this.Usequential=Usequential;
        this.Ucompared=Ucompared;
        this.Utotal=Utotal;
        this.Uaverage=Uaverage;
}
    public TrendViewVo(double Rsequential,double Rcompared,double Raverage){
        this.Rsequential=Rsequential;
        this.Rcompared=Rcompared;
        this.Raverage=Raverage;

    }
}
