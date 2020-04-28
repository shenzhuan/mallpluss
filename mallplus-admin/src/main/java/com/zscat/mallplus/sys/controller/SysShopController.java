package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysShop;
import com.zscat.mallplus.sys.service.ISysShopService;
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
 * @date 2020-03-29
 * 门店管理
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysShop")
public class SysShopController {

    @Resource
    private ISysShopService ISysShopService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有门店管理列表")
    @ApiOperation("根据条件查询所有门店管理列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:sysShop:read')")
    public Object getSysShopByPage(SysShop entity,
                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysShopService.page(new Page<SysShop>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有门店管理列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存门店管理")
    @ApiOperation("保存门店管理")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:sysShop:create')")
    public Object saveSysShop(@RequestBody SysShop entity) {
        try {
            entity.setCtime(new Date());
            if (ISysShopService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存门店管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新门店管理")
    @ApiOperation("更新门店管理")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:sysShop:update')")
    public Object updateSysShop(@RequestBody SysShop entity) {
        try {
            if (ISysShopService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新门店管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除门店管理")
    @ApiOperation("删除门店管理")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:sysShop:delete')")
    public Object deleteSysShop(@ApiParam("门店管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("门店管理id");
            }
            if (ISysShopService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除门店管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给门店管理分配门店管理")
    @ApiOperation("查询门店管理明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:sysShop:read')")
    public Object getSysShopById(@ApiParam("门店管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("门店管理id");
            }
            SysShop coupon = ISysShopService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询门店管理明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除门店管理")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除门店管理")
    @PreAuthorize("hasAuthority('sys:sysShop:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysShopService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysShop entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysShop> personList = ISysShopService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysShop.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysShop> personList = EasyPoiUtils.importExcel(file, SysShop.class);
        ISysShopService.saveBatch(personList);
    }
}


