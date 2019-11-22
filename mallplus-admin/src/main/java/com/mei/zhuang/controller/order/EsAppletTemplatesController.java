package com.mei.zhuang.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.service.order.EsAppletTemplateService;
import com.mei.zhuang.service.order.impl.WechatApiService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.entity.order.EsAppletTemplates;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "模版消息库接口管理", description = "", tags = {"模版消息库接口管理"})
@Slf4j
@RestController
@RequestMapping("/api/appletTemplates")
public class EsAppletTemplatesController {

    @Resource
    private EsAppletTemplateService esAppletTemplateService;
    @Resource
    WechatApiService wechatApiService;

    @SysLog(MODULE = "模版消息库接口管理", REMARK = "查询模版消息库接口")
    @ApiOperation("查询模版消息库接口")
    @PostMapping("/list")
    public Object select(EsAppletTemplates entity) {
        try {
            return esAppletTemplateService.select(entity);
        } catch (Exception e) {
            log.error("查询模版消息库接口：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "模版消息库接口管理", REMARK = "查询模版消息库详情")
    @ApiOperation("查询模版消息库详情")
    @PostMapping("/detail")
    public Object detail(EsAppletTemplates entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定编号");
            }
            return esAppletTemplateService.getOne(new QueryWrapper<>(entity));
        } catch (Exception e) {
            log.error("查询模版消息库详情接口：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "模版消息库接口管理", REMARK = "删除模版消息库")
    @ApiOperation("删除模版消息库")
    @PostMapping("/delete")
    public Object deletes(String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("请指定编号");
            }
            String[] attr = ids.split(",");
            Boolean bool = false;
            for (int i = 0; i < attr.length; i++) {
                bool = esAppletTemplateService.removeById(Long.parseLong(attr[i]));
            }
            return new CommonResult().success("success", bool);
        } catch (Exception e) {
            log.error("删除模版消息库接口：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "模版消息库接口管理", REMARK = "查询所有模版不分页")
    @ApiOperation("查询所有模版不分页")
    @PostMapping("/selectAll")
    public Object selectAll() {
        try {
            EsAppletTemplates appletTemplates = new EsAppletTemplates();
            appletTemplates.setStatus(1);
            return new CommonResult().success("success", esAppletTemplateService.list(new QueryWrapper<>(appletTemplates)));
        } catch (Exception e) {
            log.error("查询所有模版不分页接口：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "模版消息库接口管理", REMARK = "同步模版消息")
    @ApiOperation("同步模版消息")
    @PostMapping("/syncTempl")
    public Object syncTempl(@RequestParam Long tenantId) {
        try {
            JSONObject jsonObject = wechatApiService.synTemplates(tenantId);
            System.out.println("测试数据：" + jsonObject);
            return new CommonResult().success("success", jsonObject);
        } catch (Exception e) {
            log.error("同步模版消息接口：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

}
