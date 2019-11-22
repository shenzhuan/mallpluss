package com.mei.zhuang.service.order;


import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.data.trade.TradeDataByHeadVo;
import com.mei.zhuang.vo.data.trade.TradeDataEntity;
import com.mei.zhuang.vo.order.ExportParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-06-27 9:27
 * @Description: 交易分析服务类
 */
public interface ITradeDataAnalyzeService {

    TradeDataByHeadVo getHeadData(TradeAnalyzeParam param) throws Exception;

    List<TradeDataEntity> getTradeData(TradeAnalyzeParam param, boolean isAsc) throws Exception;

    List<TradeDataEntity> getDetailData(TradeAnalyzeParam param) throws Exception;

    boolean exportDetailData(TradeAnalyzeParam param, ExportParam exportParam, HttpServletResponse response);
}
