package com.mei.zhuang.controller.order;

import com.arvato.ec.common.vo.data.trade.TradeAnalyzeParam;
import com.arvato.service.order.api.service.DataViewService;
import com.arvato.utils.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Auther: Tiger
 * @Date: 2019-06-25 15:11
 * @Description:数据概览
 */
@Slf4j
@Api(value = "数据概览管理", description = "", tags = {"数据概览管理"})
@RestController
@RequestMapping("/api/data/dataView")
public class DataViewController {

    @Resource
    private DataViewService viewService;

    @SysLog(MODULE = "数据概览", REMARK = "数据概览")
    @ApiOperation("数据概览整体看板")
    @PostMapping("/DataViewSelect")
    public Object DataViewSelect(TradeAnalyzeParam param) throws Exception {
        return viewService.viewVoList(param);
    }
    @SysLog(MODULE = "数据概览", REMARK = "数据概览")
    @ApiOperation("数据概览实体看板")
    @PostMapping("/EntityViewSelect")
    public Object EntityViewSelect(TradeAnalyzeParam param) throws Exception {
        return viewService.entityList(param);
    }

    @SysLog(MODULE = "数据概览", REMARK = "数据概览")
    @ApiOperation("订单趋势图")
    @PostMapping("/OrderViewSelect")
    public Object OrderViewSelect(TradeAnalyzeParam param) throws Exception {
        return viewService.TrendList(param);
    }
    @SysLog(MODULE = "数据概览", REMARK = "数据概览")
    @ApiOperation("浏览量趋势图")
    @PostMapping("/visitorsList")
    public Object visitorsList(TradeAnalyzeParam param) throws Exception {
        return viewService.visitorsList(param);
    }

    @SysLog(MODULE = "数据概览", REMARK = "数据概览")
    @ApiOperation("客单价趋势图")
    @PostMapping("/unitList")
    public Object unitList(TradeAnalyzeParam param) throws Exception {
        return viewService.unitList(param);
    }
    @SysLog(MODULE = "数据概览", REMARK = "数据概览")
    @ApiOperation("转化率趋势图")
    @PostMapping("/rateList")
    public Object rateList(TradeAnalyzeParam param) throws Exception {
        return viewService.rateList(param);
    }

    @SysLog(MODULE = "数据概览", REMARK = "数据概览")
    @ApiOperation("实体看板趋势图")
    @PostMapping("/PayViewList")
    public Object PayViewList(TradeAnalyzeParam param) throws Exception {
        return viewService.PayViewList(param);
    }

}
