package com.mei.zhuang.vo.data.trade;

import lombok.Data;

@Data
public class TrafficAnalysis {
    private double trafficData; //页面流量数据
    private double bounceRate;//页面跳出率
    private double bounceSequential;//环比
    private double bounceSame;//同比
    private double bounceTotal;//合计
    private double bounceAvg;//均值

    private String Time;
    private String DataTime;


    public TrafficAnalysis(){}

    public TrafficAnalysis(double bounceTotal,double bounceAvg){
        this.bounceTotal=bounceTotal;
        this.bounceAvg=bounceAvg;
    }

}
