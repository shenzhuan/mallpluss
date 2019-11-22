package com.mei.zhuang.vo.data.trade;

import lombok.Data;

/**
 * 流量分析
 */
@Data
public class TrafficAnalysisParam {
    private int visitors;//访客数
    private int views;//浏览量
    private int shopVisitors;//商品访客数
    private int shopViews;//商品浏览量
    private int SharedVisitors;//分享访问人数
    private int SharedVisits;//分享访问次数
    private String DataTime;//日期
    private Boolean DisplayBeforeData = false;

    private TrafficAnalysisParamVo paramVo;


}
