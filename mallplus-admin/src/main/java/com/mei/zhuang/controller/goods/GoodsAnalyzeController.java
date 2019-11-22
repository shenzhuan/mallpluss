package com.mei.zhuang.controller.goods;


import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.service.goods.IGoodsAnalyService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.data.goods.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 17:14
 * @Description:商品分析Controller层
 */
@Slf4j
@Api(value = "商品分析管理", description = "", tags = {"商品分析管理"})
@RestController
@RequestMapping("/api/data/goods")
public class GoodsAnalyzeController {

    @Resource
    private IGoodsAnalyService goodsAnalyService;

    @SysLog(MODULE = "商品分析管理", REMARK = "商品头部访问统计")
    @ApiOperation("商品头部访问统计")
    @RequestMapping(value = "/goodsStatic", method = RequestMethod.POST)
    public Object orderStatic(GoodsAnalyzeParam param) throws Exception {
        return goodsAnalyService.goodsStatic(param);
    }

    @SysLog(MODULE = "商品分析管理", REMARK = "商品趋势图")
    @ApiOperation("商品趋势图")
    @RequestMapping(value = "/goodsTrendMap", method = RequestMethod.POST)
    public Object goodsTrendMap(GoodsTrendMapParam param) throws Exception {
        return new CommonResult().success(goodsAnalyService.goodsTrendMapStatic(param));
    }

    @SysLog(MODULE = "商品分析管理", REMARK = "商品销量排行榜")
    @ApiOperation("商品销量排行榜")
    @RequestMapping(value = "/goodsRankTopBySaleCount", method = RequestMethod.POST)
    public Object goodsRankTopBySaleCount(GoodsRankTopParam param) throws Exception {
        return new CommonResult().success(goodsAnalyService.goodsRankTopBySaleCount(param));
    }

    @SysLog(MODULE = "商品分析管理", REMARK = "商品销量排行榜数据下载")
    @ApiOperation("商品销量排行榜数据下载")
    @RequestMapping(value = "/goodsBySaledownload", method = RequestMethod.POST)
    public Object goodsBySaledownload(GoodsRankTopParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<GoodsRankTopSaleVo> goodsRankTopSaleVos = goodsAnalyService.goodsRankTopBySaleCount(param);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("商品销量排行榜数据列表");
        // 新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"排名", "商品", "销量"};
        // headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 在表中存放查询到的数据放入对应的列
        for (int i = 0; i < goodsRankTopSaleVos.size(); i++) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(goodsRankTopSaleVos.get(i).getTop());
            row1.createCell(1).setCellValue(goodsRankTopSaleVos.get(i).getThumb() + goodsRankTopSaleVos.get(i).getGoodsNameAndOption());
            row1.createCell(2).setCellValue(goodsRankTopSaleVos.get(i).getSaleCount());
            rowNum++;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sf.format(new Date());
        String fileName = "数据-商品销量排行榜" + date + ".xls";// 设置要导出的文件的名字
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition",
                "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        return new CommonResult().success("下载成功");
    }


    @SysLog(MODULE = "商品分析管理", REMARK = "商品支付金额排行榜")
    @ApiOperation("商品支付金额排行榜")
    @RequestMapping(value = "/goodsRankTopByPrice", method = RequestMethod.POST)
    public Object goodsRankTopByPrice(GoodsRankTopParam param) throws Exception {
        return new CommonResult().success(goodsAnalyService.goodsRankTopByPrice(param));
    }

    @SysLog(MODULE = "商品分析管理", REMARK = "商品支付金额排行榜数据下载")
    @ApiOperation("商品支付金额排行榜数据下载")
    @RequestMapping(value = "/goodsByPricedownload", method = RequestMethod.POST)
    public Object goodsByPricedownload(GoodsRankTopParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<GoodsRankTopPayPriceVo> goodsRankTopPayPriceVos = goodsAnalyService.goodsRankTopByPrice(param);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商品支付金额排行榜数据列表");
            // 新增数据行，并且设置单元格数据
            int rowNum = 1;
            String[] headers = {"排名", "商品", "支付金额"};
            // headers表示excel表中第一行的表头
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            // 在表中存放查询到的数据放入对应的列
            for (int i = 0; i < goodsRankTopPayPriceVos.size(); i++) {
                HSSFRow row1 = sheet.createRow(rowNum);
                row1.createCell(0).setCellValue(goodsRankTopPayPriceVos.get(i).getTop());
                row1.createCell(1).setCellValue(goodsRankTopPayPriceVos.get(i).getThumb() + goodsRankTopPayPriceVos.get(i).getGoodsNameAndOption());
                row1.createCell(2).setCellValue(goodsRankTopPayPriceVos.get(i).getTotalPayAmount().doubleValue());
                rowNum++;
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sf.format(new Date());
            String fileName = "数据-商品支付金额排行榜" + date + ".xls";// 设置要导出的文件的名字
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());
            return new CommonResult().success("下载成功");
        } catch (Exception e) {
            log.error("查找详细数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();

    }


    @SysLog(MODULE = "商品分析管理", REMARK = "商品排行明细")
    @ApiOperation("商品排行明细")
    @RequestMapping(value = "/goodsRankTopDetail", method = RequestMethod.POST)
    public Object goodsRankTopDetail(GoodsAnalyzeParam param) throws Exception {
        return new CommonResult().success(goodsAnalyService.goodsRankTopRanking(param));
    }

    @SysLog(MODULE = "商品分析管理", REMARK = "商品排行明细数据下载")
    @ApiOperation("商品排行明细数据下载")
    @RequestMapping(value = "/goodsDetaildownload", method = RequestMethod.POST)
    public Object goodsDetaildownload(GoodsAnalyzeParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
      /*  param.setEndTime(param.getEndTime() + " 23:59:59.999");
        param.setStartTime(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS));*/
            List<EsShopOrderGoods> esShopOrderGoods = goodsAnalyService.goodsRankTopRanking(param);
            System.out.println(esShopOrderGoods + "排行明细数据源");
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商品排行明细数据列表");
            // 新增数据行，并且设置单元格数据
            int rowNum = 1;
            String[] headers = {"商品", "支付件数", "支付金额", "复购人数", "商品访客数", "商品浏览量"};
            // headers表示excel表中第一行的表头
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            // 在表中存放查询到的数据放入对应的列
            for (int i = 0; i < esShopOrderGoods.size(); i++) {
                HSSFRow row1 = sheet.createRow(rowNum);
                row1.createCell(0).setCellValue(null != esShopOrderGoods.get(i).getTitle() ? esShopOrderGoods.get(i).getTitle() : "");
                row1.createCell(1).setCellValue(null != esShopOrderGoods.get(i).getGoodscount() ? esShopOrderGoods.get(i).getGoodscount() : 0);
                row1.createCell(2).setCellValue(esShopOrderGoods.get(i).getTotal().doubleValue());
                row1.createCell(3).setCellValue(esShopOrderGoods.get(i).getPurchaseNumber());
                row1.createCell(4).setCellValue(esShopOrderGoods.get(i).getGoodsUV());
                row1.createCell(5).setCellValue(esShopOrderGoods.get(i).getGooodsPU());
                rowNum++;
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sf.format(new Date());
            String fileName = "数据-商品排行明细" + date + ".xls";// 设置要导出的文件的名字
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("查找详细数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();

    }


}
