package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.arvato.service.marking.api.service.DiscountService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.mei.zhuang.entity.marking.EsShopDiscount;
import com.mei.zhuang.entity.marking.EsShopDiscountRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 限时折扣管理
 */

@Slf4j
@Api(value = "限时折扣管理", description = "", tags = {"限时折扣管理"})
@RestController
@RequestMapping("/api/discount")
public class DiscountController {


    @Resource
    private DiscountService discountService;

    @SysLog(MODULE = "限时折扣管理", REMARK = "根据条件查询所有限时折扣列表")
    @ApiOperation("根据条件查询所有限时折扣列表")
    @PostMapping(value = "/list")
    public Object getDiscountByPage(EsShopDiscount entity,
                                    @RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            PageHelper.startPage(current, size);
            List<EsShopDiscount> esShopDiscount = discountService.slelectDiscount();
            entity.setTotal((int) PageHelper.freeTotal());
            Map<String, Object> map = new HashMap<>();
            map.put("rows", esShopDiscount);
            map.put("size", size);
            map.put("total", entity.getTotal());
            map.put("current", current);
            return new CommonResult().success(map);
        } catch (Exception e) {
            log.error("根据条件查询所有限时折扣列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "限时折扣管理", REMARK = "保存限时折扣")
    @ApiOperation("保存限时折扣")
    @PostMapping(value = "/save")
    public Object saveDiscount(EsShopDiscount entity) {
        try {
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getSelectgoods(), GoodsSepcVo.class);
            List<EsShopDiscountRule> list2 = JSONObject.parseArray(entity.getSelectrule(), EsShopDiscountRule.class);
            // 部分商品参与优惠设置
            entity.setGoodsSepcVoList(list);
            // 优惠规则设置
            entity.setRuleList(list2);

            if (discountService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存限时折扣：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "限时折扣管理", REMARK = "更新限时折扣")
    @ApiOperation("更新限时折扣")
    @PostMapping(value = "/update")
    public Object updateDiscount(EsShopDiscount entity) {
        try {
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getSelectgoods(), GoodsSepcVo.class);
            List<EsShopDiscountRule> list2 = JSONObject.parseArray(entity.getSelectrule(), EsShopDiscountRule.class);
            entity.setGoodsSepcVoList(list);
            entity.setRuleList(list2);
            if (discountService.update(entity) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新限时折扣：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "限时折扣管理", REMARK = "删除限时折扣")
    @ApiOperation("删除限时折扣")
    @PostMapping(value = "/delete")
    public Object deleteDiscount(@ApiParam("限时折扣id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("限时折扣id");
            }
            EsShopDiscount manjian = discountService.selectById(id);
            if (discountService.deleteid(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除限时折扣：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "限时折扣管理", REMARK = "查询限时折扣明细")
    @ApiOperation("查询限时折扣明细")
    @PostMapping(value = "/detail")
    public Object getDiscountById(@ApiParam("限时折扣id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("限时折扣id");
            }
            EsShopDiscount coupon = discountService.selectById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询限时折扣明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
    @SysLog(MODULE = "限时折扣管理", REMARK = "查询折扣商品明细")
    @ApiOperation("查询折扣商品明细")
    @PostMapping(value = "/detailgoods")
    public Object getManJiangoodsById(@ApiParam("折扣id") @RequestParam Long discountId) {
        try {
            if (ValidatorUtils.empty(discountId)) {
                return new CommonResult().paramFailed("折扣商品id");
            }
            return new CommonResult().success(discountService.selectgoodsid(discountId));
        } catch (Exception e) {
            log.error("查询折扣明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
    @SysLog(MODULE = "限时折扣管理", REMARK = "查询折扣优惠设置明细")
    @ApiOperation("查询折扣优惠设置明细")
    @PostMapping(value = "/detailcouponid")
    public Object getManJiancouponById(@ApiParam("折扣id") @RequestParam Long discountId) {
        try {
            if (ValidatorUtils.empty(discountId)) {
                return new CommonResult().paramFailed("查询折扣优惠设置id");
            }
            return new CommonResult().success(discountService.selectcouponid(discountId));

        } catch (Exception e) {
            log.error("查询折扣优惠设置明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
    @SysLog(MODULE = "限时折扣管理", REMARK = "修改状态")
    @ApiOperation("修改状态 status 为列表的状态")
    @PostMapping(value = "/updateStatus")
    public Object updateShowStatus(@RequestParam Long id, @RequestParam Integer status) {
        int count = discountService.updateShowStatus(id, status);
        if (count > 0) {
            return new CommonResult().success();
        }
        return new CommonResult().success();
    }
    @SysLog(MODULE = "限时折扣管理", REMARK = "修改状态查询")
    @ApiOperation("修改状态查询")
    @PostMapping(value = "/Status")
    public Object Status(@RequestParam Integer status) {
        if (status == 0) {
            int man = discountService.selectstatus();
            if (man >= 1) {
                return new CommonResult().success(3);
            } else {
                return new CommonResult().success(4);
            }
        } else {
            return new CommonResult().success(4);
        }

    }
}
