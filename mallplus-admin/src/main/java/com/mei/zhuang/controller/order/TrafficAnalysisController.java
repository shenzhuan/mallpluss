package com.mei.zhuang.controller.order;

import com.arvato.ec.common.vo.data.trade.TradeAnalyzeParam;
import com.arvato.ec.common.vo.data.trade.TrafficAnalysisParam;
import com.arvato.service.order.api.service.TrafficAnalysiService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
    流量分析
 */
@Api(value = "流量设置管理", description = "", tags = {"流量设置管理"})
@Slf4j
@RestController
@RequestMapping("/api/Traffic")
public class TrafficAnalysisController {

    @Resource
    private TrafficAnalysiService analysiService;

    @SysLog(MODULE = "流量分析", REMARK = "流量分析")
    @ApiOperation("流量分析详细数据")
    @PostMapping("/TrafficViewSelect")
    public Object TrafficViewSelect(TradeAnalyzeParam param) throws Exception {
        return analysiService.TrafficList(param);
    }

    @SysLog(MODULE = "流量分析", REMARK = "流量分析")
    @ApiOperation("流量分析详细数据下载")
    @PostMapping("/TrafficViewdownload")
    public Object TrafficViewdownload(TradeAnalyzeParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<TrafficAnalysisParam> trafficAnalysis = analysiService.TrafficList(param);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("商品排行明细数据列表");
        // 新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {  "日期", "浏览量", "访客数","分享访问次数","分享访问人数","商品浏览量","商品访客数" };
        // headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 在表中存放查询到的数据放入对应的列
        for (int i = 0; i < trafficAnalysis.size(); i++) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(trafficAnalysis.get(i).getDataTime());
            row1.createCell(1).setCellValue(trafficAnalysis.get(i).getViews());
            row1.createCell(2).setCellValue(trafficAnalysis.get(i).getVisitors());
            row1.createCell(3).setCellValue(trafficAnalysis.get(i).getSharedVisits());
            row1.createCell(4).setCellValue(trafficAnalysis.get(i).getSharedVisitors());
            row1.createCell(5).setCellValue(trafficAnalysis.get(i).getShopViews());
            row1.createCell(6).setCellValue(trafficAnalysis.get(i).getShopVisitors());
            rowNum++;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sf.format(new Date());
        String fileName = "数据-流量分析详细" + date + ".xls";// 设置要导出的文件的名字
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition",
                "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        response.flushBuffer();
        workbook.write(response.getOutputStream());

        return new CommonResult().success("下载成功");
    }

    @SysLog(MODULE = "流量分析", REMARK = "流量分析")
    @ApiOperation("流量分头部数据")
    @PostMapping("/HeadParam")
    public Object HeadParam(TradeAnalyzeParam param) throws Exception {
        return analysiService.HeadParam(param);
    }
    @SysLog(MODULE = "流量分析", REMARK = "流量分析")
    @ApiOperation("页面跳出率")
    @PostMapping("/TrSelect")
    public Object TrSelect(TradeAnalyzeParam param) throws Exception {
        return analysiService.TrSelect(param);
    }

    @SysLog(MODULE = "流量分析", REMARK = "流量分析")
    @ApiOperation("流量数据")
    @PostMapping("/TrafficData")
    public Object TrafficData(TradeAnalyzeParam param) throws Exception {
        return analysiService.TrafficData(param);
    }

}
