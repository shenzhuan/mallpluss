package com.mei.zhuang.controller.order;

import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.service.order.ShopOrderService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.data.dataview.TransactionAnalysisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/9/12 17:13
 * @Version 1.0
 **/
@Api(value = "数据-交易分析", description = "", tags = {"数据-交易分析"})
@Slf4j
@RestController
@RequestMapping("/api/trannsaction")
public class TransactionAnalysisController {

    @Resource
    private ShopOrderService shopOrderService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @SysLog(MODULE = "数据-交易分析", REMARK = "详细数据")
    @ApiOperation("详细数据")
    @RequestMapping(value = "/dataDetail", method = RequestMethod.POST)
    public Object dataDetail(@RequestParam("beginTime")String beginTime,@RequestParam("endTime")String endTime)  {
        try{
            List<TransactionAnalysisVo> analysisVoList = new ArrayList<TransactionAnalysisVo>();
            Date beTime=sdf.parse(beginTime);
            Date edTime=sdf.parse(endTime);
            List<Date> list =findDates(beTime,edTime);
            for (Date da:list) {
                TransactionAnalysisVo analysisVo = new TransactionAnalysisVo();
                EsShopOrder esShopOrder = new EsShopOrder();
                //1.查询支付金额
                esShopOrder.setStatus(3);
                esShopOrder.setBeginTime(sdf.format(da)+" 00:00:00");
                esShopOrder.setEndTime(sdf.format(da)+" 23:59:59");
                List<EsShopOrder> paymentAmountList = shopOrderService.selCountDateDetail(esShopOrder);
                if(list != null && list.size()>0){
                    for (EsShopOrder order:paymentAmountList) {
                        analysisVo.setPaymentAmount(analysisVo.getPaymentAmount().add(order.getPayPrice()));
                    }
                }else{
                    analysisVo.setPaymentAmount(BigDecimal.valueOf(0.00));
                }
                //2.支付订单数
                analysisVo.setPaymentOrderNum(paymentAmountList.size());
                //3.付款人数
                esShopOrder.setBeginTime(beginTime);
                esShopOrder.setEndTime(endTime);
                List<Integer> listMemberId = shopOrderService.paymentNumber(esShopOrder);
                HashSet h = new HashSet(listMemberId);
                listMemberId.clear();
                listMemberId.addAll(h);
                analysisVo.setPaymentNumber(listMemberId.size());
                //4.付款订单数
                analysisVo.setPaymentOrder(paymentAmountList.size());
                //5.客单价  detail:总金额/总订单量=客单价
                if(analysisVo.getPaymentAmount().equals(BigDecimal.valueOf(0))){
                    analysisVo.setCustomerUnitPrice(BigDecimal.valueOf(0));
                }else{
                    analysisVo.setCustomerUnitPrice(analysisVo.getPaymentAmount().divide(BigDecimal.valueOf(analysisVo.getPaymentOrder()),2, RoundingMode.HALF_DOWN));
                }

                //6.退款订单金额
                esShopOrder = new EsShopOrder();
                esShopOrder.setStatus(4);
                esShopOrder.setBeginTime(sdf.format(da)+" 00:00:00");
                esShopOrder.setEndTime(sdf.format(da)+" 23:59:59");
                List<EsShopOrder> refundOrderList = shopOrderService.selCountDateDetail(esShopOrder);
                if(refundOrderList != null && refundOrderList.size()>0){
                    for (EsShopOrder order:refundOrderList) {
                        analysisVo.setRefundOrder(analysisVo.getRefundOrder().add(order.getPayPrice()));
                    }
                }else{
                    analysisVo.setRefundOrder(BigDecimal.valueOf(0.00));
                }
                analysisVo.setTime(da);
                analysisVoList.add(analysisVo);
            }

            return new CommonResult().success("success",analysisVoList);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "数据-交易分析", REMARK = "数据下载")
    @ApiOperation("数据下载")
    @RequestMapping(value = "/dataDown", method = RequestMethod.POST)
    public Object dataDown(@RequestParam("startTime")String startTime, @RequestParam("endTime")String endTime, HttpServletRequest request, HttpServletResponse response)  {
        try{
            List<TransactionAnalysisVo> analysisVoList = new ArrayList<TransactionAnalysisVo>();
            Date beTime=sdf.parse(startTime);
            Date edTime=sdf.parse(endTime);
            List<Date> list =findDates(beTime,edTime);
            for (Date da:list) {
                TransactionAnalysisVo analysisVo = new TransactionAnalysisVo();
                EsShopOrder esShopOrder = new EsShopOrder();
                //1.查询支付金额
                esShopOrder.setStatus(3);
                esShopOrder.setBeginTime(sdf.format(da)+" 00:00:00");
                esShopOrder.setEndTime(sdf.format(da)+" 23:59:59");
                System.out.println(esShopOrder.getEndTime()+esShopOrder.getBeginTime());
                List<EsShopOrder> paymentAmountList = shopOrderService.selCountDateDetail(esShopOrder);
                if(list != null && list.size()>0){
                    for (EsShopOrder order:paymentAmountList) {
                        analysisVo.setPaymentAmount(analysisVo.getPaymentAmount().add(order.getPayPrice()));
                    }
                }else{
                    analysisVo.setPaymentAmount(BigDecimal.valueOf(0.00));
                }
                //2.支付订单数
                analysisVo.setPaymentOrderNum(paymentAmountList.size());
                //3.付款人数
                esShopOrder.setBeginTime(startTime);
                esShopOrder.setEndTime(endTime);
                List<Integer> listMemberId = shopOrderService.paymentNumber(esShopOrder);
                HashSet h = new HashSet(listMemberId);
                listMemberId.clear();
                listMemberId.addAll(h);
                analysisVo.setPaymentNumber(listMemberId.size());
                //4.付款订单数
                analysisVo.setPaymentOrder(paymentAmountList.size());
                //5.客单价  detail:总金额/总订单量=客单价
                if(analysisVo.getPaymentAmount().equals(BigDecimal.valueOf(0))){
                    analysisVo.setCustomerUnitPrice(BigDecimal.valueOf(0));
                }else{
                    analysisVo.setCustomerUnitPrice(analysisVo.getPaymentAmount().divide(BigDecimal.valueOf(analysisVo.getPaymentOrder()),2, RoundingMode.HALF_DOWN));
                }

                //6.退款订单金额
                esShopOrder = new EsShopOrder();
                esShopOrder.setStatus(4);
                esShopOrder.setBeginTime(sdf.format(da)+" 00:00:00");
                esShopOrder.setEndTime(sdf.format(da)+" 23:59:59");
                List<EsShopOrder> refundOrderList = shopOrderService.selCountDateDetail(esShopOrder);
                if(refundOrderList != null && refundOrderList.size()>0){
                    for (EsShopOrder order:refundOrderList) {
                        analysisVo.setRefundOrder(analysisVo.getRefundOrder().add(order.getRefundPrice()));
                    }
                }else{
                    analysisVo.setRefundOrder(BigDecimal.valueOf(0.00));
                }
                analysisVo.setTime(da);
                analysisVoList.add(analysisVo);
            }
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("交易分析列表");
            // 新增数据行，并且设置单元格数据
            int rowNum = 1;
            String[] headers = {  "日期", "支付金额(元)", "支付订单数", "付款人数","付款订单数","客单价(元)","退款订单金额(元)" };
            // headers表示excel表中第一行的表头
            HSSFRow row = sheet.createRow(0);
            // 在excel表中添加表头
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            // 在表中存放查询到的数据放入对应的列
            for (int i = 0; i < analysisVoList.size(); i++) {
                HSSFRow row1 = sheet.createRow(rowNum);
                row1.createCell(0).setCellValue(null != sdf.format(analysisVoList.get(i).getTime()) ? sdf.format(analysisVoList.get(i).getTime()) : "暂无");
                row1.createCell(1).setCellValue( null != analysisVoList.get(i).getPaymentAmount() ? analysisVoList.get(i).getPaymentAmount().toString() : "暂无");
                row1.createCell(2).setCellValue( null != analysisVoList.get(i).getPaymentOrderNum() ? analysisVoList.get(i).getPaymentOrderNum().toString() : "暂无");
                row1.createCell(3).setCellValue( null != analysisVoList.get(i).getPaymentNumber() ? analysisVoList.get(i).getPaymentNumber().toString() : "暂无");
                row1.createCell(4).setCellValue( null != analysisVoList.get(i).getPaymentOrder() ? analysisVoList.get(i).getPaymentOrder().toString() : "暂无");
                row1.createCell(5).setCellValue( null != analysisVoList.get(i).getCustomerUnitPrice() ? analysisVoList.get(i).getCustomerUnitPrice().toString() : "暂无");
                row1.createCell(6).setCellValue(analysisVoList.get(i).getRefundOrder().doubleValue());
                rowNum++;
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sf.format(new Date());
            String fileName = "数据-交易分析" + date + ".xls";// 设置要导出的文件的名字
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());

            return new CommonResult().success("success",analysisVoList);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed();
        }

    }

    //时间处理
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        System.out.println(calEnd.getTime());
        // 测试此日期是否在指定日期之后
        System.out.println(calBegin.getTime());
        while (calEnd.getTime().after(calBegin.getTime()))  {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

}
