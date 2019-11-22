package com.mei.zhuang.controller.member;


import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.service.member.ICustGroupService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.data.customer.*;
import com.mei.zhuang.vo.data.customer.CustPropsParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Tiger
 * @Date: 2019-06-25 15:11
 * @Description:数据分析Controller层
 */
@Slf4j
@Api(value = "客群分析管理", description = "", tags = {"客群分析管理"})
@RestController
@RequestMapping("/api/data/custGroup")
public class CustGroupAnalyzeController {

    @Resource
    private ICustGroupService custGroupService;

    @SysLog(MODULE = "客群分析管理", REMARK = "查询访问数据")
    @ApiOperation("查询访问数据")
    @RequestMapping(value = "/getVisitedData", method = RequestMethod.POST)
    @ResponseBody
    public Object getVisitedData(CustGroupIndexParam param) {
        try {
//            param.setShopId(1l);
            return  custGroupService.getVisitedData(param);
        } catch (Exception e) {
            log.error("查询访问数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "客群分析管理", REMARK = "查询客户分析数据")
    @ApiOperation("查询客户分析数据")
    @RequestMapping(value = "/selCustAnalyData", method = RequestMethod.POST)
    @ResponseBody
    public Object selCustAnalyData(CustPropsParam param) {
        try {
            return custGroupService.selCustAnalyData(param);
        } catch (Exception e) {
            log.error("查询访问数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "客群分析管理", REMARK = "成交客户分析")
    @ApiOperation("成交客户分析")
    @RequestMapping(value = "/getTradeScuInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object getTradeScuInfo(CustTradeSuccessParam param) {
        try {
            return new CommonResult().success(custGroupService.getTradeScuInfo(param));
        } catch (Exception e) {
            log.error("成交客户分析：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "客群分析管理", REMARK = "成交客户分析")
    @ApiOperation("成交客户分析数据下载")
    @RequestMapping(value = "/getTradedownload", method = RequestMethod.POST)
    @ResponseBody
    public Object getTradedownload(CustTradeSuccessParam param, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, CustTradeSuccessInfoVo> tradeScuInfo = custGroupService.getTradeScuInfo(param);
            System.out.println(tradeScuInfo.toString()+"客户数据");
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("成交客户分析数据列表");
            // 新增数据行，并且设置单元格数据
            int rowNum = 1;
            String[] headers = {  "客户类型", "成交客户数", "客户数占比","支付订单数","客单价(元)","支付金额(元)","支付金额占比" };
            // headers表示excel表中第一行的表头
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            // 在表中存放查询到的数据放入对应的列
            List<CustTradeSuccessInfoVo>listinfo=new ArrayList<>();
            for(Map.Entry<String, CustTradeSuccessInfoVo> vo:tradeScuInfo.entrySet()){
                listinfo.add(vo.getValue());
            }
            for (int i = 0; i < listinfo.size(); i++) {
                HSSFRow row1 = sheet.createRow(rowNum);
                row1.createCell(0).setCellValue(listinfo.get(i).getCustTypeEn());
                System.out.println(listinfo.get(i).getCustTypeEn());
                row1.createCell(1).setCellValue(listinfo.get(i).getTradeSuccessCount());
                row1.createCell(2).setCellValue(listinfo.get(i).getCustCountToScale());
                row1.createCell(3).setCellValue(listinfo.get(i).getPayCount());
                row1.createCell(4).setCellValue(listinfo.get(i).getUnitPriceByOne().doubleValue());
                row1.createCell(5).setCellValue(listinfo.get(i).getPayAmount().doubleValue());
                row1.createCell(6).setCellValue(listinfo.get(i).getPayAmountToScale());
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
        } catch (Exception e) {
            log.error("成交客户分析：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "客群分析管理", REMARK = "客户趋势图")
    @ApiOperation("客户趋势图")
    @RequestMapping(value = "/getTendencyMapData", method = RequestMethod.POST)
    @ResponseBody
    public Object getTendencyMapData(CustTendencyParam param) {
        try {
//            param.setShopId(1l);
            return new CommonResult().success(custGroupService.getTendencyMapData(param));
        } catch (Exception e) {
            log.error("客户趋势图：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

}
