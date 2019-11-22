package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.entity.marking.EsShopCouponShopping;
import com.mei.zhuang.entity.marking.EsShopCouponsTopupGoods;
import com.mei.zhuang.service.marking.CouponShoppingService;
import com.mei.zhuang.vo.CommonResult;
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
@Api(value = "购物发券管理", description = "", tags = {"购物发券管理"})
@RestController
@RequestMapping("/api/couponshopping")
public class CouponShoppingController {

    @Resource
    private CouponShoppingService couponshopService;

    @SysLog(MODULE = "购物发券管理", REMARK = "添加购物发券")
    @ApiOperation("添加购物发券")
    @PostMapping(value = "/save")
    public Object saveCouponTopup(EsShopCouponShopping entity) {
        try {
            List<EsShopCouponsTopupGoods> list = JSONObject.parseArray(entity.getCouponsGoodsList(), EsShopCouponsTopupGoods.class);
            entity.setShopGoodsList(list);
            List<EsShopCouponNewRule> list2 = JSONObject.parseArray(entity.getConponlist(), EsShopCouponNewRule.class);
            entity.setCouponShopList(list2);
            if (couponshopService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("添加购物发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "购物发券管理", REMARK = "修改购物发券")
    @ApiOperation("修改购物发券")
    @PostMapping(value = "/update")
    public Object CouponTopup( EsShopCouponShopping entity) {
        try {
            List<EsShopCouponsTopupGoods> list = JSONObject.parseArray(entity.getCouponsGoodsList(), EsShopCouponsTopupGoods.class);
            entity.setShopGoodsList(list);
            List<EsShopCouponNewRule> list2 = JSONObject.parseArray(entity.getConponlist(), EsShopCouponNewRule.class);
            entity.setCouponShopList(list2);
            if (couponshopService.update(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("修改购物发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }


    @SysLog(MODULE = "购物发券管理", REMARK = "购物发券删除")
    @ApiOperation("购物发券删除")
    @PostMapping(value = "/deleteshopping")
    public Object deleteCouponTopup(@RequestParam long id) {
        try {
            if (couponshopService.deletecouponid(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("购物发券删除：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "购物发券管理", REMARK = "购物明细")
    @ApiOperation("购物明细")
    @PostMapping(value = "/selectshopid")
    public Object selecttopupid(@RequestParam long id) {
        try {
            return new CommonResult().success(couponshopService.selectshopid(id));
        } catch (Exception e) {
            log.error("购物明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "购物发券管理", REMARK = "商品明细查询")
    @ApiOperation("商品明细查询")
    @PostMapping(value = "/selectgoodsid")
    public Object selectgoodsid(@RequestParam long id) {
        try {
            return new CommonResult().success(couponshopService.goodsList(id));
        } catch (Exception e) {
            log.error("商品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "购物发券管理", REMARK = "购物状态修改")
    @ApiOperation("购物状态修改")
    @PostMapping(value = "/updatestatusid")
    public Object updatestatus(@RequestParam Integer activitiesOpen, @RequestParam Long id) {
        try {
            if (couponshopService.updatestatusid(activitiesOpen, id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("购物状态修改：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "购物发券管理", REMARK = "购物发券查询")
    @ApiOperation("购物发券查询")
    @PostMapping(value = "/selectshopping")
    public Object selecttopup(EsShopCouponShopping entity, @RequestParam(value = "current", defaultValue = "1") Integer current,
                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            PageHelper.startPage(current, size);
            return new CommonResult().success(PageInfo.of(couponshopService.selectshopping()));
        } catch (Exception e) {
            log.error("购物发券查询：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }


}
