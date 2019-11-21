package com.mei.zhuang.controller.order;

import com.arvato.service.order.api.service.EsAppletSetService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.mei.zhuang.entity.order.EsAppletSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(value = "小程序设置管理", description = "", tags = {"小程序设置管理"})
@RestController
@RequestMapping("/appletset")
public class EsAppletSetController {

    @Resource
    private EsAppletSetService esAppletSetService;

    @SysLog(MODULE = "小程序设置管理", REMARK = "新增小程序设置")
    @ApiOperation("新增小程序设置")
    @PostMapping("/save")
    public Object save(EsAppletSet entity) {
        try {
            if (ValidatorUtils.empty(entity.getAppId())) {
                return new CommonResult().failed("小程序ID不得为空");
            }
            if (ValidatorUtils.empty(entity.getAppSecret())) {
                return new CommonResult().failed("小程序密钥不得为空");
            }
            Integer num = esAppletSetService.save(entity);
            return new CommonResult().success("success", num);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "小程序设置管理", REMARK = "查询小程序设置")
    @ApiOperation("查询小程序设置")
    @PostMapping("/select")
    public Object select(EsAppletSet entity) {
        try {
            return new CommonResult().success("success", esAppletSetService.select());
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "小程序设置管理", REMARK = "编辑小程序设置")
    @ApiOperation("编辑小程序设置")
    @PostMapping("/update")
    public Object update(EsAppletSet entity) {
        try {
            Integer num = esAppletSetService.update(entity);
            return new CommonResult().success("success", num);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

}
