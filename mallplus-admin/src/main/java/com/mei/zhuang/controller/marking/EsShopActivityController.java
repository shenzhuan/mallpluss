package com.mei.zhuang.controller.marking;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsMemberActivatyRecord;
import com.mei.zhuang.entity.marking.EsShopActivity;
import com.mei.zhuang.entity.marking.EsShopActivityPrize;
import com.mei.zhuang.service.marking.EsMemberActivatyRecordService;
import com.mei.zhuang.service.marking.EsShopActivityService;
import com.mei.zhuang.utils.ImgBase64Util;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "抽奖有礼管理", description = "", tags = {"抽奖有礼管理"})
@RestController
@RequestMapping("/api/activaty")
public class EsShopActivityController {

    @Resource
    private EsShopActivityService esShopActivityService;
    @Resource
    private EsMemberActivatyRecordService esMemberActivatyRecordService;

    @SysLog(MODULE = "抽奖有礼管理", REMARK = "查询抽奖有礼列表")
    @ApiOperation("查询抽奖有礼列表")
    @PostMapping("/list")
    public Object selPageList(EsShopActivity entity) {
        try {
            if (entity.getStatus() != null && entity.getStatus() == -1) {
                entity.setStatus(null);
            }
            return new CommonResult().success("success", esShopActivityService.selPageList(entity));
        } catch (Exception e) {
            log.error("查询抽奖有礼列表异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "抽奖有礼管理", REMARK = "查询抽奖有礼详情")
    @ApiOperation("查询抽奖有礼详情")
    @PostMapping("/detail")
    public Object detail(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("编号为空");
            }
            return new CommonResult().success("success", esShopActivityService.detail(id));
        } catch (Exception e) {
            log.error("查询抽奖有礼详情异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "抽奖有礼管理", REMARK = "修改抽奖有礼详情")
    @ApiOperation("修改抽奖有礼详情")
    @PostMapping("/update")
    public Object update(EsShopActivity entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("编号为空");
            }
            List<EsShopActivityPrize> list = JSONObject.parseArray(entity.getActivatyPrize(), EsShopActivityPrize.class);
            Integer num = 100;
            if (list != null) {
                for (EsShopActivityPrize prize : list) {
                    num -= prize.getWinning();
                }
                entity.setList(list);
            }
            entity.setWinRate(num);
            boolean bool = esShopActivityService.updates(entity);
            if (bool == true) {
                return new CommonResult().success("修改成功");
            } else {
                return new CommonResult().success("修改失败");
            }

        } catch (Exception e) {
            log.error("修改抽奖有礼详情异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "抽奖有礼管理", REMARK = "删除抽奖有礼")
    @ApiOperation("删除抽奖有礼")
    @PostMapping("/delete")
    public Object delete(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("编号为空");
            }
            return new CommonResult().success("success", esShopActivityService.deletes(id));
        } catch (Exception e) {
            log.error("删除抽奖有礼异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "抽奖有礼管理", REMARK = "新增抽奖有礼")
    @ApiOperation("新增抽奖有礼")
    @PostMapping("/save")
    public Object save(EsShopActivity entity) {
        try {
            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("活动名称不得为空");
            }
            List<EsShopActivityPrize> list = JSONObject.parseArray(entity.getActivatyPrize(), EsShopActivityPrize.class);
            Integer num = 100;
            if (list != null) {
                for (EsShopActivityPrize prize : list) {
                    num -= prize.getWinning();
                }
                entity.setList(list);
            }
            entity.setWinRate(num);
            boolean bool = esShopActivityService.save(entity);
            if (bool == true) {
                return new CommonResult().success("添加成功");
            } else {
                return new CommonResult().success("添加失败");
            }
        } catch (Exception e) {
            log.error("新增抽奖有礼异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "抽奖有礼管理", REMARK = "抽奖有礼二维码")
    @ApiOperation("抽奖有礼二维码")
    @PostMapping("/activatyQRCode")
    public Object activatyQRCode(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定编号");
            }
            String img =  null;

                    //ImgBase64Util.StringUtil(id, "pages/goods/detail/index");

            return new CommonResult().success("success", img);
        } catch (Exception e) {
            log.error("抽奖有礼二维码异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "抽奖有礼管理", REMARK = "查询用户抽奖记录")
    @ApiOperation("查询用户抽奖记录列表")
    @PostMapping("/selPageListMember")
    public Object selPageListMeber(EsMemberActivatyRecord entity) {
        try {
            if (ValidatorUtils.empty(entity.getActivatyId())) {
                return new CommonResult().failed("请指定活动编号");
            }
            return new CommonResult().success("success", esMemberActivatyRecordService.selPageList(entity));
        } catch (Exception e) {
            log.error("查询用户抽奖记录列表异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "抽奖有礼管理", REMARK = "用户抽奖记录列表数据导出")
    @ApiOperation("用户抽奖记录列表数据导出")
    @RequestMapping(value = "/excelDownloads", method = RequestMethod.POST)
    public void excelDownloads(EsMemberActivatyRecord entity, HttpServletRequest request, HttpServletResponse response) {

        try {
            @SuppressWarnings("resource")
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("用户抽奖记录列表");
            List<EsMemberActivatyRecord> list = esMemberActivatyRecordService.list(new QueryWrapper<>(entity));
            // 新增数据行，并且设置单元格数据
            int rowNum = 1;
            String[] headers = {"编号", "时间", "昵称", "openid", "状态", "奖项", "奖品"};
            // headers表示excel表中第一行的表头
            HSSFRow row = sheet.createRow(0);
            // 在excel表中添加表头
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            // 在表中存放查询到的数据放入对应的列
            for (int i = 0; i < list.size(); i++) {
                HSSFRow row1 = sheet.createRow(rowNum);
                row1.createCell(0).setCellValue(null != list.get(i).getId() ? list.get(i).getId().toString() : "暂无");
                row1.createCell(1).setCellValue(null != list.get(i).getCreateTime() ? list.get(i).getCreateTime().toString() : "暂无");
                row1.createCell(2).setCellValue(null != list.get(i).getNickName() ? list.get(i).getNickName() : "暂无");
                row1.createCell(3).setCellValue(null != list.get(i).getOpenId() ? list.get(i).getOpenId() : "暂无");
                row1.createCell(4).setCellValue(null != list.get(i).getIsWin() ? list.get(i).getIsWin().toString() : "暂无");
                row1.createCell(5).setCellValue(null != list.get(i).getPrizeLevel() ? list.get(i).getPrizeLevel().toString() : "暂无");
                row1.createCell(6).setCellValue(null != list.get(i).getPrizeName() ? list.get(i).getPrizeName() : "暂无");
                rowNum++;
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sf.format(new Date());
            String fileName = "用户抽奖记录列表" + date + ".xls";// 设置要导出的文件的名字
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());

        } catch (Exception e) {
            log.error("用户抽奖记录列表数据导出", e);
            e.printStackTrace();
        }
        System.out.println("已导出");
    }
}
