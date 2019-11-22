package com.mei.zhuang.service.order;

import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.data.trade.TrafficAnalysis;
import com.mei.zhuang.vo.data.trade.TrafficAnalysisParam;

import java.util.List;
import java.util.Map;

/**
 * 流量分析
 */
public interface TrafficAnalysiService {
    //详细数据
    List<TrafficAnalysisParam> TrafficList(TradeAnalyzeParam param) throws Exception;

    //头部数据
    TrafficAnalysisParam HeadParam(TradeAnalyzeParam param) throws Exception;

    //页面跳出率
    Map<String, Object> TrSelect(TradeAnalyzeParam param) throws Exception;
    //流量数据
    List<TrafficAnalysis> TrafficData(TradeAnalyzeParam param) throws Exception;

}
