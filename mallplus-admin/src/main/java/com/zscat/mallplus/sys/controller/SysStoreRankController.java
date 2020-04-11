package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysStoreRank;
import com.zscat.mallplus.sys.service.ISysStoreRankService;
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
 * 商户等级
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysStoreRank")
public class SysStoreRankController {

    @Resource
    private ISysStoreRankService ISysStoreRankService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有商户等级列表")
    @ApiOperation("根据条件查询所有商户等级列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:sysStoreRank:read')")
    public Object getSysStoreRankByPage(SysStoreRank entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysStoreRankService.page(new Page<SysStoreRank>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有商户等级列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存商户等级")
    @ApiOperation("保存商户等级")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:sysStoreRank:create')")
    public Object saveSysStoreRank(@RequestBody SysStoreRank entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISysStoreRankService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存商户等级：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新商户等级")
    @ApiOperation("更新商户等级")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:sysStoreRank:update')")
    public Object updateSysStoreRank(@RequestBody SysStoreRank entity) {
        try {
            if (ISysStoreRankService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新商户等级：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除商户等级")
    @ApiOperation("删除商户等级")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:sysStoreRank:delete')")
    public Object deleteSysStoreRank(@ApiParam("商户等级id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商户等级id");
            }
            if (ISysStoreRankService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除商户等级：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给商户等级分配商户等级")
    @ApiOperation("查询商户等级明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:sysStoreRank:read')")
    public Object getSysStoreRankById(@ApiParam("商户等级id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商户等级id");
            }
            SysStoreRank coupon = ISysStoreRankService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商户等级明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除商户等级")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除商户等级")
    @PreAuthorize("hasAuthority('sys:sysStoreRank:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysStoreRankService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysStoreRank entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysStoreRank> personList = ISysStoreRankService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysStoreRank.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysStoreRank> personList = EasyPoiUtils.importExcel(file, SysStoreRank.class);
        ISysStoreRankService.saveBatch(personList);
    }
}


