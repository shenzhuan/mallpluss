package com.mei.zhuang.controller.marking;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopPackageGoods;
import com.mei.zhuang.entity.marking.EsShopPackageGoodsSpec;
import com.mei.zhuang.service.marking.PackageGoodsService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
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

@Slf4j
@Api(value = "套餐商品管理", description = "", tags = {"套餐商品管理"})
@RestController
@RequestMapping("/api/packagegoods")
public class PackageGoodsController {

    @Resource
    private PackageGoodsService packageService;

    @SysLog(MODULE = "套餐商品管理", REMARK = "根据条件查询所有套餐列表")
    @ApiOperation("根据条件查询所有套餐列表")
    @PostMapping(value = "/list")
    public Object getPackageByPage(EsShopPackageGoods goods,
                                   @RequestParam(value = "current", defaultValue = "1") Integer current,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            PageHelper.startPage(current, size);
            return new CommonResult().success(PageInfo.of(packageService.packageList(goods)));
        } catch (Exception e) {
            log.error("根据条件查询所有套餐列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "套餐商品管理", REMARK = "保存套餐商品")
    @ApiOperation("保存套餐商品")
    @PostMapping(value = "/save")
    public Object savePackage(EsShopPackageGoods entity) {
        try {
            List<EsShopPackageGoodsSpec> list = JSONObject.parseArray(entity.getPackagespecList(), EsShopPackageGoodsSpec.class);
            entity.setPackageGoodsSpecList(list);
            if (packageService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存套餐商品：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("保存失败");
    }

    @SysLog(MODULE = "套餐商品管理", REMARK = "更新套餐商品")
    @ApiOperation("更新套餐商品")
    @PostMapping(value = "/update")
    public Object updatePackage(EsShopPackageGoods entity) {
        try {
            List<EsShopPackageGoodsSpec> list = JSONObject.parseArray(entity.getPackagespecList(), EsShopPackageGoodsSpec.class);
            entity.setPackageGoodsSpecList(list);
            if (packageService.update(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新套餐商品：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("修改失败");
    }

    @SysLog(MODULE = "套餐商品管理", REMARK = "批量删除套餐商品")
    @ApiOperation("批量删除套餐商品")
    @PostMapping(value = "/delete")
    public Object deletePackage(@ApiParam("套餐id") @RequestParam String id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("套餐id");
            }
            if (packageService.deletePackage(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("批量删除套餐商品：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "套餐商品管理", REMARK = "查询套餐明细")
    @ApiOperation("查询套餐明细")
    @PostMapping(value = "/detail")
    public Object getPackageById(@ApiParam("套餐id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("套餐id");
            }
            return new CommonResult().success(packageService.packageList(id));
        } catch (Exception e) {
            log.error("查询套餐明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "套餐商品管理", REMARK = "套餐状态修改")
    @ApiOperation("套餐状态修改")
    @PostMapping(value = "/updatestatus")
    public Object updatestatus(@RequestParam Integer status, @RequestParam String pid) {
        try {
            return new CommonResult().success(packageService.updatestatus(status, pid));
        } catch (Exception e) {
            log.error("套餐状态修改：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "套餐商品同步", REMARK = "套餐商品同步")
    @ApiOperation("套餐商品同步")
    @PostMapping(value = "/updatepackage")
    public Object updatepackage(@RequestParam String packageName, @RequestParam long goodId) {
        try {
            return new CommonResult().success(packageService.updatePackage(packageName, goodId));
        } catch (Exception e) {
            log.error("套餐商品同步：%s", e.getMessage(), e);
            return new CommonResult().failed("套餐商品同步失败");
        }
    }

    @SysLog(MODULE = "套餐商品删除同步", REMARK = "套餐商品删除同步")
    @ApiOperation("套餐商品删除同步")
    @PostMapping(value = "/deletepackage")
    public Object delete(@RequestParam long goodId, @RequestParam int according) {
        try {
            return new CommonResult().success(packageService.delete(goodId, according));
        } catch (Exception e) {
            log.error("套餐商品删除同步：%s", e.getMessage(), e);
            return new CommonResult().failed("套餐商品删除同步失败");
        }
    }

}
