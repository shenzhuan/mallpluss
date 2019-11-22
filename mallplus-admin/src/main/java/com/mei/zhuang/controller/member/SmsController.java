package com.mei.zhuang.controller.member;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.member.EsCoreSms;
import com.mei.zhuang.entity.member.EsCoreSmsTemplate;
import com.mei.zhuang.entity.member.SmsVariable;
import com.mei.zhuang.service.member.ISmsService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.SmsParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;


@Api(value = "短信设置管理", description = "", tags = {"短信设置管理"})
@Slf4j
@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Resource
    private ISmsService smsService;

    @SysLog(MODULE = "短信设置管理", REMARK = "查询短信模块列表")
    @ApiOperation("查询短信模块列表")
    @PostMapping("/list")
    public Object list(SmsParam smsParam) {
        try {
            Page<EsCoreSmsTemplate> page = smsService.selectPageExt(smsParam);
            return new CommonResult().success(page);
        } catch (Exception e) {
            log.error("查询短信模块列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "短信设置管理", REMARK = "保存短信模板")
    @ApiOperation("保存短信模板")
    @PostMapping("/save")
    public Object save(EsCoreSmsTemplate entity) {
        try {
            if (entity.getShopId() == null) {
                entity.setShopId(1l);
            }
            //非空处理
            String columnS = "smsName,type,sms_template_id,sms_sign,status,content,serverId";
            Object result = veryFieldEmpty(entity, columnS);
            if (result != null) {
                return result;
            }
            if (smsService.isExistRepeatName(entity.getSmsName()))
                return new CommonResult().failed("已存在相同的模板名称，{" + entity.getSmsName() + "}");

            List<SmsVariable> smsVariableList = JSON.parseArray(entity.getData(), SmsVariable.class);
            if (smsVariableList != null && smsVariableList.size() != 0) {
                boolean existTemplateType = this.smsService.isExistTemplateByStatus(entity);
                if (existTemplateType) {
                    return new CommonResult().failed("已存在正在启用的同一类型模板");
                }
            }
            entity.setIsDelete(0);
            entity.setCreateTime(new Date());
            if (smsService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存短信模板：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "短信设置管理", REMARK = "删除短信模板")
    @ApiOperation("删除短信模板")
    @PostMapping("/delete")
    public Object deletePayment(@ApiParam("短信模板id") @RequestParam Long id) {
        try {
            //非空处理
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().success("短信模板id 不能为空");
            }
            if (this.smsService.deleteById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除短信模板：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "短信设置管理", REMARK = "修改短信模板")
    @ApiOperation("修改短信模板")
    @PostMapping("/update")
    public Object update(EsCoreSmsTemplate entity) {
        try {
            if (entity.getSmsName() == null) {
                return new CommonResult().paramFailed("短信模板名称不能为空");
            }
            List<EsCoreSmsTemplate> list = smsService.list(new QueryWrapper<>(new EsCoreSmsTemplate())
                    .eq("sms_name", entity.getSmsName()));
            for (EsCoreSmsTemplate template : list) {
                if (template.getId().intValue() != entity.getId().intValue() && template.getSmsName().equals(entity.getSmsName())) {
                    return new CommonResult().failed("已存在相同的模板名称，{" + entity.getSmsName() + "}");
                }
            }

            //如果想要启动一个模板， 就校验 是否存在已启用的模板
            if (entity.getStatus() == 1) {
                List<SmsVariable> varList = JSON.parseArray(entity.getData(), SmsVariable.class);
                if (varList != null && varList.size() != 0) {
                    SmsVariable smsVariable = varList.get(0);
                    //查找出 同类别
                    EsCoreSmsTemplate template = this.smsService.getOne(new QueryWrapper<>(new EsCoreSmsTemplate())
                            .eq("template_type", smsVariable.getSmsTypeFunctionId())
                            .eq("status", 1)
                            .eq("is_delete", 0)
                            .eq("server_id", entity.getServerId())
                    );

                    if (template.getId() != entity.getId()) {
                        return new CommonResult().failed("已存在正在启用的同一类型模板,不能将此模板设置为已启用,要将此模板设置启用，请先禁用");
                    }
                }
            }

            if (this.smsService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("修改短信设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "短信设置管理", REMARK = "根据id查询某个短信模板")
    @ApiOperation("根据id查询某个短信模板")
    @PostMapping("/selectById")
    public Object selectById(@ApiParam("短信模板id") @RequestParam Long id) {
        try {
            EsCoreSmsTemplate shop = smsService.getById(id);
            if (shop == null) {
                return new CommonResult().success("没有此id的数据");
            }
            return new CommonResult().success(shop);
        } catch (Exception e) {
            log.error("根据id查询某个短信模板：%s", e.getMessage(), e);
        }

        return new CommonResult().failed();
    }

    /**
     * 验证一些必须的字段（提示信息中文名的话， 得加注解获取 待完善， 全局待完善）
     *
     * @param entity  实体
     * @param colmunS 已逗号(,)分隔开的字段
     * @return
     */
    private Object veryFieldEmpty(EsCoreSmsTemplate entity, String colmunS) {
        try {
            String[] colmuns = colmunS.split(",");
            int len = colmuns.length;
            Class<? extends EsCoreSmsTemplate> clazz = entity.getClass();
            for (int i = 0; i < len; i++) {
                String fieldS = colmuns[i];
                if (fieldS != null) {
                    fieldS = fieldS.trim();
                    Field field = clazz.getDeclaredField(fieldS);
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (ValidatorUtils.empty(value) && !value.equals(0)) {
                        return new CommonResult().failed(fieldS + " 不能为空");
                    }
                }
            }
        } catch (Exception e) {
            log.error("判断非空错误：%s", e.getMessage(), e);
        }
        return null;
    }

    @SysLog(MODULE = "短信设置管理", REMARK = "查询短信类型列表")
    @ApiOperation("查询短信类型列表")
    @PostMapping("/selSmsTypeList")
    public Object selSmsTypeList() {
        try {
            return new CommonResult().success(smsService.getSmsTypeInfo());
        } catch (Exception e) {
            log.error("查询短信类型列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "短信设置管理", REMARK = "查询短信服务商列表")
    @ApiOperation("查询短信服务商列表")
    @PostMapping("/selSmsServerList")
    public Object selSmsServerList() {
        try {
            return new CommonResult().success(smsService.getServerInfo());
        } catch (Exception e) {
            log.error("查询短信服务商列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "短信设置管理", REMARK = "保存所有服务商信息")
    @ApiOperation("保存所有服务商信息")
    @PostMapping("/saveSmsServerList")
    public Object saveSmsServerList(@ApiParam("服务商信息json数据") @RequestParam String serverInfoListS) {
//        @RequestBody List<EsCoreSms> serverInfoList
        try {
            //只能存在一个服务商在启用状态中

            List<EsCoreSms> serverInfoList = JSONObject.parseArray(serverInfoListS, EsCoreSms.class);
            int count = 0;
            if (serverInfoList != null && serverInfoList.size() != 0) {
                for (EsCoreSms entity : serverInfoList) {
                    Integer status = entity.getStatus();
                    if (status == 1 && status != null) {
                        ++count;
                    }
                    if (count > 1) {
                        return new CommonResult().failed("只能存在一个服务商在启用状态");
                    }
                }
            }

            return new CommonResult().success(smsService.updSmsServerInfopAllById(serverInfoList));
        } catch (Exception e) {
            log.error("保存所有服务商信息：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "短信设置管理", REMARK = "根据短信类型id查找短信类型功能")
    @ApiOperation("根据短信类型id查找短信类型功能")
    @PostMapping("/selSmsFuncListById")
    public Object selSmsFunctionListBySmsTypeId(@ApiParam("服务商信息json数据") @RequestParam Long smsTypeId) {
        try {
            return new CommonResult().success(smsService.getSmsFuncBySmsTypeId(smsTypeId));
        } catch (Exception e) {
            log.error("查询短信类型列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    private boolean isExistRepeatName(String templateName) {
        return this.smsService.isExistRepeatName(templateName);
    }


}

