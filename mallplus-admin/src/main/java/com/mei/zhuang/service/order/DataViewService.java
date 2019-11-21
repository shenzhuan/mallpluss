package com.mei.zhuang.service.order;

import com.arvato.ec.common.vo.data.trade.DataViewVo;
import com.arvato.ec.common.vo.data.trade.EntityView;
import com.arvato.ec.common.vo.data.trade.TradeAnalyzeParam;
import com.arvato.ec.common.vo.data.trade.TrendView;

import java.util.List;
import java.util.Map;


public interface DataViewService {
    //整体看板
    DataViewVo viewVoList(TradeAnalyzeParam param) throws Exception;
    //实体看板
    EntityView entityList(TradeAnalyzeParam param) throws Exception;

    //订单趋势图
    List<TrendView> TrendList(TradeAnalyzeParam param) throws Exception;

    //浏览量,访客量趋势图
    List<TrendView> visitorsList(TradeAnalyzeParam param) throws Exception;
    //单客价趋势图
    Map<String, Object> unitList(TradeAnalyzeParam param) throws Exception;

    Map<String,Object> rateList(TradeAnalyzeParam param) throws Exception;

    Map<String, Object> PayViewList(TradeAnalyzeParam param) throws  Exception;



}
