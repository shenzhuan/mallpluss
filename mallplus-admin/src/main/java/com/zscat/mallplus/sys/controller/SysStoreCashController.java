package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysStoreCash;
import com.zscat.mallplus.sys.service.ISysStoreCashService;
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
 * @date 2020-04-10
 * 门店管理
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysStoreCash")
public class SysStoreCashController {

    @Resource
    private ISysStoreCashService ISysStoreCashService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有门店管理列表")
    @ApiOperation("根据条件查询所有门店管理列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:sysStoreCash:read')")
    public Object getSysStoreCashByPage(SysStoreCash entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysStoreCashService.page(new Page<SysStoreCash>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time")));
        } catch (Exception e) {
            log.error("根据条件查询所有门店管理列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存门店管理")
    @ApiOperation("保存门店管理")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:sysStoreCash:create')")
    public Object saveSysStoreCash(@RequestBody SysStoreCash entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISysStoreCashService.save(entity)) {
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
    @PreAuthorize("hasAuthority('sys:sysStoreCash:update')")
    public Object updateSysStoreCash(@RequestBody SysStoreCash entity) {
        try {
            if (ISysStoreCashService.updateById(entity)) {
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
    @PreAuthorize("hasAuthority('sys:sysStoreCash:delete')")
    public Object deleteSysStoreCash(@ApiParam("门店管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("门店管理id");
            }
            if (ISysStoreCashService.removeById(id)) {
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
    @PreAuthorize("hasAuthority('sys:sysStoreCash:read')")
    public Object getSysStoreCashById(@ApiParam("门店管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("门店管理id");
            }
            SysStoreCash coupon = ISysStoreCashService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询门店管理明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除门店管理")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除门店管理")
    @PreAuthorize("hasAuthority('sys:sysStoreCash:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysStoreCashService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysStoreCash entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysStoreCash> personList = ISysStoreCashService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysStoreCash.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysStoreCash> personList = EasyPoiUtils.importExcel(file, SysStoreCash.class);
        ISysStoreCashService.saveBatch(personList);
    }

    @ApiOperation(value = "批量更新显示状态")
    @RequestMapping(value = "/update/audit", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量更新显示状态")
    public Object audit(@RequestParam("ids") List<Long> ids,
                        @RequestParam("showStatus") Integer showStatus) {
        int count = ISysStoreCashService.audit(ids, showStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }
}


