package com.mei.zhuang.service.member;


import com.mei.zhuang.vo.data.customer.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 14:02
 * @Description: 客群分析业务层
 */
public interface ICustGroupService {

    /**
     * 获得客户分析头部分析数据
     *
     * @param param
     * @return
     */
    Object getVisitedData(CustGroupIndexParam param) throws Exception;


    /**
     * 获取客户属性数据
     *
     * @param param
     * @return
     * @throws Exception
     */
    Object selCustAnalyData(CustPropsParam param) throws Exception;


    Map<String, CustTradeSuccessInfoVo> getTradeScuInfo(CustTradeSuccessParam param);


    /**
     * 获得客户趋势图数据
     *
     * @param param
     * @return
     */
    List<CustTendencyInfoVo> getTendencyMapData(CustTendencyParam param);

    /**
     * 获得客户交易留存分析数据
     *
     * @param param
     * @return
     */
    Object getCustRemainScaleByDate(CustTradeRemainParam param);


}
