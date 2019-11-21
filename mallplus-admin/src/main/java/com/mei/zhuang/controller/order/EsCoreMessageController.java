package com.mei.zhuang.controller.order;

import com.arvato.service.order.api.service.EsAppletTemplateService;
import com.arvato.service.order.api.service.EsCoreMessageTemplateService;
import com.arvato.service.order.api.service.impl.WechatApiService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.mei.zhuang.entity.order.EsAppletTemplates;
import com.mei.zhuang.entity.order.EsCoreMessageTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Api(value = "模板消息管理", description = "", tags = {"模板消息管理"})
@RestController
@RequestMapping("/cor/message")
public class EsCoreMessageController {

    @Resource
    private EsCoreMessageTemplateService esCoreMessageTemplateService;
    @Resource
    private EsAppletTemplateService esAppletTemplateService;
    @Resource
    private WechatApiService wechatApiService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SysLog(MODULE = "模板消息管理", REMARK = "同步微信模板消息")
    @ApiOperation("同步微信模板消息")
    @PostMapping("/synTemplates")
    public Object synTemplates(@RequestParam("shopId") Long shopId) {
        try {
            return new CommonResult().success("success", wechatApiService.synTemplates(shopId));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "模板消息管理", REMARK = "查询消息模板")
    @ApiOperation("查询消息模板")
    @PostMapping("/selectMessageTemplate")
    public Object selectMessageTemplate(EsCoreMessageTemplate entity) {
        try {
            return new CommonResult().success("success", esCoreMessageTemplateService.select(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "模板消息管理", REMARK = "查询消息模板")
    @ApiOperation("查询消息模板")
    @PostMapping("/selectAll")
    public Object selectAll() {
        try {
            return new CommonResult().success("success", esCoreMessageTemplateService.selectList(new QueryWrapper<>()));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "模板消息管理", REMARK = "修改消息模板")
    @ApiOperation("修改消息模板")
    @PostMapping("/update")
    public Object update(EsCoreMessageTemplate entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定ID");
            }
            if (ValidatorUtils.empty(entity.getTemplateId())) {
                return new CommonResult().failed("请选择模板");
            }
            EsAppletTemplates esAppletTemplates = esAppletTemplateService.selectById(entity.getTemplateId());
            if (esAppletTemplates != null) {
                entity.setOriginalTemplateId(esAppletTemplates.getTemplateId());
            }
            return new CommonResult().success("success", esCoreMessageTemplateService.updateById(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "模板消息管理", REMARK = "删除消息模板")
    @ApiOperation("删除消息模板")
    @PostMapping("/delete")
    public Object delete(@RequestParam("ids") String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("请指定ID");
            }
            String[] idAttr = ids.split(",");
            boolean bool = false;
            for (int i = 0; i < idAttr.length; i++) {
                EsCoreMessageTemplate template = new EsCoreMessageTemplate();
                template.setId(Long.parseLong(idAttr[i]));
                bool = esCoreMessageTemplateService.deleteById(template);
            }
            return new CommonResult().success("success", bool);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "模板消息管理", REMARK = "查询消息模板详情")
    @ApiOperation("查询消息模板详情")
    @PostMapping("/detail")
    public Object detail(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定ID");
            }
            EsCoreMessageTemplate esCoreMessage = esCoreMessageTemplateService.selectById(id);
            esCoreMessage.setAppletTemplatesList(esAppletTemplateService.selectList(new QueryWrapper<>()));
            return new CommonResult().success("success", esCoreMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "模板消息管理", REMARK = "新增消息模板")
    @ApiOperation("新增消息模板")
    @PostMapping("/save")
    public Object save(EsCoreMessageTemplate entity) {
        try {
            if (ValidatorUtils.empty(entity.getTitle())) {
                return new CommonResult().failed("请指定模版名称");
            }
            if (ValidatorUtils.empty(entity.getTemplateId())) {
                return new CommonResult().failed("请选择模板");
            }
            EsAppletTemplates esAppletTemplates = esAppletTemplateService.selectById(entity.getTemplateId());
            if (esAppletTemplates != null) {
                entity.setOriginalTemplateId(esAppletTemplates.getTemplateId());
            }
            String time = sdf.format(new Date());
            entity.setCreateTime(sdf.parse(time));
            return new CommonResult().success("success", esCoreMessageTemplateService.insert(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "模板消息管理", REMARK = "启用禁用模版消息")
    @ApiOperation("启用禁用模版消息")
    @PostMapping("/templUpdStatus")
    public Object templUpdStatus(@RequestParam("ids") String ids, @RequestParam("status") Integer status) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("请指定模版编号");
            }
            String[] attr = ids.split(",");
            boolean bool = false;
            for (int i = 0; i < attr.length; i++) {
                EsCoreMessageTemplate esCoreMessageTemplate = new EsCoreMessageTemplate();
                esCoreMessageTemplate.setId(Long.parseLong(attr[i]));
                esCoreMessageTemplate.setStatus(status);
                bool = esCoreMessageTemplateService.updateById(esCoreMessageTemplate);
            }
            return new CommonResult().success("success", bool);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

}
