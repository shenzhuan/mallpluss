package com.mei.zhuang.controller.goods;

import com.mei.zhuang.service.goods.EsDecorateTemplateService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.goods.EsDecorateTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 10:54
 * @Description:
 */
@Slf4j
@Api(value = "自定义页面配置", description = "", tags = {"自定义页面配置"})
@RestController
@RequestMapping("/shop/decorateTemplate")
public class EsDecorateTemplateController {
    @Resource
    private EsDecorateTemplateService decorateTemplateService;

    @SysLog(MODULE = "自定义页面配置", REMARK = "添加自定义页面配置")
    @ApiOperation("添加自定义页面配置")
    @PostMapping(value = "/saves")
    public Object saveBrand(@RequestBody EsDecorateTemplate entity) {
        try {
            return decorateTemplateService.save(entity);
        } catch (Exception e) {
            log.error("添加自定义页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "发布自定义页面配置")
    @ApiOperation("发布自定义页面配置")
    @PostMapping(value = "/release")
    public Object release(@RequestBody EsDecorateTemplate entity) {
        try {
            return decorateTemplateService.release(entity);
        } catch (Exception e) {
            log.error("添加自定义页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "删除自定义页面配置")
    @ApiOperation("删除自定义页面配置")
    @DeleteMapping(value = "/delete{brandid}")
    public Object deleteBrand(@PathVariable Long brandid) {
        try {
            return new CommonResult().success(decorateTemplateService.getById(brandid));
        } catch (Exception e) {
            log.error("删除自定义页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "查询自定义页面配置明细")
    @ApiOperation("查询自定义页面配置明细")
    @PostMapping(value = "/detail{brandid}")
    public Object detailBrand(@PathVariable Long brandid) {
        try {
            return decorateTemplateService.getById(brandid);
        } catch (Exception e) {
            log.error("查询明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "更改自定义页面配置信息")
    @ApiOperation("更改自定义页面配置信息")
    @PostMapping(value = "/update")
    public Object updateBrand(@RequestBody EsDecorateTemplate entity) {
        try {
            decorateTemplateService.updateById(entity);
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("更改自定义页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
}
