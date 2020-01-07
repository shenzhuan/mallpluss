package com.zscat.mallplus.cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.entity.CmsZhaoPin;
import com.zscat.mallplus.cms.service.ICmsZhaoPinService;
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
 * @date 2020-01-06
 * 招聘管理
 */
@Slf4j
@RestController
@RequestMapping("/cms/cmsZhaoPin")
public class CmsZhaoPinController {

    @Resource
    private ICmsZhaoPinService ICmsZhaoPinService;

    @SysLog(MODULE = "cms", REMARK = "根据条件查询所有招聘管理列表")
    @ApiOperation("根据条件查询所有招聘管理列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('cms:cmsZhaoPin:read')")
    public Object getCmsZhaoPinByPage(CmsZhaoPin entity,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ICmsZhaoPinService.page(new Page<CmsZhaoPin>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有招聘管理列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "保存招聘管理")
    @ApiOperation("保存招聘管理")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('cms:cmsZhaoPin:create')")
    public Object saveCmsZhaoPin(@RequestBody CmsZhaoPin entity) {
        try {
            entity.setCreateTime(new Date());
            if (ICmsZhaoPinService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存招聘管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "更新招聘管理")
    @ApiOperation("更新招聘管理")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('cms:cmsZhaoPin:update')")
    public Object updateCmsZhaoPin(@RequestBody CmsZhaoPin entity) {
        try {
            if (ICmsZhaoPinService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新招聘管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "删除招聘管理")
    @ApiOperation("删除招聘管理")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('cms:cmsZhaoPin:delete')")
    public Object deleteCmsZhaoPin(@ApiParam("招聘管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("招聘管理id");
            }
            if (ICmsZhaoPinService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除招聘管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "给招聘管理分配招聘管理")
    @ApiOperation("查询招聘管理明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('cms:cmsZhaoPin:read')")
    public Object getCmsZhaoPinById(@ApiParam("招聘管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("招聘管理id");
            }
            CmsZhaoPin coupon = ICmsZhaoPinService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询招聘管理明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除招聘管理")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "cms", REMARK = "批量删除招聘管理")
    @PreAuthorize("hasAuthority('cms:cmsZhaoPin:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ICmsZhaoPinService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "cms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, CmsZhaoPin entity) {
        // 模拟从数据库获取需要导出的数据
        List<CmsZhaoPin> personList = ICmsZhaoPinService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", CmsZhaoPin.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "cms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<CmsZhaoPin> personList = EasyPoiUtils.importExcel(file, CmsZhaoPin.class);
        ICmsZhaoPinService.saveBatch(personList);
    }
}


