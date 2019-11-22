package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopManjian;
import com.mei.zhuang.entity.marking.EsShopManjianRule;
import com.mei.zhuang.service.marking.ManJianService;
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

/**
 * 满减管理
 */

@Slf4j
@Api(value = "满减管理", description = "", tags = {"满减管理"})
@RestController
@RequestMapping("/api/manJian")
public class ManJianController {


    @Resource
    private ManJianService manJianService;

    @SysLog(MODULE = "满减管理", REMARK = "根据条件查询所有满减列表")
    @ApiOperation("根据条件查询所有满减列表")
    @PostMapping(value = "/list")
    public Object getManJianByPage(EsShopManjian entity,
                                   @RequestParam(value = "current", defaultValue = "1") Integer current,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            PageHelper.startPage(current, size);
            //  List<EsShopManjian> esShopManjians = manJianService.slelectMan();
            return new CommonResult().success(PageInfo.of(manJianService.slelectMan()));
        } catch (Exception e) {
            log.error("根据条件查询所有满减列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满减管理", REMARK = "保存满减")
    @ApiOperation("保存满减")
    @PostMapping(value = "/save")
    public Object saveManJian(EsShopManjian entity) {
        try {
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getSelectgoods(), GoodsSepcVo.class);
            List<EsShopManjianRule> list2 = JSONObject.parseArray(entity.getSelectrule(), EsShopManjianRule.class);
            // 部分商品参与优惠设置
            entity.setGoodsSepcVoList(list);
            // 优惠规则设置
            entity.setRuleList(list2);

            if (manJianService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存满减：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("保存失败");
    }

    @SysLog(MODULE = "满减管理", REMARK = "更新满减")
    @ApiOperation("更新满减")
    @PostMapping(value = "/update")
    public Object updateManJian(EsShopManjian entity) {
        try {
            // JSON String to Bean
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getSelectgoods(), GoodsSepcVo.class);
            List<EsShopManjianRule> list2 = JSONObject.parseArray(entity.getSelectrule(), EsShopManjianRule.class);
            // 部分商品参与优惠设置
            entity.setGoodsSepcVoList(list);
            // 优惠规则设置
            entity.setRuleList(list2);
            if (manJianService.update(entity) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新满减：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("修改失败");
    }

    @SysLog(MODULE = "满减管理", REMARK = "删除满减")
    @ApiOperation("删除满减")
    @PostMapping(value = "/delete")
    public Object deleteManJian(@ApiParam("满减id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("满减id");
            }
            if (manJianService.deleteid(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除满减：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满减管理", REMARK = "查询满减明细")
    @ApiOperation("查询满减明细")
    @PostMapping(value = "/detail")
    public Object getManJianById(@ApiParam("满减id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("满减id");
            }
            return new CommonResult().success(manJianService.getById(id));
        } catch (Exception e) {
            log.error("查询满减明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "满减管理", REMARK = "查询满减商品明细")
    @ApiOperation("查询满减商品明细")
    @PostMapping(value = "/detailgoods")
    public Object getManJiangoodsById(@ApiParam("满减id") @RequestParam Long manjianId) {
        try {
            if (ValidatorUtils.empty(manjianId)) {
                return new CommonResult().paramFailed("满减商品id");
            }
            return new CommonResult().success(manJianService.selectgoodsid(manjianId));
        } catch (Exception e) {
            log.error("查询满减明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "满减管理", REMARK = "查询满减优惠设置明细")
    @ApiOperation("查询满减优惠设置明细")
    @PostMapping(value = "/detailcouponid")
    public Object getManJiancouponById(@ApiParam("满减id") @RequestParam Long manjianId) {
        try {
            if (ValidatorUtils.empty(manjianId)) {
                return new CommonResult().paramFailed("查询满减优惠设置id");
            }
            return new CommonResult().success(manJianService.selectcouponid(manjianId));
        } catch (Exception e) {
            log.error("查询满减优惠设置明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "满减管理", REMARK = "修改状态")
    @ApiOperation("修改状态 status 为列表的状态")
    @PostMapping(value = "/updateStatus")
    public Object updateShowStatus(@RequestParam Long id, @RequestParam Integer status) {
        int count = manJianService.updateShowStatus(id, status);
        if (count > 0) {
            return new CommonResult().success();
        }
        return new CommonResult().success();
    }

    @SysLog(MODULE = "满减管理", REMARK = "修改状态查询")
    @ApiOperation("修改状态查询")
    @PostMapping(value = "/Status")
    public Object Status(@RequestParam Integer status) {
        if (status == 0) {
            int man = manJianService.selectstatus();
            if (man >= 1) {
                return new CommonResult().success(3);
            } else {
                return new CommonResult().success(4);
            }
        } else {
            return new CommonResult().success(4);
        }

    }

    @SysLog(MODULE = "满减管理", REMARK = "删除编辑满额立减的商品")
    @ApiOperation("删除编辑满额立减的商品")
    @PostMapping(value = "/deletegoodsid")
    public Object deletegoodsid(@ApiParam("商品id") @RequestParam Long goodsid) {
        try {
            if (ValidatorUtils.empty(goodsid)) {
                return new CommonResult().paramFailed("商品id");
            }
            return new CommonResult().success(manJianService.deletegoodsid(goodsid));

        } catch (Exception e) {
            log.error("删除满减：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


}
