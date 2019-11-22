package com.mei.zhuang.controller.marking;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopGoodsRules;
import com.mei.zhuang.service.marking.RulesSpecService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/*
商品规则
 */
@Slf4j
@Api(value = "商品规则管理", description = "", tags = {"商品规则管理"})
@RestController
@RequestMapping("/api/rules")
public class GoodsRulesController {

    @Resource
    private RulesSpecService rulesService;

    @SysLog(MODULE = "商品规则管理", REMARK = "根据条件查询所有规则商品列表")
    @ApiOperation("根据条件查询所有规则商品列表")
    @PostMapping(value = "/list")
    public Object getGoodsRulesByPage(EsShopGoodsRules entity,
                                      @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            PageHelper.startPage(current, size);
            return new CommonResult().success(PageInfo.of(rulesService.lsitrules(entity.getGoodsname())));
        } catch (Exception e) {
            log.error("根据条件查询所有规则商品列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "商品规则管理", REMARK = "删除规则商品")
    @ApiOperation("删除规则商品")
    @PostMapping(value = "/delete")
    public Object deleteManJian(@ApiParam("规则商品id") @RequestParam String id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("规则商品id");
            }
            if (rulesService.deleterule(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除规则商品：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "商品规则管理", REMARK = "保存规则")
    @ApiOperation("保存规则")
    @PostMapping(value = "/save")
    public Object saverules(EsShopGoodsRules entitys) {
        try {

            List<GoodsSepcVo> list = JSONObject.parseArray(entitys.getRulegoods(), GoodsSepcVo.class);
            entitys.setListrulesgoods(list);
            if (rulesService.save(entitys)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存规则：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("保存失败");
    }

    @SysLog(MODULE = "商品规则管理", REMARK = "编辑规则")
    @ApiOperation("编辑规则")
    @PostMapping(value = "/update")
    public Object updaterules(EsShopGoodsRules entity) {
        try {
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getRulegoods(), GoodsSepcVo.class);
            entity.setListrulesgoods(list);
            if (rulesService.update(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("编辑规则：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("编辑失败");
    }

    @SysLog(MODULE = "商品规则管理", REMARK = "规则明细")
    @ApiOperation("规则明细")
    @PostMapping(value = "/detail")
    public Object detailrules(long id) {
        try {

            return new CommonResult().success(rulesService.detailrule(id));
        } catch (Exception e) {
            log.error("规则明细：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("查询明细失败");
    }

    @SysLog(MODULE = "商品规则管理", REMARK = "规则商品明细")
    @ApiOperation("规则商品明细")
    @PostMapping(value = "/detailgoods")
    public Object detailrulesgoods(long rulesId) {
        try {
            return new CommonResult().success(rulesService.listrulesspec(rulesId));
        } catch (Exception e) {
            log.error("规则商品明细：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("查询明细失败");
    }

    @SysLog(MODULE = "商品规则同步", REMARK = "商品规则同步")
    @ApiOperation("商品规则同步")
    @PostMapping(value = "/updateRule")
    public Object updateRule(@RequestParam String goodsname, @RequestParam long goodsId) {
        try {
            return new CommonResult().success(rulesService.updateRule(goodsname, goodsId));
        } catch (Exception e) {
            log.error("商品规则同步：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("商品规则同步");
    }

    @SysLog(MODULE = "商品规则删除同步", REMARK = "商品规则删除同步")
    @ApiOperation("商品规则删除同步")
    @PostMapping(value = "/deleterules")
    public Object deleterules(@RequestParam long goodsId, @RequestParam int according) {
        try {
            return new CommonResult().success(rulesService.delete(goodsId, according));
        } catch (Exception e) {
            log.error("商品规则删除同步：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("商品规则删除同步失败");
    }

}
