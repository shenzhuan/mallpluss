package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysStoreClass;
import com.zscat.mallplus.sys.service.ISysStoreClassService;
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
 * @date 2020-04-22
 * 商户分类
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysStoreClass")
public class SysStoreClassController {

    @Resource
    private ISysStoreClassService ISysStoreClassService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有商户分类列表")
    @ApiOperation("根据条件查询所有商户分类列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:sysStoreClass:read')")
    public Object getSysStoreClassByPage(SysStoreClass entity,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysStoreClassService.page(new Page<SysStoreClass>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有商户分类列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存商户分类")
    @ApiOperation("保存商户分类")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:sysStoreClass:create')")
    public Object saveSysStoreClass(@RequestBody SysStoreClass entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISysStoreClassService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存商户分类：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新商户分类")
    @ApiOperation("更新商户分类")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:sysStoreClass:update')")
    public Object updateSysStoreClass(@RequestBody SysStoreClass entity) {
        try {
            if (ISysStoreClassService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新商户分类：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除商户分类")
    @ApiOperation("删除商户分类")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:sysStoreClass:delete')")
    public Object deleteSysStoreClass(@ApiParam("商户分类id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商户分类id");
            }
            if (ISysStoreClassService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除商户分类：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给商户分类分配商户分类")
    @ApiOperation("查询商户分类明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:sysStoreClass:read')")
    public Object getSysStoreClassById(@ApiParam("商户分类id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商户分类id");
            }
            SysStoreClass coupon = ISysStoreClassService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商户分类明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除商户分类")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除商户分类")
    @PreAuthorize("hasAuthority('sys:sysStoreClass:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysStoreClassService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysStoreClass entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysStoreClass> personList = ISysStoreClassService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysStoreClass.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysStoreClass> personList = EasyPoiUtils.importExcel(file, SysStoreClass.class);
        ISysStoreClassService.saveBatch(personList);
    }
}


