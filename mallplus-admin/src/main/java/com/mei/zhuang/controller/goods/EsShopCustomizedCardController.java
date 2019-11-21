package com.mei.zhuang.controller.goods;

import com.alibaba.fastjson.JSONObject;
import com.arvato.service.goods.api.service.EsShopCardMessageServer;
import com.arvato.service.goods.api.service.EsShopCustomizedCardService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.mei.zhuang.entity.goods.EsShopCardMessage;
import com.mei.zhuang.entity.goods.EsShopCardMessageCut;
import com.mei.zhuang.entity.goods.EsShopCustomizedCard;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(value = "定制卡片列表管理", description = "", tags = {"定制卡片列表管理"})
@RestController
@RequestMapping("/shop/card")
public class EsShopCustomizedCardController {

    @Resource
    private EsShopCustomizedCardService esShopCustomizedCardService;
    @Resource
    private EsShopCardMessageServer esShopCardMessageServer;

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "查询定制卡片列表")
    @ApiOperation("查询定制卡片列表")
    @PostMapping("/list")
    public Object list(EsShopCustomizedCard entity) {
        try {
            return new CommonResult().success("success", esShopCustomizedCardService.selCardPage(entity));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询定制卡片列表异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "新增定制卡片列表")
    @ApiOperation("新增定制卡片列表")
    @PostMapping("/save")
    public Object save(EsShopCustomizedCard entity) {
        try {
            return new CommonResult().success("success", esShopCustomizedCardService.insert(entity));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增定制卡片列表异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "查询定制卡片详情")
    @ApiOperation("查询定制卡片详情")
    @PostMapping("/detail")
    public Object detail(@RequestParam("id") Long id) {
        try {
            return new CommonResult().success("success", esShopCustomizedCardService.selectById(id));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询定制卡片详情异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "修改定制卡片")
    @ApiOperation("修改定制卡片")
    @PostMapping("/update")
    public Object update(EsShopCustomizedCard entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedCardService.updateById(entity));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改定制卡片异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "删除定制卡片")
    @ApiOperation("删除定制卡片")
    @PostMapping("/delete")
    public Object delete(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedCardService.deleteById(id));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除定制卡片异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "新增卡片描述")
    @ApiOperation("新增卡片描述")
    @PostMapping("/saveDetail")
    public Object saveDetail(EsShopCustomizedCard entity) {
        try {
            entity.setId(Long.parseLong("1"));
            return new CommonResult().success("success", esShopCustomizedCardService.insert(entity));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增卡片描述异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "修改卡片描述")
    @ApiOperation("修改卡片描述")
    @PostMapping("/updateDetail")
    public Object updateDetail(@RequestParam("content") String content) {
        try {
            EsShopCustomizedCard card = new EsShopCustomizedCard();
            card.setId(Long.parseLong("1"));
            card.setCode(content);
            return new CommonResult().success("success", esShopCustomizedCardService.updateById(card));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改卡片描述异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "查询卡片描述")
    @ApiOperation("查询卡片描述")
    @PostMapping("/selectDetail")
    public Object selectDetail() {
        try {
            return new CommonResult().success("success", esShopCustomizedCardService.selectById(1));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询卡片描述异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "查询卡片寄语设置")
    @ApiOperation("查询卡片寄语设置")
    @PostMapping("/selCardMessage")
    public Object selCardMessage() {
        try {
            return new CommonResult().success("success", esShopCardMessageServer.selPageList());
        } catch (Exception e) {
            log.error("查询卡片寄语设置异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "新增卡片寄语设置")
    @ApiOperation("新增卡片寄语设置")
    @PostMapping("/insCardMessage")
    public Object insCardMessage(EsShopCardMessage entity) {
        try {
            List<EsShopCardMessageCut> list = JSONObject.parseArray(entity.getCutList(), EsShopCardMessageCut.class);
            entity.setListCut(list);
            return new CommonResult().success("success", esShopCardMessageServer.save(entity));
        } catch (Exception e) {
            log.error("新增卡片寄语设置异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "定制卡片列表管理", REMARK = "修改卡片消息")
    @ApiOperation("修改卡片消息")
    @PostMapping("/updCardMessage")
    public Object updCardMessage(EsShopCardMessage entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定编号");
            }
            List<EsShopCardMessageCut> list = JSONObject.parseArray(entity.getCutList(), EsShopCardMessageCut.class);
            entity.setListCut(list);
            return new CommonResult().success("success", esShopCardMessageServer.updates(entity));
        } catch (Exception e) {
            log.error("修改卡片寄语设置异常：", e);
            return new CommonResult().failed();
        }
    }


}
