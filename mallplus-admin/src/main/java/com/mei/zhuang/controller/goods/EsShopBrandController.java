package com.mei.zhuang.controller.goods;


import com.arvato.service.goods.api.service.EsShopBrandService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.mei.zhuang.entity.goods.EsShopBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Api(value = "品牌管理", description = "", tags = {"品牌管理"})
@RestController
@RequestMapping("/shop/brand")
public class EsShopBrandController {

    @Resource
    private EsShopBrandService brandService;

    @SysLog(MODULE = "品牌管理", REMARK = "添加品牌")
    @ApiOperation("添加品牌")
    @PostMapping(value = "/saves")
    public Object saveBrand(@RequestBody EsShopBrand entity) {
        try {
            return brandService.saveBrand(entity);
        } catch (Exception e) {
            log.error("添加品牌：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "品牌管理", REMARK = "删除品牌")
    @ApiOperation("删除品牌")
    @DeleteMapping(value = "/delete{brandid}")
    public Object deleteBrand(@PathVariable Long brandid) {
        try {
            return new CommonResult().success(brandService.deleteBrand(brandid));
        } catch (Exception e) {
            log.error("删除品牌：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "品牌管理", REMARK = "查询品牌明细")
    @ApiOperation("查询品牌明细")
    @PostMapping(value = "/detail{brandid}")
    public Object detailBrand(@PathVariable Long brandid) {
        try {
            return brandService.detailBrand(brandid);
        } catch (Exception e) {
            log.error("查询明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "品牌管理", REMARK = "更改品牌信息")
    @ApiOperation("更改品牌信息")
    @PostMapping(value = "/update")
    public Object updateBrand(@RequestBody EsShopBrand entity) {
        try {
            brandService.updateBrand(entity);
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("更改品牌：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
}
