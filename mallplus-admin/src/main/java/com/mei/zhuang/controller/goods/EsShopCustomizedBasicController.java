package com.mei.zhuang.controller.goods;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.service.goods.EsShopCustomizedBasicService;
import com.mei.zhuang.service.goods.EsShopCustomizedLegendService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import com.mei.zhuang.entity.goods.EsShopCustomizedLegend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "刻字服务管理", description = "", tags = {"刻字服务管理"})
@RestController
@RequestMapping("/shop/basic")
public class EsShopCustomizedBasicController {

    @Resource
    private EsShopCustomizedLegendService esShopCustomizedLegendService;
    @Resource
    private EsShopCustomizedBasicService esShopCustomizedBasicService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SysLog(MODULE = "刻字服务管理", REMARK = "查询样图上传列表")
    @ApiOperation("查询样图上传列表")
    @PostMapping("/list")
    private Object selLegendPage(EsShopCustomizedLegend entity) {
        try {
            PageHelper.startPage(entity.getCurrent(), entity.getSize());
            // List<EsShopFullGift> esShopDiscount = fullGiftService.slelectPurchase2();
            return new CommonResult().success(PageInfo.of(esShopCustomizedLegendService.list(new QueryWrapper<>(entity))));
        } catch (Exception e) {
            log.error("查询样图上传列表常:", e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "查询样图上传列表详情")
    @ApiOperation("查询样图上传列表详情")
    @PostMapping("/detailLegend")
    private Object detailLegend(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedLegendService.getById(id));
        } catch (Exception e) {
            log.error("查询样图上传列表详情异常:", e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "新增样图上传")
    @ApiOperation("新增样图上传")
    @PostMapping("/insertLegend")
    private Object insertLegend(EsShopCustomizedLegend entity) {
        try {
            if (ValidatorUtils.empty(entity.getTitle())) {
                return new CommonResult().failed("请指定名称");
            }
            String time = sdf.format(new Date());
            entity.setCreateTime(sdf.parse(time));
            return new CommonResult().success("success", esShopCustomizedLegendService.save(entity));
        } catch (Exception e) {
            log.error("新增样图上传异常:", e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "修改样图上传")
    @ApiOperation("修改样图上传")
    @PostMapping("/updateLegend")
    private Object updateLegend(EsShopCustomizedLegend entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedLegendService.updateById(entity));
        } catch (Exception e) {
            log.error("修改样图上传异常:", e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "删除样图上传")
    @ApiOperation("删除样图上传")
    @PostMapping("/deletLegend")
    private Object deletLegend(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedLegendService.removeById(id));
        } catch (Exception e) {
            log.error("修改样图上传异常:", e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "查询刻字服务基本信息")
    @ApiOperation("查询刻字服务基本信息")
    @PostMapping("/detailBasic")
    private Object detailBasic(EsShopCustomizedBasic entity) {
        try {
            return new CommonResult().success("success", esShopCustomizedBasicService.getOne(new QueryWrapper<>()));
        } catch (Exception e) {
            log.error("新增刻字服务基本信息异常:", e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "查询刻字服务基本信息")
    @ApiOperation("查询刻字服务基本信息")
    @PostMapping("/detailBasics")
    private EsShopCustomizedBasic detailBasics(@RequestParam("id") Long id) {
        try {
            EsShopCustomizedBasic basic=esShopCustomizedBasicService.detail(id);
            return basic;
        } catch (Exception e) {
            log.error("新增刻字服务基本信息异常:", e);
            return null;
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "新增刻字服务基本信息")
    @ApiOperation("新增刻字服务基本信息")
    @PostMapping("/saveBasic")
    private Object saveBasic(EsShopCustomizedBasic entity) {
        try {
            if (ValidatorUtils.empty(entity.getTitle())) {
                return new CommonResult().failed("请指定标题");
            }
            return new CommonResult().success("success", esShopCustomizedBasicService.save(entity));
        } catch (Exception e) {
            log.error("新增刻字服务基本信息异常:", e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "修改刻字服务基本信息")
    @ApiOperation("修改刻字服务基本信息")
    @PostMapping("/updateBasic")
    private Object updateBasic(EsShopCustomizedBasic entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedBasicService.updateById(entity));
        } catch (Exception e) {
            log.error("修改刻字服务基本信息异常:", e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "刻字服务管理", REMARK = "查询刻字服务Basic")
    @ApiOperation("查询刻字服务Basic")
    @PostMapping("/selBasicInfoByIds")
    private Object selBasicInfoByIds(List<Long> basicIds) {
        try {
            return new CommonResult().success("success", esShopCustomizedBasicService.listByIds(basicIds));
        } catch (Exception e) {
            log.error("查询刻字服务Basic:", e);
            return new CommonResult().failed();
        }

    }



}
