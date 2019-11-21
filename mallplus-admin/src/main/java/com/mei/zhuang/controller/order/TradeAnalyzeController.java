package com.mei.zhuang.controller.order;

import com.arvato.ec.common.vo.data.trade.TradeAnalyzeParam;
import com.arvato.ec.common.vo.order.ExportParam;
import com.arvato.service.order.api.service.ITradeDataAnalyzeService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.date.DateUtil;
import com.arvato.utils.date.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Auther: Tiger
 * @Date: 2019-06-25 15:11
 * @Description:数据分析Controller层
 */
@Slf4j
@Api(value = "数据分析管理", description = "", tags = {"数据分析管理"})
@RestController
@RequestMapping("/api/data/trade")
public class TradeAnalyzeController {

    @Resource
    private ITradeDataAnalyzeService tradeDataAnalyzeService;

    @SysLog(MODULE = "数据分析管理", REMARK = "查询头部数据")
    @ApiOperation("查询头部数据")
    @RequestMapping(value = "/getHeadData", method = RequestMethod.POST)
    @ResponseBody
    public Object headData(TradeAnalyzeParam param) {
        try {
            return new CommonResult().success(tradeDataAnalyzeService.getHeadData(param));
        } catch (Exception e) {
            log.error("查询头部数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "数据分析管理", REMARK = "查找交易数据趋势图")
    @ApiOperation("查找交易数据趋势图")
    @RequestMapping(value = "/getTradeData", method = RequestMethod.POST)
    @ResponseBody
    public Object tradeData(TradeAnalyzeParam param) {
        try {
            return new CommonResult().success(tradeDataAnalyzeService.getTradeData(param, true));
        } catch (Exception e) {
            log.error("查找交易数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "数据分析管理", REMARK = "查找详细数据")
    @ApiOperation("查找详细数据")
    @RequestMapping(value = "/getDetailData", method = RequestMethod.POST)
    @ResponseBody
    public Object detailData(TradeAnalyzeParam param) {
        try {
            return new CommonResult().success(tradeDataAnalyzeService.getDetailData(param));
        } catch (Exception e) {
            log.error("查找详细数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "数据分析管理", REMARK = "查找详细数据")
    @ApiOperation("导出交易分析详细数据")
    @RequestMapping(value = "/exportDetailData", method = RequestMethod.POST)
    public Object export(TradeAnalyzeParam param,
                         ExportParam exportParam,
                         HttpServletResponse response) {
        try {
            exportParam.setFileName(DateUtils.format(new Date(),DateUtil.YYYY_MM_DD) + "导出交易分析详细数据");
            exportParam.setSheetName("导出交易分析详细数据");
            exportParam.setPath("D:/A");
            exportParam.setHeaders("数据关系日期,支付金额,付款人数,付款订单数,客单价,退款订单金额");
            exportParam.setColumns("relationDate,payAmount,payCount,actualPayCount,unitPriceByOne,refundPrice");

            boolean exportFlag = tradeDataAnalyzeService.exportDetailData(param, exportParam, response);
            if (exportFlag) {
                return new CommonResult().success("导出成功！");
            }
        } catch (Exception e) {
            log.error("查找详细数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


}
