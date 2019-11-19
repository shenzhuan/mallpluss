package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsDiyPage;
import com.zscat.mallplus.sms.service.ISmsDiyPageService;
import com.zscat.mallplus.sms.service.ISmsDiyPageService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 页面配置 前端控制器
 * </p>
 *
 * @author zscat
 * @since 2019-10-17
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsDiyPage")
public class SmsDiyPageController {
    @Resource
    private ISmsDiyPageService ISmsDiyPageService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有页面配置列表")
    @ApiOperation("根据条件查询所有页面配置列表")
    @GetMapping(value = "/list")
    public Object getSmsDiyPageByPage(SmsDiyPage entity,
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsDiyPageService.page(new Page<SmsDiyPage>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有页面配置列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存页面配置")
    @ApiOperation("保存页面配置")
    @PostMapping(value = "/create")
    public Object saveSmsDiyPage(@RequestBody SmsDiyPage entity) {
        try {
            if (ISmsDiyPageService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新页面配置")
    @ApiOperation("更新页面配置")
    @PostMapping(value = "/update/{id}")
    public Object updateSmsDiyPage(@RequestBody SmsDiyPage entity) {
        try {
            if (ISmsDiyPageService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除页面配置")
    @ApiOperation("删除页面配置")
    @GetMapping(value = "/delete/{id}")
    public Object deleteSmsDiyPage(@ApiParam("页面配置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("页面配置id");
            }
            if (ISmsDiyPageService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给页面配置分配页面配置")
    @ApiOperation("查询页面配置明细")
    @GetMapping(value = "/{id}")
    public Object getSmsDiyPageById(@ApiParam("页面配置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("页面配置id");
            }
            SmsDiyPage coupon = ISmsDiyPageService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询页面配置明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }
}

