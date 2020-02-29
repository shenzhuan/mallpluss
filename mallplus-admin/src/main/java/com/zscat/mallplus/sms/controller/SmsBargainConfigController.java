package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsBargainConfig;
import com.zscat.mallplus.sms.service.ISmsBargainConfigService;
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
 * @date 2019-12-27
 * 砍价商品
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsBargainConfig")
public class SmsBargainConfigController {

    @Resource
    private ISmsBargainConfigService ISmsBargainConfigService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有砍价商品列表")
    @ApiOperation("根据条件查询所有砍价商品列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsBargainConfig:read')")
    public Object getSmsBargainConfigByPage(SmsBargainConfig entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsBargainConfigService.page(new Page<SmsBargainConfig>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有砍价商品列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存砍价商品")
    @ApiOperation("保存砍价商品")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsBargainConfig:create')")
    public Object saveSmsBargainConfig(@RequestBody SmsBargainConfig entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISmsBargainConfigService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存砍价商品：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新砍价商品")
    @ApiOperation("更新砍价商品")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsBargainConfig:update')")
    public Object updateSmsBargainConfig(@RequestBody SmsBargainConfig entity) {
        try {
            if (ISmsBargainConfigService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新砍价商品：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除砍价商品")
    @ApiOperation("删除砍价商品")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsBargainConfig:delete')")
    public Object deleteSmsBargainConfig(@ApiParam("砍价商品id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("砍价商品id");
            }
            if (ISmsBargainConfigService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除砍价商品：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给砍价商品分配砍价商品")
    @ApiOperation("查询砍价商品明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsBargainConfig:read')")
    public Object getSmsBargainConfigById(@ApiParam("砍价商品id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("砍价商品id");
            }
            SmsBargainConfig coupon = ISmsBargainConfigService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询砍价商品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除砍价商品")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除砍价商品")
    @PreAuthorize("hasAuthority('sms:smsBargainConfig:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsBargainConfigService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsBargainConfig entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsBargainConfig> personList = ISmsBargainConfigService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsBargainConfig.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsBargainConfig> personList = EasyPoiUtils.importExcel(file, SmsBargainConfig.class);
        ISmsBargainConfigService.saveBatch(personList);
    }
}


