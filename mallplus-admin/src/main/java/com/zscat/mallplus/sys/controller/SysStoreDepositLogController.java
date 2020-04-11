package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysStoreCash;
import com.zscat.mallplus.sys.entity.SysStoreDepositLog;
import com.zscat.mallplus.sys.service.ISysStoreDepositLogService;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author mallplus
 * @date 2020-04-10
 * 门店管理
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysStoreDepositLog")
public class SysStoreDepositLogController {

    @Resource
    private ISysStoreDepositLogService ISysStoreDepositLogService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有门店管理列表")
    @ApiOperation("根据条件查询所有门店管理列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:read')")
    public Object getSysStoreDepositLogByPage(SysStoreDepositLog entity,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysStoreDepositLogService.page(new Page<SysStoreDepositLog>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time")));
        } catch (Exception e) {
            log.error("根据条件查询所有门店管理列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存门店管理")
    @ApiOperation("保存门店管理")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:create')")
    public Object saveSysStoreDepositLog(@RequestBody SysStoreDepositLog entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISysStoreDepositLogService.save(entity)) {
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
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:update')")
    public Object updateSysStoreDepositLog(@RequestBody SysStoreDepositLog entity) {
        try {
            if (ISysStoreDepositLogService.updateById(entity)) {
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
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:delete')")
    public Object deleteSysStoreDepositLog(@ApiParam("门店管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("门店管理id");
            }
            if (ISysStoreDepositLogService.removeById(id)) {
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
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:read')")
    public Object getSysStoreDepositLogById(@ApiParam("门店管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("门店管理id");
            }
            SysStoreDepositLog coupon = ISysStoreDepositLogService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询门店管理明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除门店管理")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除门店管理")
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysStoreDepositLogService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysStoreDepositLog entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysStoreDepositLog> personList = ISysStoreDepositLogService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysStoreDepositLog.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysStoreDepositLog> personList = EasyPoiUtils.importExcel(file, SysStoreDepositLog.class);
        ISysStoreDepositLogService.saveBatch(personList);
    }

    @SysLog(MODULE = "sys", REMARK = "预存款充值")
    @ApiOperation("预存款充值")
    @PostMapping(value = "/addStoreMoney")
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:addStoreMoney')")
    public Object addStoreMoney(@RequestBody SysStoreDepositLog entity) {
        try {
            return new CommonResult().success(ISysStoreDepositLogService.addStoreMoney(entity));
        } catch (Exception e) {
            log.error("预存款充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }

    }

    @SysLog(MODULE = "sys", REMARK = "预存款提现")
    @ApiOperation("预存款提现")
    @PostMapping(value = "/drawStoreMoney")
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:drawStoreMoney')")
    public Object drawStoreMoney(@RequestBody SysStoreCash entity) {
        try {
            return new CommonResult().success(ISysStoreDepositLogService.drawStoreMoney(entity));
        } catch (Exception e) {
            log.error("预存款提现：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }

    }

    @SysLog(MODULE = "sys", REMARK = "预存款提现审核")
    @ApiOperation("预存款提现审核")
    @PostMapping(value = "/auditStoreMoney")
    @PreAuthorize("hasAuthority('sys:sysStoreDepositLog:auditStoreMoney')")
    public Object auditStoreMoney(@RequestParam("id") Long id,
                                  @RequestParam("showStatus") Integer showStatus,
                                  @RequestParam("amount") BigDecimal amount) {
        try {
            SysStoreCash entity = new SysStoreCash();
            entity.setStatus(showStatus);
            entity.setAmount(amount);
            entity.setId(id);
            return new CommonResult().success(ISysStoreDepositLogService.auditStoreMoney(entity));
        } catch (Exception e) {
            log.error("预存款提现：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }

    }
}


