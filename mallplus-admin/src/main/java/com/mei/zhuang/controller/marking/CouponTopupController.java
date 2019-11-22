package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.service.marking.CouponsTopupService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.github.pagehelper.PageHelper;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.entity.marking.EsShopCouponsTopup;
import com.mei.zhuang.entity.marking.EsShopCouponsTopupGoods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "满额发券管理", description = "", tags = {"满额发券管理"})
@RestController
@RequestMapping("/api/coupontopup")
public class CouponTopupController {

    @Resource
    private CouponsTopupService couponService;

    @SysLog(MODULE = "满额发券管理", REMARK = "添加满额发券")
    @ApiOperation("添加满额发券")
    @PostMapping(value = "/save")
    public Object saveCouponTopup(EsShopCouponsTopup entity) {
        try {
            List<EsShopCouponsTopupGoods> list = JSONObject.parseArray(entity.getCouponGoodsList(), EsShopCouponsTopupGoods.class);
            List<EsShopCouponNewRule> list2 = JSONObject.parseArray(entity.getCouponList(), EsShopCouponNewRule.class);
            entity.setCouponsTopupGoodsList(list);
            entity.setCouponsList(list2);
            if (couponService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("添加满额发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满额发券管理", REMARK = "修改满额发券")
    @ApiOperation("修改满额发券")
    @PostMapping(value = "/update")
    public Object CouponTopup(EsShopCouponsTopup entity) {
        try {
            List<EsShopCouponsTopupGoods> list = JSONObject.parseArray(entity.getCouponGoodsList(), EsShopCouponsTopupGoods.class);
            List<EsShopCouponNewRule> list2 = JSONObject.parseArray(entity.getCouponList(), EsShopCouponNewRule.class);
            entity.setCouponsTopupGoodsList(list);
            entity.setCouponsList(list2);
            if (couponService.update(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("修改满额发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满额发券管理", REMARK = "修改满额发券")
    @ApiOperation("修改满额发券")
    @PostMapping(value = "/deletenew")
    public Object deleteCouponNew(@RequestParam Long goodsId, @RequestParam String couponTypeid) {
        try {
            if (couponService.deletegoodsid(goodsId, couponTypeid) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("通用商品删除：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满额发券管理", REMARK = "满额删除")
    @ApiOperation("满额删除")
    @PostMapping(value = "/deletetopup")
    public Object deleteCouponTopup(@RequestParam long id) {
        try {
            if (couponService.deletecouponid(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("满额删除：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满额发券管理", REMARK = "满额状态修改")
    @ApiOperation("满额状态修改")
    @PostMapping(value = "/updatestatusid")
    public Object updatestatus(@RequestParam Integer activitiesOpen, @RequestParam Long id) {
        try {
            if (couponService.updatestatusid(activitiesOpen, id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("满额状态修改：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满额发券管理", REMARK = "满额明细")
    @ApiOperation("满额明细")
    @PostMapping(value = "/selecttopupid")
    public Object selecttopupid(@RequestParam Long id) {
        try {
            return new CommonResult().success(couponService.selectTopupid(id));
        } catch (Exception e) {
            log.error("满额明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "满额发券管理", REMARK = "满额查询")
    @ApiOperation("满额查询")
    @PostMapping(value = "/selecttopup")
    public Object selecttopup(EsShopCouponsTopup topup, @RequestParam(value = "current", defaultValue = "1") Integer current,
                              @RequestParam(value = "size", defaultValue = "5") Integer size) {
        try {
            PageHelper.startPage(current, size);
            List<Map<String, Object>> maps = couponService.selectTopup(topup);
            topup.setTotal((int) PageHelper.freeTotal());
            Map<String, Object> map = new HashMap<>();
            map.put("rows", maps);
            map.put("total", topup.getTotal());
            map.put("current", current);
            map.put("size", size);
            return new CommonResult().success(map);
        } catch (Exception e) {
            log.error("满额查询：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }


}
