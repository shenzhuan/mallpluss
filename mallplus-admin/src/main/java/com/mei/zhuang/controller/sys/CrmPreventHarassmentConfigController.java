package com.mei.zhuang.controller.sys;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.entity.sys.CrmPreventHarassmentConfig;
import com.mei.zhuang.service.sys.ICrmPreventHarassmentConfigService;
import com.mei.zhuang.vo.BizResult;
import com.mei.zhuang.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author aam
 * rvato te * @since 2018-08-28
 */
@Api(value = "防骚扰", description = "防骚扰配置相关", tags = {"防骚扰配置"})
@RestController
@RequestMapping("/crmPreventHarassmentConfig")
public class CrmPreventHarassmentConfigController {

    @Resource
    private ICrmPreventHarassmentConfigService configService;


    @ApiOperation("查询防骚扰配置")
    @GetMapping("/{type}")
    public BizResult get(@ApiParam("类型,1=营销类") @PathVariable String type) {
        return Result.success(
                this.configService.getOne(new QueryWrapper<>(new CrmPreventHarassmentConfig() {{
                    this.setType(type);
                }}))
        );
    }


    @ApiOperation("保存防骚扰配置")
    @PostMapping
    public BizResult save(@ApiParam("防骚扰配置信息") CrmPreventHarassmentConfig config) {
        Assert.isTrue(
                this.configService.count(new QueryWrapper<>(new CrmPreventHarassmentConfig() {{
                    this.setType(config.getType());
                }})) == 0,
                "操作失败"
        );

        return Result.toResult(
                this.configService.save(config)
        );
    }


    @ApiOperation("更新防骚扰配置")
    @PutMapping
    public BizResult update(@ApiParam("防骚扰配置信息") CrmPreventHarassmentConfig config) {
        String dataConfig = config.getDataConfig();
        JSONObject jsonObject = JSON.parseObject(dataConfig);
        return Result.toResult(
                this.configService.updateById(config)
        );
    }

}

