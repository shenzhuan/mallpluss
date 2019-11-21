package com.mei.zhuang.controller.order;

import com.arvato.ec.common.vo.EsMiniprogram;
import com.arvato.service.order.api.orm.dao.EsCoreMessageTemplateMapper;
import com.arvato.service.order.api.service.EsMiniprogramService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(value = "小程序模板消息管理", description = "", tags = {"小程序模板消息管理"})
@RestController
@RequestMapping("/cor/miniprogram")
public class EsMiniprogramController {

    @Resource
    private EsMiniprogramService esMiniprogramService;
    @Resource
    private EsCoreMessageTemplateMapper esCoreMessageTemplateMapper;

    @SysLog(MODULE = "小程序模板消息管理", REMARK = "查询小程序设置")
    @ApiOperation("查询小程序设置")
    @PostMapping("/selMiniprogram")
    public Object selectMessageTemplate(EsMiniprogram entity) {
        try {

            EsMiniprogram esMiniprogram = esMiniprogramService.selectOne(new QueryWrapper<>(entity));
            if (esMiniprogram != null && !esMiniprogram.equals("")) {
                esMiniprogram.setListOrderTemplate(esCoreMessageTemplateMapper.selectList(new QueryWrapper<>()));
            }
            return new CommonResult().success("success", esMiniprogram);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "小程序模板消息管理", REMARK = "更新小程序设置")
    @ApiOperation("更新小程序设置")
    @PostMapping("/updMiniprogram")
    public Object updMiniprogram(EsMiniprogram entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定小程序编号");
            }
            return new CommonResult().success("success", esMiniprogramService.updateById(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "小程序模板消息管理", REMARK = "新增小程序设置")
    @ApiOperation("新增小程序设置")
    @PostMapping("/save")
    public Object save(EsMiniprogram entity) {
        try {
            if (ValidatorUtils.empty(entity.getAppid())) {
                return new CommonResult().failed("请指定小程序APPID");
            }
            if (ValidatorUtils.empty(entity.getAppSecret())) {
                return new CommonResult().failed("请指定小程序APPSecret");
            }
            return new CommonResult().success("success", esMiniprogramService.insert(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }
}
