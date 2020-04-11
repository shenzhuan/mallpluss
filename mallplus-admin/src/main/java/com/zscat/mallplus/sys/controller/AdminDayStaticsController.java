package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.AdminDayStatics;
import com.zscat.mallplus.sys.service.IAdminDayStaticsService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author mallplus
 * @date 2020-04-11
 * 商户日统计
 */
@Slf4j
@RestController
@RequestMapping("/sys/adminDayStatics")
public class AdminDayStaticsController {

    @Resource
    private IAdminDayStaticsService IAdminDayStaticsService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有商户日统计列表")
    @ApiOperation("根据条件查询所有商户日统计列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:adminDayStatics:read')")
    public Object getAdminDayStaticsByPage(AdminDayStatics entity,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IAdminDayStaticsService.page(new Page<AdminDayStatics>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time")));
        } catch (Exception e) {
            log.error("根据条件查询所有商户日统计列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存商户日统计")
    @ApiOperation("保存商户日统计")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:adminDayStatics:create')")
    public Object saveAdminDayStatics(@RequestBody AdminDayStatics entity) {
        try {
            entity.setCreateTime(new Date());
            if (IAdminDayStaticsService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存商户日统计：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新商户日统计")
    @ApiOperation("更新商户日统计")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:adminDayStatics:update')")
    public Object updateAdminDayStatics(@RequestBody AdminDayStatics entity) {
        try {
            if (IAdminDayStaticsService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新商户日统计：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除商户日统计")
    @ApiOperation("删除商户日统计")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:adminDayStatics:delete')")
    public Object deleteAdminDayStatics(@ApiParam("商户日统计id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商户日统计id");
            }
            if (IAdminDayStaticsService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除商户日统计：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给商户日统计分配商户日统计")
    @ApiOperation("查询商户日统计明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:adminDayStatics:read')")
    public Object getAdminDayStaticsById(@ApiParam("商户日统计id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商户日统计id");
            }
            AdminDayStatics coupon = IAdminDayStaticsService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商户日统计明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除商户日统计")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除商户日统计")
    @PreAuthorize("hasAuthority('sys:adminDayStatics:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IAdminDayStaticsService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, AdminDayStatics entity) {
        // 模拟从数据库获取需要导出的数据
        List<AdminDayStatics> personList = IAdminDayStaticsService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", AdminDayStatics.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<AdminDayStatics> personList = EasyPoiUtils.importExcel(file, AdminDayStatics.class);
        IAdminDayStaticsService.saveBatch(personList);
    }
}


