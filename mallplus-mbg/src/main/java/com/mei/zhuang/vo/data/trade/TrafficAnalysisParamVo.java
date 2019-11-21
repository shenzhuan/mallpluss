package com.mei.zhuang.vo.data.trade;

import lombok.Data;

@Data
public class TrafficAnalysisParamVo {
    private double visitorsVo;//访客数比例
    private double viewsVo;//浏览量比例
    private double shopVisitorsVo;//商品访客数比例
    private double shopViewsVo;//商品浏览量比例
    private double SharedVisitorsVo;//分享访问人数比例
    private double SharedVisitsVo;//分享访问次数比例

    public TrafficAnalysisParamVo(double visitorsVo,double viewsVo,double shopVisitorsVo,double shopViewsVo,double SharedVisitorsVo,double SharedVisitsVo){
        this.shopViewsVo=viewsVo;
        this.visitorsVo=visitorsVo;
        this.shopViewsVo=shopViewsVo;
        this.shopVisitorsVo=shopVisitorsVo;
        this.SharedVisitsVo=SharedVisitsVo;
        this.SharedVisitorsVo=SharedVisitorsVo;
    }


}
